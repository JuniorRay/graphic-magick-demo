package com.tcl.cloud.graphics.controller;

import com.tcl.cloud.graphics.config.TclConfig;
import com.tcl.cloud.graphics.constant.Constants;
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
 * @Title webp路由
 * @Description WebpController
 * @Program graphic-magick-demo
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021-08-16 11:03
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@RestController
@Slf4j
@RequestMapping("/webp")
@Api(tags={"【webp处理】Controller"})
public class WebpController {
    @Autowired
    private com.tcl.cloud.graphics.service.WebpService webpService;

    /**
     * 转换成为webp
     *
     * @author JuniorRay
     * @date 2021/8/16 11:28
     * @param width 调整文件宽
     * @param height 调整文件高
     * @param nearLossless 无损压缩级别(near_lossless)
     * @param q 为RGB通道指定压缩参数（q）
     * @param z 切换无损压缩模式级别(z)
     * @param mt 采用多线程编码(mt)
     * @param sourceFileName 源文件名称
     * @return
     */
    @GetMapping("/")
    @ApiOperation("webp转换")
    public void webp(
            @ApiParam(name = "width", value = "调整文件宽")
            @RequestParam(value = "width", required = false) Integer width,
            @ApiParam(name = "height", value = "调整文件高")
            @RequestParam(value = "height", required = false) Integer height,
            @ApiParam(name = "nearLossless", value = "无损压缩级别(near_lossless)")
            @RequestParam(value = "nearLossless", required = false) Integer nearLossless,
            @ApiParam(name = "q", value = "为RGB通道指定压缩参数（q）")
            @RequestParam(value = "q", required = false) Float q,
            @ApiParam(name = "z", value = "切换无损压缩模式级别(z)")
            @RequestParam(value = "z", required = false) Integer z,
            @ApiParam(name = "mt", value = "采用多线程编码(mt)")
            @RequestParam(value = "mt", required = false) String mt,
            @ApiParam(name = "sourceFileName", value = "源文件名称")
            @RequestParam(value = "sourceFileName", required = true) String sourceFileName,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws Exception {
        if(!sourceFileName.contains(".")){
            throw new Exception("未知文件格式！！！"+sourceFileName);
        }
        // 本地资源路径
        String localPath = TclConfig.getProfile();
        // 数据资源地址
        String filePath = localPath + StringUtils.substringAfter(sourceFileName, Constants.RESOURCE_PREFIX);
        File filePathFile = new File(filePath);
        log.info("filePathFile.getName()={}",filePathFile.getName());
        // 获取缓存webp位置+旧文件名组成新文件夹
        String desFileName = filePathFile.getName().split("\\.")[0] + ".webp";
        String newPath =  TclConfig.getWebpCachePath() + File.separator + desFileName;
        try {
            webpService.toWebp(width,height,nearLossless,q,z,mt,filePath,newPath,response,request);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("webp转换失败", e);
        }
    }
}
