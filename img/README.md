# AutoNotes
���Ǽ�����ǰд��Idea�Զ�ע�Ͳ����ʵ�ֵ�Ч��������idea������Ҫע�͵�ʱ���Զ��л�Ϊ�������뷨��д�����ʱ���л�ΪӢ�����뷨
��ʱд��һƪ���� ���⣺

https://blog.csdn.net/qq_39635776/article/details/126559518  

����ֱ�ӿ������Ҳ�У��Ҹ��ƹ�����
��ʱ���Ǹ������ϴ���github�ˣ�����żȻ�����������м���С�ֵ������ۣ�����������ϴ�����Ǹ��

# ������ʹ��  
һ��ͼ˵������  
![img_14.png](IdeaProjects/myplugin/img/img_14.pngts/myplugin/img/img_14.png)

��������ⲻ֪��������������û���������Ǿ���������f...

д�����ⲻ��Ҫдע�ͣ�����ע�͵�ʱ�����Ǿ���Ҫ�ڴ�������ע���������л������������Ȼ���Ƽ򵥵���ż��Ҳ���Щ������統ǰ���뷨������Ӣ������ʱҪ���л����������뷨�Ͳ���ʹ��shift��ݼ��ˣ�����Ҫͨ�� win+�ո����ctrl+shift���л����뷨��������԰�װ�˶�����뷨�����ܻ���Ϊ�ڸ������뷨֮����л����˷�ʱ�䣬���ⲿ��ʱ����Ӧ�������㡣

# ����
��дһ������ܹ��Զ�ʶ����������ģ��ж�coder��Ҫ���еĲ������Զ��л����뷨�����統����Ƶ��˴��������������뷨�л�ΪӢ�ģ�������ƶ���ע�����������뷨�л�Ϊ���ġ�



�����������һ����ŵĹ�����ʾ

��ʾ-CSDNֱ��
https://live.csdn.net/v/235592

# ������ʵ���Է���
���ܵ���Բ��Ϊ��idea������壨���ܣ������뷨�л��������������жϡ�

ͨ���������Ϸ���Ҫʵ�����Ϲ�����Ҫ��ѧ��idea�������������ûѧ��Ҳ�ܿ������ģ���windows���뷨����ԭ���˽⣩��idea��������е�PSI��ܣ�ʹ��ĳЩAPI����������ȡ���������ģ���

ʵ�ֵĹ�����Ȼ�ܼ򵥲���Ҫ���ǵ�ϸ����ʵ���ǱȽ϶�ģ�����java docע�ͺ�java��ע���������������Ե�ע�ͣ����⻹Ҫ����ϵͳ�����뷨�Ĺ���ʽ���Ѽ���ͬƽ̨�����䡣����Ϊ�˼��������ֻ����Windows 64λ����ϵͳ��java���룬����ֻʵ�ֻ������ܡ�Ȼ�����������ȫ���Կ���ҵĴ��������ˣ������Щ�˹�����ɶ�ģ��������������

���������Լ��������Ͽ�����ȷ�����ܹ���ʵ�֣���ô���ǽ�����һ������ɣ�

# ǰ��֪ʶ
���ڰ����漰����һЩ����֪ʶ������windows��̣�jni��PSI�ȵ�����������ͳһ�򵥽���һ�顣
## 1. ���뷨�л�ԭ��
   �ο����ͣ�https://blog.csdn.net/fengbangyue/article/details/7346333  
�ò��ʹ�Ž�����һЩ����������ͨ������ϵͳ��������ʵ�����뷨�л���

�ο�MSDN����Ӧ������˵�����������ʵ�����뷨�л��Ŀ��Կ�MSDN��ϸ˵������Ϊ��Ҳûѧϰ��windows�����ؼ����������Ҹտ�ʼ��ʱ��Ҳ��Ӳ��ͷƤ���������ĵ��Ľ����ҵ��ؼ��ļ���������Ȼ����Ӧ�ĺ���˵���������ͷ���ֵ�ȵȡ�

ActivateKeyboardLayout (Windows CE 5.0) | Microsoft Docs
https://docs.microsoft.com/en-us/previous-versions/windows/embedded/aa452845(v=msdn.10)

