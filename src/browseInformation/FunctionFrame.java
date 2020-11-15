package browseInformation;

import instrument.Instrument;
import mainWindow.MainWindow;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;

final class FunctionFrame {
    protected static JInternalFrame setFrame(String title) {
        for (Component component : BrowserData.functionPane.getComponents()) component.setEnabled(false);
        JInternalFrame jf = new JInternalFrame(title, false, false);
        jf.setLayout(new BorderLayout());
        jf.setBounds(MainWindow.desktopPane.getSize().width / 2 - 250, MainWindow.desktopPane.getSize().height / 2 - 200, 500, 400);
        jf.setVisible(true);
        MainWindow.desktopPane.add(jf);
        jf.addInternalFrameListener(new InternalFrameListener() {
            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosing(InternalFrameEvent e) {
            }

            public void internalFrameClosed(InternalFrameEvent e) {
                for (Component component : BrowserData.functionPane.getComponents()) {
                    component.setEnabled(true);
                }
                if (BrowserData.isComparing) {
                    BrowserData.jb4.setEnabled(false);
                }
            }

            public void internalFrameIconified(InternalFrameEvent e) {
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            public void internalFrameActivated(InternalFrameEvent e) {
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
            }
        });
        return jf;
    }

    protected static void screeningWindow() {
        JInternalFrame jf = setFrame("选择需要显示的统计项目");
        JCheckBox[] jc = new JCheckBox[13 + BrowserData.noteNumber];
        Container c = jf.getContentPane();
        JPanel jp1 = new JPanel();
        JButton jb1 = new JButton("确定");
        JButton jb2 = new JButton("取消");
        JButton jb3 = new JButton("全选");
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        c.add(jp1, BorderLayout.SOUTH);
        JPanel jp2 = new JPanel(new GridLayout(0, 3));
        JScrollPane js = new JScrollPane(jp2);
        c.add(js, BorderLayout.CENTER);
        jc[0] = new JCheckBox("买入时间");
        jc[1] = new JCheckBox("操作分类");
        jc[2] = new JCheckBox("图形分类");
        jc[3] = new JCheckBox("买入价格（元）");
        jc[4] = new JCheckBox("买入数量（股）");
        jc[5] = new JCheckBox("买入类型");
        jc[6] = new JCheckBox("卖出价格（元）");
        jc[7] = new JCheckBox("交易费用（元）");
        jc[8] = new JCheckBox("卖出类型/时间");
        jc[9] = new JCheckBox("盈亏额（元）");
        jc[10] = new JCheckBox("盈亏率");
        jc[11] = new JCheckBox("图片1/注释");
        jc[12] = new JCheckBox("图片2/注释");
        for (int i = 0; i < BrowserData.noteNumber; i++) {
            jc[13 + i] = new JCheckBox(BrowserData.noteName[i]);
        }
        jb1.addActionListener(arg0 -> {
            for (int i = 0; i < 13 + BrowserData.noteNumber; i++) {
                BrowserData.ifShowing[i] = jc[i].isSelected();
            }
            jf.dispose();
            BrowserData.loadPane();
        });
        jb2.addActionListener(arg0 -> jf.dispose());
        jb3.addActionListener(arg0 -> {
            if (Instrument.isAllSelected(jc)) {
                for (JCheckBox jCheckBox : jc) {
                    jCheckBox.setSelected(false);
                }
                jb3.setText("全选");
            } else {
                for (JCheckBox jCheckBox : jc) {
                    jCheckBox.setSelected(true);
                }
                jb3.setText("全不选");
            }
        });
        for (int i = 0; i < jc.length; i++) {
            jc[i].setPreferredSize(new Dimension(150, 20));
            if (BrowserData.ifShowing[i]) {
                jc[i].setSelected(true);
            }
            jp2.add(jc[i]);
            jc[i].addActionListener(e -> {
                if (Instrument.isAllSelected(jc)) {
                    jb3.setText("全不选");
                } else {
                    jb3.setText("全选");
                }
            });
        }
        if (Instrument.isAllSelected(jc)) {
            jb3.setText("全不选");
        }
    }

    protected static void sortWindow() {
        JInternalFrame jf = setFrame("选择需要排序的统计项目");
        Container c = jf.getContentPane();
        JPanel jp1 = new JPanel();
        JButton jb1 = new JButton("升序排序");
        JButton jb2 = new JButton("降序排序");
        JButton jb3 = new JButton("取消");
        jp1.add(jb1);
        jp1.add(jb2);
        jp1.add(jb3);
        c.add(jp1, BorderLayout.SOUTH);
        JPanel jp2 = new JPanel(new FlowLayout());
        JRadioButton[] jr = new JRadioButton[7];
        jr[0] = new JRadioButton("买入时间");
        jr[1] = new JRadioButton("买入价格");
        jr[2] = new JRadioButton("买入数量");
        jr[3] = new JRadioButton("卖出价格");
        jr[4] = new JRadioButton("交易费用");
        jr[5] = new JRadioButton("盈亏额");
        jr[6] = new JRadioButton("盈亏率");
        ButtonGroup group1 = new ButtonGroup();
        for (int i = 0; i < 7; i++) {
            jr[i].setPreferredSize(new Dimension(150, 80));
            group1.add(jr[i]);
            jp2.add(jr[i]);
        }
        c.add(jp2, BorderLayout.CENTER);
        jb1.addActionListener(arg0 -> {
            int Sort = getChoose(jr);
            if (Sort == -1) {
                return;
            } else {
                BrowserData.sort = Sort;
            }
            BrowserData.ascend = true;
            jf.dispose();
            BrowserData.loadPane();
        });
        jb2.addActionListener(arg0 -> {
            int Sort = getChoose(jr);
            if (Sort == -1) {
                return;
            } else {
                BrowserData.sort = Sort;
            }
            BrowserData.ascend = false;
            jf.dispose();
            BrowserData.loadPane();
        });
        jb3.addActionListener(arg0 -> jf.dispose());
    }

    private static int getChoose(JRadioButton[] jr) {
        for (int i = 0; i < 7; i++) {
            if (jr[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }
}