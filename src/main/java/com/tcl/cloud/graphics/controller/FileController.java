package com.tcl.cloud.graphics.controller;


import com.tcl.cloud.graphics.config.TclConfig;
import com.tcl.cloud.graphics.constant.Constants;
import com.tcl.cloud.graphics.exception.CustomException;
import com.tcl.cloud.graphics.service.GraphicMagickService;
import com.tcl.cloud.graphics.utils.file.FileUtils;
import com.tcl.cloud.graphics.utils.text.Convert;
import com.tcl.cloud.graphics.utils.text.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Title CommonController
 * @Description 通用请求处理
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@RestController
@Slf4j
@RequestMapping("/")
@Api(tags={"【文件处理】Controller"})
public class FileController
{
    @Autowired
    private GraphicMagickService graphicMagickService;
    @Autowired
    private com.tcl.cloud.graphics.service.WebpService webpService;
    /**
     * 文件图片处理渲染
     */
    @GetMapping({"/profile/","/profile/{fileName}/**"})
    @ApiOperation("图片处理渲染")
    public void renderFile(
            @ApiParam(name = "fileName", value = "文件名称")
            @PathVariable("fileName") String fileName,
            @ApiParam(name = "process", value = "处理命令（process=image/format,webp/resize,800*600）")
            @RequestParam(name = "process", required = false) String process,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        // 手动处理url,因为上面的fileName 获取错误，无法获取完整的带很多个“/”的图片路径
        String url = request.getRequestURI();
        if(StringUtils.isEmpty(process)){
            process = request.getParameter("process");
        }
        fileName = url.split("\\?")[0];
        // 如果处理命令为空,
        // 或者不包含webp,且resize为空
        // 或者不包含webp,且resize存在，且800*600大小不存在
        if(StringUtils.isEmpty(process) ||
                (!process.contains(",webp") && (!process.contains("resize,")) ) ||
                (!process.contains(",webp") && ( process.contains("resize,") && process.split("resize,").length <= 1 ))
        ){
            // 本地资源路径
            String localPath = TclConfig.getProfile();
            // 数据资源地址
            String downloadPath = localPath + StringUtils.substringAfter(fileName, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, File.separator);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
            return;
        }
        Integer width = null;
        Integer height = null;
        // 存在设置的宽高
        if(process.contains("resize,") && process.split("resize,").length > 1 ){
            try{
                String size = process.split("resize,", 2)[1];
                String[] sizeArray = size.split("\\*");
                if(sizeArray.length<=1){
                    throw new CustomException("宽高设置不正确！");
                }
                width = Convert.toInt(sizeArray[0]);
                height = Convert.toInt(sizeArray[1]);
            }catch (Exception e){
                e.printStackTrace();
                throw new CustomException(e.getMessage());
            }
        }

        // 处理webp压缩流程
        if(process.contains("image/format,webp")){
            // 本地资源路径
            String localPath = TclConfig.getProfile();
            // 数据资源地址
            String filePath = localPath + StringUtils.substringAfter(fileName, Constants.RESOURCE_PREFIX);
            File filePathFile = new File(filePath);
            log.info("filePathFile.getName()={}",filePathFile.getName());
            // 获取缓存webp位置+旧文件名组成新文件夹
            String desFileName = filePathFile.getName().split("\\.")[0] + ".webp";
            String newPath =  TclConfig.getWebpCachePath() + File.separator + desFileName;
            try {
                webpService.toWebp(width,height,null,null,null,null,filePath,newPath,response,request);
            }catch (Exception e) {
                e.printStackTrace();
                log.error("webp转换失败", e);
                throw new CustomException("webp转换失败"+e.getMessage());
            }

        }else{// 处理普通图片不压缩
            try {

                // 本地资源路径
                String localPath = TclConfig.getProfile();
                // 数据资源地址
                String filePath = localPath + StringUtils.substringAfter(fileName, Constants.RESOURCE_PREFIX);
                // 获取缓存裁剪cache位置+旧文件名组成新文件夹
                String newPath =  TclConfig.getZoomImageCachePath(width,height) + File.separator + fileName;

                //TODO 考虑这里是不是以后可以加缓存newPathFile？？？
                if (graphicMagickService.zoomImage(filePath, newPath, width, height)) {

                    // 下载名称
                    String downloadName = StringUtils.substringAfterLast(newPath, File.separator);
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
                throw new CustomException("Graphic处理异常："+e.getMessage());
            }
        }
    }
}
