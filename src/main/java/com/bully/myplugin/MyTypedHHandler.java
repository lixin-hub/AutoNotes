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

}