��Ϊ���ʹ��java�����д����java����������������ϵ�����java����û���ṩ�������뷨��صĺ������������Ҫ�ȰѲ������뷨�ĺ�����װ�ã�Ȼ������jni������java�������C������������ʵ���˵�һ�������㣬�л����뷨��

�����Ǵ���չʾ������ͷ�������е���ֲ��ù�����������ͣ���Ҫ���ǵ�������������������ʵ���л���������Ψһ�ģ�ֻ�����бȽϼ򵥵�һ�֣�

```
#include "com_bully_myplugin_jni_InputManagerJni.h"
#include "Windows.h"

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2Chinese(JNIEnv *env, jobject obj) {
//��ȡ��ǰ����
HWND hwnd = GetForegroundWindow();
//�����뷨�ȸ�ΪӢ��
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
//�����뷨�޸�Ϊ����
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
}

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2English(JNIEnv *env, jobject obj) {
//��ȡ��ǰ����
HWND hwnd = GetForegroundWindow();
//�����뷨�ȸ�Ϊ����
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
//�����뷨�޸�ΪӢ��
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
}```

C���Դ���Ҳ�̵ܶģ�

��Ȼ������ֱ�ӵķ��������򵥵Ļ�����ͨ��python�ű�ʵ�����뷨���л���������ʵ���ʶ���һ���ġ�
```
# ����һ��ʾ�� Python �ű���
```import ctypes

import win32api
import win32gui
from win32con import WM_INPUTLANGCHANGEREQUEST


def set_english_inputer():
# 0x0409ΪӢ�����뷨��lid_hex�� ����һ��Ϊ0x0804
name=win32api.GetKeyboardLayoutName()
state=win32api.GetKeyboardState()
print("state",state)
print("����",name)
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
print("Ӣ�����뷨�л��ɹ���")


