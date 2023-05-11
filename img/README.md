# AutoNotes
这是几个月前写的Idea自动注释插件，实现的效果就是在idea里面想要注释的时候自动切换为中文输入法，写代码的时候切换为英文输入法
当时写了一篇博客 在这：

https://blog.csdn.net/qq_39635776/article/details/126559518  

或者直接看下面的也行，我复制过来了
当时忘记给代码上传到github了，今天偶然发现评论区有几个小兄弟在评论，于是我这才上传，抱歉。

# 背景与痛点  
一张图说明。。  
![img_14.png](IdeaProjects/myplugin/img/img_14.pngts/myplugin/img/img_14.png)

上面的问题不知道友友们遇到过没，反正我是经常遇到，f...

写代码免不了要写注释，但是注释的时候我们经常要在代码区和注释区进行切换，这个过程虽然看似简单但是偶尔也会出些差错。比如当前输入法是美国英文输入时要想切换到中文输入法就不能使用shift快捷键了，而是要通过 win+空格或者ctrl+shift来切换输入法。如果电脑安装了多个输入法还可能会因为在各种输入法之间的切换而浪费时间，而这部分时间理应用来摸鱼。

# 需求
编写一个插件能够自动识别代码上下文，判断coder将要进行的操作，自动切换输入法。比如当光标移到了代码区，程序将输入法切换为英文，当光标移动到注释区程序将输入法切换为中文。



下面的链接是一个大概的功能演示

演示-CSDN直播
https://live.csdn.net/v/235592

# 技术可实现性分析
功能点可以拆分为：idea插件主体（搭框架），输入法切换，代码上下文判断。

通过查阅资料发现要实现以上功能需要先学会idea基本插件开发（没学过也能看懂本文），windows输入法管理原理（了解），idea插件开发中的PSI框架（使用某些API可以用来获取程序上下文），

实现的功能虽然很简单不过要考虑的细节其实还是比较多的，比如java doc注释和java行注释甚至是其他语言的注释，另外还要操作系统对输入法的管理方式，已及不同平台的适配。不过为了简单起见我们只适配Windows 64位操作系统和java代码，并且只实现基础功能。然后后续迭代完全可以靠大家的聪明才智了，比如加些人工智能啥的（俺不会哈哈）。

经过分析以及参照资料可以明确需求能够被实现，那么我们进入下一个步骤吧！

# 前置知识
由于案例涉及到了一些额外知识，包括windows编程，jni，PSI等等所以在这里统一简单解释一遍。
## 1. 输入法切换原理
   参考博客：https://blog.csdn.net/fengbangyue/article/details/7346333  
该博客大概介绍了一些函数，我们通过调用系统函数可以实现输入法切换。

参考MSDN对相应函数的说明：具体如何实现输入法切换的可以看MSDN详细说明，因为我也没学习过windows编程相关技术，所以我刚开始的时候也是硬着头皮看，根据文档的介绍找到关键的几个函数，然后看相应的函数说明，参数和返回值等等。

ActivateKeyboardLayout (Windows CE 5.0) | Microsoft Docs
https://docs.microsoft.com/en-us/previous-versions/windows/embedded/aa452845(v=msdn.10)

因为插件使用java代码编写，而java代码运行在虚拟机上的所以java本身没有提供操作输入法相关的函数，因此我们要先把操作输入法的函数封装好，然后利用jni技术在java里面调用C函数，这样就实现了第一个技术点，切换输入法！

下面是代码展示，函数头和名字有点奇怪不用管这个后面会解释，主要就是调用了两个函数（这里实现切换方法不是唯一的，只是其中比较简单的一种）

```
#include "com_bully_myplugin_jni_InputManagerJni.h"
#include "Windows.h"

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2Chinese(JNIEnv *env, jobject obj) {
//获取当前窗口
HWND hwnd = GetForegroundWindow();
//将输入法先改为英文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
//将输入法修改为中文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
}

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2English(JNIEnv *env, jobject obj) {
//获取当前窗口
HWND hwnd = GetForegroundWindow();
//将输入法先改为中文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
//将输入法修改为英文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
}```

C语言代码也很短的！

当然这是最直接的方法，更简单的还可以通过python脚本实现输入法的切换，但是其实本质都是一样的。
```
# 这是一个示例 Python 脚本。
```import ctypes

import win32api
import win32gui
from win32con import WM_INPUTLANGCHANGEREQUEST


