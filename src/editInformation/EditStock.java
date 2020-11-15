package editInformation;

import instrument.ChangeStockID;
import instrument.Instrument;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import mainWindow.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

final class EditStock {
    static Sheet sheet;
    static JList<String> list;
    static JScrollBar jsb;

    protected static void editStockPanel() {
        DefaultListModel<String> dlm = readClassification();
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new BorderLayout());
        list = new JList<>();
        list.setFont(new Font("黑体", Font.BOLD, 18));
        list.setModel(dlm);
        JScrollPane js = new JScrollPane(list);
        jsb = js.getVerticalScrollBar();
        js.setBorder(BorderFactory.createTitledBorder("股票信息"));
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
        JButton jb4 = new JButton("查找");
        JButton jb5 = new JButton("返回");
        Instrument.buttonType(jb1);
        Instrument.buttonType(jb2);
        Instrument.buttonType(jb3);
        Instrument.buttonType(jb4);
        Instrument.buttonType(jb5);
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        jp1.add(jb4);
        jp1.add(new JLabel());
        jp1.add(jb5);
        list.addListSelectionListener((e) -> {
            if (list.getValueIsAdjusting()) {
                Instrument.resetJPanel(jp2);
            }
        });
        jb1.addActionListener((arg0) -> {
            list.clearSelection();
            Instrument.resetJPanel(jp2);
            addPanel(jp2, 1);
        });
        jb2.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            if (list.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, "请先选择一支股票！", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            addPanel(jp2, 2);
        });
        jb3.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            if (list.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, "请先选择一支股票！", "警告", JOptionPane.WARNING_MESSAGE);
            } else if (deleteStock(list.getSelectedIndex())) {
                Instrument.resetJPanel(jp2);
                list.setModel(readClassification());
                list.clearSelection();
            }
        });
        jb4.addActionListener((arg0) -> {
            Instrument.resetJPanel(jp2);
            addPanel(jp2, 3);
        });
        jb5.addActionListener((arg0) -> ChooseEditItem.setEditItemPane());
    }

    private static DefaultListModel<String> readClassification() {
        Workbook book;
        DefaultListModel<String> dlm = new DefaultListModel<>();
        try {
            book = Workbook.getWorkbook(new File("./setting/StockInformation.xls"));
            sheet = book.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                String stocks = Instrument.changeToStockID(sheet.getCell(0, i).getContents());
                String names = sheet.getCell(1, i).getContents();
                dlm.addElement(stocks + "           " + names);
            }
        } catch (BiffException | IOException var2) {
            JOptionPane.showMessageDialog(null, "读取股票信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return dlm;
    }

    private static void addPanel(JPanel jp, int which) {
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 0;
        gb1.gridy = 0;
        gb1.weightx = 6;
        gb1.weighty = 1;
        gb1.fill = GridBagConstraints.BOTH;
        JTextArea jt1 = new JTextArea();
        jt1.setLineWrap(true);
        if (which == 2) {
            jt1.setDocument(new Instrument.JTextLimited(6, Instrument.JTextLimited.limitTypeEnum.INTEGER));
            jt1.setText(Instrument.changeToStockID(sheet.getCell(0, list.getSelectedIndex()).getContents()));
        } else {
            Instrument.setMarkWords(jt1, "在此输入股票代码", new Instrument.JTextLimited(6, Instrument.JTextLimited.limitTypeEnum.INTEGER));
        }
        jt1.setBorder(BorderFactory.createLineBorder(Color.black));
        jp.add(jt1, gb1);
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 0;
        gb2.gridy = 1;
        gb2.weightx = 6;
        gb2.weighty = 1;
        gb2.fill = GridBagConstraints.BOTH;
        JTextArea jt2 = new JTextArea();
        jt2.setLineWrap(true);
        if (which == 2) {
            jt2.setDocument(new Instrument.JTextLimited(200, Instrument.JTextLimited.limitTypeEnum.TEXT));
            jt2.setText(sheet.getCell(1, list.getSelectedIndex()).getContents().trim());
        } else {
            Instrument.setMarkWords(jt2, "在此输入股票名", new Instrument.JTextLimited(200, Instrument.JTextLimited.limitTypeEnum.TEXT));
        }
        jt2.setBorder(BorderFactory.createLineBorder(Color.black));
        jp.add(jt2, gb2);
        GridBagConstraints gb3 = new GridBagConstraints();
        gb3.gridx = 1;
        gb3.gridy = 0;
        gb3.weightx = 1;
        gb3.weighty = 1;
        gb3.ipadx = 100;
        gb3.gridheight = 2;
        gb3.fill = GridBagConstraints.BOTH;
        JButton jb1 = new JButton("确定");
        Instrument.buttonType(jb1);
        jp.add(jb1, gb3);
        GridBagConstraints gb4 = new GridBagConstraints();
        gb4.gridx = 2;
        gb4.gridy = 0;
        gb4.weightx = 1;
        gb4.weighty = 1;
        gb4.ipadx = 100;
        gb4.gridheight = 2;
        gb4.fill = GridBagConstraints.BOTH;
        JButton jb2 = new JButton("取消");
        Instrument.buttonType(jb2);
        jp.add(jb2, gb4);
        jb1.addActionListener((arg0) -> {
            boolean success = false;
            String ID = jt1.getText();
            String name = jt2.getText();
            int select = list.getSelectedIndex();
            if (which == 1) {
                if (repeatDetection(ID, name, true, true)) {
                    return;
                }
                if (addStock(ID, name)) {
                    success = true;
                }
            } else if (which == 2) {
                if (repeatDetection(ID, name, false, true)) {
                    return;
                }
                if (changeStock(ID, name, select)) {
                    success = true;
                }
            } else {
                if (repeatDetection(ID, name, true, false)) {
                    Instrument.resetJPanel(jp);
                } else {
                    JOptionPane.showMessageDialog(null, "股票不存在！", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (success) {
                Instrument.resetJPanel(jp);
                list.setModel(readClassification());
                list.clearSelection();
                if (which == 1) {
                    list.setSelectedIndex(list.getMaxSelectionIndex());
                    jsb.setValue(jsb.getMaximum());
                }
            }
        });
        jb2.addActionListener((arg0) -> Instrument.resetJPanel(jp));
    }

    private static boolean repeatDetection(String ID, String name, boolean b, boolean show) {
        boolean sid = false, sname = false;
        int iid = 0, iname = 0;
        for (int i = 0; i < sheet.getRows(); i++)
            if (ID.equals(Instrument.changeToStockID(sheet.getCell(0, i).getContents()))) {
                sid = true;
                iid = i;
            }
        for (int i = 0; i < sheet.getRows(); i++)
            if (name.equals(sheet.getCell(1, i).getContents())) {
                iname = i;
                sname = true;
            }
        if (!b) {
            if (sid && sname) {
                jsb.setValue((int) (((float) iid / (float) sheet.getRows()) * (float) jsb.getMaximum()));
                list.setSelectedIndex(iid);
                if (show) {
                    JOptionPane.showMessageDialog(null, "已有该股票！", "提示", JOptionPane.WARNING_MESSAGE);
                }
                return true;
            }
        } else {
            if (sid || sname) {
                if (sid) {
                    jsb.setValue((int) (((float) iid / (float) sheet.getRows()) * (float) jsb.getMaximum()));
                    list.setSelectedIndex(iid);
                } else {
                    jsb.setValue((int) (((float) iname / (float) sheet.getRows()) * (float) jsb.getMaximum()));
                    list.setSelectedIndex(iname);
                }
                if (show) {
                    JOptionPane.showMessageDialog(null, "已有该股票！", "提示", JOptionPane.WARNING_MESSAGE);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean addStock(String ID, String name) {
        if (ID.length() != 6 || name.length() > 6 || name.length() < 2) {
            JOptionPane.showMessageDialog(null, "您的输入中存在非法字符或超出长度！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File("./setting/StockInformation.xls"));
            WritableSheet sheet1 = book.createSheet("Sheet1", 0);
            for (int i = 0; i < sheet.getRows(); i++) {
                jxl.write.Label label1 = new jxl.write.Label(0, i, sheet.getCell(0, i).getContents());
                jxl.write.Label label2 = new jxl.write.Label(1, i, sheet.getCell(1, i).getContents());
                try {
                    sheet1.addCell(label1);
                    sheet1.addCell(label2);
                } catch (WriteException e) {
                    JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            jxl.write.Label label1 = new jxl.write.Label(0, sheet.getRows(), ID);
            jxl.write.Label label2 = new jxl.write.Label(1, sheet.getRows(), name);
            try {
                sheet1.addCell(label1);
                sheet1.addCell(label2);
                book.write();
                book.close();
            } catch (WriteException e) {
                JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean changeStock(String ID, String name, int select) {
        if (ID.length() != 6 || name.length() > 6 || name.length() < 2) {
            JOptionPane.showMessageDialog(null, "输入不合法！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!sheet.getCell(0, select).getContents().equals(ID)) {
            new ChangeStockID(sheet.getCell(0, select).getContents(), ID).traverse();
        }
        try {
            WritableWorkbook book = Workbook.createWorkbook(new File("./setting/StockInformation.xls"));
            WritableSheet sheet1 = book.createSheet("Sheet1", 0);
            for (int i = 0; i < sheet.getRows(); i++) {
                jxl.write.Label label1;
                jxl.write.Label label2;
                if (i == select) {
                    label1 = new jxl.write.Label(0, i, ID);
                    label2 = new jxl.write.Label(1, i, name);
                } else {
                    label1 = new jxl.write.Label(0, i, sheet.getCell(0, i).getContents());
                    label2 = new jxl.write.Label(1, i, sheet.getCell(1, i).getContents());
                }
                try {
                    sheet1.addCell(label1);
                    sheet1.addCell(label2);
                } catch (WriteException e) {
                    JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            try {
                book.write();
                book.close();
            } catch (WriteException e) {
                JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean deleteStock(int select) {
        int i1 = JOptionPane.showConfirmDialog(null, "确认删除吗？", "删除股票信息", JOptionPane.YES_NO_OPTION);
        if (i1 == 0) {
            try {
                WritableWorkbook book = Workbook.createWorkbook(new File("./setting/StockInformation.xls"));
                WritableSheet sheet1 = book.createSheet("Sheet1", 0);
                for (int i = 0; i < sheet.getRows(); i++) {
                    jxl.write.Label label1;
                    jxl.write.Label label2;
                    if (i < select) {
                        label1 = new jxl.write.Label(0, i, sheet.getCell(0, i).getContents());
                        label2 = new jxl.write.Label(1, i, sheet.getCell(1, i).getContents());
                        try {
                            sheet1.addCell(label1);
                            sheet1.addCell(label2);
                        } catch (WriteException e) {
                            JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else if (i > select) {
                        label1 = new jxl.write.Label(0, i - 1, sheet.getCell(0, i).getContents());
                        label2 = new jxl.write.Label(1, i - 1, sheet.getCell(1, i).getContents());
                        try {
                            sheet1.addCell(label1);
                            sheet1.addCell(label2);
                        } catch (WriteException e) {
                            JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
                try {
                    book.write();
                    book.close();
                } catch (WriteException e) {
                    JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}