set_english_inputer()
```
����java������ԭ�򣬲�����ʹ��python����C���Ƕ�Ҫ�������ڲ���е�����������ִ�е����⡣

## 2.ʲô��Jni
�����ᵽ�˿����Ե����⣬�������Ǿ��������

JNI��Java Native Interface����д��ͨ��ʹ�� Java���ؽӿ���д���򣬿���ȷ�������ڲ�ͬ��ƽ̨�Ϸ�����ֲ�� [1]  ��Java1.1��ʼ��JNI��׼��Ϊjavaƽ̨��һ���֣�������Java�������������д�Ĵ�����н�����JNIһ��ʼ��Ϊ�˱����ѱ������ԣ�������C��C++����Ƶģ�����������������ʹ������������ԣ�ֻҪ����Լ����֧�־Ϳ����ˡ�ʹ��java�뱾���ѱ���Ĵ��뽻����ͨ����ɥʧƽ̨����ֲ�ԡ����ǣ���Щ������������ǿ��Խ��ܵģ������Ǳ���ġ����磬ʹ��һЩ�ɵĿ⣬��Ӳ��������ϵͳ���н���������Ϊ����߳�������ܡ�JNI��׼����Ҫ��֤���ش����ܹ������κ�Java �����������

**��˵���� ���ṩ�ṩ��һ����׼��javaֻд����������c����дʵ�֣�����java�����hash()��������native���������java�õ���һ��dll�ļ�������ļ���C����д�ĺ�������װ��������ļ����档java����������dll֮��Ϳ���ͬ��java native�������������c������**

jni��д���裺

1. ��д����native�����ķ�����java��

2. ʹ��javac�����������д��java��

3. Ȼ��ʹ��javah + java����������չ��Ϊh��ͷ�ļ�

4. ʹ��C/C++ʵ�ֱ��ط���

5. ��C/C++��д���ļ����ɶ�̬���ӿ�

6. java�������dll��̬���ӿ�

7. ����native����

�����ǲ��ǲ���ͦ�����ʵһ��һ�������߻�ܼ򵥣����õ��ģ�����ֻ�������߹���������ѧ��Ŷ��

��ϸ����Ͳ���������������ܡ�����jni������Ҫ��ϸ�˽�����������ٶȰٿơ�

JNI_�ٶȰٿ�
JNI��Java Native Interface����д��ͨ��ʹ�� Java���ؽӿ���д���򣬿���ȷ�������ڲ�ͬ��ƽ̨�Ϸ�����ֲ����Java1.1��ʼ��JNI��׼��Ϊjavaƽ̨��һ���֣�������Java�������������д�Ĵ�����н�����JNIһ��ʼ��Ϊ�˱����ѱ������ԣ�������C��C++����Ƶģ�����������������ʹ������������ԣ�ֻҪ����Լ����֧�־Ϳ����ˡ�ʹ��java�뱾���ѱ���Ĵ��뽻����ͨ����ɥʧƽ̨����ֲ�ԡ����ǣ���Щ������������ǿ��Խ��ܵģ������Ǳ���ġ����磬ʹ��һЩ�ɵĿ⣬��Ӳ��������ϵͳ���н���������Ϊ����߳�������ܡ�JNI��׼����Ҫ��֤���ش����ܹ������κ�Java �����������

https://baike.baidu.com/item/JNI/9412164  
��ʹ��jni������ʵ����java�����е���c/c++��������Ȼ�����ʹ��PythonҲ����������������ģ��������ﲻ���룬�ṩһ�����Ӱɣ�

https://www.jianshu.com/p/4281ce5e137f

# ��ʼ����
## 1.������Ŀ
�ο����ͣ�https://blog.csdn.net/sawiii/article/details/105952995

������Ŀ��������ͼ򵥽������������ϱȽ϶ࡣ��Ҫע����ǲ������Gradel���й���������ȷ������ȷ������Gradel��

��һ���Ǵ��������Ŀ�������������Ʒ���jdk�汾�������ϤkotlionҲ����ѡ��ʹ��kotlin���Կ�����

![img_1.png](IdeaProjects/myplugin/img/img_1.pngcts/myplugin/img/img_1.png)

����������˲����Ŀ�Ŀ�ܡ��ǲ��Ǻܼ򵥣�����

������Ŀ¼�ṹ���Լ��Ĵ��붼д��src���棬Ȼ��resource�����plugin.xml��idea�Զ������Ĳ�������ļ���������android���嵥�ļ������Ժ��������кܶ������

![img_2.png](IdeaProjects/myplugin/img/img_2.pngcts/myplugin/img/img_2.png)

����Ŀǰ���Ǵ�����һ��java��Ŀ���ѣ����������ǰ���֮ǰ��˼·����������

## 2.���뷨�л����ܵ�ʵ��
��������Ҫʵ�����뷨����Ҫ��һ�����ʵ�ʱ���Զ��л������ʱ�������Ǽ������룬����ƶ�����ݼ������ȵȡ���ô������������ÿ�ݼ�ʵ��һ�����뷨�л��Ĺ��ܣ�Ȼ��������չ��

��ͼ���������ڱ༭���Ҽ��˵���ʾ2�˸���ݼ����ܣ������ǰ��¶�Ӧ��ݼ�ʱ���ó����л�����Ӧ���뷨��

![img_3.png](IdeaProjects/myplugin/img/img_3.pngcts/myplugin/img/img_3.png)

������plugin.xml����ע��action�Ϳ�ݼ�������class����ָ����action�Ĵ����࣬�������Ҫ�����Լ����壬add-to-group��ǩ��������action�˵�λ�õģ�������������EditorPopupMenuҲ���Ǳ༭���Ҽ������˵�����ݼ�ʱͨ��keyboard-shortcut��ǩ���õģ�ע����ǿ�ݼ���Ҫ���Ѿ����ڵĿ�ݼ���ͻ��
```
  <!-- action���ã���ťչʾ��������Ҫ�������� -->
    <actions>
        <action id="transEn" class="com.bully.myplugin.listener.TransEnAction" text="�л�Ӣ������"
                description="ctrl+shift+X �л�ΪӢ��">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl shift X"/>
        </action>
        <action id="transCh" class="com.bully.myplugin.listener.TransChAction" text="�л���������"
                description="ctrl+shift+D �л�Ϊ����">
            <add-to-group group-id="EditorPopupMenu" anchor="first"/>
            <keyboard-shortcut keymap="$default" first-keystroke="ctrl shift D"/>
        </action>
    </actions>