def set_english_inputer():
# 0x0409为英文输入法的lid_hex的 中文一般为0x0804
name=win32api.GetKeyboardLayoutName()
state=win32api.GetKeyboardState()
print("state",state)
print("名字",name)
hwnd = win32gui.GetForegroundWindow()
title = win32gui.GetWindowText(hwnd)
im_list = win32api.GetKeyboardLayoutList()
im_list = list(map(hex, im_list))
for d in im_list:
print(d)
result = win32api.SendMessage(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409)
result = win32api.SendMessage(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804)
print(result)
if result == 0:
print("英文输入法切换成功！")


set_english_inputer()
```
由于java环境的原因，不论是使用python还是C我们都要解决如何在插件中调用其他语言执行的问题。

## 2.什么是Jni
上面提到了跨语言的问题，现在我们就来解决。

JNI是Java Native Interface的缩写，通过使用 Java本地接口书写程序，可以确保代码在不同的平台上方便移植。 [1]  从Java1.1开始，JNI标准成为java平台的一部分，它允许Java代码和其他语言写的代码进行交互。JNI一开始是为了本地已编译语言，尤其是C和C++而设计的，但是它并不妨碍你使用其他编程语言，只要调用约定受支持就可以了。使用java与本地已编译的代码交互，通常会丧失平台可移植性。但是，有些情况下这样做是可以接受的，甚至是必须的。例如，使用一些旧的库，与硬件、操作系统进行交互，或者为了提高程序的性能。JNI标准至少要保证本地代码能工作在任何Java 虚拟机环境。

**简单说就是 它提供提供了一个标准，java只写函数声明，c里面写实现，比如java里面的hash()方法就是native方法。最后java用的是一个dll文件，这个文件将C语言写的函数都封装在了这个文件里面。java代码加载这个dll之后就可以同过java native方法调用里面的c函数。**

jni编写步骤：

1. 编写带有native声明的方法的java类

2. 使用javac命令编译所编写的java类

3. 然后使用javah + java类名生成扩展名为h的头文件

4. 使用C/C++实现本地方法

5. 将C/C++编写的文件生成动态链接库

6. java里面加载dll动态链接库

7. 调用native函数

看着是不是步骤挺多的其实一步一步跟着走会很简单，不用担心，不过只有亲自走过才能真正学会哦！

详细代码和操作将会在下面介绍。关于jni技术想要详细了解的这里贴个百度百科。

JNI_百度百科
JNI是Java Native Interface的缩写，通过使用 Java本地接口书写程序，可以确保代码在不同的平台上方便移植。从Java1.1开始，JNI标准成为java平台的一部分，它允许Java代码和其他语言写的代码进行交互。JNI一开始是为了本地已编译语言，尤其是C和C++而设计的，但是它并不妨碍你使用其他编程语言，只要调用约定受支持就可以了。使用java与本地已编译的代码交互，通常会丧失平台可移植性。但是，有些情况下这样做是可以接受的，甚至是必须的。例如，使用一些旧的库，与硬件、操作系统进行交互，或者为了提高程序的性能。JNI标准至少要保证本地代码能工作在任何Java 虚拟机环境。

https://baike.baidu.com/item/JNI/9412164  
我使用jni技术来实现在java环境中调用c/c++函数，当然如果是使用Python也是有其他解决方案的，不过这里不深入，提供一个链接吧！

https://www.jianshu.com/p/4281ce5e137f

# 开始编码
## 1.搭建插件项目
参考博客：https://blog.csdn.net/sawiii/article/details/105952995

关于项目创建这里就简单介绍了网上资料比较多。需要注意的是插件采用Gradel进行构建，所有确保你正确配置了Gradel。

第一步是创建插件项目，这里配置名称符合jdk版本，如果熟悉kotlion也可以选择使用kotlin语言开发。

![img_1.png](IdeaProjects/myplugin/img/img_1.pngcts/myplugin/img/img_1.png)

这样就完成了插件项目的框架。是不是很简单！！！

下面是目录结构，自己的代码都写在src里面，然后resource里面的plugin.xml是idea自动创建的插件配置文件（类似于android的清单文件），以后会对他进行很多操作。

![img_2.png](IdeaProjects/myplugin/img/img_2.pngcts/myplugin/img/img_2.png)

不过目前就是创建了一个java项目而已，接下来我们按照之前的思路继续分析。

## 2.输入法切换功能的实现
首先我们要实现输入法必需要在一个合适的时机自动切换，这个时机可能是键盘输入，光标移动，快捷键触发等等。那么简单起见我们先用快捷键实现一个输入法切换的功能，然后慢慢扩展。

看图。。我们在编辑器右键菜单显示2了个快捷键功能，但我们按下对应快捷键时就让程序切换到对应输入法。

![img_3.png](IdeaProjects/myplugin/img/img_3.pngcts/myplugin/img/img_3.png)

首先在plugin.xml里面注册action和快捷键。其中class属性指定了action的处理类，这个类需要我们自己定义，add-to-group标签就是配置action菜单位置的，这里配置在了EditorPopupMenu也就是编辑器右键弹出菜单。快捷键时通过keyboard-shortcut标签配置的，注意的是快捷键不要和已经存在的快捷键冲突。
```
  <!-- action配置，按钮展示在哪里需要在这配置 -->
    <actions>
        <action id="transEn" class="com.bully.myplugin.listener.TransEnAction" text="切换英文输入"
                description="ctrl+shift+X 切换为英文">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl shift X"/>
        </action>
        <action id="transCh" class="com.bully.myplugin.listener.TransChAction" text="切换中文输入"
                description="ctrl+shift+D 切换为中文">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl shift D"/>
        </action>
    </actions>
