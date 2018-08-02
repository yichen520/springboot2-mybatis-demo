package com.dhht.model;

import lombok.Data;

import java.util.Date;
@Data
public class FileInfo {
    private String id;

    private String fileName;

    private String filePath;

    private Date createTime;

    private String operationRecordId;

}