```
ok������ע����action��Ϳ�ݼ����������ǾͶ�action����ʵ�֡�

����Ŀ�ṹ���Ұ����뷨�л���������2�������棬һ��ת��Ϊ���ģ�һ��ת��ΪӢ�ġ�

![img_4.png](IdeaProjects/myplugin/img/img_4.pngcts/myplugin/img/img_4.png)

���ǿ���TransEnAction��������ݡ�
```
package com.bully.myplugin.listener;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;

public class TransEnAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("��ݼ��л�ΪӢ��");
        InputManagerJni.getSingleton().any2English_1();
    }
}
```


����Ƚ϶̣��򵥷����¡��������ǵ�action��Ҫ�̳�AnAction�࣬Ȼ����дactionPerformed�������÷������ڿ�ݼ��������Զ����á��ص�����������һ��AnActionEvent��������������ǳ����ã������õ���ǰ��Ϊ�ĸ�����Ϣ����������û���õ���

������������ֻд��һ�д��� :InputManagerJni.getSingleton().any2English_1();�ô��������һ�����뷨ת���ķ��������ת��������ͨ��jni��ʽʵ�ֵġ����������Ƿ���һ��InputManager��������ɶ ��
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
* @description ͨ��jni ����win32 api ʵ�����뷨�л�
* @date 2022/8/11 13:11
  */
  public class InputManagerJni {
  //1 -english
  public static int status=1;
  public static int status0=1;
  //�Ƿ����ת��
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
    * ת��ΪӢ�����뷨
      */
      public native void any2English();

  /**
    * ��ǰ���뷨 0-����,1-Ӣ��,2-����
      */
      public native int state();

  /**
    * ��ȡ���뷨�б�-����
      */
      public native String list();

  /**
    * �Ƴ����뷨 0-����,1-Ӣ��,-����
      */
      public native String remove(int flag);

  /**
    * ת��Ϊ�������뷨
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
���Կ�����ʵInputManagerJni������ֻ����������Ӧ������δ��ʵ�֡�
```
public native void any2Chinese();
/**
* ת��ΪӢ�����뷨
*/
public native void any2English();
��ʱ�����һ��ǰ��֪ʶ������ܵ�Jni�������ݣ���ʵ�����native������Ӧ������ͷ�ļ��ĺ���������

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
���ͷ�ļ���ͨ�����Զ����ɵġ����巽���ǣ�

1. ��javac��InputManagerJni.java�ļ�����ΪInputManagerJni.class��

2.��javah��InputManagerJni.class����Ϊcom_bully_myplugin_jni_InputManagerJni.h������������Զ����ɵģ�

ok,��������ͨ��java�ļ�������ͷ�ļ�������������Ӧ��Ҫ��д����ʵ���ˡ�������ʹ����Clion������C���Եı�д���������������ʹ���Լ�ϰ�ߵ�ide���б�д��

���ȿ���Clion�Ļ������ã������ҵ�Ŀ¼�ṹ��ʹ��Cmake���й�����

![img_5.png](IdeaProjects/myplugin/img/img_5.pngcts/myplugin/img/img_5.png)

��ǰ���õĹ�����

![img_6.png](IdeaProjects/myplugin/img/img_6.pngcts/myplugin/img/img_6.png)

Cmake���ã������и�Cmakeѡ���ɫ��򣩣������ʹ�õ���vs2022 ��������д������vs�汾�������ϲ飬���й��������ʾ�ҹ�������64λ�Ķ�̬���ӿ⡣

![img_7.png](IdeaProjects/myplugin/img/img_7.pngcts/myplugin/img/img_7.png)

���⻹��һ�㣬��Ϊ����Ҫ����windows api���԰�װvs��ʱ��ǵ�����MFC

![img_8.png](IdeaProjects/myplugin/img/img_8.pngcts/myplugin/img/img_8.png)

