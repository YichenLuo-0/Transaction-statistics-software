package mainWindow;

import instrument.Instrument;

import javax.swing.*;
import java.awt.*;

public final class ChangeUserInformation {
    protected static void setChangeNamePane() {
        Instrument.resetJPanel(MainWindow.mainPanel);
        MainWindow.mainPanel.setLayout(new GridBagLayout());
        MainWindow.mainPanel.add(new JLabel("这里还没有写完"));
    }
}
