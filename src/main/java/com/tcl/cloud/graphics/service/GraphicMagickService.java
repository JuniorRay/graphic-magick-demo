package com.tcl.cloud.graphics.service;

import com.tcl.cloud.graphics.config.GraphicMagickConfig;
import com.tcl.cloud.graphics.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
@Service
@Slf4j
public class GraphicMagickService {

    @Autowired
    private GraphicMagickConfig graphicMagickConfig;

    private ConvertCmd convertCmd;
    private IdentifyCmd identifyCmd;

    @PostConstruct
    public void init() {
        convertCmd = new ConvertCmd(true);
        //convertCmd.setSearchPath(graphicMagickConfig.getGraphicMagickPath());

        identifyCmd = new IdentifyCmd(true);
        //identifyCmd.setSearchPath(graphicMagickConfig.getGraphicMagickPath());
        log.info("init======");
    }

    /**
     * 获得图片文件大小[小技巧来获得图片大小]
     * @param imagePath 文件路径
     * @return 文件大小 */
    public int getSize(String imagePath) {
        int size = 0;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imagePath);
            size = inputStream.available();
            inputStream.close();
            inputStream = null;
        } catch (FileNotFoundException e) {
            size = 0;
            log.info("文件未找到!");
        } catch (IOException e) {
            e.printStackTrace();
            size = 0;
            log.info("读取文件大小错误!");
        } finally {
            // 可能异常为关闭输入流,所以需要关闭输入流
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.info("关闭文件读入流异常");
                }
                inputStream = null;

            }
        }
        return size;
    }

    /**
     * 获得图片的宽度
     *
     * @param imagePath 文件路径
     * @return 图片宽度
     */
    public int getWidth(String imagePath) {
        int line = 0;
        try {
            IMOperation op = new IMOperation();
            // 设置获取宽度参数
            op.format("%w");
            op.addImage(1);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, imagePath);
            ArrayList<String> cmdOutput = output.getOutput();
            assert cmdOutput.size() == 1;
            line = Integer.parseInt(cmdOutput.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            line = 0;
            log.info("运行指令出错!");
        }
        return line;
    }

    /**
     * 获得图片的高度
     *
     * @param imagePath 文件路径
     * @return 图片高度
     */
    public int getHeight(String imagePath) {
        int line = 0;
        try {
            IMOperation op = new IMOperation();
            // 设置获取高度参数
            op.format("%h");
            op.addImage(1);
            //IdentifyCmd identifyCmd = new IdentifyCmd(true);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, imagePath);
            ArrayList<String> cmdOutput = output.getOutput();
            assert cmdOutput.size() == 1;
            line = Integer.parseInt(cmdOutput.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            line = 0;
            log.info("运行指令出错!"+e.toString());
        }
        return line;
    }

    /**
     * 图片信息
     *
     * @param imagePath
     * @return
     */
    public String getImageInfo(String imagePath) {
        String line = null;
        try {
            IMOperation op = new IMOperation();
            op.format("width:%w,height:%h,path:%d%f,size:%b%[EXIF:DateTimeOriginal]");
            op.addImage(1);
            //IdentifyCmd identifyCmd = new IdentifyCmd(true);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, imagePath);
            ArrayList<String> cmdOutput = output.getOutput();
            assert cmdOutput.size() == 1;
            line = cmdOutput.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }

    /**
     * 裁剪图片
     *
     * @param imagePath 源图片路径
     * @param newPath 处理后图片路径
     * @param x 起始X坐标
     * @param y 起始Y坐标
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @return 返回true说明裁剪成功,否则失败
     */
    public boolean cutImage(String imagePath, String newPath, int x, int y,
                            int width, int height) {
        boolean flag = false;
        try {
            File imagePathFile = new File(imagePath);
            if(!imagePathFile.exists()){
                throw new CustomException("原图片路径imagePath文件不存在！！！");
            }
            File newPathFile = new File(newPath);
            newPath = newPathFile.getAbsolutePath();
            if(!newPathFile.exists()){
                newPathFile.getParentFile().mkdirs();
                newPathFile.createNewFile();
            }
            IMOperation op = new IMOperation();
            op.addImage(imagePath);
            /** width：裁剪的宽度 * height：裁剪的高度 * x：裁剪的横坐标 * y：裁剪纵坐标 */
            op.crop(width, height, x, y);
            op.addImage(newPath);
            convertCmd.run(op);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("文件读取错误!");
            flag = false;
        } catch (InterruptedException e) {
            flag = false;
        } catch (IM4JavaException e) {
            flag = false;
        } finally {

        }
        return flag;
    }

    /**
     * 根据尺寸缩放图片[等比例缩放:参数height为null,按宽度缩放比例缩放;参数width为null,按高度缩放比例缩放]
     *
     * @param imagePath 源图片路径
     * @param newPath 处理后图片路径
     * @param width 缩放后的图片宽度
     * @param height 缩放后的图片高度
     * @return 返回true说明缩放成功,否则失败
     */
    public boolean zoomImage(String imagePath, String newPath, Integer width,
                             Integer height) {

        boolean flag = false;
        try {
            File imagePathFile = new File(imagePath);
            if(!imagePathFile.exists()){
                throw new CustomException("原图片路径imagePath文件不存在！！！");
            }
            File newPathFile = new File(newPath);
            newPath = newPathFile.getAbsolutePath();
            if(!newPathFile.exists()){
                newPathFile.getParentFile().mkdirs();
                newPathFile.createNewFile();
            }
            IMOperation op = new IMOperation();
            op.addImage(imagePath);
            // 根据高度缩放图片
            if (width == null) {
                op.resize(null, height);
            } else if (height == null) {
                // 根据宽度缩放图片
                op.resize(width);
            } else {
                op.resize(width, height);
            }
            op.addImage(newPath);
            convertCmd.run(op);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("文件读取错误!");
            flag = false;
        } catch (InterruptedException e) {
            flag = false;
        } catch (IM4JavaException e) {
            flag = false;
        } finally {

        }
        return flag;
    }

    /**
     * 图片旋转
     *
     * @param imagePath 源图片路径
     * @param newPath 处理后图片路径
     * @param degree 旋转角度
     */
    public boolean rotate(String imagePath, String newPath, double degree) {
        boolean flag = false;
        try {
            File imagePathFile = new File(imagePath);
            if(!imagePathFile.exists()){
                throw new CustomException("原图片路径imagePath文件不存在！！！");
            }
            File newPathFile = new File(newPath);
            newPath = newPathFile.getAbsolutePath();
            if(!newPathFile.exists()){
                newPathFile.getParentFile().mkdirs();
                newPathFile.createNewFile();
            }
            // 1.将角度转换到0-360度之间
            degree = degree % 360;
            if (degree <= 0) {
                degree = 360 + degree;
            }
            IMOperation op = new IMOperation();
            op.addImage(imagePath);
            op.rotate(degree);
            op.addImage(newPath);
            convertCmd.run(op);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            log.info("图片旋转失败!");
        }
        return flag;
    }

}