��Ŀ¼�µ� cmakeLists�ļ�
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
srcĿ¼�µ�CmakeLists�ļ�

project(InputManager)
add_library(InputManager SHARED  InputManager.c)`
InputManager.c�ļ� ������ʵ�����л��Ĵ��룬
````
����һ��Ϊɶת������Ҫ�ȵ���ת��Ӣ�ĵĺ���:��Ϊ����ٶ����뷨�����������뷨�������ǰ���shift��������ľ���Ӣ�ģ����������Ҫ�л�Ϊ����״̬�Ͳ���ֱ�ӵ��ú�������ΪӢ��״̬�İٶ����뷨��ʵҲ��0x0804������Ҫ��ת��ΪӢ�����뷨����ת��Ϊ�������뷨����������������Լ���ģ��������������������Ҷ�windows��������Ϥ���Ծ����������ˣ���ͬ��Ӣ��Ҳ��һ���ġ�
```
//
// Created by LIXIN on 2022/8/10.
//

#include "com_bully_myplugin_jni_InputManagerJni.h"
#include "Windows.h"

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2Chinese(JNIEnv *env, jobject obj) {
//��ȡ��ǰ����
HWND hwnd = GetForegroundWindow();
//�����뷨�ȸ�ΪӢ��
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
//�����뷨�޸�Ϊ����
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
}

