package browseInformation;

import instrument.AddPanel;
import instrument.DealNumber;
import instrument.Instrument;
import mainWindow.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public final class BrowserData {
    // 面板
    protected static JPanel inside, top, left, functionPane, southPane;
    protected static JButton jb3, jb4;
    // 基础信息
    public static int noteNumber;
    public static int dealNumber;
    public static String[] noteName;
    protected static String[][] dataInformation;
    private static Instrument.FindStockName stockName;
    // 显示信息
    protected static Boolean[] ifShowing;
    // 排序信息
    protected static int sort;
    protected static Boolean ascend;
    // 删改信息
    protected static boolean showEdit;
    // 筛选信息
    protected static int compareNumber;
    protected static boolean isComparing;
    // 进度条
    private static int progress;
    private static boolean ifEnd;

    public static void setDataPane() {
        SelectAndCompare.classifyInformation = new String[10];
        noteName = Instrument.getNotes();
        noteNumber = noteName.length;
        stockName = new Instrument.FindStockName();
        sort = 0;
        ascend = true;
        showEdit = false;
        isComparing = false;
        compareNumber = 1;
        ifShowing = new Boolean[13 + noteNumber];
        for (int i = 0; i < 13 + noteNumber; i++) {
            ifShowing[i] = i < 11;
        }
        loadData();
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new BorderLayout());
        inside = new JPanel(new FlowLayout());
        JScrollPane js1 = new JScrollPane(inside);
        js1.getVerticalScrollBar().setUnitIncrement(20);
        top = new JPanel(new FlowLayout());
        js1.setColumnHeaderView(top);
        left = new JPanel();
        js1.setRowHeaderView(left);
        js1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        MainWindow.mainPanel.add(js1, BorderLayout.CENTER);
        functionPane = new JPanel(new GridLayout(0, 5));
        addFunctionButton(functionPane);
        southPane = new JPanel();
        southPane.setPreferredSize(new Dimension(0, 100));
        MainWindow.mainPanel.add(southPane, BorderLayout.SOUTH);
        loadPane();
    }

    private static void loadData() {
        DealNumber d1 = new DealNumber();
        d1.traverse();
        dealNumber = d1.dealNumber;
        AddPanel a1 = new AddPanel();
        a1.traverse();
        dataInformation = a1.dataInformation;
    }

    private static void addFunctionButton(JPanel function) {
        JButton jb1 = new JButton("显示/隐藏");
        Instrument.buttonType(jb1);
        function.add(jb1);
        JButton jb2 = new JButton("排序");
        Instrument.buttonType(jb2);
        function.add(jb2);
        jb3 = new JButton("筛选/比较");
        Instrument.buttonType(jb3);
        function.add(jb3);
        jb4 = new JButton("删除/修改");
        Instrument.buttonType(jb4);
        function.add(jb4);
        JButton jb5 = new JButton("返回主菜单");
        Instrument.buttonType(jb5);
        function.add(jb5);
        jb1.addActionListener(arg0 -> FunctionFrame.screeningWindow());
        jb2.addActionListener(arg0 -> FunctionFrame.sortWindow());
        jb3.addActionListener(arg0 -> {
            if (isComparing) {
                compareNumber = 1;
                SelectAndCompare.classifyInformation = new String[10];
                jb3.setText("筛选/比较");
                jb4.setEnabled(true);
                isComparing = false;
                loadPane();
            } else {
                SelectAndCompare.selectFrame();
            }
        });
        jb4.addActionListener(arg0 -> {
            showEdit = !showEdit;
            jb1.setEnabled(!showEdit);
            jb2.setEnabled(!showEdit);
            jb3.setEnabled(!showEdit);
            if (showEdit) {
                jb4.setText("取消删除/修改");
            } else {
                jb4.setText("删除/修改");
            }
            loadPane();
        });
        jb5.addActionListener(arg0 -> {
            MainWindow.mainButton();
            exit();
        });
    }

    protected static void loadPane() {
        progress = 0;
        ifEnd = false;
        Instrument.resetJPanel(southPane);
        southPane.setLayout(new GridLayout());
        JProgressBar jp = new JProgressBar();
        jp.setStringPainted(true);
        jp.setString("数据加载中……");
        southPane.add(jp);
        MainWindow.mainFrame.paintAll(MainWindow.mainFrame.getGraphics());
        Thread progressThread = new Thread(() -> {
            int totalNumber = dealNumber * compareNumber;
            if (totalNumber == 0) {
                totalNumber++;
            }
            while (!ifEnd) {
                jp.setValue((progress * 100) / totalNumber);
                jp.paintImmediately(0, 0, jp.getSize().width, jp.getSize().height);
            }
        });
        progressThread.start();
        if (dealNumber != 0) {
            Sort.sortWithData(sort);
        }
        JButton[] changeButton = new JButton[dealNumber];
        JButton[] deleteButton = new JButton[dealNumber];
        changeAndDelete(changeButton, deleteButton);
        Instrument.resetJPanel(inside);
        Instrument.resetJPanel(top);
        Instrument.resetJPanel(left);
        inside.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel[] total = new JPanel[compareNumber];
        int width = 0;
        int height = 57;
        for (int i = 0; i < compareNumber; i++) {
            total[i] = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            JPanel title = new JPanel(new FlowLayout());
            title.setBackground(Color.LIGHT_GRAY);
            title.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            setTitle(title);
            width = title.getPreferredSize().width;
            int totalNum = 0;
            int finishNum = 0;
            float totalAmount = 0;
            float totalRate = 0;
            for (int j = 0; j < dealNumber; j++) {
                if (SelectAndCompare.comparable(j, i)) {
                    JPanel each = addDealPane(j, changeButton, deleteButton);
                    height = each.getPreferredSize().height;
                    total[i].add(each);
                    totalNum++;
                    if (dataInformation[j][0] != null && !dataInformation[j][8].equals("-")) {
                        finishNum++;
                        totalAmount = totalAmount + Float.parseFloat(dataInformation[j][14]);
                        totalRate = totalRate + Float.parseFloat(dataInformation[j][15]);
                    }
                }
                progress++;
            }
            if (total[i].getComponents().length == 0) {
                JLabel jl = new JLabel("无数据", JLabel.CENTER);
                jl.setPreferredSize(new Dimension(width, 57));
                jl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                total[i].add(jl);
            }
            inside.add(total[i]);
            DecimalFormat de = new DecimalFormat("0.00");
            String space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            String string;
            if (finishNum != 0) {
                string = "<html><body>交易总量：" + totalNum + space + "已完成：" + finishNum + space + "合计盈亏额："
                        + de.format(totalAmount) + "元" + space + "平均盈亏额：" + de.format(totalAmount / finishNum)
                        + "元" + space + "合计盈亏率：" + de.format(totalRate * 100) + "%" + space + "平均盈亏率："
                        + de.format((totalRate / finishNum) * 100) + "%";
            } else {
                string = "<html><body>交易总量：" + totalNum + space + "已完成：0" + space + "合计盈亏额：0.00元" + space
                        + "平均盈亏额：0.00元" + space + "合计盈亏率：0.00%" + space + "平均盈亏率：0.00%";
            }
            JLabel jl1 = new JLabel(string, JLabel.CENTER);
            jl1.setPreferredSize(new Dimension(width, 57));
            jl1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            jl1.setOpaque(true);
            jl1.setBackground(Color.WHITE);
            JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            jPanel.setPreferredSize(new Dimension(width, 114));
            jPanel.add(jl1);
            jPanel.add(title);
            top.add(jPanel);
        }
        int long1 = 0;
        for (int i = 0; i < compareNumber; i++) {
            if (total[i].getComponents().length > long1) {
                long1 = total[i].getComponents().length;
            }
        }
        for (int i = 0; i < compareNumber; i++) {
            total[i].setPreferredSize(new Dimension(width, height * long1));
        }
        left.setPreferredSize(new Dimension(40, height * long1));
        for (int i = 0; i < long1; i++) {
            JLabel jLabel = new JLabel(String.valueOf(i + 1), JLabel.CENTER);
            jLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            jLabel.setOpaque(true);
            jLabel.setBackground(Color.LIGHT_GRAY);
            jLabel.setPreferredSize(new Dimension(40, height));
            left.add(jLabel);
        }
        ifEnd = true;
        Instrument.resetJPanel(southPane);
        southPane.setLayout(new GridLayout());
        southPane.add(functionPane);
        System.gc();
    }

    private static void setTitle(JPanel title) {
        if (ifShowing[0]) {
            title.add(setJLabel("买入时间", 120));
        }
        title.add(setJLabel("股票信息", 120));
        if (ifShowing[1]) {
            title.add(setJLabel("操作分类", 120));
        }
        if (ifShowing[2]) {
            title.add(setJLabel("图形分类", 120));
        }
        if (ifShowing[3]) {
            title.add(setJLabel("买入价格（元）", 120));
        }
        if (ifShowing[4]) {
            title.add(setJLabel("买入数量（股）", 120));
        }
        if (ifShowing[5]) {
            title.add(setJLabel("买入类型", 120));
        }
        if (ifShowing[6]) {
            title.add(setJLabel("卖出价格（元）", 120));
        }
        if (ifShowing[7]) {
            title.add(setJLabel("交易费用（元）", 120));
        }
        if (ifShowing[8]) {
            title.add(setJLabel("卖出类型/时间", 120));
        }
        if (ifShowing[9]) {
            title.add(setJLabel("盈亏额（元）", 120));
        }
        if (ifShowing[10]) {
            title.add(setJLabel("盈亏率", 120));
        }
        if (ifShowing[11]) {
            title.add(setJLabel("图片1/注释", 404));
        }
        if (ifShowing[12]) {
            title.add(setJLabel("图片2/注释", 404));
        }
        for (int i = 0; i < noteNumber; i++)
            if (ifShowing[13 + i]) {
                title.add(setJLabel(noteName[i], 200));
            }
        if (showEdit) {
            title.add(setJLabel("修改", 120));
            title.add(setJLabel("删除", 120));
        }
    }

    private static JPanel addDealPane(int load, JButton[] changeButton, JButton[] deleteButton) {
        JPanel each = new JPanel(new FlowLayout());
        each.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (ifShowing[0]) {
            each.add(setJLabel(dataInformation[load][4] + "年 " + dataInformation[load][5] + "月 "
                    + dataInformation[load][6] + "日", 120));
        }
        each.add(setJLabel("<html><body>" + dataInformation[load][1] + "<br>"
                + stockName.getName(dataInformation[load][1]), 120));
        if (ifShowing[1]) {
            String[] cll1 = dataInformation[load][0].split("/");
            if (cll1[2].equals("machine")) {
                each.add(setJLabel("波段操作", 120));
            } else {
                each.add(setJLabel("超短操作", 120));
            }
        }
        if (ifShowing[2]) {
            String[] cll1 = dataInformation[load][0].split("/");
            if (cll1[2].equals("machine")) {
                each.add(setJLabel("-", 120));
            } else if (cll1[2].equals("none")) {
                each.add(setJLabel("<分类已删除>", 120));
            } else {
                each.add(setJLabel("<html><body>" + dataInformation[load][0].split("/")[4], 120));
            }
        }
        DecimalFormat de = new DecimalFormat("0.00");
        if (ifShowing[3]) {
            each.add(setJLabel(de.format(Float.parseFloat(dataInformation[load][2])), 120));
        }
        if (ifShowing[4]) {
            each.add(setJLabel(dataInformation[load][3], 120));
        }
        if (ifShowing[5]) {
            if (dataInformation[load][0].contains("machine") || dataInformation[load][7].equals("true")) {
                each.add(setJLabel("正常买入", 120));
            } else {
                each.add(setJLabel("抢跑买入", 120));
            }
        }
        if (!dataInformation[load][8].equals("-")) {
            if (ifShowing[6]) {
                each.add(setJLabel(de.format(Float.parseFloat(dataInformation[load][8])), 120));
            }
            if (ifShowing[7]) {
                each.add(setJLabel(de.format(Float.parseFloat(dataInformation[load][9])), 120));
            }
            if (ifShowing[8]) {
                if (dataInformation[load][13].equals("false")) {
                    each.add(setJLabel("<html><body>延迟卖出<br>" + dataInformation[load][10] + "年 "
                            + dataInformation[load][11] + "月 " + dataInformation[load][12] + "日", 120));
                } else {
                    each.add(setJLabel("正常卖出", 120));
                }
            }
            if (ifShowing[9]) {
                each.add(setJLabel(de.format(Float.parseFloat(dataInformation[load][14])), 120));
            }
            if (ifShowing[10]) {
                each.add(setJLabel(de.format(Float.parseFloat(dataInformation[load][15]) * 100) + "%",
                        120));
            }
        } else {
            for (int i = 6; i < 11; i++) {
                if (ifShowing[i]) {
                    each.add(setJLabel("-", 120));
                }
            }
        }
        if (ifShowing[11]) {
            Picture picture1 = new Picture(load, true);
            each.add(picture1.getPicPanel());
        }
        if (ifShowing[12]) {
            Picture picture2 = new Picture(load, false);
            each.add(picture2.getPicPanel());
        }
        for (int i = 0; i < noteNumber; i++) {
            JTextArea Notes = new JTextArea(dataInformation[load][18 + i]);
            Notes.setLineWrap(true);
            Notes.setEditable(false);
            JScrollPane js3 = new JScrollPane(Notes);
            js3.setPreferredSize(new Dimension(200, 90));
            js3.setBorder(BorderFactory.createEtchedBorder());
            if (ifShowing[13 + i]) {
                each.add(js3);
            }
        }
        if (showEdit) {
            each.add(changeButton[load]);
            each.add(deleteButton[load]);
        }
        return each;
    }

    private static JLabel setJLabel(String text, int x) {
        JLabel jl = new JLabel(text);
        jl.setPreferredSize(new Dimension(x, 45));
        return jl;
    }

    private static void changeAndDelete(JButton[] changeButton, JButton[] deleteButton) {
        for (int i = 0; i < dealNumber; i++) {
            changeButton[i] = new JButton("修改");
            changeButton[i].setPreferredSize(new Dimension(120, 45));
            Instrument.buttonType(changeButton[i]);
            deleteButton[i] = new JButton("删除");
            deleteButton[i].setPreferredSize(new Dimension(120, 45));
            Instrument.buttonType(deleteButton[i]);
            int finalI = i;
            changeButton[i].addActionListener(e -> System.out.println(1));
            deleteButton[i].addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(null, "确认删除此交易吗？",
                        "删除", JOptionPane.YES_NO_OPTION) == 0) {
                    Instrument.deleteFileOrDirectory((String) dataInformation[finalI][0].subSequence(0,
                            dataInformation[finalI][0].length() - 1));
                    loadData();
                    loadPane();
                }
            });
        }
    }

    private static void exit() {
        inside = null;
        top = null;
        left = null;
        functionPane = null;
        southPane = null;
        noteName = null;
        dataInformation = null;
        stockName = null;
        ifShowing = null;
        SelectAndCompare.classifyInformation = null;
    }
}