```
ok，现在注册了action类和快捷键接下来我们就对action进行实现。

看项目结构，我把输入法切换定义在了2个类里面，一个转换为中文，一个转换为英文。

![img_4.png](IdeaProjects/myplugin/img/img_4.pngcts/myplugin/img/img_4.png)

我们看看TransEnAction里面的内容。
```
package com.bully.myplugin.listener;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;

public class TransEnAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("快捷键切换为英文");
        InputManagerJni.getSingleton().any2English_1();
    }
}
```


代码比较短，简单分析下。首先我们的action类要继承AnAction类，然后重写actionPerformed方法，该方法会在快捷键触发后自动调用。回调方法传入了一个AnActionEvent参数，这个参数非常有用，可以拿到当前行为的各种信息，不过这里没有用到。

方法里面我们只写了一行代码 :InputManagerJni.getSingleton().any2English_1();该代码调用了一个输入法转换的方法，这个转换方法是通过jni方式实现的。接下来我们分析一下InputManager里面做了啥 。
```
package com.bully.myplugin.jni;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

/**
* @author LIXIN
* @description 通过jni 调用win32 api 实现输入法切换
* @date 2022/8/11 13:11
  */
  public class InputManagerJni {
  //1 -english
  public static int status=1;
  public static int status0=1;
  //是否进行转换
  public static boolean doTrans=true;
  private static volatile InputManagerJni inputManagerJni = new InputManagerJni();
  public static Robot robot;
  static {
  try {
  robot = new Robot();
  } catch (AWTException e) {
  throw new RuntimeException(e);
  }
  try {
  String libpath = System.getProperty("java.library.path");
  if (libpath == null || libpath.length() == 0) {
  throw new RuntimeException("java.library.path is null");
  }

           String path = null;
           StringTokenizer st = new StringTokenizer(libpath, System.getProperty("path.separator"));
           if (st.hasMoreElements()) {
               path = st.nextToken();
           } else {
               throw new RuntimeException("can not split library path:" + libpath);
           }

           URL resource = InputManagerJni.class.getResource("/InputManager_64.dll");
           InputStream inputStream = resource.openStream();
           final File dllFile = new File(new File(path), "InputManager_64.dll");
           if (!dllFile.exists()) {
               FileOutputStream outputStream = new FileOutputStream(dllFile);
               byte[] array = new byte[8192];
               for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
                   outputStream.write(array, 0, i);
               }
               outputStream.close();
           }
           Runtime.getRuntime().addShutdownHook(new Thread() {
               public void run() {
                   if (dllFile.exists()) {
                       boolean delete = dllFile.delete();
                       System.out.println("delete : " + delete);
                   }
               }
           });
       } catch (Throwable e) {
           throw new RuntimeException("load jacob.dll error!", e);
       }
       System.loadLibrary("InputManager_64");
  }

  private InputManagerJni() {
  }

  public static InputManagerJni getSingleton() {
  if (inputManagerJni == null) {
  synchronized (InputManagerJni.class) {
  if (inputManagerJni == null) {
  inputManagerJni = new InputManagerJni();
  }
  }
  }
  return inputManagerJni;
  }

  public native void any2Chinese();
  /**
    * 转换为英文输入法
      */
      public native void any2English();

  /**
    * 当前输入法 0-中文,1-英文,2-其他
      */
      public native int state();

  /**
    * 获取输入法列表-保留
      */
      public native String list();

  /**
    * 移除输入法 0-中文,1-英文,-保留
      */
      public native String remove(int flag);

  /**
    * 转换为中文输入法
      */
      public void any2Chinese_1(){
      if (!doTrans) return;
      status=0;
      any2Chinese();
      System.out.println("toChinese");
      }

  public void any2English_1() {
  if (!doTrans) return;
  any2English();
  status=1;
  System.out.println("toEnglish");
  }

  private void pressShift(){
  if (robot==null){
  try {
  robot=new Robot();
  } catch (AWTException e) {
  throw new RuntimeException(e);
  }
  }
  robot.keyPress(KeyEvent.VK_SHIFT);
  robot.keyRelease(KeyEvent.VK_SHIFT);
  }
  }
