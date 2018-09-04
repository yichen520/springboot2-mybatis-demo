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
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class ImageGenerate {

    private  final int WIDTH = 400;//图片宽度
    private  final int HEIGHT = 400;//图片高度
    private final String image= "★";
//    @Value("${file.local.root}")
    private String filePath ="C:/temp/seal";

    //中心图案五角星大小
    private int centerImageFont = 120;

    //印章名称
    private int sealNameFont = 25;

    //圆的宽度
    private int circleWidth = 6;
    //防伪线偏移角度(在5-10之间)
    private int skew = 8;

    //防伪线长度
    private int ilength = 9;

    public  String seal(Map map) {
        String message = (String)map.get("useDepartment");
        String centerName = (String)map.get("sealType");
        String code = (String)map.get("sealCode");
        String centerImage = (String)map.get("centerImage");
        try {
            String sealPath = filePath+"/"+message+"/"+message+".png";
            BufferedImage image =  startGraphics2D(message,centerName,code,centerImage);
            File dest = new File(sealPath);
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            ImageIO.write(image, "png", new File(sealPath));
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
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius+circleWidth, CENTERY + radius+circleWidth);
        g.setStroke(new BasicStroke(circleWidth));//设置圆的宽度
        g.draw(circle);


        //绘制中间的五角星
        if (centerImage == null){
            g.setFont(new Font("宋体", Font.BOLD, centerImageFont));
            g.drawString(image, CENTERX-(120/2), CENTERY+(120/3));
        }else {
            g.setFont(new Font("宋体", Font.BOLD, centerImageFont));
            g.drawString(centerImage, CENTERX-(120/2), CENTERY+(120/3));
        }


        //添加印章名称
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sealNameFont));
        g.drawString(centerName, CENTERX -(60), CENTERY +(80));

        nameDraw(g,radius,CENTERX,CENTERY,message);

        codeDraw(g,radius,CENTERX,CENTERY,code,message);

        fangwei(g,radius,CENTERX,CENTERY,ilength,skew);

        return buffImg;
    }

    public  void  fangwei(Graphics2D g,int radius,int CENTERX,int CENTERY,int w,int n){
        Random random = new Random();
        int r1= random.nextInt((60-n))+ 0;
        int r2= random.nextInt((60-n))+ 60;
        int r3= random.nextInt((60-n))+ 120;
        int r4= random.nextInt((60-n))+ 180;
        int r5= random.nextInt((60-n))+ 240;
        int r6= random.nextInt((60-n))+ 300;
        g.setStroke( new BasicStroke( 1.5F ));
        g.setColor(Color.white);
        int x1,y1,x2,y2;
        int[] array ={r1,r2,r3,r4,r5,r6};

        for (int i=0;i<array.length;i++){
            x1 = CENTERX + (int)((radius+2)*Math.cos((array[i]*Math.PI)/180));
            y1 = CENTERX - (int)((radius+2)*Math.sin((array[i]*Math.PI)/180));
            x2 = CENTERY +  (int)((radius+w+2)*Math.cos(((array[i]+random.nextInt(n*2)+(-n))*Math.PI)/180));
            y2 = CENTERY - (int)((radius+w+2)*Math.sin(((array[i]+random.nextInt(n*2)+(-n))*Math.PI)/180));
            g.drawLine(x1,y1,x2,y2);
        }
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

        //输入的字数
        int ilength = messages.length;

        //设置字体属性
        int fontsize;
        if (message.length() <=14){
            fontsize = 31;
        }else if( message.length()==15 ){
            fontsize = 29;
        }
        else if(message.length()==16  ){
            fontsize = 27;
        }
        else if(message.length()==17  ){
            fontsize = 26;
        }
        else if(message.length()==18 ){
            fontsize = 24;
        }else {
            fontsize = 24;
        }
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
                AffineTransform transform = AffineTransform.getRotateInstance(aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(aa);
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
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 -ay + char_interval/2* Math.sin(aa)));
            }

            //中心点的左边
            for (int i=first;i>-1;i--)
            {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 - ay - char_interval/2* Math.sin(aa)));
            }
        }
    }


    //生成二维码信息并返回
    public int[][] moulageData(Map map){

        String message = (String)map.get("useDepartment");
        String centerName = (String)map.get("sealType");
        String code = (String)map.get("sealCode");
        String centerImage = (String)map.get("centerImage");

        int [][] data0 = new int[400][400];
        for(int i=0;i<data0.length;i++){
            for(int j=0;j<data0.length;j++){
                data0[i][j]= 0;
            }
        }

        //前景图层
        int [][] data1 = new int[HEIGHT][WIDTH];
        BufferedImage image = startGraphicsFront2D(map);
        //文件命名
        String filePath1 = filePath+"/"+message+"/"+message+"1.png";
        File dest = new File(filePath1);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(image, "png", new File(filePath1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //读取图层信息并将二维信息放入数组
        try {
            BufferedImage bimg = ImageIO.read(new File(filePath1));
            for(int i=0;i<data1.length;i++){
                for(int j=0;j<data1.length;j++){
                    data1[i][j]=bimg.getRGB(i,j);
                    if (data1[i][j] != -1){
                        data1[i][j]= 1;
                    }else {
                        data1[i][j]= 0;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //编码图层
        int [][] data2 = new int[HEIGHT][WIDTH];
        BufferedImage codeimage = startGraphicsCode2D( code, message);
        String filePath2 = filePath+"/"+message+"/"+message+"2.png";
        try {
            ImageIO.write(codeimage, "png", new File(filePath2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            BufferedImage bimg = ImageIO.read(new File(filePath2));
            for(int i=0;i<data2.length;i++){
                for(int j=0;j<data2.length;j++){
                    data2[i][j]=bimg.getRGB(i,j);
                    if (data2[i][j] != -1){
                        data2[i][j]= 2;
                    }else {
                        data2[i][j]= 0;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        int [][] data3 = new int[HEIGHT][WIDTH];
        BufferedImage trueimage = startGraphicstrue2D();
        String filePath3 = filePath+"/"+message+"/"+message+"3.png";
        try {
            ImageIO.write(trueimage, "png", new File(filePath3));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            BufferedImage bimg = ImageIO.read(new File(filePath3));
            for(int i=0;i<data3.length;i++){
                for(int j=0;j<data3.length;j++){
                    data3[i][j]=bimg.getRGB(i,j);
                    if (data3[i][j] != -16777216){
                        data3[i][j]= 3;
                    }else {
                        data3[i][j]= 0;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        //将几个图层进行比较
        int [][] data = new int[HEIGHT][WIDTH];
        for(int i=0;i<WIDTH;i++){
            for(int j=0;j<WIDTH;j++){
                int max = 0;
                if (data1[i][j]>data0[i][j]){
                    max=data1[i][j];
                }
                if(data2[i][j]>data1[i][j]){
                    max=data2[i][j];
                }
                if(data3[i][j]>data2[i][j]){
                    max=data3[i][j];
                }
                data[i][j] =max;
            }
        }
        return data;
    }



    public  BufferedImage startGraphicsFront2D(Map map){
        String message = (String)map.get("useDepartment");
        String centerName = (String)map.get("sealType");
        String centerImage = (String)map.get("centerImage");
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

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
        g.setStroke(new BasicStroke(circleWidth));//设置圆的宽度
        g.draw(circle);


        //绘制中间的五角星
        if (centerImage == null){
            g.setFont(new Font("宋体", Font.BOLD, centerImageFont));
            g.drawString(image, CENTERX-(120/2), CENTERY+(120/3));
        }else {
            g.setFont(new Font("宋体", Font.BOLD, centerImageFont));
            g.drawString(centerImage, CENTERX-(120/2), CENTERY+(120/3));
        }


        //添加印章名称
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sealNameFont));
        g.drawString(centerName, CENTERX -(40), CENTERY +(80));

        nameDraw(g,radius,CENTERX,CENTERY,message);

        return buffImg;
    }

    public  BufferedImage startGraphicsCode2D(String code,String message){
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        //使得背景为白色
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.RED);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int radius = HEIGHT/3;//周半径
        int CENTERX = WIDTH/2;//画图所出位置
        int CENTERY = HEIGHT/2;//画图所处位置
        codeDraw(g,radius,CENTERX,CENTERY,code, message);

        return buffImg;
    }

    //画图防伪线
    public  BufferedImage startGraphicstrue2D(){
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();

        //
        g.setPaint(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.white);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int radius = HEIGHT/3;//周半径
        int CENTERX = WIDTH/2;//画图所出位置
        int CENTERY = HEIGHT/2;//画图所处位置

        //7 代表宽度    5代表角度
        fangwei(g,radius,CENTERX,CENTERY,ilength,skew);

        return buffImg;
    }

}