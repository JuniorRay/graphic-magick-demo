package com.tcl.cloud.graphics.utils;
/**
 * @Title 判断操作系统
 * @Description JudgeSystem
 * @Program 项目名
 * @Author JuniorRay
 * @Version 1.0
 * @Date 2021/8/16 12:58
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
public class JudgeSystem {
    public final static String LINUX = "linux";
    public final static String WINDOWS = "windows";
    public final static String OTHER_SYSTEM = "other system";
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取当前操作系统
     * @return
     */
    public String getOtherSystem() {
        if (isLinux()) {
            return LINUX;
        } else if (isWindows()) {
            return WINDOWS;
        } else {
            return OTHER_SYSTEM;
        }
    }

    public static void main(String[] args) {
        boolean isLinux = isLinux();
        System.out.println(isLinux);
        boolean isWindows = isWindows();
        System.out.println(isWindows);
        System.out.println(System.getProperty("os.name"));
        String sys = new JudgeSystem().getOtherSystem();
        System.out.println(sys);
    }

}
