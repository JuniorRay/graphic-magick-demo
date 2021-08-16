package com.tcl.cloud.graphics;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Title FileServerServletInitializer
 * @Description web容器中进行部署
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
public class FileServerServletInitializer extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(FileServerApplication.class);
    }
}
