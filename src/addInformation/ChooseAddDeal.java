package addInformation;

import instrument.FindUnfinished;
import instrument.Instrument;
import mainWindow.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ChooseAddDeal {
    public static void setAddDealPane() {
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new GridBagLayout());
        JButton jb1 = new JButton("添加波段操作");
        jb1.setFont(new Font("黑体", Font.BOLD, 15));
        jb1.setPreferredSize(new Dimension(450, 120));
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 1;
        gb1.gridy = 1;
        gb1.weightx = 4;
        gb1.weighty = 3;
        MainWindow.mainPanel.add(jb1, gb1);
        JButton jb2 = new JButton("添加超短操作");
        jb2.setFont(new Font("黑体", Font.BOLD, 15));
        jb2.setPreferredSize(new Dimension(450, 120));
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 1;
        gb2.gridy = 2;
        gb2.weightx = 4;
        gb2.weighty = 3;
        MainWindow.mainPanel.add(jb2, gb2);
        JButton jb3 = new JButton("批量导入交易数据");
        jb3.setPreferredSize(new Dimension(300, 80));
        GridBagConstraints gb3 = new GridBagConstraints();
        gb3.gridx = 2;
        gb3.gridy = 4;
        gb3.weightx = 2;
        gb3.weighty = 1;
        gb3.anchor = GridBagConstraints.SOUTHEAST;
        gb3.insets = new Insets(0, 0, 0, 20);
        MainWindow.mainPanel.add(jb3, gb3);
        JButton jb4 = new JButton("返回主菜单");
        jb4.setPreferredSize(new Dimension(300, 80));
        GridBagConstraints gb4 = new GridBagConstraints();
        gb4.gridx = 2;
        gb4.gridy = 5;
        gb4.weightx = 2;
        gb4.weighty = 1;
        gb4.anchor = GridBagConstraints.SOUTHEAST;
        gb4.insets = new Insets(0, 0, 50, 20);
        MainWindow.mainPanel.add(jb4, gb4);
        Instrument.placeholderComponents(0, 0, 1, 3, MainWindow.mainPanel);
        Instrument.placeholderComponents(0, 3, 1, 3, MainWindow.mainPanel);
        Instrument.placeholderComponents(3, 6, 1, 1, MainWindow.mainPanel);
        Instrument.buttonType(jb1);
        Instrument.buttonType(jb2);
        Instrument.buttonType(jb3);
        Instrument.buttonType(jb4);
        jb1.addActionListener(arg0 -> AddNewDeal.setAddPanel(1));
        jb2.addActionListener(arg0 -> AddNewDeal.setAddPanel(2));
        jb4.addActionListener(arg0 -> MainWindow.mainButton());
    }
}

final class AddNewDeal {
    static int robotOrHuman;
    static JPanel jp1, jp2, buy, sell;
    static JButton stock, jb1, jb2;
    static JTextField jt1, jt2, jt3, jt4;
    static JRadioButton jr1, jr2, jr3, jr4;
    static JComboBox<String> jc1, jc2, jc3, jc4, jc5, jc6, jc7, jc8, jc9;
    static String ID;
    static String[] pathName;
    static AddPicture picPanel1, picPanel2;

    protected static void setStockLabel(String stock) {
        ID = stock.substring(0, 6);
        AddNewDeal.stock.setText(stock);
        jt1.requestFocus();
    }

    protected static void setAddPanel(int robotOrHuman) {
        AddNewDeal.robotOrHuman = robotOrHuman;
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 0;
        gb1.gridy = 1;
        gb1.weightx = 1;
        gb1.weighty = 1;
        gb1.insets = new Insets(0, 0, 0, 0);
        gb1.fill = GridBagConstraints.BOTH;
        jp1 = new JPanel();
        jp1.setLayout(new GridLayout(1, 2));
        jb1 = new JButton("返回");
        jb2 = new JButton("确定");
        jp1.add(jb2);
        jp1.add(jb1);
        MainWindow.mainPanel.add(jp1, gb1);
        Instrument.buttonType(jb1);
        Instrument.buttonType(jb2);
        jp2 = new JPanel();
        jp2.setLayout(null);
        jp2.setPreferredSize(new Dimension(1200, 740 + (robotOrHuman - 1) * 50 + 140 * Instrument.getNotes().length));
        addNewData(robotOrHuman);
    }

