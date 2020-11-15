package addInformation;

import instrument.Instrument;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import mainWindow.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

final class FindStock {
    static String[] ID, name;

    protected static void findFrame() {
        load();
        final String[] ID = FindStock.ID;
        final String[] name = FindStock.name;
        FindStock.ID = null;
        FindStock.name = null;
        System.gc();
        final String[][] information = {new String[ID.length]};
        MainWindow.mainFrame.setEnabled(false);
        JFrame jf = new JFrame("股票选择");
        jf.setLayout(new BorderLayout());
        jf.setSize(500, 400);
        jf.setLocationRelativeTo(null);
        jf.setAlwaysOnTop(true);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
        Container c = jf.getContentPane();
        JPanel jp1 = new JPanel();
        JButton jb1 = new JButton("确定");
        JButton jb2 = new JButton("取消");
        jp1.add(jb1);
        jp1.add(jb2);
        c.add(jp1, BorderLayout.SOUTH);
        JPanel jp2 = new JPanel(null);
        JLabel jl1 = new JLabel("输入股票代码");
        jl1.setBounds(20, 20, 100, 30);
        jp2.add(jl1);
        JTextField jt1 = new JTextField();
        jt1.setBounds(120, 20, 200, 30);
        jt1.setDocument(new Instrument.JTextLimited(6, Instrument.JTextLimited.limitTypeEnum.INTEGER));
        jp2.add(jt1);
        JList<String> list = new JList<>();
        list.setFixedCellHeight(20);
        JScrollPane js = new JScrollPane(list);
        js.setBounds(20, 60, 450, 260);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jp2.add(js);
        c.add(jp2, BorderLayout.CENTER);
        final Thread[] addThread = {new Thread(() -> {
        })};
        list.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());
                if (key.equals("向上箭头")) {
                    if (list.getSelectedIndex() == 0) {
                        list.clearSelection();
                        jt1.requestFocus();
                    }
                } else if (key.equals("Enter")) {
                    if (addThread[0].isAlive()) {
                        addThread[0].interrupt();
                    }
                    returnMainWindow(list, information, jf);
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        jt1.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                String key = KeyEvent.getKeyText(e.getKeyCode());
                if (key.matches("\\d") || key.matches("NumPad-\\d") || key.equals("Backspace")) {
                    String ID1 = jt1.getText();
                    if (addThread[0].isAlive()) {
                        addThread[0].interrupt();
                    }
                    if (ID1.equals("")) {
                        list.setModel(new DefaultListModel<>());
                    } else {
                        DefaultListModel<String> dlm = new DefaultListModel<>();
                        list.setModel(dlm);
                        information[0] = new String[ID.length];
                        addThread[0] = new AddStockThread(information, ID, name, ID1, list, dlm);
                        addThread[0].start();
                    }
                } else if (key.equals("向下箭头")) {
                    if (list.getLastVisibleIndex() != -1) {
                        list.requestFocus();
                        list.setSelectedIndex(0);
                    }
                }
            }
        });
        jb1.addActionListener(arg0 -> {
            if (addThread[0].isAlive()) {
                addThread[0].interrupt();
            }
            returnMainWindow(list, information, jf);
        });
        jb2.addActionListener(arg0 -> {
            information[0] = null;
            jf.dispose();
            MainWindow.mainFrame.setEnabled(true);
            MainWindow.mainFrame.setVisible(true);
        });
        Instrument.addWindowListener(jf);
    }

    private static void returnMainWindow(JList<String> list, String[][] information, JFrame jf) {
        int select = list.getSelectedIndex();
        if (select != -1) {
            AddNewDeal.setStockLabel(information[0][list.getSelectedIndex()]);
            information[0] = null;
            jf.dispose();
            MainWindow.mainFrame.setEnabled(true);
            MainWindow.mainFrame.setVisible(true);
        }
    }

    private static void load() {
        Workbook book;
        Sheet sheet;
        try {
            book = Workbook.getWorkbook(new File("./setting/StockInformation.xls"));
            sheet = book.getSheet(0);
            int l = sheet.getRows();
            ID = new String[l];
            name = new String[l];
            for (int i = 0; i < l; i++) {
                ID[i] = Instrument.changeToStockID(sheet.getCell(0, i).getContents());
                name[i] = sheet.getCell(1, i).getContents();
            }
        } catch (BiffException | IOException var2) {
            JOptionPane.showMessageDialog(null, "读取股票信息失败！", "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

final class AddStockThread extends Thread {
    String[][] information;
    String[] ID, name;
    String ID1;
    JList<String> list;
    DefaultListModel<String> dlm;

    protected AddStockThread(String[][] information, String[] ID, String[] name, String ID1, JList<String> list,
                             DefaultListModel<String> dlm) {
        this.information = information;
        this.ID = ID;
        this.name = name;
        this.ID1 = ID1;
        this.list = list;
        this.dlm = dlm;
    }

    @Override
    public void run() {
        int j = 0;
        for (int i = 0; i < ID.length; i++) {
            if (this.isInterrupted()) {
                return;
            } else if (ID[i].contains(ID1)) {
                information[0][j++] = ID[i] + "     " + name[i];
                dlm.addElement("<html>" + ID[i].replace(ID1, "<span style=\"color:red;\">" + ID1 + "</span>")
                        + "&nbsp&nbsp&nbsp&nbsp;" + name[i]);
            }
        }
        if (dlm.getSize() == 1) {
            list.requestFocus();
            list.setSelectedIndex(0);
        }
    }
}