```
可以看到其实InputManagerJni类里面只是声明了相应方法并未做实现。
```
public native void any2Chinese();
/**
* 转换为英文输入法
*/
public native void any2English();
这时请回忆一下前置知识里面介绍的Jni部分内容，其实这里的native方法对应了下面头文件的函数声明。

/*
* Class:     com_bully_myplugin_jni_InputManagerJni
* Method:    any2Chinese
* Signature: ()V
  */
  JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2Chinese
  (JNIEnv *, jobject);

/*
* Class:     com_bully_myplugin_jni_InputManagerJni
* Method:    any2English
* Signature: ()V
  */
  JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2English
  (JNIEnv *, jobject);
```
这个头文件是通过是自动生成的。具体方法是：

1. 用javac将InputManagerJni.java文件编译为InputManagerJni.class。

2.用javah将InputManagerJni.class编译为com_bully_myplugin_jni_InputManagerJni.h（这个名称是自动生成的）

ok,现在我们通过java文件生成了头文件，接下来我们应该要编写函数实现了。这里我使用了Clion来进行C语言的编写，读者有需求可以使用自己习惯的ide进行编写。

首先看看Clion的基本配置，这是我的目录结构，使用Cmake进行构建。

![img_5.png](IdeaProjects/myplugin/img/img_5.pngcts/myplugin/img/img_5.png)

提前配置的工具链

![img_6.png](IdeaProjects/myplugin/img/img_6.pngcts/myplugin/img/img_6.png)

Cmake配置，这里有个Cmake选项（红色框框），如果你使用的是vs2022 可以这样写。其他vs版本可以网上查，都有规律这里表示我构建的是64位的动态链接库。

![img_7.png](IdeaProjects/myplugin/img/img_7.pngcts/myplugin/img/img_7.png)

另外还有一点，因为我们要调用windows api所以安装vs的时候记得下载MFC

![img_8.png](IdeaProjects/myplugin/img/img_8.pngcts/myplugin/img/img_8.png)

主目录下的 cmakeLists文件
````
cmake_minimum_required(VERSION 3.23)
project(InputManager)

set(CMAKE_CXX_STANDARD 14)
find_package(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})

include_directories(jni)
set(BUILD_USE_64BITS on)
add_subdirectory(src)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY D:/AllCode/Clion/InputManager/build)
src目录下的CmakeLists文件

project(InputManager)
add_library(InputManager SHARED  InputManager.c)`
InputManager.c文件 这里面实现了切换的代码，
````
解释一下为啥转换中文要先调用转换英文的函数:因为比如百度输入法就是中文输入法，当我们按下shift键后输入的就是英文，这是如果想要切换为中文状态就不能直接调用函数了因为英文状态的百度输入法其实也是0x0804，所以要先转换为英文输入法，再转换为中文输入法。（这个方法是我自己想的，可能有其他方法但是我对windows函数不熟悉所以就这样将就了）。同理英文也是一样的。
```
//
// Created by LIXIN on 2022/8/10.
//

#include "com_bully_myplugin_jni_InputManagerJni.h"
#include "Windows.h"

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2Chinese(JNIEnv *env, jobject obj) {
//获取当前窗口
HWND hwnd = GetForegroundWindow();
//将输入法先改为英文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
//将输入法修改为中文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
}

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2English(JNIEnv *env, jobject obj) {
//获取当前窗口
HWND hwnd = GetForegroundWindow();
//将输入法先改为中文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
//将输入法修改为英文
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
}
```