    private static void addNewData(int robotOrHuman) {
        buy = new JPanel();
        buy.setLayout(null);
        buy.setBounds(0, 0, 600, 165);
        buy.setBorder(BorderFactory.createTitledBorder("买入信息"));
        sell = new JPanel();
        sell.setLayout(null);
        sell.setBounds(600, 0, 600, 165);
        sell.setBorder(BorderFactory.createTitledBorder("卖出信息"));
        JLabel jl1 = new JLabel("股票信息：", SwingConstants.LEFT);
        jl1.setBounds(20, 20, 80, 30);
        buy.add(jl1);
        stock = new JButton("选择股票信息");
        stock.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        stock.setContentAreaFilled(false);
        stock.setBounds(100, 20, 300, 30);
        buy.add(stock);
        JLabel jl2 = new JLabel("买入价格：", SwingConstants.LEFT);
        jl2.setBounds(20, 70, 80, 30);
        buy.add(jl2);
        jt1 = new JTextField();
        jt1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jt1.setBounds(100, 70, 100, 30);
        jt1.setDocument(new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        buy.add(jt1);
        JLabel jl3 = new JLabel("买入数量：", SwingConstants.LEFT);
        jl3.setBounds(220, 70, 80, 30);
        buy.add(jl3);
        jt2 = new JTextField();
        jt2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jt2.setBounds(300, 70, 100, 30);
        jt2.setDocument(new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.INTEGER));
        buy.add(jt2);
        JLabel jl4 = new JLabel("买入日期：", SwingConstants.LEFT);
        buy.add(jl4);
        jc1 = new JComboBox<>();
        buy.add(jc1);
        jc2 = new JComboBox<>();
        buy.add(jc2);
        jc3 = new JComboBox<>();
        buy.add(jc3);
        Instrument.dataSelect(jc1, jc2, jc3);
        jc1.setSelectedIndex(jc1.getItemCount() - 1);
        jc2.setSelectedIndex(jc2.getItemCount() - 1);
        jc3.setSelectedIndex(jc3.getItemCount() - 1);
        JLabel jl5 = new JLabel("年", SwingConstants.LEFT);
        jl5.setBounds(330, 120, 30, 30);
        buy.add(jl5);
        JLabel jl6 = new JLabel("月", SwingConstants.LEFT);
        jl6.setBounds(420, 120, 30, 30);
        buy.add(jl6);
        JLabel jl7 = new JLabel("日", SwingConstants.LEFT);
        jl7.setBounds(510, 120, 30, 30);
        buy.add(jl7);
        jp2.add(buy);
        jr1 = new JRadioButton("正常买入");
        jr1.setBounds(20, 125, 80, 20);
        buy.add(jr1);
        jr2 = new JRadioButton("抢跑买入");
        jr2.setBounds(100, 125, 80, 20);
        buy.add(jr2);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(jr1);
        group1.add(jr2);
        JLabel jl8 = new JLabel("调取未完成数据：", SwingConstants.LEFT);
        jl8.setBounds(20, 20, 100, 30);
        sell.add(jl8);
        jc4 = new JComboBox<>();
        jc4.addItem("<创建新的交易>");
        jc4.setBounds(120, 20, 300, 30);
        sell.add(jc4);
        JLabel jl9 = new JLabel("卖出价格：", SwingConstants.LEFT);
        jl9.setBounds(20, 70, 80, 30);
        sell.add(jl9);
        jt3 = new JTextField();
        jt3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jt3.setBounds(120, 70, 100, 30);
        jt3.setDocument(new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        sell.add(jt3);
        JLabel jl10 = new JLabel("交易费用：", SwingConstants.LEFT);
        jl10.setBounds(240, 70, 80, 30);
        sell.add(jl10);
        jt4 = new JTextField();
        jt4.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jt4.setBounds(320, 70, 100, 30);
        jt4.setDocument(new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        sell.add(jt4);
        jr3 = new JRadioButton("正常卖出");
        jr3.setBounds(20, 125, 80, 20);
        sell.add(jr3);
        jr4 = new JRadioButton("延迟卖出");
        jr4.setBounds(100, 125, 80, 20);
        sell.add(jr4);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(jr3);
        group2.add(jr4);
        JLabel jl11 = new JLabel("卖出日期：", SwingConstants.LEFT);
        jl11.setBounds(200, 120, 80, 30);
        sell.add(jl11);
        jc5 = new JComboBox<>();
        jc5.setBounds(260, 120, 60, 30);
        sell.add(jc5);
        jc6 = new JComboBox<>();
        jc6.setBounds(360, 120, 50, 30);
        sell.add(jc6);
        jc7 = new JComboBox<>();
        jc7.setBounds(450, 120, 50, 30);
        sell.add(jc7);
        Instrument.dataSelect(jc5, jc6, jc7);
        jc5.setSelectedIndex(jc5.getItemCount() - 1);
        jc6.setSelectedIndex(jc6.getItemCount() - 1);
        jc7.setSelectedIndex(jc7.getItemCount() - 1);
        JLabel jl12 = new JLabel("年", SwingConstants.LEFT);
        jl12.setBounds(330, 120, 30, 30);
        sell.add(jl12);
        JLabel jl13 = new JLabel("月", SwingConstants.LEFT);
        jl13.setBounds(420, 120, 30, 30);
        sell.add(jl13);
        JLabel jl14 = new JLabel("日", SwingConstants.LEFT);
        jl14.setBounds(510, 120, 30, 30);
        sell.add(jl14);
        jl11.setVisible(false);
        jl12.setVisible(false);
        jl13.setVisible(false);
        jl14.setVisible(false);
        jc5.setVisible(false);
        jc6.setVisible(false);
        jc7.setVisible(false);
        jr3.addActionListener(e -> {
            jl11.setVisible(false);
            jl12.setVisible(false);
            jl13.setVisible(false);
            jl14.setVisible(false);
            jc5.setVisible(false);
            jc6.setVisible(false);
            jc7.setVisible(false);
        });
        jr4.addActionListener(e -> {
            jl11.setVisible(true);
            jl12.setVisible(true);
            jl13.setVisible(true);
            jl14.setVisible(true);
            jc5.setVisible(true);
            jc6.setVisible(true);
            jc7.setVisible(true);
        });
        jp2.add(sell);
        jc8 = new JComboBox<>();
        jc9 = new JComboBox<>();
        if (robotOrHuman == 2) {
            picPanel1 = new AddPicture(jp2, 1, 250);
            picPanel2 = new AddPicture(jp2, 2, 250);
            jl4.setBounds(200, 120, 80, 30);
            jc1.setBounds(260, 120, 60, 30);
            jc2.setBounds(360, 120, 50, 30);
            jc3.setBounds(450, 120, 50, 30);
            jl5.setBounds(330, 120, 30, 30);
            jl6.setBounds(420, 120, 30, 30);
            jl7.setBounds(510, 120, 30, 30);
            jr1.setVisible(true);
            jr2.setVisible(true);
            JLabel jl15 = new JLabel("图形分类：", SwingConstants.LEFT);
            jl15.setBounds(20, 200, 80, 30);
            jp2.add(jl15);
            jc8.addItem("<请选择一级分类>");
            jc8.setBounds(100, 200, 300, 30);
            jp2.add(jc8);
            jc9.addItem("<请选择二级分类>");
            jc9.setBounds(420, 200, 300, 30);
            jp2.add(jc9);
            Instrument.addClassifyItem(jc8, jc9);
            AddNotes.addNote(773, jp2);
        } else {
            picPanel1 = new AddPicture(jp2, 1, 200);
            picPanel2 = new AddPicture(jp2, 2, 200);
            jl4.setBounds(20, 120, 300, 30);
            jc1.setBounds(100, 120, 60, 30);
            jc2.setBounds(200, 120, 50, 30);
            jc3.setBounds(290, 120, 50, 30);
            jl5.setBounds(170, 120, 30, 30);
            jl6.setBounds(260, 120, 30, 30);
            jl7.setBounds(350, 120, 30, 30);
            jr1.setVisible(false);
            jr2.setVisible(false);
            AddNotes.addNote(723, jp2);
        }
        stock.addActionListener(arg0 -> FindStock.findFrame());
        jb1.addActionListener(arg0 -> ChooseAddDeal.setAddDealPane());
        jb2.addActionListener(arg0 -> checkAndSave(robotOrHuman));
        JScrollPane js = new JScrollPane(jp2);
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 0;
        gb2.gridy = 0;
        gb2.weighty = 7;
        gb2.insets = new Insets(0, 0, 0, 0);
        gb2.fill = GridBagConstraints.BOTH;
        MainWindow.mainPanel.add(js, gb2);
        final int[] abc = {0};
        JScrollBar jsb = js.getVerticalScrollBar();
        jsb.setUnitIncrement(20);
        jsb.addAdjustmentListener(e -> {
            if (abc[0] < 2 && jsb.getValue() != 0) {
                jsb.setValue(0);
                abc[0]++;
            }
        });
        loadHistory(robotOrHuman);
        FindStock.findFrame();
    }

    private static void loadHistory(int robotOrHuman) {
        FindUnfinished unfinished = new FindUnfinished();
        unfinished.traverse();
        String[] buyInf = unfinished.total.split("I");
        StringBuilder jlInf = new StringBuilder();
        Instrument.FindStockName f2 = new Instrument.FindStockName();
        if (!buyInf[0].equals("")) {
            for (String s : buyInf) {
                int type = 2;
                String[] buyInf1 = s.split("\n");
                if (buyInf1[7].contains("./database/machine/")) {
                    type = 1;
                }
                if (type == robotOrHuman) {
                    jlInf.append(s).append("I");
                    jc4.addItem(buyInf1[0] + "    " + f2.getName(buyInf1[0]) + "    " + buyInf1[3] + "年" + buyInf1[4] + "月" + buyInf1[5] + "日");
                }
            }
        }
        String[] jlInf1 = jlInf.toString().split("I");
        pathName = new String[jlInf1.length];
        if (jc4.getItemCount() != 1) {
            for (int i = 0; i < jlInf1.length; i++) {
                pathName[i] = jlInf1[i].split("\n")[7];
            }
        }
        jc4.addActionListener(e -> {
            if (!Objects.requireNonNull(jc4.getSelectedItem()).equals("<创建新的交易>")) {
                String[] buyInf1 = jlInf1[jc4.getSelectedIndex() - 1].split("\n");
                picPanel1.note.setText(Instrument.getFileData(new File(buyInf1[7] + "pictureNote1")));
                picPanel2.note.setText(Instrument.getFileData(new File(buyInf1[7] + "pictureNote2")));
                for (int i = 0; i < AddNotes.notes.length; i++) {
                    File notes = new File(buyInf1[7] + AddNotes.notes[i]);
                    if (notes.exists()) {
                        AddNotes.jt[i].setText(Instrument.getFileData(notes));
                    }
                }
                ID = buyInf1[0];
                stock.setText(buyInf1[0] + "     " + f2.getName(buyInf1[0]));
                jt1.setText(buyInf1[1]);
                jt2.setText(buyInf1[2]);
                for (int q1 = 0; q1 < jc1.getItemCount(); q1++) {
                    if (Integer.parseInt(jc1.getItemAt(q1)) == Integer.parseInt(buyInf1[3])) {
                        jc1.setSelectedIndex(q1);
                        break;
                    }
                }
                for (int q1 = 0; q1 < jc2.getItemCount(); q1++) {
                    if (Integer.parseInt(jc2.getItemAt(q1)) == Integer.parseInt(buyInf1[4])) {
                        jc2.setSelectedIndex(q1);
                        break;
                    }
                }
                for (int q1 = 0; q1 < jc3.getItemCount(); q1++) {
                    if (Integer.parseInt(jc3.getItemAt(q1)) == Integer.parseInt(buyInf1[5])) {
                        jc3.setSelectedIndex(q1);
                        break;
                    }
                }
                String[] path = buyInf1[7].split("/");
                if (path.length == 6) {
                    for (int q1 = 0; q1 < jc8.getItemCount(); q1++) {
                        if (jc8.getItemAt(q1).equals(path[3])) {
                            jc8.setSelectedIndex(q1);
                            break;
                        }
                    }
                    for (int q1 = 0; q1 < jc9.getItemCount(); q1++) {
                        if (jc9.getItemAt(q1).equals(path[4])) {
                            jc9.setSelectedIndex(q1);
                            break;
                        }
                    }
                }
                if (buyInf1[6].equals("true")) {
                    jr1.setSelected(true);
                } else {
                    jr2.setSelected(true);
                }
                File pic1 = new File(buyInf1[7] + "picture1.jpg");
                if (pic1.exists()) {
                    try {
                        Instrument.copyFile(pic1, "temporary1.jpg");
                        picPanel1.load();
                    } catch (Exception ioException) {
                        JOptionPane.showMessageDialog(null, "读取文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    picPanel1.resetLabel();
                }
                File pic2 = new File(buyInf1[7] + "picture2.jpg");
                if (pic2.exists()) {
                    try {
                        Instrument.copyFile(pic2, "temporary2.jpg");
                        picPanel2.load();
                    } catch (Exception ioException) {
                        JOptionPane.showMessageDialog(null, "读取文件失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    picPanel2.resetLabel();
                }
            } else {
                picPanel1.note.setText("在此输入图片备注");
                picPanel2.note.setText("在此输入图片备注");
                for (int i = 0; i < AddNotes.notes.length; i++) AddNotes.jt[i].setText("");
                stock.setText("选择股票信息");
                jt1.setText("");
                jt2.setText("");
                jc1.setSelectedIndex(jc1.getItemCount() - 1);
                jc2.setSelectedIndex(jc2.getItemCount() - 1);
                jc3.setSelectedIndex(jc3.getItemCount() - 1);
                if (robotOrHuman == 2) {
                    jc8.setSelectedIndex(0);
                    jc9.setSelectedIndex(0);
                }
                jr1.setSelected(true);
                jr2.setSelected(false);
                picPanel1.resetLabel();
                picPanel2.resetLabel();
            }
        });
    }

    private static void checkAndSave(int robotOrHuman) {
        InputDatabase database = new InputDatabase();
        if (stock.getText().equals("选择股票信息")) {
            JOptionPane.showMessageDialog(null, "请选择一支股票！", "提示", JOptionPane.WARNING_MESSAGE);
            stock.requestFocus();
            return;
        }
        database.getStockID(ID);
        if (!database.getBuyingPrice(jt1.getText())) {
            JOptionPane.showMessageDialog(null, "请输入正确的买入价格！", "提示", JOptionPane.WARNING_MESSAGE);
            jt1.requestFocus();
            return;
        }
        if (!database.getBuyingNumber(jt2.getText())) {
            JOptionPane.showMessageDialog(null, "请输入买入数量！", "提示", JOptionPane.WARNING_MESSAGE);
            jt2.requestFocus();
            return;
        }
        if (robotOrHuman == 2) {
            if (!jr1.isSelected() && !jr2.isSelected()) {
                JOptionPane.showMessageDialog(null, "请选择买入类型！", "提示", JOptionPane.WARNING_MESSAGE);
                jr1.requestFocus();
                return;
            } else {
                database.getBuyingMode(jr1.isSelected());
            }
            if (!database.getCategory((String) Objects.requireNonNull(jc8.getSelectedItem()), (String) jc9.getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "请选择操作分类！", "提示", JOptionPane.WARNING_MESSAGE);
                jc8.requestFocus();
                return;
            }
        }
        database.getBuyingDay((String) jc1.getSelectedItem(), (String) Objects.requireNonNull(jc2.getSelectedItem()),
                (String) jc3.getSelectedItem());
        database.getPictureNotes(picPanel1.note.getText(), 1);
        database.getPictureNotes(picPanel2.note.getText(), 2);
        if (jt3.getText().length() == 0 && jt4.getText().length() == 0
                && Objects.requireNonNull(jc4.getSelectedItem()).equals("<创建新的交易>")) {
            database.createFilePath(robotOrHuman);
            database.inputBuying();
            database.inputNotes();
            JOptionPane.showMessageDialog(null, "您尚未填写卖出信息 ，已添加为未完成交易！");
        } else {
            if (!database.getSellingPrice(jt3.getText())) {
                JOptionPane.showMessageDialog(null, "请输入正确的卖出价格！", "提示", JOptionPane.WARNING_MESSAGE);
                jt3.requestFocus();
                return;
            }
            if (!database.getPoundage(jt4.getText())) {
                JOptionPane.showMessageDialog(null, "请输入正确的交易费用！", "提示", JOptionPane.WARNING_MESSAGE);
                jt4.requestFocus();
                return;
            }
            if (!jr3.isSelected() && !jr4.isSelected()) {
                JOptionPane.showMessageDialog(null, "请选择卖出类型！", "提示", JOptionPane.WARNING_MESSAGE);
                jr3.requestFocus();
                return;
            } else if (jr3.isSelected()) {
                database.getSellingMode(true);
            } else {
                database.getSellingMode(false);
                if (!database.getSellingDay((String) jc5.getSelectedItem(), (String) jc6.getSelectedItem(),
                        (String) jc7.getSelectedItem())) {
                    JOptionPane.showMessageDialog(null, "卖出日期错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    jc5.requestFocus();
                    return;
                }
            }
            if (Objects.requireNonNull(jc4.getSelectedItem()).equals("<创建新的交易>")) {
                database.createFilePath(robotOrHuman);
            } else {
                database.pathName = pathName[jc4.getSelectedIndex() - 1];
            }
            database.inputBuying();
            database.inputSelling();
            database.inputNotes();
            JOptionPane.showMessageDialog(null, "添加交易成功！");
        }
        setAddPanel(robotOrHuman);
    }
}

final class AddPicture {
    protected JLabel jl;
    protected JButton screenshot, add, delete;
    protected JTextArea note;
    protected int which;

    protected AddPicture(JPanel jp, int which, int location) {
        this.which = which;
        deletePicture();
        jl = new JLabel("添加新的图片", SwingConstants.CENTER);
        jl.setFont(new Font("黑体", Font.BOLD, 20));
        jl.setForeground(Color.GRAY);
        String title;
        if (which == 1) title = "图片1";
        else title = "图片2";
        jl.setBorder(BorderFactory.createTitledBorder(title));
        screenshot = new JButton("截图");
        add = new JButton("导入");
        delete = new JButton("删除");
        delete.setVisible(false);
        Instrument.buttonType(screenshot);
        Instrument.buttonType(add);
        Instrument.buttonType(delete);
        note = new JTextArea();
        note.setLineWrap(true);
        note.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Instrument.setMarkWords(note, "在此输入图片备注", new Instrument.JTextLimited(200,
                Instrument.JTextLimited.limitTypeEnum.TEXT));
        screenshot.addActionListener(arg0 -> new CaptureScreen(which));
        add.addActionListener(arg0 -> {
            boolean ifNeedImport;
            if (which == 1) {
                ifNeedImport = Instrument.importImage("temporary1.jpg");
            } else {
                ifNeedImport = Instrument.importImage("temporary2.jpg");
            }
            if (ifNeedImport) {
                try {
                    load();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "读取文件失败，请重试！",
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        delete.addActionListener(arg0 -> {
            screenshot.setVisible(true);
            add.setVisible(true);
            delete.setVisible(false);
            jl.setIcon(null);
            jl.setText("添加新的图片");
            jl.setFont(new Font("黑体", Font.BOLD, 20));
            jl.setForeground(Color.GRAY);
            File file;
            if (which == 1) {
                file = new File("temporary1.jpg");
            } else {
                file = new File("temporary2.jpg");
            }
            file.delete();
        });
        if (which == 1) {
            jl.setBounds(20, location, 500, 333);
            screenshot.setBounds(20, location + 343, 245, 50);
            add.setBounds(275, location + 343, 245, 50);
            delete.setBounds(20, location + 343, 500, 50);
            note.setBounds(20, location + 403, 500, 100);
        } else {
            jl.setBounds(540, location, 500, 333);
            screenshot.setBounds(540, location + 343, 245, 50);
            add.setBounds(795, location + 343, 245, 50);
            delete.setBounds(540, location + 343, 500, 50);
            note.setBounds(540, location + 403, 500, 100);
        }
        jp.add(jl);
        jp.add(screenshot);
        jp.add(add);
        jp.add(delete);
        jp.add(note);
    }

    protected void resetLabel() {
        jl.setIcon(null);
        jl.setText("添加新的图片");
        jl.setFont(new Font("黑体", Font.BOLD, 20));
    }

    protected void load() throws IOException {
        jl.setText(null);
        ImageIcon image;
        if (which == 1) {
            image = new ImageIcon(ImageIO.read(new File("temporary1.jpg")));
        } else {
            image = new ImageIcon(ImageIO.read(new File("temporary2.jpg")));
        }
        image.setImage(image.getImage().getScaledInstance(500, 333, Image.SCALE_DEFAULT));
        jl.setIcon(image);
        screenshot.setVisible(false);
        add.setVisible(false);
        delete.setVisible(true);
    }

    private static void deletePicture() {
        File file0 = new File("temporary1.jpg");
        if (file0.exists()) {
            file0.delete();
        }
        File file1 = new File("temporary2.jpg");
        if (file1.exists()) {
            file1.delete();
        }
    }
}

final class AddNotes {
    static String[] notes;
    static JTextArea[] jt;

    protected static void addNote(int y, JPanel jp) {
        notes = Instrument.getNotes();
        jt = new JTextArea[notes.length];
        for (int i = 0; i < notes.length; i++) {
            JLabel jl = new JLabel(notes[i], SwingConstants.LEFT);
            jl.setBounds(20, y, 800, 30);
            jp.add(jl);
            jt[i] = new JTextArea();
            jt[i].setLineWrap(true);
            jt[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            jt[i].setBounds(20, y + 30, 400, 100);
            jp.add(jt[i]);
            y = y + 140;
        }
    }
}