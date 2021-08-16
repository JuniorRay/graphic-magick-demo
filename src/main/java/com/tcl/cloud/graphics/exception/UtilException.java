package com.tcl.cloud.graphics.exception;

/**
 * @Title UtilException
 * @Description 工具类异常
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 */
public class UtilException extends RuntimeException
{
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e)
    {
        super(e.getMessage(), e);
    }

    public UtilException(String message)
    {
        super(message);
    }

    public UtilException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