这样代码就写完了，最后运行构建就会生成dll文件（这里是64位的）。（对Cmake不熟悉的可以直接搜教程网上很多，我也不是很懂就不讲了，反正里面坑挺多的）

回到IDEA，吧dll文件放到resource目录下面

![img_9.png](IdeaProjects/myplugin/img/img_9.pngcts/myplugin/img/img_9.png)

先想想现在我们都干了啥：

我们定义了native方法，并且基于jni规则做了实现，最后把dll文件引入到了java项目resource目录里面，那么接下来我们就是要加载库了。

加载的代码很简单，只需要一行代码，但是这个loadLibary函数对参数是有要求的，它需要输入一个库文件的名字，并且这个文件名要在一下目录中

1）和jre相关的一些目录

2）程序当前目录

3）Windows目录

4）系统目录（system32）

5）系统环境变量path指定目录

而我们的文件是在resource里面所有不能直接这样处理。  
````
static{
....
....
System.loadLibrary("InputManager_64");
}
````
看看下面这段代码，我对resource里面的文件做了一下处理。如果java.library.path里面没有这个文件，那么我们就把它写进去。
````
static {

        try {
            String libpath = System.getProperty("java.library.path");
            if (libpath == null || libpath.length() == 0) {
                throw new RuntimeException("java.library.path is null");
            }
 
            String path = null;
            StringTokenizer st = new StringTokenizer(libpath, System.getProperty("path.separator"));
            if (st.hasMoreElements()) {
                path = st.nextToken();
            } else {
                throw new RuntimeException("can not split library path:" + libpath);
            }
 
            URL resource = InputManagerJni.class.getResource("/InputManager_64.dll");
            InputStream inputStream = resource.openStream();
            final File dllFile = new File(new File(path), "InputManager_64.dll");
            if (!dllFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(dllFile);
                byte[] array = new byte[8192];
                for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
                    outputStream.write(array, 0, i);
                }
                outputStream.close();
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    if (dllFile.exists()) {
                        boolean delete = dllFile.delete();
                        System.out.println("delete : " + delete);
                    }
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException("load jacob.dll error!", e);
        }
        System.loadLibrary("InputManager_64");
    }
````
这样我们的第一个功能就做完了。经过测试，没啥问题。

## 3.检测代码上下文切换输入法
之前我们实现了根据快捷键切换输入法，现在我们要根据代码上下文来动态的切换了。就像开头的视频那样。

首先我们了解一个东西：PSI，这个是框架提供的一套API，代码分析会用到。因为这里只涉及到分析是不是注释代码，所以比较简单，要深入了解还得看看官方文档， 这里提供一个博客地址。

编写一个IDEA插件之：使用PSI分析Java代码 - 腾讯云开发者社区-腾讯云
PSI是Program Structure Interface的缩写，即程序结构接口。
https://cloud.tencent.com/developer/article/1731496
直接上代码吧！

看看这个先，MyProjectManagerListener.java 这个类负责监听项目的一下变化，比如项目打开后，我们注册了一些文档变化的监听（这个并没有使用），光标移动的监听等等。还让输入法变成了英文。这个类学

![img_10.png](IdeaProjects/myplugin/img/img_10.pngts/myplugin/img/img_10.png)

这个类需要在Plugin.xml文件里面进行注册

![img_11.png](IdeaProjects/myplugin/img/img_11.pngts/myplugin/img/img_11.png)
下面这个是CareListener的一个实现类，他在上面的ProjectManager类里面进行了注册。当光标移动时会回调里面的caretPositionChanged方法，然后我们再去做光标处相应的代码分析。
```
package com.bully.myplugin.listener;

import com.bully.myplugin.JudgeService;
import com.bully.myplugin.Process;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import org.jetbrains.annotations.NotNull;

public class PfcCaretListener implements CaretListener{
int CaretIndex;

    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
        Process.caretPositionChanged(event);
    }

}
```
现在我们能够监听光标变化了但是我们通过CaretEvent对象其实拿不到输入的字符和PSIFile对象，也就是是我们并不能预测用户输入和获取代码上下文。当然我们也可以通过各种if判断来确定当前光标是否处于注释下。不过这里使用Psi的api更简单。

