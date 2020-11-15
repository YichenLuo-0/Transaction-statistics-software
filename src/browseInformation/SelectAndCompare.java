package browseInformation;

import instrument.Instrument;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.Objects;

import static browseInformation.BrowserData.dataInformation;

final class SelectAndCompare {
    private static JInternalFrame jf;
    private static CardLayout card1, card2;
    private static Container c;
    private static JPanel addPanel, function;
    private static JButton sureButton;
    private static JCheckBox[] ifChoose;
    protected static String[] classifyInformation;

    protected static void selectFrame() {
        classifyInformation = new String[10];
        BrowserData.compareNumber = 0;
        jf = FunctionFrame.setFrame("设置比较模式");
        card1 = new CardLayout();
        jf.setLayout(card1);
        c = jf.getContentPane();
        addPanel = new JPanel();
        panel1();
        c.add(addPanel, "0");
        JPanel jp2 = new JPanel();
        panel2(jp2);
        c.add(jp2, "1");
        JPanel jp3 = new JPanel();
        panel3(jp3);
        c.add(jp3, "2");
    }

    private static void panel1() {
        Instrument.resetJPanel(addPanel);
        addPanel.setLayout(new BorderLayout());
        JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jp1.setPreferredSize(new Dimension(450, 40 * (BrowserData.compareNumber + 1)));
        JPanel[] classify = new JPanel[BrowserData.compareNumber];
        JLabel[] classifyLabel = new JLabel[BrowserData.compareNumber];
        JButton[] classifyButton = new JButton[BrowserData.compareNumber];
        for (int i = 0; i < BrowserData.compareNumber; i++) {
            classify[i] = new JPanel(new GridLayout());
            classify[i].setPreferredSize(new Dimension(450, 40));
            classify[i].setBorder(BorderFactory.createEtchedBorder());
            StringBuilder string = new StringBuilder("第" + (i + 1) + "栏：");
            for (int j = 0; j < 10; j++) {
                String[] s1 = classifyInformation[j].split("\\*");
                if (!s1[i].equals("f")) {
                    switch (j) {
                        case (0) -> string.append("日期、");
                        case (1) -> string.append("操作分类、");
                        case (2) -> string.append("图形分类、");
                        case (3) -> string.append("买入价格、");
                        case (4) -> string.append("买入数量、");
                        case (5) -> string.append("买入类型、");
                        case (6) -> string.append("卖出价格、");
                        case (7) -> string.append("卖出类型、");
                        case (8) -> string.append("盈亏额、");
                        case (9) -> string.append("盈亏率、");
                    }
                }
            }
            classifyLabel[i] = new JLabel(string.substring(0, string.length() - 1), JLabel.CENTER);
            classify[i].add(classifyLabel[i]);
            classifyButton[i] = new JButton("删除");
            classifyButton[i].setContentAreaFilled(false);
            classifyButton[i].setBorder(BorderFactory.createEtchedBorder());
            int finalI1 = i;
            classifyButton[i].addActionListener(arg0 -> {
                BrowserData.compareNumber--;
                for (int k = 0; k < classifyInformation.length; k++) {
                    String[] tem = classifyInformation[k].split("\\*");
                    StringBuilder tem1 = new StringBuilder();
                    for (int j = 0; j < tem.length; j++) {
                        if (j != finalI1) {
                            tem1.append(tem[j]).append("*");
                        }
                    }
                    classifyInformation[k] = tem1.toString();
                }
                panel1();
            });
            classify[i].add(classifyButton[i]);
            jp1.add(classify[i]);
        }
        JButton jb1 = new JButton("新增一栏");
        Instrument.buttonType(jb1);
        jb1.setPreferredSize(new Dimension(450, 40));
        jp1.add(jb1);
        JScrollPane js = new JScrollPane(jp1);
        addPanel.add(js, BorderLayout.CENTER);
        JPanel jp2 = new JPanel();
        JButton jb2 = new JButton("确定");
        JButton jb3 = new JButton("取消");
        jb1.addActionListener(arg0 -> card1.show(c, "1"));
        jb2.addActionListener(arg0 -> {
            if (jp1.getComponents().length == 1) {
                JOptionPane.showMessageDialog(null, "请添加至少一个比较栏！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            BrowserData.jb3.setText("取消筛选/比较");
            BrowserData.isComparing = true;
            jf.dispose();
            jf = null;
            BrowserData.loadPane();
        });
        jb3.addActionListener(arg0 -> {
            BrowserData.compareNumber = 1;
            jf.dispose();
            jf = null;
        });
        jp2.add(jb2);
        jp2.add(jb3);
        addPanel.add(jp2, BorderLayout.SOUTH);
    }

    private static void panel2(JPanel jp) {
        jp.setLayout(new BorderLayout());
        JPanel jp1 = new JPanel();
        sureButton = new JButton("添加");
        JButton jb2 = new JButton("取消");
        jp1.add(sureButton);
        jp1.add(jb2);
        jp.add(jp1, BorderLayout.SOUTH);
        jb2.addActionListener(arg0 -> card1.show(c, "0"));
        JPanel jp2 = new JPanel(new GridLayout(0, 2));
        ifChoose = new JCheckBox[10];
        JButton[] jb = new JButton[10];
        ifChoose[0] = new JCheckBox("根据日期筛选");
        ifChoose[1] = new JCheckBox("根据操作分类筛选");
        ifChoose[2] = new JCheckBox("根据图形分类筛选");
        ifChoose[3] = new JCheckBox("根据买入价格筛选");
        ifChoose[4] = new JCheckBox("根据买入数量筛选");
        ifChoose[5] = new JCheckBox("根据买入类型筛选");
        ifChoose[6] = new JCheckBox("根据卖出价格筛选");
        ifChoose[7] = new JCheckBox("根据卖出类型筛选");
        ifChoose[8] = new JCheckBox("根据盈亏额筛选");
        ifChoose[9] = new JCheckBox("根据盈亏率筛选");
        for (int i = 0; i < 10; i++) {
            jb[i] = new JButton("设置条件");
            Instrument.buttonType(jb[i]);
            jp2.add(ifChoose[i]);
            jp2.add(jb[i]);
            int showNumber = i;
            jb[i].addActionListener(arg -> {
                card1.show(c, "2");
                card2.show(function, String.valueOf(showNumber));
            });
        }
        jp.add(jp2, BorderLayout.CENTER);
    }

    private static void panel3(JPanel jp) {
        jp.setLayout(new BorderLayout());
        JPanel jp1 = new JPanel();
        JButton jb = new JButton("完成");
        jp1.add(jb);
        jp.add(jp1, BorderLayout.SOUTH);
        jb.addActionListener(arg0 -> card1.show(c, "1"));
        card2 = new CardLayout();
        function = new JPanel(card2);
        jp.add(function, BorderLayout.CENTER);
        functionPanel();
    }

    private static void functionPanel() {
        JPanel jp1 = new JPanel(null);
        JComboBox<String> year1 = new JComboBox<>(), month1 = new JComboBox<>(), day1 = new JComboBox<>();
        Instrument.addDataBox(jp1, year1, month1, day1, 120, 20);
        JLabel p1jl1 = new JLabel("设置下限：");
        p1jl1.setBounds(20, 20, 100, 30);
        jp1.add(p1jl1);
        JComboBox<String> year2 = new JComboBox<>(), month2 = new JComboBox<>(), day2 = new JComboBox<>();
        Instrument.addDataBox(jp1, year2, month2, day2, 120, 80);
        JLabel p1jl2 = new JLabel("设置上限：");
        p1jl2.setBounds(20, 80, 100, 30);
        jp1.add(p1jl2);
        function.add(jp1, "0");
        JPanel jp2 = new JPanel(null);
        JComboBox<String> p2c1 = typeText(jp2, "波段操作", "超短操作");
        function.add(jp2, "1");
        JPanel jp3 = new JPanel(null);
        String[] first = Instrument.getFirstClassify();
        JComboBox<String> p3c1 = new JComboBox<>(first);
        p3c1.setBounds(120, 20, 300, 30);
        jp3.add(p3c1);
        JComboBox<String> p3c2 = new JComboBox<>();
        p3c2.setBounds(120, 80, 300, 30);
        jp3.add(p3c2);
        JLabel p3jl1 = new JLabel("选择一级分类：");
        p3jl1.setBounds(20, 20, 100, 30);
        jp3.add(p3jl1);
        JLabel p3jl2 = new JLabel("选择二级分类：");
        p3jl2.setBounds(20, 80, 100, 30);
        p3c1.addItem("<已删除的分类>");
        p3c2.addItem("<全部>");
        for (String s : Instrument.getSecondClassify((String) p3c1.getSelectedItem())) p3c2.addItem(s);
        p3c1.addActionListener(e -> {
            p3c2.removeAllItems();
            p3c2.addItem("<全部>");
            if (!Objects.requireNonNull(p3c1.getSelectedItem()).equals("<已删除的分类>")) {
                for (String s : Instrument.getSecondClassify((String) p3c1.getSelectedItem())) {
                    p3c2.addItem(s);
                }
            }
        });
        jp3.add(p3jl2);
        function.add(jp3, "2");
        JPanel jp4 = new JPanel(null);
        JTextField[] p4jt = limitText(jp4, "元", new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT),
                new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        function.add(jp4, "3");
        JPanel jp5 = new JPanel(null);
        JTextField[] p5jt = limitText(jp5, "股", new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.INTEGER),
                new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.INTEGER));
        function.add(jp5, "4");
        JPanel jp6 = new JPanel(null);
        JComboBox<String> p6jc = typeText(jp6, "正常买入", "抢跑买入");
        function.add(jp6, "5");
        JPanel jp7 = new JPanel(null);
        JTextField[] p7jt = limitText(jp7, "元", new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT),
                new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        function.add(jp7, "6");
        JPanel jp8 = new JPanel(null);
        JComboBox<String> p8jc = typeText(jp8, "正常卖出", "延迟卖出");
        function.add(jp8, "7");
        JPanel jp9 = new JPanel(null);
        JTextField[] p9jt = limitText(jp9, "元", new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT),
                new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        function.add(jp9, "8");
        JPanel jp10 = new JPanel(null);
        JTextField[] p10jt = limitText(jp10, "%", new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT),
                new Instrument.JTextLimited(13, Instrument.JTextLimited.limitTypeEnum.FLOAT));
        function.add(jp10, "9");
        sureButton.addActionListener(arg0 -> {
            boolean allSelected = false;
            for (JCheckBox jCheckBox : ifChoose) if (jCheckBox.isSelected()) allSelected = true;
            if (!allSelected) {
                JOptionPane.showMessageDialog(null, "请选择至少一个筛选条件！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ifChoose[0].isSelected()) {
                int y1 = Integer.parseInt((String) Objects.requireNonNull(year1.getSelectedItem()));
                int m1 = Integer.parseInt((String) Objects.requireNonNull(month1.getSelectedItem()));
                int d1 = Integer.parseInt((String) Objects.requireNonNull(day1.getSelectedItem()));
                int y2 = Integer.parseInt((String) Objects.requireNonNull(year2.getSelectedItem()));
                int m2 = Integer.parseInt((String) Objects.requireNonNull(month2.getSelectedItem()));
                int d2 = Integer.parseInt((String) Objects.requireNonNull(day2.getSelectedItem()));
                if (y1 > y2 || (y1 == y2 && m1 > m2) || (y1 == y2 && m1 == m2 && d1 > d2)) {
                    JOptionPane.showMessageDialog(null, "日期错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "0");
                    return;
                }
            }
            if (ifChoose[3].isSelected()) {
                assert false;
                if (boundDetection(p4jt[0].getText(), p4jt[1].getText())) {
                    JOptionPane.showMessageDialog(null, "买入价格错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "3");
                    return;
                }
            }
            if (ifChoose[4].isSelected()) {
                assert false;
                if (boundDetection(p5jt[0].getText(), p5jt[1].getText())) {
                    JOptionPane.showMessageDialog(null, "买入数量错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "4");
                    return;
                }
            }
            if (ifChoose[6].isSelected()) {
                assert false;
                if (boundDetection(p7jt[0].getText(), p7jt[1].getText())) {
                    JOptionPane.showMessageDialog(null, "卖出价格错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "6");
                    return;
                }
            }
            if (ifChoose[8].isSelected()) {
                assert false;
                if (boundDetection(p9jt[0].getText(), p9jt[1].getText())) {
                    JOptionPane.showMessageDialog(null, "盈亏额错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "8");
                    return;
                }
            }
            if (ifChoose[9].isSelected()) {
                assert false;
                if (boundDetection(p10jt[0].getText(), p10jt[1].getText())) {
                    JOptionPane.showMessageDialog(null, "盈亏率错误！", "提示", JOptionPane.WARNING_MESSAGE);
                    card1.show(c, "2");
                    card2.show(function, "9");
                    return;
                }
            }
            if (ifChoose[0].isSelected()) {
                String y1 = (String) Objects.requireNonNull(year1.getSelectedItem());
                String m1 = (String) Objects.requireNonNull(month1.getSelectedItem());
                String d1 = (String) Objects.requireNonNull(day1.getSelectedItem());
                String y2 = (String) Objects.requireNonNull(year2.getSelectedItem());
                String m2 = (String) Objects.requireNonNull(month2.getSelectedItem());
                String d2 = (String) Objects.requireNonNull(day2.getSelectedItem());
                classifyInformation[0] = addIntoString(classifyInformation[0], y1 + "." + m1 + "." + d1 + "-" + y2 + "." + m2 + "." + d2);
            } else {
                classifyInformation[0] = addIntoString(classifyInformation[0], "f");
            }
            if (ifChoose[1].isSelected()) {
                classifyInformation[1] = addIntoString(classifyInformation[1], String.valueOf(p2c1.getSelectedIndex()));
            } else {
                classifyInformation[1] = addIntoString(classifyInformation[1], "f");
            }
            if (ifChoose[2].isSelected()) {
                classifyInformation[2] = addIntoString(classifyInformation[2], p3c1.getSelectedItem() + "." + p3c2.getSelectedItem());
            } else {
                classifyInformation[2] = addIntoString(classifyInformation[2], "f");
            }
            if (ifChoose[3].isSelected()) {
                classifyInformation[3] = addIntoString(classifyInformation[3], p4jt[0].getText() + "^" + p4jt[1].getText());
            } else {
                classifyInformation[3] = addIntoString(classifyInformation[3], "f");
            }
            if (ifChoose[4].isSelected()) {
                classifyInformation[4] = addIntoString(classifyInformation[4], p5jt[0].getText() + "^" + p5jt[1].getText());
            } else {
                classifyInformation[4] = addIntoString(classifyInformation[4], "f");
            }
            if (ifChoose[5].isSelected()) {
                classifyInformation[5] = addIntoString(classifyInformation[5], String.valueOf(p6jc.getSelectedIndex()));
            } else {
                classifyInformation[5] = addIntoString(classifyInformation[5], "f");
            }
            if (ifChoose[6].isSelected()) {
                classifyInformation[6] = addIntoString(classifyInformation[6], p7jt[0].getText() + "^" + p7jt[1].getText());
            } else {
                classifyInformation[6] = addIntoString(classifyInformation[6], "f");
            }
            if (ifChoose[7].isSelected()) {
                classifyInformation[7] = addIntoString(classifyInformation[7], String.valueOf(p8jc.getSelectedIndex()));
            } else {
                classifyInformation[7] = addIntoString(classifyInformation[7], "f");
            }
            if (ifChoose[8].isSelected()) {
                classifyInformation[8] = addIntoString(classifyInformation[8], p9jt[0].getText() + "^" + p9jt[1].getText());
            } else {
                classifyInformation[8] = addIntoString(classifyInformation[8], "f");
            }
            if (ifChoose[9].isSelected()) {
                classifyInformation[9] = addIntoString(classifyInformation[9], p10jt[0].getText() + "^" + p10jt[1].getText());
            } else {
                classifyInformation[9] = addIntoString(classifyInformation[9], "f");
            }
            BrowserData.compareNumber++;
            panel1();
            card1.show(c, "0");
        });
    }

    private static String addIntoString(String source, String word) {
        if (source == null) {
            source = word + "*";
        } else {
            source = source + word + "*";
        }
        return source;
    }

    private static boolean boundDetection(String up, String down) {
        String isInteger = "-?\\d+(\\.\\d{1,2})?";
        if (!up.matches(isInteger) || !down.matches(isInteger)) {
            return true;
        }
        return Float.parseFloat(up) > Float.parseFloat(down);
    }

    private static JTextField[] limitText(JPanel jp, String unit, Document doc1, Document doc2) {
        JTextField[] jt = new JTextField[2];
        JLabel jl1 = new JLabel("输入下限");
        JLabel jl2 = new JLabel("输入上限");
        jt[0] = new JTextField();
        jt[1] = new JTextField();
        jl1.setBounds(20, 20, 80, 30);
        jp.add(jl1);
        jt[0].setBounds(120, 20, 150, 30);
        jt[0].setDocument(doc1);
        jp.add(jt[0]);
        jl2.setBounds(20, 80, 80, 30);
        jp.add(jl2);
        jt[1].setBounds(120, 80, 150, 30);
        jt[1].setDocument(doc2);
        jp.add(jt[1]);
        JLabel jl3 = new JLabel(unit);
        jl3.setBounds(290, 20, 50, 30);
        JLabel jl4 = new JLabel(unit);
        jl4.setBounds(290, 80, 50, 30);
        jp.add(jl3);
        jp.add(jl4);
        return jt;
    }

    private static JComboBox<String> typeText(JPanel jp, String t1, String t2) {
        JComboBox<String> lim = new JComboBox<>();
        lim.setBounds(120, 20, 100, 30);
        lim.addItem(t1);
        lim.addItem(t2);
        JLabel jl = new JLabel("选择类型：");
        jl.setBounds(20, 20, 100, 20);
        jp.add(jl);
        jp.add(lim);
        return lim;
    }

    protected static boolean comparable(int line, int column) {
        if (classifyInformation == null) {
            return true;
        }
        for (String s : classifyInformation) {
            if (s == null) {
                return true;
            }
        }
        boolean judge = true;
        String[] buyDate = classifyInformation[0].split("\\*");
        if (!buyDate[column].equals("f")) {
            String[] Date = buyDate[column].split("-");
            int y1 = Integer.parseInt(Date[0].split("\\.")[0]);
            int m1 = Integer.parseInt(Date[0].split("\\.")[1]);
            int d1 = Integer.parseInt(Date[0].split("\\.")[2]);
            int y2 = Integer.parseInt(Date[1].split("\\.")[0]);
            int m2 = Integer.parseInt(Date[1].split("\\.")[1]);
            int d2 = Integer.parseInt(Date[1].split("\\.")[2]);
            int y = Integer.parseInt(dataInformation[line][4]);
            int m = Integer.parseInt(dataInformation[line][5]);
            int d = Integer.parseInt(dataInformation[line][6]);
            if ((y < y1 || (y == y1 && m < m1) || (y == y1 && m == m1 && d < d1))
                    || (y > y2 || (y == y2 && m > m2) || (y == y2 && m == m2 && d > d2))) {
                judge = false;
            }
        }
        String[] cll1 = classifyInformation[1].split("\\*");
        if (!cll1[column].equals("f")) {
            String[] cll11 = dataInformation[line][0].split("/");
            if ((cll1[column].equals("0") && (cll11[2].equals("human") || cll11[2].equals("none"))) || (cll1[column].equals("1") && cll11[2].equals("machine"))) {
                judge = false;
            }
        }
        String[] cll2 = classifyInformation[2].split("\\*");
        if (!cll2[column].equals("f")) {
            String[] cll21 = dataInformation[line][0].split("/");
            String[] cll22 = cll2[column].split("\\.");
            if (cll21[2].equals("machine")) {
                judge = false;
            } else if (cll21[2].equals("none")) {
                if (!cll22[0].equals("<已删除的分类>")) {
                    judge = false;
                }
            } else if (!cll21[3].equals(cll22[0])) {
                judge = false;
            } else if (!cll22[1].equals("<全部>") && !cll21[4].equals(cll22[1])) {
                judge = false;
            }
        }
        String[] buyPrice = classifyInformation[3].split("\\*");
        if (!buyPrice[column].equals("f")) {
            String[] buyP1 = buyPrice[column].split("\\^");
            if (Float.parseFloat(dataInformation[line][2]) < Float.parseFloat(buyP1[0])
                    || Float.parseFloat(dataInformation[line][2]) > Float.parseFloat(buyP1[1])) {
                judge = false;
            }
        }
        String[] buyNum = classifyInformation[4].split("\\*");
        if (!buyNum[column].equals("f")) {
            String[] buyN1 = buyNum[column].split("\\^");
            if (Integer.parseInt(dataInformation[line][3]) < Integer.parseInt(buyN1[0])
                    || Integer.parseInt(dataInformation[line][3]) > Integer.parseInt(buyN1[1])) {
                judge = false;
            }
        }
        String[] buyType = classifyInformation[5].split("\\*");
        if (!buyType[column].equals("f")) {
            String[] cll51 = dataInformation[line][0].split("/");
            if (cll51[2].equals("machine") || (buyType[column].equals("0") && dataInformation[line][7].equals("false")
                    || buyType[column].equals("1") && dataInformation[line][7].equals("true"))) {
                judge = false;
            }
        }
        String[] sellPrice = classifyInformation[6].split("\\*");
        if (!sellPrice[column].equals("f")) {
            String[] sellP1 = sellPrice[column].split("\\^");
            if (dataInformation[line][8].equals("-")) {
                judge = false;
            } else if (Float.parseFloat(dataInformation[line][8]) < Float.parseFloat(sellP1[0])
                    || Float.parseFloat(dataInformation[line][8]) > Float.parseFloat(sellP1[1])) {
                judge = false;
            }
        }
        String[] sellType = classifyInformation[7].split("\\*");
        if (!sellType[column].equals("f")) {
            if (dataInformation[line][13].equals("-")) {
                judge = false;
            } else if ((sellType[column].equals("0") && dataInformation[line][13].equals("false"))
                    || (sellType[column].equals("1") && dataInformation[line][13].equals("true"))) {
                judge = false;
            }
        }
        String[] amount = classifyInformation[8].split("\\*");
        if (!amount[column].equals("f")) {
            String[] amountN = amount[column].split("\\^");
            if (dataInformation[line][14].equals("-")) {
                judge = false;
            } else if (Float.parseFloat(dataInformation[line][14]) < Float.parseFloat(amountN[0])
                    || Float.parseFloat(dataInformation[line][14]) > Float.parseFloat(amountN[1])) {
                judge = false;
            }
        }
        String[] rate = classifyInformation[9].split("\\*");
        if (!rate[column].equals("f")) {
            String[] reteN = rate[column].split("\\^");
            if (dataInformation[line][15].equals("-")) {
                judge = false;
            } else if (Float.parseFloat(dataInformation[line][15]) * 100 < Float.parseFloat(reteN[0])
                    || Float.parseFloat(dataInformation[line][15]) * 100 > Float.parseFloat(reteN[1])) {
                judge = false;
            }
        }
        return judge;
    }
}