package com.bully.myplugin;

import com.bully.myplugin.jni.InputManagerJni;
import com.bully.myplugin.listener.PfcCaretListener;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.DocumentUtil;
import org.jetbrains.annotations.NotNull;

public class JudgeService {
    static Project mProject;
    static Editor mEditor;
    static PsiFile mPsiFile;
    static int noteModeNormal = 0;
    //doc 注释
    static int noteModeDoc = 1;
    static int offset, oldOffset;
    static int currentCaretIndex = -1;
    //是否是空白行
    static boolean isWitheLine = false;
    static String newFragment;
    static int lineA, lineB, moveLength;
    static int lastLine = Integer.MIN_VALUE;
    static int lastLine1 = Integer.MIN_VALUE;
    static int activeTimes = 0;
    static int activeTimes1 = 0;
    static TextRange blockRange;

    public static void beforeJudge(@NotNull DocumentEvent event, PfcCaretListener pfcCaretListener, Project project) {
        mProject = project;
        Document document = event.getDocument();
        isWitheLine = DocumentUtil.isLineEmpty(document, document.getLineNumber(event.getOffset()));
        oldOffset = event.getOffset() - event.getNewFragment().length();
        //插入之前的行号
        lineA = document.getLineNumber(oldOffset);
    }

    //文档类容发生改变
    public static void beforeJudge(char c, Project project, Editor editor, FileType fileType) {
        mProject = project;
        mEditor = editor;
        Document document = editor.getDocument();
        oldOffset = editor.getCaretModel().getOffset();
        isWitheLine = DocumentUtil.isLineEmpty(document, document.getLineNumber(oldOffset));
        //插入之前的行号
        lineA = document.getLineNumber(oldOffset);
    }

    public static void judgement(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        mEditor = editor;
        mProject = project;
        newFragment = c + "";
        mPsiFile = file;
        //没有注释的中文出现了需要做些处理
        Document document = editor.getDocument();
        //当前输入字符距离文档头部的字符数目
        offset = editor.getCaretModel().getOffset();
        lineB = document.getLineNumber(offset);
        moveLength = offset - oldOffset;
        currentCaretIndex = offset;
        System.out.println("noted：" + isNoted());
        if (isBlockNote()) {
            if (blockRange!=null&&blockRange.containsOffset(offset)&&activeTimes1==0) {
//                光标在注释内
                InputManagerJni.getSingleton().any2Chinese_1();
                activeTimes1++;
            }
            if (blockRange!=null&&!blockRange.containsOffset(offset)){
                InputManagerJni.getSingleton().any2English_1();
                activeTimes1=0;
            }

            blockRange=null;
            return;
        }

//       没有被注释 且输入的是英文 不管
        if (!Util.isContainChinese(newFragment)) {
            return;
        }
        System.out.println("输入的是中文");
        //获取当前光标下前面2字符包括刚刚输入的字符
//        int firstNonSpaceCharOffset = DocumentUtil.getFirstNonSpaceCharOffset(document, lineNumber);
//        String pre2str = document.getText(TextRange.from(Math.max(offset - 1, firstNonSpaceCharOffset), 2));
        //添加合适的注释
        addLineNotes();

    }

    //光标位置发生改变
    public static boolean isNoted() {
        return isLineNote() || isBlockNote();
    }


    public static void caretPositionChanged(CaretEvent event) {
        Caret caret = event.getCaret();
        if (caret == null) return;
        currentCaretIndex = caret.getOffset();
        mEditor = event.getEditor();
        Document document = mEditor.getDocument();
        int lineNumber = document.getLineNumber(currentCaretIndex);
        System.out.println(lineNumber);
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        String lineStr = document.getText(TextRange.from(lineStartOffset, currentCaretIndex - lineStartOffset));
        System.out.println(lineStr);
        boolean lineNote = isLineNote();
        System.out.println("lineNote :" + lineNote);
        if (lineNote) {
            //鼠标在不同一行 并且 第一次换行时，切换，后面不切换
            if (lastLine != lineNumber || activeTimes == 0) {
                InputManagerJni.getSingleton().any2Chinese_1();
                activeTimes++;
            }
            //当换行后有重置
            if (lastLine != lineNumber) {
                activeTimes = 0;
            }
        } else {
            InputManagerJni.getSingleton().any2English_1();
        }
        lastLine = lineNumber;
    }

