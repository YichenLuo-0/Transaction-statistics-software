package editInformation;

import instrument.Instrument;
import mainWindow.MainWindow;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

final class EditClassification {
    static JTree tree;
    static DefaultMutableTreeNode[] node1;
    static DefaultMutableTreeNode[][] node2;
    static TreeSelectionListener ts1, ts2;

    protected static void editClassificationPanel() {
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new BorderLayout());
        tree = new JTree();
        tree.setFont(new Font("黑体", Font.BOLD, 18));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        JScrollPane js = new JScrollPane(tree);
        js.setBorder(BorderFactory.createTitledBorder("分类名称"));
        MainWindow.mainPanel.add(js, BorderLayout.CENTER);
        JPanel jp1 = new JPanel(new GridLayout(6, 0, 20, 20));
        jp1.setPreferredSize(new Dimension(200, 0));
        MainWindow.mainPanel.add(jp1, BorderLayout.EAST);
        JPanel jp2 = new JPanel();
        jp2.setPreferredSize(new Dimension(0, 150));
        jp2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MainWindow.mainPanel.add(jp2, BorderLayout.SOUTH);
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
        readClassification();
        ts1 = e -> Instrument.resetJPanel(jp2);
        tree.addTreeSelectionListener(ts1);
        jb1.addActionListener(arg0 -> {
            Instrument.resetJPanel(jp2);
            tree.clearSelection();
            addClassificationPanel(jp2);
        });
        jb2.addActionListener(arg0 -> {
            Instrument.resetJPanel(jp2);
            if (tree.getSelectionCount() == 0) {
                JOptionPane.showMessageDialog(null, "请先选择一个分类！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                classificationPane(jp2, 3);
            }
        });
        jb3.addActionListener(arg0 -> {
            Instrument.resetJPanel(jp2);
            if (tree.getSelectionCount() == 0) {
                JOptionPane.showMessageDialog(null, "请先选择一个分类！", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                if (deleteClassification()) {
                    Instrument.resetJPanel(jp2);
                    readClassification();
                    tree.clearSelection();
                }
            }
        });
        jb4.addActionListener(arg0 -> ChooseEditItem.setEditItemPane());
    }

    private static void readClassification() {
        Instrument.newConfigurationFile();
        String[] Classification1 = Instrument.getFirstClassify();
        String[][] Classification2 = new String[Classification1.length][];
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        node1 = new DefaultMutableTreeNode[Classification1.length];
        node2 = new DefaultMutableTreeNode[Classification1.length][];
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        for (int i = 0; i < Classification1.length; i++) {
            node1[i] = new DefaultMutableTreeNode(Classification1[i]);
            root.add(node1[i]);
            Classification2[i] = Instrument.getSecondClassify(Classification1[i]);
            node2[i] = new DefaultMutableTreeNode[Classification2[i].length];
            for (int i1 = 0; i1 < Classification2[i].length; i1++) {
                node2[i][i1] = new DefaultMutableTreeNode(Classification2[i][i1], false);
                node1[i].add(node2[i][i1]);
            }
        }
        tree.setModel(dtm);
        for (DefaultMutableTreeNode dmt : node1) tree.expandPath(new TreePath(dmt.getPath()));
    }

    private static void addClassificationPanel(JPanel jp) {
        jp.setLayout(new GridLayout());
        JButton jb1 = new JButton("增加一级分类");
        JButton jb2 = new JButton("增加二级分类");
        Instrument.buttonType(jb1);
        Instrument.buttonType(jb2);
        jp.add(jb1);
        jp.add(jb2);
        jb1.addActionListener(arg0 -> {
            Instrument.resetJPanel(jp);
            classificationPane(jp, 1);
        });
        jb2.addActionListener(arg0 -> {
            Instrument.resetJPanel(jp);
            jp.setLayout(new GridBagLayout());
            JLabel jl = new JLabel("请在上方选择框中选择一个一级分类 您填写的新二级分类将增加至该分类目录下");
            jl.setFont(new Font("黑体", Font.BOLD, 20));
            jp.add(jl);
            tree.removeTreeSelectionListener(ts1);
            ts2 = e -> {
                if (Objects.requireNonNull(tree.getSelectionPath()).getPath().length == 2) {
                    Instrument.resetJPanel(jp);
                    classificationPane(jp, 2);
                    tree.removeTreeSelectionListener(ts2);
                    tree.addTreeSelectionListener(ts1);
                }
            };
            tree.addTreeSelectionListener(ts2);
        });
    }

    private static void classificationPane(JPanel jp, int which) {
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 0;
        gb1.gridy = 0;
        gb1.weightx = 6;
        gb1.weighty = 1;
        gb1.fill = GridBagConstraints.BOTH;
        JTextArea jt = new JTextArea();
        jt.setLineWrap(true);
        if (which == 3) {
            jt.setDocument(new Instrument.JTextLimited(200, Instrument.JTextLimited.limitTypeEnum.TEXT));
            Object[] path = Objects.requireNonNull(tree.getSelectionPath()).getPath();
            jt.setText(Objects.toString(path[path.length - 1]));
        } else {
            Instrument.setMarkWords(jt, "在此输入新的分类名（200字以内）", new Instrument.JTextLimited(200, Instrument.JTextLimited.limitTypeEnum.TEXT));
        }
        jp.add(jt, gb1);
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 1;
        gb2.gridy = 0;
        gb2.weightx = 1;
        gb2.weighty = 1;
        gb2.ipadx = 100;
        gb2.fill = GridBagConstraints.BOTH;
        JButton jb1 = new JButton("确定");
        Instrument.buttonType(jb1);
        jp.add(jb1, gb2);
        GridBagConstraints gb3 = new GridBagConstraints();
        gb3.gridx = 2;
        gb3.gridy = 0;
        gb3.weightx = 1;
        gb3.weighty = 1;
        gb3.ipadx = 100;
        gb3.fill = GridBagConstraints.BOTH;
        JButton jb2 = new JButton("取消");
        Instrument.buttonType(jb2);
        jp.add(jb2, gb3);
        jb1.addActionListener(arg0 -> {
            String text = jt.getText();
            String check = "[^/:*\"<>|?.\\\\]*";
            if (text.equals("") || !text.equals("在此输入新的分类名（200字以内）")) {
                if (!text.matches(check)) {
                    JOptionPane.showMessageDialog(null, "您的输入中存在非法字符！", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    boolean success = false;
                    if (which == 1) success = addFirstClassification(text);
                    else if (which == 2) success = addSecondClassification(text);
                    else if (which == 3) success = changeClassification(text);
                    if (success) {
                        Instrument.resetJPanel(jp);
                        readClassification();
                        tree.clearSelection();
                    }
                }
            }
        });
        jb2.addActionListener(arg0 -> Instrument.resetJPanel(jp));
    }

    private static boolean addFirstClassification(String text) {
        for (DefaultMutableTreeNode dmt : node1)
            if (dmt.getPath()[1].toString().equals(text)) {
                JOptionPane.showMessageDialog(null, "分类名称重复！", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        return Instrument.additionalIntoFile("./setting/classify", text);
    }

    private static boolean addSecondClassification(String text) {
        String Cll1 = Objects.toString(Objects.requireNonNull(tree.getSelectionPath()).getPath()[1]);
        for (String Cll2 : Instrument.getSecondClassify(Cll1)) {
            if (Cll2.equals(text)) {
                JOptionPane.showMessageDialog(null, "分类名称重复！", "提示", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return Instrument.additionalIntoFile("./setting/classification/" + Cll1, text);
    }

    private static boolean changeClassification(String text) {
        Object[] Cll = Objects.requireNonNull(tree.getSelectionPath()).getPath();
        if (Cll.length == 2) {
            String[] Classification1 = Instrument.getFirstClassify();
            for (int i = 0; i < Classification1.length; i++) {
                if (Classification1[i].equals(text)) {
                    JOptionPane.showMessageDialog(null, "分类名称重复！", "提示", JOptionPane.WARNING_MESSAGE);
                    return false;
                } else if (Classification1[i].equals(Objects.toString(Cll[1]))) {
                    Classification1[i] = text;
                }
            }
            File re1 = new File("./database/human/" + Cll[1]);
            if (re1.exists()) {
                if (!re1.renameTo(new File("./database/human/" + text))) {
                    JOptionPane.showMessageDialog(null, "重命名文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            File re2 = new File("./setting/classification/" + Cll[1]);
            if (re2.exists()) {
                if (!re2.renameTo(new File("./setting/classification/" + text))) {
                    JOptionPane.showMessageDialog(null, "重命名文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            File file = new File("./setting/classify");
            try {
                FileWriter writer = new FileWriter(file, false);
                for (int i = 0; i < Classification1.length; i++)
                    if (i == Classification1.length - 1) {
                        writer.write(Classification1[i]);
                    } else {
                        writer.write(Classification1[i] + "\n");
                    }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            String[] Classification2 = Instrument.getSecondClassify(Objects.toString(Cll[1]));
            for (int i = 0; i < Classification2.length; i++) {
                if (Classification2[i].equals(text)) {
                    JOptionPane.showMessageDialog(null, "分类名称重复！", "提示", JOptionPane.WARNING_MESSAGE);
                    return false;
                } else if (Classification2[i].equals(Objects.toString(Cll[2]))) {
                    Classification2[i] = text;
                }
            }
            File re = new File("./database/human/" + Cll[1] + "/" + Cll[2]);
            if (re.exists()) {
                if (!re.renameTo(new File("./database/human/" + Cll[1] + "/" + text))) {
                    JOptionPane.showMessageDialog(null, "重命名文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            File file = new File("./setting/classification/" + Cll[1]);
            try {
                FileWriter writer = new FileWriter(file, false);
                for (int i = 0; i < Classification2.length; i++)
                    if (i == Classification2.length - 1) {
                        writer.write(Classification2[i]);
                    } else {
                        writer.write(Classification2[i] + "\n");
                    }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private static boolean deleteClassification() {
        if (JOptionPane.showConfirmDialog(null, "确认删除吗？该分类下所有交易将被移至未分类区！", "删除备注", JOptionPane.YES_NO_OPTION) != 0) {
            return false;
        }
        Object[] Cll = Objects.requireNonNull(tree.getSelectionPath()).getPath();
        if (Cll.length == 2) {
            String[] Classification1 = Instrument.getFirstClassify();
            String[] NewClassification1 = new String[Classification1.length - 1];
            int j = 0;
            for (String s : Classification1) {
                if (!s.equals(Objects.toString(Cll[1]))) {
                    NewClassification1[j] = s;
                    j++;
                }
            }
            File delete = new File("./setting/classification/" + Cll[1]);
            if (delete.exists()) {
                if (!delete.delete()) {
                    JOptionPane.showMessageDialog(null, "删除错误，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            File file = new File("./setting/classify");
            try {
                FileWriter writer = new FileWriter(file, false);
                for (int i = 0; i < NewClassification1.length; i++)
                    if (i == NewClassification1.length - 1) {
                        writer.write(NewClassification1[i]);
                    } else {
                        writer.write(NewClassification1[i] + "\n");
                    }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            File move = new File("./database/human/" + Cll[1]);
            if (move.exists()) {
                for (File file1 : Objects.requireNonNull(move.listFiles())) {
                    if (moveToDeleteClassify(file1)) {
                        JOptionPane.showMessageDialog(null, "移动文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                if (!move.delete()) {
                    JOptionPane.showMessageDialog(null, "移动文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return checkNull(file, Instrument.getFirstClassify());
        } else {
            String[] Classification2 = Instrument.getSecondClassify(Objects.toString(Cll[1]));
            String[] NewClassification2 = new String[Classification2.length - 1];
            int j = 0;
            for (String s : Classification2)
                if (!s.equals(Objects.toString(Cll[2]))) {
                    NewClassification2[j] = s;
                    j++;
                }
            File file = new File("./setting/classification/" + Cll[1]);
            try {
                FileWriter writer = new FileWriter(file, false);
                for (int i = 0; i < NewClassification2.length; i++)
                    if (i == NewClassification2.length - 1) {
                        writer.write(NewClassification2[i]);
                    } else {
                        writer.write(NewClassification2[i] + "\n");
                    }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            File move = new File("./database/human/" + Cll[1] + "/" + Cll[2]);
            if (move.exists()) {
                if (moveToDeleteClassify(move)) {
                    JOptionPane.showMessageDialog(null, "移动文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return checkNull(file, Instrument.getSecondClassify(Objects.toString(Cll[1])));
        }
    }

    private static boolean moveToDeleteClassify(File path) {
        File[] list = path.listFiles();
        if (list != null) {
            for (File file : list) {
                String[] pathName = file.getPath().split("\\\\");
                int name = Integer.parseInt(pathName[5]);
                File newPath = new File("./database/none/" + name);
                while (true) {
                    if (newPath.exists()) {
                        name++;
                        newPath = new File("./database/none/" + name);
                    } else {
                        if (!file.renameTo(newPath)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return !path.delete();
    }

    private static boolean checkNull(File file, String[] s) {
        if (s.length == 1 && s[0].equals("")) {
            try {
                FileWriter writer = new FileWriter(file, false);
                writer.write("分类1");
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}