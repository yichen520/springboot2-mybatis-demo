package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class FileInfo {
    private String id;

    private String fileName;

    private String fileExt;

    private String filePath;

    private Date createTime;

    private String createMemo;

    private int createType;

    private String creatorId;

    private String creatorName;

    private boolean register;

    private String registerMemo;

    private Date registerTime;
}