package com.bully.myplugin;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        System.out.println("test 点击");
    }
}
