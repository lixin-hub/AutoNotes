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
