package editInformation;

import instrument.ChangeNoteName;
import instrument.DeleteNotes;
import instrument.Instrument;
import instrument.Instrument.JTextLimited;
import instrument.Instrument.JTextLimited.limitTypeEnum;
import mainWindow.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

final class EditNotes {
    static String[] notes;
    static JList<String> list;

    protected static void editNotePanel() {
        notes = Instrument.getNotes();
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new BorderLayout());
        list = new JList<>(notes);
        list.setFont(new Font("黑体", Font.BOLD, 18));
        JScrollPane js = new JScrollPane(list);
        js.setBorder(BorderFactory.createTitledBorder("备注项目名称"));
        MainWindow.mainPanel.add(js, "Center");
        JPanel jp1 = new JPanel(new GridLayout(6, 0, 20, 20));
        jp1.setPreferredSize(new Dimension(200, 0));
        MainWindow.mainPanel.add(jp1, "East");
        JPanel jp2 = new JPanel();
        jp2.setPreferredSize(new Dimension(0, 150));
        jp2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MainWindow.mainPanel.add(jp2, "South");
        JButton jb1 = new JButton("增加");
        JButton jb2 = new JButton("修改");
        JButton jb3 = new JButton("删除");
        JButton jb4 = new JButton("返回");
        Instrument.buttonType(jb1);
        Instrument.buttonType(jb2);
        Instrument.buttonType(jb3);
        Instrument.buttonType(jb4);
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        jp1.add(new JLabel());
        jp1.add(new JLabel());
        jp1.add(jb4);
        list.addListSelectionListener((e) -> {
            if (list.getValueIsAdjusting()) {
                Instrument.resetJPanel(jp2);
            }
        });
        jb1.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            list.clearSelection();
            notesPanel(jp2, true, 0);
        });
        jb2.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            int i = list.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(null, "请先选择一个备注项目！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                notesPanel(jp2, false, i);
            }
        });
        jb3.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            int i = list.getSelectedIndex();
            if (i == -1) {
                JOptionPane.showMessageDialog(null, "请先选择一个备注项目！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                Instrument.newConfigurationFile();
                deleteNotes(i);
            }
        });
        jb4.addActionListener((arg0) -> ChooseEditItem.setEditItemPane());
    }

    private static void notesPanel(JPanel jp, boolean which, int selected) {
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 0;
        gb1.gridy = 0;
        gb1.weightx = 6.0D;
        gb1.weighty = 1.0D;
        gb1.fill = 1;
        JTextArea jt = new JTextArea();
        jt.setLineWrap(true);
        if (!which) {
            jt.setDocument(new JTextLimited(200, limitTypeEnum.TEXT));
            jt.setText(notes[selected]);
        } else {
            Instrument.setMarkWords(jt, "在此输入新的备注名（200字以内）", new JTextLimited(200, limitTypeEnum.TEXT));
        }
        jp.add(jt, gb1);
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 1;
        gb2.gridy = 0;
        gb2.weightx = 1.0D;
        gb2.weighty = 1.0D;
        gb2.ipadx = 100;
        gb2.fill = 1;
        JButton jb = new JButton("确定");
        Instrument.buttonType(jb);
        jp.add(jb, gb2);
        GridBagConstraints gb3 = new GridBagConstraints();
        gb3.gridx = 2;
        gb3.gridy = 0;
        gb3.weightx = 1.0D;
        gb3.weighty = 1.0D;
        gb3.ipadx = 100;
        gb3.fill = 1;
        JButton jb2 = new JButton("取消");
        Instrument.buttonType(jb2);
        jp.add(jb2, gb3);
        jb.addActionListener((arg0) -> {
            String text = jt.getText();
            String check = "[^/:*\"<>|?.\\\\]*";
            if (!text.equals("在此输入新的备注名（200字以内）") && !text.equals("")) {
                if (!text.matches(check)) {
                    JOptionPane.showMessageDialog(null, "存在非法字符！", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    String[] var7 = notes;
                    for (String note : var7) {
                        if (note.equals(text)) {
                            JOptionPane.showMessageDialog(null, "备注名称重复！", "提示", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    Instrument.newConfigurationFile();
                    boolean success;
                    if (which) {
                        success = addNotes(jt.getText());
                    } else {
                        success = changeNotes(jt.getText(), selected);
                    }
                    if (success) {
                        Instrument.resetJPanel(jp);
                        notes = Instrument.getNotes();
                        list.setListData(notes);
                        list.clearSelection();
                    }
                }
            }
        });
        jb2.addActionListener((arg0) -> Instrument.resetJPanel(jp));
    }

    private static boolean addNotes(String text) {
        return Instrument.additionalIntoFile("./setting/annotation", text);
    }

    private static boolean changeNotes(String text, int selected) {
        File file = new File("./setting/annotation");
        try {
            (new ChangeNoteName(notes[selected], text)).traverse();
            notes[selected] = text;
            FileWriter writer = new FileWriter(file, false);
            for (int i = 0; i < notes.length; ++i) {
                writer.write(i == notes.length - 1 ? notes[i] : notes[i] + "\n");
            }
            writer.close();
            return true;
        } catch (IOException var5) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void deleteNotes(int selected) {
        if (JOptionPane.showConfirmDialog(null, "确认删除吗？您将失去所有交易信息中该项备注的内容！", "删除备注", JOptionPane.YES_NO_OPTION) == 0) {
            File file = new File("./setting/annotation");
            try {
                (new DeleteNotes(notes[selected])).traverse();
                FileWriter writer = new FileWriter(file, false);
                int i1;
                if (selected == notes.length - 1) {
                    for (i1 = 0; i1 < notes.length; ++i1) {
                        if (i1 != selected) {
                            writer.write(i1 == notes.length - 2 ? notes[i1] : notes[i1] + "\n");
                        }
                    }
                } else {
                    for (i1 = 0; i1 < notes.length; ++i1) {
                        if (i1 != selected) {
                            writer.write(i1 == notes.length - 1 ? notes[i1] : notes[i1] + "\n");
                        }
                    }
                }
                writer.close();
                if (Instrument.getFileData(file).equals("")) {
                    FileWriter writer1 = new FileWriter(file, false);
                    writer1.write("备注1");
                    writer1.close();
                }
                notes = Instrument.getNotes();
                list.setListData(notes);
                list.clearSelection();
            } catch (IOException var5) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}