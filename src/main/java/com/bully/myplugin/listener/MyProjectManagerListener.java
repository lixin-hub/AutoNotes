package com.bully.myplugin.listener;

import com.bully.myplugin.jni.InputManagerJni;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorGutterAction;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.event.EditorEventListener;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;


public class MyProjectManagerListener implements ProjectManagerListener  {
    private final Object pfcCaretListener = new PfcCaretListener();

    @Override
    public void projectOpened(@NotNull Project project) {
        EditorFactory instance = EditorFactory.getInstance();
        EditorEventMulticaster eventMulticaster = instance.getEventMulticaster();
        eventMulticaster.addCaretListener((CaretListener) pfcCaretListener);
        eventMulticaster.addDocumentListener((DocumentListener) pfcCaretListener);
        InputManagerJni.getSingleton().any2English_1();
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        EditorEventMulticaster eventMulticaster = EditorFactory.getInstance().getEventMulticaster();
        eventMulticaster.removeCaretListener((CaretListener) pfcCaretListener);
        eventMulticaster.removeDocumentListener((DocumentListener) pfcCaretListener);
    }
}
