package com.tcl.cloud.graphics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Title TclConfig
 * @Description 读取项目相关配置
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 */
@Component
@ConfigurationProperties(prefix = "tcl")
public class TclConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        TclConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        TclConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }


    /**
     * 获取裁剪缓存目录
     * @param width 图片宽
     * @param height 图片高
     * @param x 图片x坐标
     * @param y 图片y坐标
     * @return 缓存路径文件夹格式
     */
    public static String getCropCachePath( int width, int height,int x, int y)
    {
       return TclConfig.getDownloadPath()
                + File.separator+"crop"+File.separator+width+"_"+height+"_"+x+"_"+y+File.separator;
    }


    /**
     * 获取缩放缓存目录
     * @param width 图片宽
     * @param height 图片高
     * @return 缓存路径文件夹格式
     */
    public static String getZoomImageCachePath( int width, int height)
    {
        return TclConfig.getDownloadPath()
                + File.separator+"zoom"+File.separator+width+"_"+height+File.separator;
    }
    /**
     * 获取缩放缓存目录
     * @param degree 旋转角度
     * @return 缓存路径文件夹格式
     */
    public static String getRotateCachePath( int degree)
    {
        return TclConfig.getDownloadPath()
                + File.separator+"rotate"+File.separator+degree+File.separator;
    }
    /**
     * 获取webp缓存目录
     * @return 缓存路径文件夹格式
     */
    public static String getWebpCachePath()
    {
        return TclConfig.getDownloadPath()
                + File.separator+"webp"+File.separator;
    }
}
