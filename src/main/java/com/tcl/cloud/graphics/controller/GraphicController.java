package com.tcl.cloud.graphics.controller;


import com.tcl.cloud.graphics.config.ServerConfig;
import com.tcl.cloud.graphics.config.TclConfig;
import com.tcl.cloud.graphics.constant.Constants;
import com.tcl.cloud.graphics.utils.file.FileUtils;
import com.tcl.cloud.graphics.service.GraphicMagickService;
import com.tcl.cloud.graphics.utils.text.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Title GraphicController
 * @Description 图片处理缩放处理器
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@RestController
@Slf4j
@RequestMapping("/img")
@Api(tags={"【图片处理缩放处理器】Controller"})
public class GraphicController
{

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private GraphicMagickService graphicMagickService;
    /**
     * 裁剪图片
     *
     * @param imageName 源图片名字
     * @param x 起始X坐标
     * @param y 起始Y坐标
     * @param width 裁剪宽度
     * @param height 裁剪高度
     * @return 处理后图片
     */
    @GetMapping("/cutImage")
    @ApiOperation("裁剪图片")
    public void cutImage(
            @ApiParam(name = "imageName", value = "图片名称")
            @RequestParam(name = "imageName", required = true) String imageName,
            @ApiParam(name = "width", value = "图片width坐标")
            @RequestParam(name = "width", required = true)   int width,
            @ApiParam(name = "height", value = "图片height坐标")
            @RequestParam(name = "height", required = true) int height,
            @ApiParam(name = "x", value = "图片x坐标")
            @RequestParam(name = "x", required = true) int x,
            @ApiParam(name = "y", value = "图片y坐标")
            @RequestParam(name = "y", required = true)   int y,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        try {
            // 本地资源路径
            String localPath = TclConfig.getProfile();
            // 数据资源地址
            String filePath = localPath + StringUtils.substringAfter(imageName, Constants.RESOURCE_PREFIX);
            // 获取缓存裁剪cache位置+旧文件名组成新文件夹
            String newPath =  TclConfig.getCropCachePath(width,height,x,y) + File.separator + imageName;

            //TODO 考虑这里是不是以后可以加缓存newPathFile？？？
            if (graphicMagickService.cutImage(filePath, newPath, x,y, width, height)) {

                // 下载名称
                String downloadName = StringUtils.substringAfterLast(newPath, "/");
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
                FileUtils.writeBytes(newPath, response.getOutputStream());
                log.info("图片裁剪成功");
            } else {
                log.info("图片裁剪失败");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据尺寸缩放图片[等比例缩放:参数height为null,按宽度缩放比例缩放;参数width为null,按高度缩放比例缩放]
     *
     * @param imageName 源图片名称
     * @param width 缩放后的图片宽度
     * @param height 缩放后的图片高度
     * @return 处理后图片
     */
    @GetMapping("/zoomImage")
    @ApiOperation("根据尺寸缩放图片")
    public void zoomImage(
            @ApiParam(name = "imageName", value = "图片名称")
            @RequestParam(name = "imageName", required = true) String imageName,
            @ApiParam(name = "width", value = "图片width坐标")
            @RequestParam(name = "width", required = true)   int width,
            @ApiParam(name = "height", value = "图片height坐标")
            @RequestParam(name = "height", required = true) int height,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        try {

            // 本地资源路径
            String localPath = TclConfig.getProfile();
            // 数据资源地址
            String filePath = localPath + StringUtils.substringAfter(imageName, Constants.RESOURCE_PREFIX);
            // 获取缓存裁剪cache位置+旧文件名组成新文件夹
            String newPath =  TclConfig.getZoomImageCachePath(width,height) + File.separator + imageName;

            //TODO 考虑这里是不是以后可以加缓存newPathFile？？？
            if (graphicMagickService.zoomImage(filePath, newPath, width, height)) {

                // 下载名称
                String downloadName = StringUtils.substringAfterLast(newPath, "/");
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
                FileUtils.writeBytes(newPath, response.getOutputStream());

                log.info("图片缩放成功");


            } else {
                log.info("图片缩放失败");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 图片旋转
     *
     * @param imageName 源图片名称
     * @param degree 旋转角度
     */
    @GetMapping("/rotate")
    @ApiOperation("图片旋转")
    public void rotate(
            @ApiParam(name = "imageName", value = "图片名称")
            @RequestParam(name = "imageName", required = true) String imageName,
            @ApiParam(name = "degree", value = "旋转角度")
            @RequestParam(name = "degree", required = true)   int degree,
            HttpServletResponse response,
            HttpServletRequest request
    ){
        try {
            // 本地资源路径
            String localPath = TclConfig.getProfile();
            // 数据资源地址
            String filePath = localPath + StringUtils.substringAfter(imageName, Constants.RESOURCE_PREFIX);
            // 获取缓存裁剪cache位置+旧文件名组成新文件夹
            String newPath =  TclConfig.getRotateCachePath(degree) + File.separator + imageName;

            //TODO 考虑这里是不是以后可以加缓存newPathFile？？？
            if (graphicMagickService.rotate(filePath, newPath, degree)) {
                // 下载名称
                String downloadName = StringUtils.substringAfterLast(newPath, "/");
                response.setCharacterEncoding("utf-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
                FileUtils.writeBytes(newPath, response.getOutputStream());

                log.info("图片旋转成功");
            } else {
                log.info("图片旋转失败");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
