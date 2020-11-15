package mainWindow;

import addInformation.ChooseAddDeal;
import browseInformation.BrowserData;
import editInformation.ChooseEditItem;
import instrument.Instrument;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

public final class MainWindow {
    public static JFrame mainFrame;
    protected static JLabel background;
    public static final JPanel mainPanel = new JPanel();
    public static final JDesktopPane desktopPane = new JDesktopPane();

    public static void windowFrame() {
        mainFrame = new JFrame("交易统计工具");
        mainFrame.setSize(1300, 700);
        mainFrame.setMinimumSize(new Dimension(1300, 700));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout());
        JLayeredPane lp = new JLayeredPane();
        mainFrame.add(lp);
        background = new JLabel();
        if (new File("./setting/bg.jpgx").exists()) {
            changeBackground();
        }
        mainPanel.setOpaque(false);
        desktopPane.setOpaque(false);
        lp.add(mainPanel, JLayeredPane.PALETTE_LAYER);
        lp.add(desktopPane, JLayeredPane.MODAL_LAYER);
        lp.add(background, JLayeredPane.DEFAULT_LAYER);
        mainButton();
        mainFrame.setVisible(true);
        Insets ins = mainFrame.getInsets();
        int vertical = ins.top + ins.bottom;
        int horizontal = ins.left + ins.right;
        mainPanel.setSize(mainFrame.getWidth() - horizontal, mainFrame.getHeight() - vertical);
        desktopPane.setSize(mainFrame.getWidth() - horizontal, mainFrame.getHeight() - vertical);
        Thread a = new Thread(() -> {
            while (true) {
                mainPanel.setSize(mainFrame.getWidth() - horizontal, mainFrame.getHeight() - vertical);
                desktopPane.setSize(mainFrame.getWidth() - horizontal, mainFrame.getHeight() - vertical);
            }
        });
        a.start();
    }

    public static void mainButton() {
        Instrument.newConfigurationFile();
        Calendar hour = Calendar.getInstance();
        int currentHour = hour.get(Calendar.HOUR_OF_DAY);
        String note;
        if (currentHour < 5) {
            note = "<html><body>夜深了，注意休息！<br>";
        } else if (currentHour < 11) {
            note = "<html><body>早上好!<br>";
        } else if (currentHour < 13) {
            note = "<html><body>中午好!<br>";
        } else if (currentHour < 19) {
            note = "<html><body>下午好!<br>";
        } else {
            note = "<html><body>晚上好!<br>";
        }
        File file = new File("./setting/user");
        String s1 = null;
        try {
            FileReader in = new FileReader(file);
            char[] byt = new char[1024];
            int len = in.read(byt);
            s1 = new String(byt, 0, len);
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        assert s1 != null;
        String[] s2 = s1.split("\n");
        String username = note + s2[0];
        Instrument.resetJPanel(mainPanel);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gb1 = new GridBagConstraints();
        gb1.gridx = 1;
        gb1.gridy = 1;
        gb1.insets = new Insets(15, 15, 15, 15);
        gb1.fill = GridBagConstraints.BOTH;
        gb1.weightx = 4;
        gb1.weighty = 1;
        JButton add = new JButton("添加新交易数据");
        add.setFont(new Font("黑体", Font.BOLD, 15));
        mainPanel.add(add, gb1);
        GridBagConstraints gb2 = new GridBagConstraints();
        gb2.gridx = 1;
        gb2.gridy = 2;
        gb2.weighty = 1;
        gb2.insets = new Insets(15, 15, 15, 15);
        gb2.fill = GridBagConstraints.BOTH;
        JButton browser = new JButton("删改/查看统计数据");
        browser.setFont(new Font("黑体", Font.BOLD, 15));
        mainPanel.add(browser, gb2);
        GridBagConstraints gb3 = new GridBagConstraints();
        gb3.gridx = 1;
        gb3.gridy = 3;
        gb3.weighty = 1;
        gb3.insets = new Insets(15, 15, 15, 15);
        gb3.fill = GridBagConstraints.BOTH;
        JButton edit = new JButton("删改/增加统计项目或股票信息");
        edit.setFont(new Font("黑体", Font.BOLD, 15));
        mainPanel.add(edit, gb3);
        GridBagConstraints gb4 = new GridBagConstraints();
        gb4.gridx = 2;
        gb4.gridy = 1;
        gb4.weightx = 6;
        JLabel jl1 = new JLabel(username, SwingConstants.LEFT);
        jl1.setFont(new Font("黑体", Font.BOLD, 20));
        jl1.setForeground(Color.DARK_GRAY);
        mainPanel.add(jl1, gb4);
        GridBagConstraints gb5 = new GridBagConstraints();
        gb5.gridx = 2;
        gb5.gridy = 4;
        gb5.anchor = GridBagConstraints.SOUTHEAST;
        gb5.insets = new Insets(5, 30, 5, 30);
        JButton changeBackground = new JButton("更换背景");
        changeBackground.setPreferredSize(new Dimension(150, 50));
        mainPanel.add(changeBackground, gb5);
        GridBagConstraints gb6 = new GridBagConstraints();
        gb6.gridx = 2;
        gb6.gridy = 5;
        gb6.anchor = GridBagConstraints.SOUTHEAST;
        gb6.insets = new Insets(5, 30, 5, 30);
        JButton editUser = new JButton("编辑账号信息");
        editUser.setPreferredSize(new Dimension(150, 50));
        mainPanel.add(editUser, gb6);
        GridBagConstraints gb7 = new GridBagConstraints();
        gb7.gridx = 2;
        gb7.gridy = 6;
        gb7.anchor = GridBagConstraints.SOUTHEAST;
        gb7.insets = new Insets(5, 30, 10, 30);
        JButton exitProgram = new JButton("退出程序");
        exitProgram.setPreferredSize(new Dimension(150, 50));
        mainPanel.add(exitProgram, gb7);
        Instrument.placeholderComponents(0, 0, 1, 1, mainPanel);
        Instrument.placeholderComponents(0, 4, 1, 1, mainPanel);
        Instrument.buttonType(add);
        Instrument.buttonType(browser);
        Instrument.buttonType(edit);
        Instrument.buttonType(changeBackground);
        Instrument.buttonType(editUser);
        Instrument.buttonType(exitProgram);
        add.addActionListener(arg0 -> ChooseAddDeal.setAddDealPane());
        browser.addActionListener(arg0 -> BrowserData.setDataPane());
        edit.addActionListener(arg0 -> ChooseEditItem.setEditItemPane());
        changeBackground.addActionListener(arg0 -> {
            if (Instrument.importImage("./setting/bg.jpgx")) {
                changeBackground();
            }
        });
        editUser.addActionListener(arg0 -> ChangeUserInformation.setChangeNamePane());
        exitProgram.addActionListener(arg0 -> System.exit(0));
    }

    private static void changeBackground() {
        ImageIcon bg = new ImageIcon("./setting/bg.jpgx");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = screenSize.width;
        int h = screenSize.height;
        Instrument.resetSize(w, h, bg);
        background.setIcon(bg);
        background.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
    }
}