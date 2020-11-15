package editInformation;

import instrument.Instrument;
import mainWindow.MainWindow;

import javax.swing.*;
import java.awt.*;

public final class ChooseEditItem {
    public static void setEditItemPane() {
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new GridBagLayout());
        JButton jb1 = addButton("删改/增加备注名称", 1);
        JButton jb2 = addButton("删改/增加超短图形分类", 2);
        JButton jb3 = addButton("删改/增加股票信息", 3);
        JButton jb4 = new JButton("返回主菜单");
        Instrument.buttonType(jb4);
        jb4.setPreferredSize(new Dimension(150, 60));
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = 4;
        gb.gridy = 2;
        gb.weightx = 1.0D;
        gb.weighty = 1.0D;
        MainWindow.mainPanel.add(jb4, gb);
        Instrument.placeholderComponents(0, 0, 1, 1, MainWindow.mainPanel);
        Instrument.placeholderComponents(4, 2, 1, 1, MainWindow.mainPanel);
        jb1.addActionListener((arg0) -> EditNotes.editNotePanel());
        jb2.addActionListener((arg0) -> EditClassification.editClassificationPanel());
        jb3.addActionListener((arg0) -> EditStock.editStockPanel());
        jb4.addActionListener((arg0) -> MainWindow.mainButton());
    }

    private static JButton addButton(String text, int gridx) {
        JButton jb = new JButton(text);
        Instrument.buttonType(jb);
        jb.setFont(new Font("黑体", Font.BOLD, 15));
        jb.setPreferredSize(new Dimension(250, 80));
        GridBagConstraints gb = new GridBagConstraints();
        gb.gridx = gridx;
        gb.gridy = 1;
        gb.weightx = 1.0D;
        gb.weighty = 1.0D;
        MainWindow.mainPanel.add(jb, gb);
        return jb;
    }
}