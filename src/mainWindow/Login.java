package mainWindow;

import instrument.Instrument;

import javax.swing.*;
import java.awt.*;
import java.io.File;

final class Login {
    public static void main(String[] args) {
        // 检查关键配置文件是否存在，没有就创建
        Instrument.newConfigurationFile();
        File file1 = new File("./setting/remember");
        // 判断用户上次登录时是否选择了自动登录
        if (file1.exists()) {
            // 进入主窗口
            MainWindow.windowFrame();
        } else {
            // 进入登录界面
            loginFrame();
        }
    }

    private static void loginFrame() {
        JFrame jf = new JFrame("用户登录");
        jf.setLayout(null);
        jf.setSize(500, 230);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        Container container = jf.getContentPane();
        JTextField jt1 = new JTextField();
        jt1.setBounds(70, 50, 300, 30);
        container.add(jt1);
        JPasswordField jt2 = new JPasswordField();
        jt2.setBounds(70, 100, 300, 30);
        container.add(jt2);
        JTextField jt3 = new JTextField();
        jt3.setBounds(70, 100, 300, 30);
        container.add(jt3);
        JButton jb = new JButton("确认");
        jb.setBounds(200, 150, 100, 30);
        container.add(jb);
        JLabel jl1 = new JLabel("用户名", SwingConstants.LEFT);
        jl1.setBounds(20, 50, 300, 30);
        container.add(jl1);
        JLabel jl2 = new JLabel("密码", SwingConstants.LEFT);
        jl2.setBounds(20, 100, 300, 30);
        container.add(jl2);
        JLabel jl3 = new JLabel("请输入您的用户名和密码：", SwingConstants.LEFT);
        jl3.setBounds(20, 5, 300, 30);
        container.add(jl3);
        JLabel jl4 = new JLabel("用户名或密码错误！请重新输入：", SwingConstants.LEFT);
        jl4.setBounds(20, 5, 300, 30);
        jl4.setForeground(Color.red);
        container.add(jl4);
        JCheckBox jc1 = new JCheckBox("自动登录");
        jc1.setBounds(380, 55, 80, 20);
        container.add(jc1);
        JCheckBox jc2 = new JCheckBox("显示密码");
        jc2.setBounds(380, 105, 80, 20);
        container.add(jc2);
        jf.setVisible(true);
        jt3.setVisible(false);
        jl4.setVisible(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jt3.setVisible(false);
        jb.addActionListener(arg0 -> {
            String username = jt1.getText();
            String password;
            if (jc2.isSelected()) {
                password = jt3.getText();
            } else {
                password = String.valueOf(jt2.getPassword());
            }
            if (detection(username, password)) {
                if (jc1.isSelected()) {
                    Instrument.newConfigurationFile();
                    File file = new File("./setting/remember");
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                jf.setVisible(false);
                MainWindow.windowFrame();
            } else {
                jt1.setText("");
                jt2.setText("");
                jt3.setText("");
                jl3.setVisible(false);
                jl4.setVisible(true);
                jt1.requestFocus();
            }
        });
        jc2.addActionListener(arg0 -> {
            if (jc2.isSelected()) {
                jt3.setText(jt2.getText());
                jt2.setVisible(false);
                jt3.setVisible(true);
            } else {
                jt2.setText(jt3.getText());
                jt2.setVisible(true);
                jt3.setVisible(false);
            }
        });
    }

    private static boolean detection(String username, String password) {
        File file = new File("./setting/user");
        String[] userInformation = Instrument.getFileData(file).split("\n");
        return userInformation[0].equals(username) & userInformation[1].equals(password);
    }
}