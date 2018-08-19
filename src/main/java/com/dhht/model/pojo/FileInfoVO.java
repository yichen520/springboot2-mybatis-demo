package com.dhht.model.pojo;

import com.dhht.model.FileInfo;
import lombok.Data;

import java.io.File;

/**
 * 文件信息
 * @author 赵兴龙
 */
@Data
public class FileInfoVO extends FileInfo {

    public FileInfoVO(FileInfo fileInfo) {
        this.setId(fileInfo.getId());
        this.setFileName(fileInfo.getFileName());
        this.setFileExt(fileInfo.getFileExt());
        this.setFilePath(fileInfo.getFilePath());
        this.setCreateTime(fileInfo.getCreateTime());
        this.setCreateType(fileInfo.getCreateType());
        this.setCreatorId(fileInfo.getCreatorId());
        this.setCreatorName(fileInfo.getCreatorName());
        this.setRegister(fileInfo.isRegister());
        this.setRegisterMemo(fileInfo.getRegisterMemo());
        this.setRegisterTime(fileInfo.getRegisterTime());
    }
    private byte[] fileData;
}
