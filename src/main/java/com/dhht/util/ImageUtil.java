package com.dhht.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class ImageUtil {

    public static void convert(String path) {
        // TODO Auto-generated constructor stub
        try {
//            File path1 = new File(path);
            BufferedImage image = ImageIO.read(new File(path));
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(
                    imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0,
                    imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage
                    .getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage
                        .getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (colorInRange(rgb))
                        alpha = 0;
                    else
                        alpha = 255;
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            // 生成图片为PNG
            String outFile = path.substring(0, path.lastIndexOf("."));
            ImageIO.write(bufferedImage, "png", new File(outFile + ".png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        if (red >= color_range && green >= color_range && blue >= color_range)
            return true;
        return false;
    }

    public static int color_range = 210;
    public static Pattern pattern = Pattern.compile("[0-9]*");

    public static boolean isNo(String str) {
        return pattern.matcher(str).matches();
    }

}