其实要解决上面的问题我们只需要能够监听键盘输入就行了

首先在plugin.xml里面注册监听类

![img_12.png](IdeaProjects/myplugin/img/img_12.pngts/myplugin/img/img_12.png)

然后实现监听类，这里继承了TypedHandlerDelegate也就是当编辑器检测到输入字符后会首先调用该类进行处理，然后再写入docment里面；

这里面涉及的关系有 Edior（编辑器） Docment（文档，就是你写的代码） Caret（光标）。

我们要做的就是当编辑器输入的时候拿到字符进行判断，比如是不是中文啥的，如果是中文并且是没有在注释环境，也没有在表达式里面，那么这个输入可能是用户想要进行注释。这时我们要在光标前面给文档插入 "//"，或者其他注释符号。

注意： 可能有人想直接监听文档（Docment）对象的改变，然后获取改变的值进行判断，其实这样是有问题的，因为文档监听器（DocmentListener）是用来监听变化的而不是用来产生变化的，否则这会引发一个嵌套的修改异常。
```
package com.bully.myplugin;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
* This is a custom {@link TypedHandlerDelegate} that handles actions activated keystrokes in the editor.
* The execute method inserts a fixed string at Offset 0 of the document.
* Document changes are made in the context of a write action.
  */
  class MyTypedHandler extends TypedHandlerDelegate {

  @Override
  public @NotNull Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file, @NotNull FileType fileType) {
  //        JudgeService.beforeJudge(c,project,editor,fileType);
  if (fileType instanceof JavaFileType) {
  Process.process(String.valueOf(c), editor, file);
  }
  return Result.CONTINUE;
  }


    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        JudgeService.judgement(c,project,editor,file);
//        new Process().process(String.valueOf(c),editor,file);
return Result.STOP;
}

}`

