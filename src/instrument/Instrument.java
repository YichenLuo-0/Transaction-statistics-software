package instrument;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import mainWindow.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

public final class Instrument {
    public static void newConfigurationFile() {
        File file = new File("./setting");
        if (!file.exists()) {
            file.mkdir();
        }
        testAndCreate(new File("./setting/user"), "new user\n00000");
        testAndCreate(new File("./setting/annotation"), "备注1\n备注2");
        testAndCreate(new File("./setting/classify"), "分类1\n分类2");
        File file1 = new File("./setting/classification");
        if (!file1.exists()) {
            file1.mkdir();
        }
        String[] classify = Instrument.getFirstClassify();
        for (String s : classify) testAndCreate(new File("./setting/classification/" + s), "分类1\n分类2");
        File file2 = new File("./database");
        if (!file2.exists()) {
            file2.mkdir();
        }
        File file3 = new File("./database/machine");
        if (!file3.exists()) {
            file3.mkdir();
        }
        File file4 = new File("./database/human");
        if (!file4.exists()) {
            file4.mkdir();
        }
        File file5 = new File("./database/none");
        if (!file5.exists()) {
            file5.mkdir();
        }
        File file6 = new File("./setting/StockInformation.xls");
        if (!file6.exists()) {
            try {
                file6.createNewFile();
                WritableWorkbook book = Workbook.createWorkbook(file6);
                book.createSheet("Sheet1", 0);
                book.write();
                book.close();
            } catch (IOException | WriteException e) {
                JOptionPane.showMessageDialog(null, "创建文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void testAndCreate(File file, String document) {
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter out = new FileWriter(file);
                out.write(document);
                out.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "创建文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void resetSize(int x, int y, ImageIcon image) {
        int x1;
        x1 = image.getIconWidth();
        int y1;
        y1 = image.getIconHeight();
        float d = (float) x / (float) y;
        float d1 = (float) x1 / (float) y1;
        float n = (float) y1 * (float) x / (float) x1;
        float n1 = (float) x1 * (float) y / (float) y1;
        if (d > d1) {
            image.setImage(image.getImage().getScaledInstance(x, (int) n, Image.SCALE_DEFAULT));
        } else if (d < d1) {
            image.setImage(image.getImage().getScaledInstance((int) n1, y, Image.SCALE_DEFAULT));
        } else {
            image.setImage(image.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT));
        }
    }

    public static String changeToStockID(String ID) {
        DecimalFormat df = new DecimalFormat("000000");
        return df.format(Integer.parseInt(ID));
    }

    public static String getFileData(File file) {
        String s1 = null;
        try {
            FileReader in = new FileReader(file);
            char[] byt = new char[1024];
            int len = in.read(byt);
            if (len != -1) s1 = new String(byt, 0, len);
            else s1 = "";
            in.close();
        } catch (Exception var5) {
            JOptionPane.showMessageDialog(null, "读取文件失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return s1;
    }

    public static Boolean additionalIntoFile(String path, String text) {
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write("\n" + text);
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static void buttonType(final JButton jb) {
        jb.setForeground(Color.DARK_GRAY);
        jb.setBackground(Color.LIGHT_GRAY);
        jb.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jb.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                jb.setBackground(Color.GRAY);
                jb.addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        jb.setBackground(Color.LIGHT_GRAY);
                    }
                });
            }
        });
    }

    public static void resetJPanel(JPanel jp) {
        jp.removeAll();
        jp.repaint();
        jp.revalidate();
        System.gc();
    }

    public static void placeholderComponents(int gridx, int gridy, int weightx, int weighty, JPanel jp) {
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = gridx;
        gb.gridy = gridy;
        gb.weightx = weightx;
        gb.weighty = weighty;
        JLabel jl3 = new JLabel();
        jp.add(jl3, gb);
    }

    public static void addDataBox(JPanel jp, JComboBox<String> year, JComboBox<String> month, JComboBox<String> day, int beginx, int beginy) {
        Instrument.dataSelect(year, month, day);
        year.setBounds(beginx, beginy, 60, 30);
        month.setBounds(beginx + 100, beginy, 50, 30);
        day.setBounds(beginx + 190, beginy, 50, 30);
        JLabel jl1 = new JLabel("年");
        JLabel jl2 = new JLabel("月");
        JLabel jl3 = new JLabel("日");
        jl1.setBounds(beginx + 70, beginy, 50, 30);
        jl2.setBounds(beginx + 160, beginy, 50, 30);
        jl3.setBounds(beginx + 250, beginy, 50, 30);
        jp.add(year);
        jp.add(month);
        jp.add(day);
        jp.add(jl1);
        jp.add(jl2);
        jp.add(jl3);
    }

    public static void dataSelect(JComboBox<String> jc1, JComboBox<String> jc2, JComboBox<String> jc3) {
        int startyear = 2020;
        Calendar year = Calendar.getInstance();
        int currentyear = year.get(Calendar.YEAR);
        do {
            String year1 = String.valueOf(startyear);
            jc1.addItem(year1);
            ++startyear;
        } while (startyear != currentyear + 1);
        resetMonth(jc1, jc2);
        resetDay(jc1, jc2, jc3);
        ItemListener il1 = (e) -> resetDay(jc1, jc2, jc3);
        jc2.addItemListener(il1);
        jc1.addItemListener((e) -> {
            jc2.removeItemListener(il1);
            resetMonth(jc1, jc2);
            resetDay(jc1, jc2, jc3);
            jc2.addItemListener(il1);
        });
    }

    public static void addClassifyItem(JComboBox<String> jc1, JComboBox<String> jc2) {
        newConfigurationFile();
        String[] classify = getFirstClassify();
        for (String value : classify) jc1.addItem(value);
        jc1.addItemListener((e) -> {
            jc2.removeAllItems();
            jc2.addItem("<请选择二级分类>");
            String secondClassify = (String) jc1.getSelectedItem();
            assert secondClassify != null;
            if (!secondClassify.equals("<请选择一级分类>")) {
                String[] classify1 = getSecondClassify((String) jc1.getSelectedItem());
                for (String s : classify1) {
                    jc2.addItem(s);
                }
            }
        });
    }

    private static void resetMonth(JComboBox<String> jc1, JComboBox<String> jc2) {
        jc2.removeAllItems();
        int startmonth = 1;
        Calendar year = Calendar.getInstance();
        int currentyear = year.get(Calendar.YEAR);
        Calendar month = Calendar.getInstance();
        int currentmonth = month.get(Calendar.MONTH) + 1;
        int year2 = Integer.parseInt((String) Objects.requireNonNull(jc1.getSelectedItem()));
        String month1;
        if (year2 == currentyear) {
            do {
                month1 = String.valueOf(startmonth);
                jc2.addItem(month1);
                ++startmonth;
            } while (startmonth != currentmonth + 1);
        } else {
            do {
                month1 = String.valueOf(startmonth);
                jc2.addItem(month1);
                ++startmonth;
            } while (startmonth != 13);
        }
    }

    private static void resetDay(JComboBox<String> jc1, JComboBox<String> jc2, JComboBox<String> jc3) {
        jc3.removeAllItems();
        int startday = 1;
        Calendar year = Calendar.getInstance();
        int currentyear = year.get(Calendar.YEAR);
        Calendar month = Calendar.getInstance();
        int currentmonth = month.get(Calendar.MONTH) + 1;
        Calendar day = Calendar.getInstance();
        int currentday = day.get(Calendar.DATE) + 1;
        int year2 = Integer.parseInt((String) Objects.requireNonNull(jc1.getSelectedItem()));
        int month2 = Integer.parseInt((String) Objects.requireNonNull(jc2.getSelectedItem()));
        int x;
        if (year2 == currentyear && month2 == currentmonth) {
            x = currentday;
        } else if ((month2 < 8 && month2 % 2 == 0) || (month2 > 7 && month2 % 2 != 0)) {
            if (month2 == 2) {
                if ((year2 % 4 != 0 || year2 % 100 == 0) && year2 % 400 != 0) {
                    x = 29;
                } else {
                    x = 30;
                }
            } else {
                x = 31;
            }
        } else {
            x = 32;
        }
        do {
            String day1 = String.valueOf(startday);
            jc3.addItem(day1);
            ++startday;
        } while (startday != x);
    }

    public static boolean isAllSelected(JCheckBox[] jc) {
        boolean isAllSelected = true;
        for (JCheckBox jCheckBox : jc) {
            if (!jCheckBox.isSelected()) {
                isAllSelected = false;
            }
        }
        return isAllSelected;
    }

    public static String[] getNotes() {
        newConfigurationFile();
        File file = new File("./setting/annotation");
        return getFileData(file).split("\n");
    }

    public static String[] getFirstClassify() {
        File file = new File("./setting/classify");
        return getFileData(file).split("\n");
    }

    public static String[] getSecondClassify(String firstClassify) {
        File file = new File("./setting/classification/" + firstClassify);
        return getFileData(file).split("\n");
    }

    public static void setMarkWords(final JTextArea jt, String text, Document doc) {
        jt.setForeground(Color.GRAY);
        jt.setText(text);
        final boolean[] first = new boolean[]{true};
        jt.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (first[0]) {
                    jt.setForeground(Color.BLACK);
                    jt.setText(null);
                    first[0] = false;
                    jt.setDocument(doc);
                }
            }
        });
    }

    public static boolean deleteFileOrDirectory(String sPath) {
        boolean flag = true;
        File dirFile = new File(sPath);
        if (!dirFile.exists()) {
            return true;
        } else if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            File[] files = dirFile.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    flag = file.delete();
                } else {
                    flag = deleteFileOrDirectory(file.getAbsolutePath());
                }
                if (!flag) {
                    break;
                }
            }
            return flag && dirFile.delete();
        }
    }

    public static void addWindowListener(JFrame jf) {
        jf.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                MainWindow.mainFrame.setEnabled(true);
                MainWindow.mainFrame.setVisible(true);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public static void copyFile(File source_file, String path) throws Exception {
        File copy_file = new File(path);
        if (!source_file.exists()) {
            throw new IOException("源文件（" + source_file + "） 不存在！");
        }
        if (!copy_file.exists()) {
            copy_file.createNewFile();
        }
        FileInputStream fis = new FileInputStream(source_file);
        FileOutputStream fos = new FileOutputStream(copy_file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] KB = new byte[1024];
        int index;
        while ((index = bis.read(KB)) != -1) {
            bos.write(KB, 0, index);
        }
        bos.close();
        bis.close();
        fos.close();
        fis.close();
    }

    public static boolean importImage(String aimPath) {
        boolean ifNeedImport = false;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("图像文件(JPG/PNG)", "JPG", "JPEG", "PNG");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int i2 = chooser.showOpenDialog(MainWindow.mainFrame);
        if (i2 == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                Instrument.copyFile(file, aimPath);
                ifNeedImport = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return ifNeedImport;
    }

    public static class JTextLimited extends PlainDocument {
        private static final long serialVersionUID = 1L;
        private final int limit;
        private final limitTypeEnum type;

        public JTextLimited(int limit, limitTypeEnum type) {
            this.limit = limit;
            this.type = type;
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            String dataType = switch (this.type) {
                case INTEGER -> "\\d*";
                case FLOAT -> "-?\\d*\\.?\\d*";
                case TEXT -> "\\S*";
            };
            if (str.matches(dataType) && this.getLength() + str.length() <= this.limit) {
                super.insertString(offset, str, attr);
            }
        }

        public enum limitTypeEnum {
            INTEGER,
            FLOAT,
            TEXT;
        }
    }

    public static class FindStockName {
        static Sheet sheet;

        public FindStockName() {
            Workbook book;
            try {
                book = Workbook.getWorkbook(new File("./setting/StockInformation.xls"));
                sheet = book.getSheet(0);
            } catch (BiffException | IOException var2) {
                JOptionPane.showMessageDialog(null, "读取股票信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        public String getName(String stockID) {
            if (sheet == null) {
                return "无法读取股票信息";
            }
            for (int i = 0; i < sheet.getRows(); i++) {
                if (stockID.equals(changeToStockID(sheet.getCell(0, i).getContents()))) {
                    return sheet.getCell(1, i).getContents();
                }
            }
            return "无此股票或已被删除";
        }
    }
}