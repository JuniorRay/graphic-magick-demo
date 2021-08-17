package com.tcl.cloud.graphics.controller;


import com.tcl.cloud.graphics.config.ServerConfig;
import com.tcl.cloud.graphics.config.TclConfig;
import com.tcl.cloud.graphics.constant.Constants;
import com.tcl.cloud.graphics.domain.Response;
import com.tcl.cloud.graphics.dto.UploadFileDTO;
import com.tcl.cloud.graphics.utils.file.FileUploadUtils;
import com.tcl.cloud.graphics.utils.file.FileUtils;
import com.tcl.cloud.graphics.utils.text.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Title CommonController
 * @Description 通用请求处理
 * @Program tcl-api-server
 * @Author Junior Ray
 * @Version 1.0
 * @Date 2020-06-21 16:41
 * @Copyright Copyright (c) 2021 TCL Inc. All rights reserved
 **/
@RestController
@Slf4j
@RequestMapping("/common")
@Api(tags={"【通用请求处理】Controller"})
public class CommonController
{

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    @ApiOperation("通用下载请求")
    public void fileDownload(
            @ApiParam(name = "fileName", value = "文件名称")
            @RequestParam(name = "fileName", required = true) String fileName,
            @ApiParam(name = "delete", value = "是否删除")
            @RequestParam(name = "delete", required = true) Boolean delete,
            HttpServletResponse response,
            HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.isValidFilename(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = TclConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    @ApiOperation("通用上传请求")
    public Response<UploadFileDTO> uploadFile(MultipartFile file ) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = TclConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            UploadFileDTO uploadFileDTO = new UploadFileDTO();
            uploadFileDTO.setFileName(fileName);
            uploadFileDTO.setUrl(url);

            return Response.success(uploadFileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().setMsg("失败："+e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    @ApiOperation("本地资源通用下载")
    public void resourceDownload(
            @ApiParam(name = "name", value = "文件名称")
            @RequestParam(name = "name", required = true) String name,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        // 本地资源路径
        String localPath = TclConfig.getProfile();
        // 数据资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, File.separator);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
}
