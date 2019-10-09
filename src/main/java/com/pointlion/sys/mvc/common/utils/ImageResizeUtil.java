package com.pointlion.sys.mvc.common.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageResizeUtil {     
    
    public static void resizeWithIcon(InputStream imageStream, String targerPath, int maxWidth, String iconPath) {     
    	resizeImageIcon(imageStream, targerPath, maxWidth, iconPath) ;   
    }     
    
    public static void resize(InputStream imageStream, String targerPath, int maxWidth) {     
    	resizeImageIcon(imageStream, targerPath, maxWidth, null);   
    }   
    private static void resizeImageIcon(InputStream imageStream, String targerPath, int maxWidth, String iconPath) {     
        OutputStream os = null; 
        BufferedImage srcImg = null;
        BufferedImage buffImg = null;
        try {     
        	srcImg = ImageIO.read(imageStream);
        	int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);
        	if(width > maxWidth){
        		//等比缩放
        		double scale = (double)maxWidth / width;
                width = (int)(width * scale);
                height = (int)(height * scale);
        	}
        	
        	int imgType = srcImg.getType();
        	String f = "jpg";
        	if(imgType == BufferedImage.TYPE_4BYTE_ABGR || imgType == BufferedImage.TYPE_4BYTE_ABGR_PRE 
        			|| imgType == BufferedImage.TYPE_INT_ARGB || imgType == BufferedImage.TYPE_INT_ARGB_PRE){
        		f = "png";
        	}
        	
//        	int imgType = BufferedImage.TYPE_INT_BGR;
//        	if(sfx.equalsIgnoreCase("png")){
//        		imgType = BufferedImage.TYPE_INT_ARGB; 
//        	}
        	
            buffImg = new BufferedImage(width, height, imgType);   
            // 得到画笔对象     
            // Graphics g= buffImg.getGraphics();     
            Graphics2D g = buffImg.createGraphics();     
    
            // 设置对线段的锯齿状边缘处理     
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
            
            g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);     
    
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度    
            if(iconPath != null){
            	// 设置水印旋转     
                //g.rotate(Math.toRadians(0),(double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2); 
            	ImageIcon imgIcon = new ImageIcon(iconPath);     
                // 得到Image对象。     
                Image img = imgIcon.getImage();    
                float alpha = 0.3f; // 透明度     
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));     
                // 表示水印图片的位置     
                g.drawImage(img, width - img.getWidth(null), height-img.getHeight(null), null);     
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
                g.dispose();
            }
            os = new FileOutputStream(targerPath);   
            
            // 生成图片     
            ImageIO.write(buffImg, f, os);     
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if (null != os)     
                    os.close();     
                if(null != imageStream){
                	imageStream.close();
                }
                if(null != srcImg){
                	srcImg = null;
                }
                if(null != buffImg){
                	buffImg = null;
                }
            } catch (Exception e) {     
                e.printStackTrace();     
            }     
        }     
    }     
    
    /**
     * 判断是否为图片后缀
     * @title isImgSuffix
     */
	public static boolean isImgSuffix(String suffix) {
    	String[] imgs = {"pic","png","tif","jpg","jpeg","bmp"};
		return Arrays.asList(imgs).contains(suffix.toLowerCase());
    }
} 