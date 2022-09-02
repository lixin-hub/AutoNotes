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
    //doc ע��
    static int noteModeDoc = 1;
    static int offset, oldOffset;
    static int currentCaretIndex = -1;
    //�Ƿ��ǿհ���
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
        //����֮ǰ���к�
        lineA = document.getLineNumber(oldOffset);
    }

    //�ĵ����ݷ����ı�
    public static void beforeJudge(char c, Project project, Editor editor, FileType fileType) {
        mProject = project;
        mEditor = editor;
        Document document = editor.getDocument();
        oldOffset = editor.getCaretModel().getOffset();
        isWitheLine = DocumentUtil.isLineEmpty(document, document.getLineNumber(oldOffset));
        //����֮ǰ���к�
        lineA = document.getLineNumber(oldOffset);
    }

    public static void judgement(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        mEditor = editor;
        mProject = project;
        newFragment = c + "";
        mPsiFile = file;
        //û��ע�͵����ĳ�������Ҫ��Щ����
        Document document = editor.getDocument();
        //��ǰ�����ַ������ĵ�ͷ�����ַ���Ŀ
        offset = editor.getCaretModel().getOffset();
        lineB = document.getLineNumber(offset);
        moveLength = offset - oldOffset;
        currentCaretIndex = offset;
        System.out.println("noted��" + isNoted());
        if (isBlockNote()) {
            if (blockRange!=null&&blockRange.containsOffset(offset)&&activeTimes1==0) {
//                �����ע����
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

//       û�б�ע�� ���������Ӣ�� ����
        if (!Util.isContainChinese(newFragment)) {
            return;
        }
        System.out.println("�����������");
        //��ȡ��ǰ�����ǰ��2�ַ������ո�������ַ�
//        int firstNonSpaceCharOffset = DocumentUtil.getFirstNonSpaceCharOffset(document, lineNumber);
//        String pre2str = document.getText(TextRange.from(Math.max(offset - 1, firstNonSpaceCharOffset), 2));
        //��Ӻ��ʵ�ע��
        addLineNotes();

    }

    //���λ�÷����ı�
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
            //����ڲ�ͬһ�� ���� ��һ�λ���ʱ���л������治�л�
            if (lastLine != lineNumber || activeTimes == 0) {
                InputManagerJni.getSingleton().any2Chinese_1();
                activeTimes++;
            }
            //�����к�������
            if (lastLine != lineNumber) {
                activeTimes = 0;
            }
        } else {
            InputManagerJni.getSingleton().any2English_1();
        }
        lastLine = lineNumber;
    }

    /**
     * �����ע��
     * ����ٿհ����������Ļ��Զ����������ע��
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
        // ����*/// Ҳ��ע��
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
        //ע��ģʽ
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
        if (isWitheLine) {//�հ���ֱ����ͷ��
            return document.getLineStartOffset(lineB);
        } else {//���ǿհ���Ҫ�ۺ��ж�
            //һ���ı�
            String lineStr = document.getText(DocumentUtil.getLineTextRange(document, lineB));
        }
        return 0;
    }

//    /**
//     * ע��һ������
//     */
//    private static void noteBlockText(Document document) {
//        //ע��ģʽ
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
//        //����֮ǰ���к�                                  ����֮����к�
//        return document.getLineNumber(offset) != document.getLineNumber(oldOffset);
//    }

    public static void deleteString(Document document) {
        Runnable runnable = () -> document.deleteString(oldOffset, offset);
        // Make the document change in the context of a write action.
        WriteCommandAction.runWriteCommandAction(mProject, runnable);
    }


}