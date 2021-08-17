package com.tcl.cloud.graphics.service;

import com.tcl.cloud.graphics.config.WebpConfig;
import com.tcl.cloud.graphics.utils.JudgeSystem;
import com.tcl.cloud.graphics.utils.file.FileUtils;
import com.tcl.cloud.graphics.utils.text.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Title webp服务层
 * @Description WebpService
 * @Program graphic-magick-demo
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021-08-16 11:04
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@Service
@Slf4j
public class WebpService {
    @Autowired
    private WebpConfig webpConfig;

    private String CWEBP_WINDOWS = "";
    private final String CWEBP_LINUX = "cwebp";

    @PostConstruct
    public void init() {
        CWEBP_WINDOWS = webpConfig.getWebpPath();
        log.info("init======");
    }
    /**
     * webp 转换图片
     * @author JuniorRay
     * @date 2021/8/16 11:22
     * @param width 调整文件宽
     * @param height 调整文件高
     * @param nearLossless 无损压缩级别(near_lossless)
     * @param q 为RGB通道指定压缩参数（q）
     * @param z 切换无损压缩模式级别(z)
     * @param mt 采用多线程编码(mt)
     * @param sourceFileName 源文件名称
     * @param desFileName 目标文件webp名称
     * @return
     */
    public void toWebp(
            Integer width, Integer height, Integer nearLossless, Float q, Integer z,
            String mt, String sourceFileName, String desFileName,
            HttpServletResponse response, HttpServletRequest request
    ) throws IOException {
        long start = System.currentTimeMillis();
        File newPathFile = new File(desFileName);
        desFileName = newPathFile.getAbsolutePath();
        if(!newPathFile.exists()){
            newPathFile.getParentFile().mkdirs();
            newPathFile.createNewFile();
        }
        String options = "";
        if (null != width && null != height) {
            options += " -resize " + width + " " + height;
        }

        if (null != mt) {
            options += " -mt ";
        }

        if (null != nearLossless) {
            options += " -near_lossless " + nearLossless;
        }

        if (null != q) {
            options += " -q " + q;
        }

        if (null != z) {
            options += " -z " + z;
        }

        if (!(new File(sourceFileName)).exists()) {
            throw new FileNotFoundException(sourceFileName+"文件不存在！！");
        }
        String command = "";
        if(JudgeSystem.isLinux()){
            command = CWEBP_LINUX + " " + sourceFileName + " -o " + desFileName + options;
        }else {
            command = CWEBP_WINDOWS + " " + sourceFileName + " -o " + desFileName + options;
        }

        log.info(command);
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(desFileName, File.separator);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
            FileUtils.writeBytes(desFileName, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            log.info("elapsed time:" + (end - start));
        }
    }

}
