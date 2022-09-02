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