最后是Process类，这个类做了具体的判断逻辑。这段代码比较长，但是思路其实比较简单，代码太丑了不想说，应该是能看明白的，不过由于我自己也是第一次写插件，很多都不会，然后资料啥的也比较少，所以哇这个bug还是超级多的，只能说勉强能用。
`
package com.bully.myplugin;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.tree.IElementType;

public class Process {
static Editor mEditor;
static PsiFile mPsiFile;
static PsiElement mPsiElement;
static Document mDocument;
static Project mProject;
static int offset;
static int lineNumber;
static String mInputChar;
static int activeTimes = 0;
static boolean processed = false;
static int deleteLine = -1;

    public static void caretPositionChanged(CaretEvent event) {
        if (processed) {
            return;
        }
        mEditor = event.getEditor();
        if (mPsiFile == null) return;
        init(mInputChar, mEditor, mPsiFile);
        if (mPsiFile == null || mPsiElement == null) return;
        //添加注释
        if (!isNeedTrans()) {
            return;
        }
        if (getLineText().isBlank()) {
            if (isNewLine(event)) {
                InputManagerJni.getSingleton().any2English();
            }
        }
        if (isNewLine(event)) {
            activeTimes = 0;
        }
        if (activeTimes != 0) {
            return;
        }
        if (isComment()) {
            InputManagerJni.getSingleton().any2Chinese_1();
            if (mInputChar != null) activeTimes++;
        } else {
            InputManagerJni.getSingleton().any2English_1();
            if (mInputChar != null) activeTimes++;
        }
 
    }
 
    static void process(String inputChar, Editor editor, PsiFile psiFile) {
        processed = false;
        InputManagerJni.doTrans = true;
        mInputChar=inputChar;
        //初始化成员
        init(inputChar, editor, psiFile);
        boolean containChinese = Util.isContainChinese(inputChar);
        if (containChinese) {
            InputManagerJni.doTrans = false;
            InputManagerJni.status0 = InputManagerJni.status;
            InputManagerJni.status = 0;
            //添加行注释
            if (!(mPsiElement instanceof PsiIdentifier)) {
                mPsiElement = psiFile.findElementAt(offset - 1);
                if (mPsiElement == null) {
                    return;
                }
            }
            if (mPsiElement.getParent() instanceof PsiErrorElement) {
                int startOffsetInParent = mPsiElement.getTextOffset();
                mDocument.insertString(startOffsetInParent, "//");
                processed = true;
                return;
            }
            if ((mPsiElement instanceof PsiIdentifier)) {
                int startOffsetInParent = mPsiElement.getTextOffset();
                PsiElement parent = mPsiElement.getParent();
                if (parent.getNextSibling() instanceof PsiErrorElement) {
                    mDocument.insertString(startOffsetInParent, "//");
                    processed = true;
                    return;
                }
                if (parent instanceof PsiJavaCodeReferenceElement) {
                    if (parent.getParent().getNextSibling() instanceof PsiErrorElement) {
                        mDocument.insertString(startOffsetInParent, "//");
                        processed = true;
                    }
                }
            }
        } else {
            InputManagerJni.status0 = InputManagerJni.status;
            InputManagerJni.status = 1;
            InputManagerJni.doTrans = true;
        }
 
 
    }
 
    private static void init(String inputChar, Editor editor, PsiFile psiFile) {
        mEditor = editor;
        mPsiFile = psiFile;
        mDocument = editor.getDocument();
        mProject = editor.getProject();
        CaretModel caretModel = editor.getCaretModel();
        offset = caretModel.getOffset();
        lineNumber = mDocument.getLineNumber(offset);
        mPsiElement = mPsiFile.findElementAt(offset);
    }
 
    private static boolean isComment() {
        return isLineEndCommented() || isMutiLineComment();
    }
 
    private static boolean isNeedTrans() {
        if (mPsiElement == null) {
            return false;
        }
        //是表达式
        if (mPsiElement.getContext() instanceof PsiLiteralExpressionImpl) {
            return false;
        }
        if (mPsiElement instanceof PsiJavaToken) {
            IElementType tokenType = ((PsiJavaToken) mPsiElement).getTokenType();
            return !(tokenType.getDebugName().equals("STRING_LITERAL"));
        }
 
        return true;
    }
 
    private static String getLineText() {
        int start = mDocument.getLineStartOffset(lineNumber);
        String line = mDocument.getText(TextRange.from(start, offset - start));
        return line.replaceAll("\t", " ").trim();
    }
 
    //判断当前上下文是否属于行注释环境
    private static boolean isLineEndCommented() {
        String lineText = getLineText();
        if (lineText.isBlank() || mDocument.getLineStartOffset(lineNumber) == offset) {
            return false;
        }
        if (lineText.endsWith("//") && !lineText.endsWith("*//")) {
            return true;
        }
        if (lineText.startsWith("//")) {
            return true;
        }
        int lineStartOffset = mDocument.getLineStartOffset(lineNumber);
        for (int i = offset - 1; i >= lineStartOffset; i--) {
            if (mPsiElement instanceof PsiComment) {
                return mPsiElement.getTextOffset() != offset;
            } else {
                mPsiElement = mPsiFile.findElementAt(i);
            }
        }
        return false;
    }
 
    //是否是多行注释，包括doc注释
    private static boolean isMutiLineComment() {
        if (getLineText().startsWith("*/")) {
            return false;
        }
        String lineText = getLineText();
        if (lineText.isBlank() || mDocument.getLineStartOffset(lineNumber) == offset) {
            return false;
        }
        if (mPsiElement instanceof PsiDocComment || mPsiElement.getContext() instanceof PsiDocComment) {
            return true;
        }
        if (mPsiElement instanceof PsiDocTag || mPsiElement.getContext() instanceof PsiDocTag) {
            return true;
        }
        if (mPsiElement instanceof PsiDocToken || mPsiElement.getContext() instanceof PsiDocToken) {
            return true;
        }
        if (mPsiElement instanceof PsiComment) {
            IElementType tokenType = ((PsiComment) mPsiElement).getTokenType();
            return tokenType.getDebugName().equals("C_STYLE_COMMENT");
        }
        PsiElement context = mPsiElement.getContext();
        if (context instanceof PsiComment) {
            IElementType tokenType = ((PsiComment) context).getTokenType();
            return tokenType.getDebugName().equals("C_STYLE_COMMENT");
        }
        return false;
    }
    //是否是新行
    private static boolean isNewLine(CaretEvent event) {
        int oldLine = event.getOldPosition().line;//不管这样是的
        int newLine = event.getNewPosition().line;
        return oldLine != newLine;
    }
}
```

最后就是打包了，执行相应gradel脚本就行，具体百度，很简单，打包后就可以安装到你的ide上了。

注意：上面那段代码，有很多逻辑不合理的地方，大家看的时候注意分辨。

源代码好像是jdk15的。