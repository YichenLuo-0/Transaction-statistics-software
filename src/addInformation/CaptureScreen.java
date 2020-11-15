package addInformation;

import mainWindow.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

final class CaptureScreen extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static JPanel c;
    private BufferedImage get;
    private JLabel jl;
    private final int which;
    static JButton start, cancel;


    CaptureScreen(int which) {
        super("屏幕截取");
        MainWindow.mainFrame.setEnabled(false);
        MainWindow.mainFrame.setVisible(false);
        initWindow();
        this.which = which;
    }

    private void initWindow() {
        start = new JButton("开始截取");
        start.addActionListener(this);
        cancel = new JButton("取消截取");
        cancel.addActionListener(this);
        JPanel buttonJP = new JPanel();
        c = new JPanel(new BorderLayout());
        jl = new JLabel("选择截取区域后双击确认", JLabel.CENTER);
        jl.setFont(new Font("黑体", Font.BOLD, 20));
        jl.setForeground(Color.GRAY);
        c.add(jl, BorderLayout.CENTER);
        buttonJP.add(start);
        buttonJP.add(cancel);
        JPanel jp = new JPanel();
        JPanel all = new JPanel();
        all.add(jp);
        all.add(buttonJP);
        this.getContentPane().add(c, BorderLayout.CENTER);
        this.getContentPane().add(all, BorderLayout.SOUTH);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new WindowListener() {
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

    private void updates() {
        this.setVisible(true);
        jl.setVisible(false);
        if (get != null) {
            PicPanel pic = new PicPanel(get);
            c.add(pic);
            SwingUtilities.updateComponentTreeUI(c);
        }
    }

    private void doStart() {
        try {
            this.setVisible(false);
            Thread.sleep(500);
            Robot ro = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension di = tk.getScreenSize();
            Rectangle rec = new Rectangle(0, 0, di.width, di.height);
            BufferedImage bi = ro.createScreenCapture(rec);
            JFrame jf = new JFrame();
            Temp temp = new Temp(jf, bi, di.width, di.height);
            jf.getContentPane().add(temp, BorderLayout.CENTER);
            jf.setUndecorated(true);
            jf.setSize(di);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    void doSave(BufferedImage get) {
        try {
            if (get == null) {
                JOptionPane.showMessageDialog(this, "图片读取失败!", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File file;
            if (which == 1) {
                file = new File("temporary1.jpg");
            } else {
                file = new File("temporary2.jpg");
            }
            if (ImageIO.write(get, "JPG", file)) {
                this.dispose();
                MainWindow.mainFrame.setEnabled(true);
                MainWindow.mainFrame.setVisible(true);
                if (which == 1) {
                    AddNewDeal.picPanel1.load();
                } else {
                    AddNewDeal.picPanel2.load();
                }
            } else {
                JOptionPane.showMessageDialog(this, "保存失败！请重试。");
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    private void doClose() {
        c.removeAll();
        c.repaint();
        c.revalidate();
        System.gc();
        jl.setVisible(true);
        this.setVisible(true);
        c.add(jl, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == start) {
            doStart();
        } else if (source == cancel) {
            this.dispose();
            MainWindow.mainFrame.setEnabled(true);
            MainWindow.mainFrame.setVisible(true);
        }
    }

    private class PicPanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 1L;
        JButton save, close;
        BufferedImage get;

        public PicPanel(BufferedImage get) {
            super(new BorderLayout());
            this.get = get;
            initPanel();
        }

        private void initPanel() {
            CaptureScreen.start.setVisible(false);
            save = new JButton("确认");
            close = new JButton("取消");
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(save);
            buttonPanel.add(close);
            JLabel icon = new JLabel(new ImageIcon(get));
            this.add(new JScrollPane(icon), BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.SOUTH);
            save.addActionListener(this);
            close.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == save) {
                doSave(get);
            } else if (source == close) {
                CaptureScreen.start.setVisible(true);
                get = null;
                doClose();
            }
        }
    }

    private class Temp extends JPanel implements MouseListener, MouseMotionListener {
        private static final long serialVersionUID = 1L;
        private final BufferedImage bi;
        private final int width, height;
        private int startX, startY, endX, endY, tempX, tempY;
        private final JFrame jf;
        private Rectangle select = new Rectangle(0, 0, 0, 0);
        private final Cursor cs = new Cursor(Cursor.CROSSHAIR_CURSOR);
        private StatesEnum current = StatesEnum.DEFAULT;
        private Rectangle[] rec;
        public static final int START_X = 1;
        public static final int START_Y = 2;
        public static final int END_X = 3;
        public static final int END_Y = 4;
        private int currentX, currentY;
        private Point p = new Point();
        private boolean showTip = true;

        public Temp(JFrame jf, BufferedImage bi, int width, int height) {
            this.jf = jf;
            this.bi = bi;
            this.width = width;
            this.height = height;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            initRecs();
        }

        private void initRecs() {
            rec = new Rectangle[8];
            for (int i = 0; i < rec.length; i++) {
                rec[i] = new Rectangle();
            }
        }

        public void paintComponent(Graphics g) {
            g.drawImage(bi, 0, 0, width, height, this);
            g.setColor(Color.RED);
            g.drawLine(startX, startY, endX, startY);
            g.drawLine(startX, endY, endX, endY);
            g.drawLine(startX, startY, startX, endY);
            g.drawLine(endX, startY, endX, endY);
            int x = Math.min(startX, endX);
            int y = Math.min(startY, endY);
            select = new Rectangle(x, y, Math.abs(endX - startX), Math.abs(endY - startY));
            int x1 = (startX + endX) / 2;
            int y1 = (startY + endY) / 2;
            g.fillRect(x1 - 2, startY - 2, 5, 5);
            g.fillRect(x1 - 2, endY - 2, 5, 5);
            g.fillRect(startX - 2, y1 - 2, 5, 5);
            g.fillRect(endX - 2, y1 - 2, 5, 5);
            g.fillRect(startX - 2, startY - 2, 5, 5);
            g.fillRect(startX - 2, endY - 2, 5, 5);
            g.fillRect(endX - 2, startY - 2, 5, 5);
            g.fillRect(endX - 2, endY - 2, 5, 5);
            rec[0] = new Rectangle(x - 5, y - 5, 10, 10);
            rec[1] = new Rectangle(x1 - 5, y - 5, 10, 10);
            rec[2] = new Rectangle((Math.max(startX, endX)) - 5, y - 5, 10, 10);
            rec[3] = new Rectangle((Math.max(startX, endX)) - 5, y1 - 5, 10, 10);
            rec[4] = new Rectangle((Math.max(startX, endX)) - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[5] = new Rectangle(x1 - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[6] = new Rectangle(x - 5, (Math.max(startY, endY)) - 5, 10, 10);
            rec[7] = new Rectangle(x - 5, y1 - 5, 10, 10);
            if (showTip) {
                g.setColor(Color.CYAN);
                g.fillRect(p.x, p.y, 170, 20);
                g.setColor(Color.RED);
                g.drawRect(p.x, p.y, 170, 20);
                g.setColor(Color.BLACK);
                g.drawString("请按住鼠标左键不放选择截图区", p.x, p.y + 15);
            }
        }

        private void initSelect(StatesEnum state) {
            switch (state) {
                case EAST -> {
                    currentX = (endX > startX ? END_X : START_X);
                    currentY = 0;
                }
                case WEST -> {
                    currentX = (endX > startX ? START_X : END_X);
                    currentY = 0;
                }
                case NORTH -> {
                    currentX = 0;
                    currentY = (startY > endY ? END_Y : START_Y);
                }
                case SOUTH -> {
                    currentX = 0;
                    currentY = (startY > endY ? START_Y : END_Y);
                }
                case NORTH_EAST -> {
                    currentY = (startY > endY ? END_Y : START_Y);
                    currentX = (endX > startX ? END_X : START_X);
                }
                case NORTH_WEST -> {
                    currentY = (startY > endY ? END_Y : START_Y);
                    currentX = (endX > startX ? START_X : END_X);
                }
                case SOUTH_EAST -> {
                    currentY = (startY > endY ? START_Y : END_Y);
                    currentX = (endX > startX ? END_X : START_X);
                }
                case SOUTH_WEST -> {
                    currentY = (startY > endY ? START_Y : END_Y);
                    currentX = (endX > startX ? START_X : END_X);
                }
                default -> {
                    currentX = 0;
                    currentY = 0;
                }
            }
        }

        public void mouseMoved(MouseEvent me) {
            doMouseMoved(me);
            initSelect(current);
            if (showTip) {
                p = me.getPoint();
                repaint();
            }
        }

        private void doMouseMoved(MouseEvent me) {
            if (select.contains(me.getPoint())) {
                this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                current = StatesEnum.MOVE;
            } else {
                StatesEnum[] st = StatesEnum.values();
                for (int i = 0; i < rec.length; i++) {
                    if (rec[i].contains(me.getPoint())) {
                        current = st[i];
                        this.setCursor(st[i].getCursor());
                        return;
                    }
                }
                this.setCursor(cs);
                current = StatesEnum.DEFAULT;
            }
        }

        public void mouseExited(MouseEvent me) {
        }

        public void mouseEntered(MouseEvent me) {
        }

        public void mouseDragged(MouseEvent me) {
            int x = me.getX();
            int y = me.getY();
            if (current == StatesEnum.MOVE) {
                startX += (x - tempX);
                startY += (y - tempY);
                endX += (x - tempX);
                endY += (y - tempY);
                tempX = x;
                tempY = y;
            } else if (current == StatesEnum.EAST || current == StatesEnum.WEST) {
                if (currentX == START_X) {
                    startX += (x - tempX);
                } else {
                    endX += (x - tempX);
                }
                tempX = x;
            } else if (current == StatesEnum.NORTH || current == StatesEnum.SOUTH) {
                if (currentY == START_Y) {
                    startY += (y - tempY);
                    tempY = y;
                } else {
                    endY += (y - tempY);
                    tempY = y;
                }
            } else if (current == StatesEnum.NORTH_EAST || current == StatesEnum.SOUTH_EAST || current == StatesEnum.SOUTH_WEST) {
                if (currentY == START_Y) {
                    startY += (y - tempY);
                    tempY = y;
                } else {
                    endY += (y - tempY);
                    tempY = y;
                }
                if (currentX == START_X) {
                    startX += (x - tempX);
                    tempX = x;
                } else {
                    endX += (x - tempX);
                    tempX = x;
                }
            } else {
                startX = tempX;
                startY = tempY;
                endX = me.getX();
                endY = me.getY();
            }
            this.repaint();
        }

        public void mousePressed(MouseEvent me) {
            showTip = false;
            tempX = me.getX();
            tempY = me.getY();
        }

        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                if (current == StatesEnum.MOVE) {
                    showTip = true;
                    p = me.getPoint();
                    startX = 0;
                    startY = 0;
                    endX = 0;
                    endY = 0;
                    repaint();
                } else {
                    jf.dispose();
                    doClose();
                }
            }
        }

        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
                Point p = me.getPoint();
                if (select.contains(p)) {
                    if (select.x + select.width < this.getWidth() && select.y + select.height < this.getHeight()) {
                        get = bi.getSubimage(select.x, select.y, select.width, select.height);
                        jf.dispose();
                        updates();
                    } else {
                        int wid = select.width, het = select.height;
                        if (select.x + select.width >= this.getWidth()) {
                            wid = this.getWidth() - select.x;
                        }
                        if (select.y + select.height >= this.getHeight()) {
                            het = this.getHeight() - select.y;
                        }
                        get = bi.getSubimage(select.x, select.y, wid, het);
                        jf.dispose();
                        updates();
                    }
                }
            }
        }
    }
}

enum StatesEnum {
    NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)), NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
    NORTH_EAST(new Cursor(Cursor.NE_RESIZE_CURSOR)), EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
    SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)), SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
    SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)), WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
    MOVE(new Cursor(Cursor.MOVE_CURSOR)), DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR));

    private final Cursor cs;

    StatesEnum(Cursor cs) {
        this.cs = cs;
    }

    public Cursor getCursor() {
        return cs;
    }
}