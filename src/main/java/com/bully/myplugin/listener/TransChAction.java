package com.bully.myplugin.listener;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TransChAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("快捷键切换为中文");
        InputManagerJni.getSingleton().any2Chinese_1();
    }
}
