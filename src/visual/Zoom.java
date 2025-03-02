package visual;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Zoom extends JDialog { // Herda de JDialog

    Robot robot;
    int zoomFactor = 2;
    PointerInfo pi;
    JPanel gui;
    Timer timer;
    JLabel zoomLabel;

    public Zoom(Frame frame) throws AWTException {
        super(frame, "Zoom");
        robot = new Robot();
        gui = new JPanel(new BorderLayout(2, 2));
        final int size = 256;
        final BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        zoomLabel = new JLabel(new ImageIcon(bi));
        
        gui.add(zoomLabel, BorderLayout.CENTER); 
        setSize(size, size);
        setContentPane(gui);
        setResizable(false);
        
        MouseListener factorListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (zoomFactor == 2) zoomFactor = 4;
                else if (zoomFactor == 4) zoomFactor = 8;
                else if (zoomFactor == 8) zoomFactor = 2;
            }
        };
        zoomLabel.addMouseListener(factorListener);

        ActionListener zoomListener = (ActionEvent e) -> {
            pi = MouseInfo.getPointerInfo();
            Point p = pi.getLocation();
            Rectangle r = new Rectangle(
                    p.x - (size / (2 * zoomFactor)),
                    p.y - (size / (2 * zoomFactor)),
                    (size / zoomFactor),
                    (size / zoomFactor));
            BufferedImage temp = robot.createScreenCapture(r);

            Graphics g = bi.getGraphics();
            g.drawImage(temp, 0, 0, size, size, null);

            g.setColor(new Color(255, 0, 0, 128));
            int x = (size / 2) - 1;
            int y = (size / 2) - 1;
            g.drawLine(0, y, size, y);
            g.drawLine(x, 0, x, size);
            g.dispose();
            zoomLabel.repaint(); // Redesenha o JLabel com a imagem atualizada
        };

        timer = new Timer(40, zoomListener);

    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
