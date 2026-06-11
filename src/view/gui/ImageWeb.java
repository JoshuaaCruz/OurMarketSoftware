package view.gui;

import javax.swing.*;
import java.awt.*;

public class ImageWeb {

    public static void generateImageWeb(String path, String name, double screenPercentage) {
        if (screenPercentage > 1)
            throw new IllegalArgumentException("Screen percentage cannot be greater than 1");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) Math.floor(screenSize.getWidth() * screenPercentage);
        int height = (int) Math.floor(screenSize.getHeight()* screenPercentage);

        JFrame web = new JFrame();
        web.setTitle(name);

        ImageIcon image = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(image);
        web.add(imageLabel);
        web.pack();
        web.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        web.setVisible(true);
    }

    public static void generateImageWeb(String path, String name) {
        generateImageWeb(path, name, 0.5);
    }
    public static void generateImageWeb(String path) {
        generateImageWeb(path, path, 0.5);
    }
}
