package com.tcl.cloud.graphics.service;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.springframework.util.Assert;

import java.io.File;
/**
 * <p>ImageMagick 在windows下是不存在的，linux一般都有(最后也请确认下，可以参考官网，不过也可以简单的运行convert 命令判断)。</p>
 * <p>软件官网:im4java.sourceforge.net ps 请翻墙。</p>
 * <p>ImageMagick官网:<a href="http://www.imagemagick.org/">http://www.imagemagick.org</a></p>
 * @Title 图片缩放，截图，缩略图工具类
 * @Description ImageMagickService
 * @Program 项目名
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021/8/12 18:57
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/

public class ImageMagickService {
    private static final String ENV_OS = System.getProperty("os.name").toLowerCase();
    private static final String ImageMagickPath = "D:\\software\\ImageMagick";

    static {
        if (isWindows()) {
            Assert.isTrue(new File(ImageMagickPath).exists(), "you shoud first install ImageMagick software to use cropper in your windows system.");
            ProcessStarter.setGlobalSearchPath(ImageMagickPath);
        }
    }
    public static boolean isWindows() {
        return (ENV_OS.indexOf("win") >= 0);
    }

    /**
     * 按照给定的参数截取图片区域
     *
     * @param imagePath 原图位置
     * @param newPath 结果图位置
     * @param x 横坐标
     * @param y 纵坐标
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static boolean crop(String imagePath, String newPath, int x, int y, int width, int height) {
        boolean flag = false;
        try {
            IMOperation op = new IMOperation();
            op.addImage();
            /** width：裁剪的宽度 * height：裁剪的高度 * x：裁剪的横坐标 * y：裁剪纵坐标 */
            op.crop(width, height, x, y);
            op.addImage();
            ConvertCmd convert = new ConvertCmd();
            convert.run(op, imagePath, newPath);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 缩放图片
     *
     * @param imagePath
     * @param newPath
     * @param width
     * @param height
     * @return
     */
    public static boolean scale(String imagePath, String newPath, int width, int height) {
        boolean flag = false;
        try {
            IMOperation op = new IMOperation();
            op.addImage();
            op.resize(width, height);
            op.addImage();
            ConvertCmd convert = new ConvertCmd();
            convert.run(op, imagePath, newPath);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(ENV_OS);
        String inputFileLocation = "d:/d.jpeg";
        String outputFileLocation = "d:/d-res.jpeg";
        boolean r = crop(inputFileLocation, outputFileLocation, 50, 50, 200, 140);
        System.out.println(r);
    }

}
