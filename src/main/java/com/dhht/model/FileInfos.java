package com.dhht.model;

import java.util.Date;

public class FileInfos {
    private String id;

    private String fileName;

    private String fileExt;

    private String filePath;

    private Date createTime;

    private String createMemo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt == null ? null : fileExt.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateMemo() {
        return createMemo;
    }

    public void setCreateMemo(String createMemo) {
        this.createMemo = createMemo == null ? null : createMemo.trim();
    }
}