package com.tcl.cloud.graphics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * @Title FileServerApplication
 * @Description 启动程序
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2021-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class FileServerApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(FileServerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  TCL文件服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
