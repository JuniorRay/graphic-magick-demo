package com.tcl.cloud.graphics.exception.file;

/**
 * @Title FileSizeLimitExceededException
 * @Description 文件名大小限制异常类
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 */
public class FileSizeLimitExceededException extends FileException
{
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize)
    {
        super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
    }
}
