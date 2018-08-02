package com.dhht.model;

import lombok.Data;

import java.util.Date;

/**
 * 系统已上传文件的信息
 */
@Data
public class FileInfo {
    private String id;

    /**
     * 文件名，不带后缀
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String fileExt;

    /**
     * 文件路径，相对于存储区域根目录的相对路径
     */
    private String filePath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时的备注
     */
    private String createMemo;

    /**
     * 注册状态，默认为false
     */
    private boolean registered = false;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 注册是的备注
     */
    private String registerMemo;
}