package com.tcl.test;

import com.tcl.cloud.graphics.FileServerApplication;
import com.tcl.cloud.graphics.service.GraphicMagickService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Title 图片测试
 * @Description ImgTest
 * @Program graphic-magick-demo
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021-08-16 03:35
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FileServerApplication.class)//这里Application是启动类
public class ImgTest {
    @Autowired
    private GraphicMagickService graphicMagickService;
    @Test
    public void test() {
        log.info("原图片大小:" + graphicMagickService.getSize("d://test.jpg") + "Bit");
        log.info("原图片宽度:" + graphicMagickService.getWidth("d://test.jpg"));
        log.info("原图片高度:" + graphicMagickService.getHeight("d://test.jpg"));
        if (graphicMagickService.zoomImage("d://test.jpg", "d://test1.jpg", 500, null)) {
            if (graphicMagickService.rotate("d://test.jpg", "d://test2.jpg", 15)) {
                if (graphicMagickService.cutImage("d://test.jpg", "d://test3.jpg", 32,
                        105, 200, 200)) {
                    log.info("cutImage编辑成功");
                } else {
                    log.info("cutImage编辑失败");
                }
            } else {
                log.info("rotate编辑失败");
            }
        } else {
            log.info("zoomImage编辑失败");
        }
    }
}
