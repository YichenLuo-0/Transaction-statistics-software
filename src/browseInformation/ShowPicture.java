package browseInformation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

final class ShowPicture {
    protected ShowPicture(String path) {
        JFrame jf = new JFrame("查看图片");
        jf.setSize(1200, 675);
        jf.setMinimumSize(new Dimension(1200, 675));
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jf.setLayout(new BorderLayout());
        Container c = jf.getContentPane();
        File Fig = new File(path);
        Figure fig = null;
        try {
            fig = new Figure(ImageIO.read(Fig));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fig != null;
        AtomicInteger width = new AtomicInteger(fig.getIconWidth());
        AtomicInteger height = new AtomicInteger(fig.getIconHeight());
        JLabel jl = new JLabel(fig);
        JScrollPane js = new JScrollPane(jl);
        c.add(js, BorderLayout.CENTER);
        JPanel button = new JPanel();
        JButton jb1 = new JButton("放大");
        JButton jb2 = new JButton("缩小");
        button.add(jb1);
        button.add(jb2);
        c.add(button, BorderLayout.SOUTH);
        Figure finalFig = fig;
        jb1.addActionListener(e -> {
            Figure fig1 = null;
            try {
                fig1 = finalFig.clone();
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();
            }
            assert fig1 != null;
            width.set((int) (width.get() * 1.2));
            height.set((int) (height.get() * 1.2));
            fig1.setImage(fig1.getImage().getScaledInstance(width.get(), height.get(), Image.SCALE_DEFAULT));
            jl.setIcon(fig1);
            js.setViewportView(jl);
        });
        jb2.addActionListener(e -> {
            Figure fig1 = null;
            try {
                fig1 = finalFig.clone();
            } catch (CloneNotSupportedException e1) {
                e1.printStackTrace();
            }
            assert fig1 != null;
            if (width.get() > 50 && height.get() > 50) {
                width.set((int) (width.get() / 1.2));
                height.set((int) (height.get() / 1.2));
            }
            fig1.setImage(fig1.getImage().getScaledInstance(width.get(), height.get(), Image.SCALE_DEFAULT));
            jl.setIcon(fig1);
            js.setViewportView(jl);
        });
        jf.setVisible(true);
    }
}

final class Figure extends ImageIcon implements Cloneable {
    protected Figure(Image image) {
        super(image);
    }

    protected Figure clone() throws CloneNotSupportedException {
        return (Figure) super.clone();
    }
}