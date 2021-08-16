package com.tcl.cloud.graphics.controller;

import com.tcl.cloud.graphics.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title 页面返回路由
 * @Description PageController
 * @Program graphic-magick-demo
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021-08-13 14:56
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@Controller
@RequestMapping("/")
public class PageController {
    /**
     * 图片编辑
     */
    @GetMapping("/")
    public String avatar(ModelMap modelMap)
    {
        User user = new User();
        modelMap.put("user", user);
        //return "/avatar";
        return "avatar";
    }
}