    /**
     * 添加行注释
     * 如果再空白行输入中文会自动再行首添加注释
     */
    private static void addLineNotes() {
        Boolean enableLineNote = Util.geBooleanSetting("enableLineNote");
        if (!enableLineNote) {
            return;
        }
        noteLineText();
    }

    private static boolean isLineNote() {
        Document document = mEditor.getDocument();
        int lineNumber = document.getLineNumber(currentCaretIndex);
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        String lineStr = document.getText(TextRange.from(lineStartOffset, currentCaretIndex - lineStartOffset));
        // 包含*/// 也是注释
        String[] split1 = lineStr.split("\\*///");
        if (split1.length > 1) {
            return true;
        }
        if (lineStr.contains("*//")) {
            return false;
        }
        return lineStr.contains("//");
    }

    private static boolean isBlockNote() {
        Document document = mEditor.getDocument();
        String lineStr = document.getText(DocumentUtil.getLineTextRange(document, lineB));
        PsiElement elementAt = mPsiFile.findElementAt(offset);
        PsiElement context = elementAt.getContext();
        if (context == null) {
            return false;
        }
        if ((context instanceof PsiComment)) {
             blockRange = context.getTextRangeInParent();
            System.out.println(((PsiComment) context).getTokenType());
            System.out.println(blockRange);
            return true;
        }
        return false;
    }

    private static void noteLineText() {
        Document document = mEditor.getDocument();
        //注释模式
        int noteMode = Util.getIntegerSetting("noteMode");
        int lineStartOffset = getStartIndex();
        if (noteMode == noteModeNormal) {
            insertString(document, lineStartOffset, "//");
        } else if (noteMode == noteModeDoc) {
            run(() -> {
                document.insertString(document.getLineStartOffset(Math.max(0, lineB)), "\n/**\n* " + newFragment + "\n*/");
                CaretModel caretModel = mEditor.getCaretModel();
                int newOffset = caretModel.getOffset();
                document.deleteString(newOffset - 1, newOffset);
                caretModel.moveToOffset(document.getLineEndOffset(lineB + 2));
            });
        }
    }

    private static int getStartIndex() {
        Document document = mEditor.getDocument();
        if (isWitheLine) {//空白行直接在头部
            return document.getLineStartOffset(lineB);
        } else {//不是空白行要综合判断
            //一行文本
            String lineStr = document.getText(DocumentUtil.getLineTextRange(document, lineB));
        }
        return 0;
    }

//    /**
//     * 注释一段文字
//     */
//    private static void noteBlockText(Document document) {
//        //注释模式
//        int noteMode = Util.getIntegerSetting("noteMode");
//        if (noteMode == noteModeNormal) {
//            for (int i = lineA; i <= lineB; i++) {
//                insertString(document, document.getLineStartOffset(i), "//");
//            }
//        } else if (noteMode == noteModeDoc) {
//            insertString(document, document.getLineStartOffset(Math.min(0, lineA)), "/**\n");
//            for (int i = lineA; i <= lineB; i++) {
//                insertString(document, document.getLineStartOffset(i), "*");
//            }
//            insertString(document, document.getLineStartOffset(lineB), "\n/*");
//        }
//    }

    public static void insertString(Document document, int offset, String text) {
        Runnable runnable = () -> document.insertString(offset, text);
        // Make the document change in the context of a write action.
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }

    public static void run(Runnable runnable) {
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }

//    private static boolean isBlockText(Document document) {
//        //插入之前的行号                                  插入之后的行号
//        return document.getLineNumber(offset) != document.getLineNumber(oldOffset);
//    }

    public static void deleteString(Document document) {
        Runnable runnable = () -> document.deleteString(oldOffset, offset);
        // Make the document change in the context of a write action.
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }


}