final class Picture {
    JPanel pic;

    protected Picture(int load, boolean which) {
        JLabel picture = new JLabel("暂无图片", JLabel.CENTER);
        picture.setPreferredSize(new Dimension(400, 225));
        File Fig;
        if (which) {
            Fig = new File(BrowserData.dataInformation[load][0] + "picture1.jpg");
        } else {
            Fig = new File(BrowserData.dataInformation[load][0] + "picture2.jpg");
        }
        if (Fig.exists()) {
            try {
                ImageIcon fig1 = new ImageIcon(ImageIO.read(Fig));
                fig1.setImage(fig1.getImage().getScaledInstance(400, 225, Image.SCALE_DEFAULT));
                picture.setIcon(fig1);
                picture.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            if (which) {
                                new ShowPicture(BrowserData.dataInformation[load][0] + "picture1.jpg");
                            } else {
                                new ShowPicture(BrowserData.dataInformation[load][0] + "picture2.jpg");
                            }
                        }
                    }

                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseEntered(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pic = new JPanel(new BorderLayout());
        pic.setBorder(BorderFactory.createEtchedBorder());
        JTextArea pictureNote;
        if (which) {
            pictureNote = new JTextArea(BrowserData.dataInformation[load][16]);
        } else {
            pictureNote = new JTextArea(BrowserData.dataInformation[load][17]);
        }
        pictureNote.setLineWrap(true);
        pictureNote.setEditable(false);
        JScrollPane js = new JScrollPane(pictureNote);
        js.setBorder(null);
        js.setPreferredSize(new Dimension(400, 90));
        pic.add(picture, BorderLayout.CENTER);
        pic.add(js, BorderLayout.SOUTH);
    }

    protected JPanel getPicPanel() {
        return pic;
    }
}