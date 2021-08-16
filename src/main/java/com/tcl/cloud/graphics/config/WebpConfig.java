package com.tcl.cloud.graphics.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Title gm配置
 * @Description GraphicMagickConfig
 * @Program graphic-magick-demo
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021-08-16 08:11
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@Configurable
@Slf4j
@Component
public class WebpConfig {

    @Value("${webpPath}")
    private String webpPath ;

    public void setWebpPath(String webpPath) {
        log.info("webpPath = " + webpPath);
        this.webpPath = webpPath;
    }

    public String getWebpPath() {
        return webpPath;
    }
}
