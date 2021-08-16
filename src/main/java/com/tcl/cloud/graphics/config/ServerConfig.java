package com.tcl.cloud.graphics.config;

import com.tcl.cloud.graphics.utils.ServletUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title ServerConfig
 * @Description 服务相关配置
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 */
@Component
public class ServerConfig
{
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