JNIEXPORT void JNICALL Java_com_bully_myplugin_jni_InputManagerJni_any2English(JNIEnv *env, jobject obj) {
//��ȡ��ǰ����
HWND hwnd = GetForegroundWindow();
//�����뷨�ȸ�Ϊ����
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0804);
//�����뷨�޸�ΪӢ��
SendMessageA(hwnd, WM_INPUTLANGCHANGEREQUEST, 0, 0x0409);
}
```

���������д���ˣ�������й����ͻ�����dll�ļ���������64λ�ģ�������Cmake����Ϥ�Ŀ���ֱ���ѽ̳����Ϻܶ࣬��Ҳ���Ǻܶ��Ͳ����ˣ����������ͦ��ģ�

�ص�IDEA����dll�ļ��ŵ�resourceĿ¼����

![img_9.png](IdeaProjects/myplugin/img/img_9.pngcts/myplugin/img/img_9.png)

�������������Ƕ�����ɶ��

���Ƕ�����native���������һ���jni��������ʵ�֣�����dll�ļ����뵽��java��ĿresourceĿ¼���棬��ô���������Ǿ���Ҫ���ؿ��ˡ�

���صĴ���ܼ򵥣�ֻ��Ҫһ�д��룬�������loadLibary�����Բ�������Ҫ��ģ�����Ҫ����һ�����ļ������֣���������ļ���Ҫ��һ��Ŀ¼��

1����jre��ص�һЩĿ¼

2������ǰĿ¼

3��WindowsĿ¼

4��ϵͳĿ¼��system32��

5��ϵͳ��������pathָ��Ŀ¼

�����ǵ��ļ�����resource�������в���ֱ����������  
````
static{
....
....
System.loadLibrary("InputManager_64");
}
````
����������δ��룬�Ҷ�resource������ļ�����һ�´������java.library.path����û������ļ�����ô���ǾͰ���д��ȥ��
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
�������ǵĵ�һ�����ܾ������ˡ��������ԣ�ûɶ���⡣

## 3.�������������л����뷨
֮ǰ����ʵ���˸��ݿ�ݼ��л����뷨����������Ҫ���ݴ�������������̬���л��ˡ�����ͷ����Ƶ������

���������˽�һ��������PSI������ǿ���ṩ��һ��API������������õ�����Ϊ����ֻ�漰�������ǲ���ע�ʹ��룬���ԱȽϼ򵥣�Ҫ�����˽⻹�ÿ����ٷ��ĵ��� �����ṩһ�����͵�ַ��

��дһ��IDEA���֮��ʹ��PSI����Java���� - ��Ѷ�ƿ���������-��Ѷ��
PSI��Program Structure Interface����д��������ṹ�ӿڡ�
https://cloud.tencent.com/developer/article/1731496
ֱ���ϴ���ɣ�

��������ȣ�MyProjectManagerListener.java ����ฺ�������Ŀ��һ�±仯��������Ŀ�򿪺�����ע����һЩ�ĵ��仯�ļ����������û��ʹ�ã�������ƶ��ļ����ȵȡ��������뷨�����Ӣ�ġ������ѧ

![img_10.png](IdeaProjects/myplugin/img/img_10.pngts/myplugin/img/img_10.png)

�������Ҫ��Plugin.xml�ļ��������ע��

![img_11.png](IdeaProjects/myplugin/img/img_11.pngts/myplugin/img/img_11.png)
���������CareListener��һ��ʵ���࣬���������ProjectManager�����������ע�ᡣ������ƶ�ʱ��ص������caretPositionChanged������Ȼ��������ȥ����괦��Ӧ�Ĵ��������
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
���������ܹ��������仯�˵�������ͨ��CaretEvent������ʵ�ò���������ַ���PSIFile����Ҳ���������ǲ�����Ԥ���û�����ͻ�ȡ���������ġ���Ȼ����Ҳ����ͨ������if�ж���ȷ����ǰ����Ƿ���ע���¡���������ʹ��Psi��api���򵥡�

��ʵҪ����������������ֻ��Ҫ�ܹ������������������

������plugin.xml����ע�������

![img_12.png](IdeaProjects/myplugin/img/img_12.pngts/myplugin/img/img_12.png)

Ȼ��ʵ�ּ����࣬����̳���TypedHandlerDelegateҲ���ǵ��༭����⵽�����ַ�������ȵ��ø�����д���Ȼ����д��docment���棻

�������漰�Ĺ�ϵ�� Edior���༭���� Docment���ĵ���������д�Ĵ��룩 Caret����꣩��

����Ҫ���ľ��ǵ��༭�������ʱ���õ��ַ������жϣ������ǲ�������ɶ�ģ���������Ĳ�����û����ע�ͻ�����Ҳû���ڱ��ʽ���棬��ô�������������û���Ҫ����ע�͡���ʱ����Ҫ�ڹ��ǰ����ĵ����� "//"����������ע�ͷ��š�

ע�⣺ ����������ֱ�Ӽ����ĵ���Docment������ĸı䣬Ȼ���ȡ�ı��ֵ�����жϣ���ʵ������������ģ���Ϊ�ĵ���������DocmentListener�������������仯�Ķ��������������仯�ģ������������һ��Ƕ�׵��޸��쳣��
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

�����Process�࣬��������˾�����ж��߼�����δ���Ƚϳ�������˼·��ʵ�Ƚϼ򵥣�����̫���˲���˵��Ӧ�����ܿ����׵ģ������������Լ�Ҳ�ǵ�һ��д������ܶ඼���ᣬȻ������ɶ��Ҳ�Ƚ��٣����������bug���ǳ�����ģ�ֻ��˵��ǿ���á�
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
        //���ע��
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
        //��ʼ����Ա
        init(inputChar, editor, psiFile);
        boolean containChinese = Util.isContainChinese(inputChar);
        if (containChinese) {
            InputManagerJni.doTrans = false;
            InputManagerJni.status0 = InputManagerJni.status;
            InputManagerJni.status = 0;
            //�����ע��
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
        //�Ǳ��ʽ
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
 
    //�жϵ�ǰ�������Ƿ�������ע�ͻ���
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
 
    //�Ƿ��Ƕ���ע�ͣ�����docע��
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
    //�Ƿ�������
    private static boolean isNewLine(CaretEvent event) {
        int oldLine = event.getOldPosition().line;//���������ǵ�
        int newLine = event.getNewPosition().line;
        return oldLine != newLine;
    }
}
```

�����Ǵ���ˣ�ִ����Ӧgradel�ű����У�����ٶȣ��ܼ򵥣������Ϳ��԰�װ�����ide���ˡ�

ע�⣺�����Ƕδ��룬�кܶ��߼�������ĵط�����ҿ���ʱ��ע��ֱ档

Դ���������jdk15�ġ