package com.dhht.common;

import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class ImageGenerate {

    private  final int WIDTH = 400;//图片宽度
    private  final int HEIGHT = 400;//图片高度
    private final String image= "★";
    @Value("${seal.template.filepath}")
    private String filePath;


    public  String seal(Map map) {
        String message = (String)map.get("userDepartment");
        String centerName = (String)map.get("sealType");
        String code = (String)map.get("sealCode");
        String centerImage = (String)map.get("centerImage");
        try {
//           String sealPath = "C:\\"+"aa\\"+message+".png";
            String sealPath = filePath+message+".png";
            BufferedImage image =  startGraphics2D(message,centerName,code,centerImage);
            File dest = new File(filePath);
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            ImageIO.write(image, "png", new File(filePath));
            return  sealPath;
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    public  BufferedImage startGraphics2D(String message,String centerName,String code,String centerImage){
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 增加下面代码使得背景透明
//        buffImg = g.getDeviceConfiguration().createCompatibleImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
//        g.dispose();
//        g = buffImg.createGraphics();
        // 背景透明代码结束

        //使得背景为白色
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.RED);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制圆
        int radius = HEIGHT/3;//周半径
        int CENTERX = WIDTH/2;//画图所出位置
        int CENTERY = HEIGHT/2;//画图所处位置

        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
        g.setStroke(new BasicStroke(4));//设置圆的宽度
        g.draw(circle);


        //绘制中间的五角星
        if (centerImage == null){
            g.setFont(new Font("宋体", Font.BOLD, 120));
            g.drawString(image, CENTERX-(120/2), CENTERY+(120/3));
        }else {
            g.setFont(new Font("宋体", Font.BOLD, 120));
            g.drawString(centerImage, CENTERX-(120/2), CENTERY+(120/3));
        }


        //添加姓名
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 25));// 写入签名
        g.drawString(centerName, CENTERX -(40), CENTERY +(30+50));

        nameDraw(g,radius,CENTERX,CENTERY,message);

        codeDraw(g,radius,CENTERX,CENTERY,code,message);

        return buffImg;
    }

    /**
     * 印章名称生成
     * @param g
     * @param radius 半径
     * @param CENTERX 起始X轴位置
     * @param CENTERY  起始Y轴位置
     * @param message  用章公司名称
     */
    public  void nameDraw(Graphics2D g,int radius,int CENTERX,int CENTERY,String message){
        //根据输入字符串得到字符数组
        String[] messages2 = message.split("",0);
        String[] messages = messages2;
        // System.arraycopy(messages2,1,messages,0,messages2.length-1);

        //输入的字数
        int ilength = messages.length;

        //设置字体属性
        int fontsize = 30;
        Font f = new Font("Serif", Font.BOLD, fontsize);

        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(message, context);

        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / ilength);
        //上坡度
        double ascent = -bounds.getY();

        int first = 0,second = 0;
        boolean odd = false;
        if (ilength%2 == 1)
        {
            first = (ilength-1)/2;
            odd = true;
        }
        else
        {
            first = (ilength)/2-1;
            second = (ilength)/2;
            odd = false;
        }


        //名称
        double radius2 = radius - ascent;
        double x0 = CENTERX;
        double y0 = CENTERY - radius + ascent;


        //旋转角度
        double a = 2*Math.asin(char_interval/(2*radius2));

        if (odd)
        {
            g.setFont(f);
            g.drawString(messages[first], (float)(x0 - char_interval/2), (float)y0);

            //中心点的右边
            for (int i=first+1;i<ilength;i++)
            {
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            //中心点的左边
            for (int i=first-1;i>-1;i--)
            {
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }

        }
        else
        {
            //中心点的右边
            for (int i=second;i<ilength;i++)
            {
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                float x1 =(float)(x0 + ax - char_interval/2* Math.cos(aa));
                float y1 = (float)(y0 + ay - char_interval/2* Math.sin(aa));
                g.drawString(messages[i],x1 ,y1 );
            }

            //中心点的左边
            for (int i=first;i>-1;i--)
            {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        }
    }
    /**
     * 印章编码生成
     * @param g
     * @param radius 半径
     * @param CENTERX 起始X轴位置
     * @param CENTERY  起始Y轴位置
     * @param message  用章公司名称
     * @param code  编码
     */
    public  void codeDraw(Graphics2D g,int radius,int CENTERX,int CENTERY,String code,String message){
        //根据输入字符串得到字符数组
        String[] messages2 = code.split("",0);
        String[] messages = messages2;
        // System.arraycopy(messages2,1,messages,0,messages2.length-1);

        //输入的字数
        int ilength = messages.length;

        //设置字体属性
        int fontsize = 18;
        Font f = new Font("Times New Roman", Font.BOLD, fontsize);

        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(message, context);

        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / ilength);
        //上坡度
        double ascent = -bounds.getY();

        int first = 0,second = 0;
        boolean odd = false;
        if (ilength%2 == 1)
        {
            first = (ilength-1)/2;
            odd = true;
        }
        else
        {
            first = (ilength)/2-1;
            second = (ilength)/2;
            odd = false;
        }


        //名称
        double radius2 = radius - ascent;
        double x0 = CENTERX;
        double y0 = CENTERY + radius - ascent+12;


        //旋转角度
        double a = (2*Math.asin(char_interval/(2*radius2)));

        if (odd)
        {
            g.setFont(f);
            g.drawString(messages[first], (float)(x0 - char_interval/2), (float)y0);

            //中心点的右边
            for (int i=first+1;i<ilength;i++)
            {
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                float f1= (float)(x0 + ax - char_interval/2* Math.cos(aa));
                float f3= (float)(y0 - ay + char_interval/2* Math.sin(aa));
                g.drawString(messages[i], f1,f3);
            }
            //中心点的左边
            for (int i=first-1;i>-1;i--)
            {
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                float f1= (float)(x0 - ax - char_interval/2* Math.cos(aa));
                float f3= (float)(y0 - ay - char_interval/2* Math.sin(aa));
                g.drawString(messages[i], f1,f3);
            }

        }
        else
        {
            //中心点的右边
            for (int i=second;i<ilength;i++)
            {
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }

            //中心点的左边
            for (int i=first;i>-1;i--)
            {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        }
    }

}