package com.dhht.dao;

import com.dhht.model.FileInfo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMapper {

//    FileInfo selectByPath(@Param("path") String path);

    int deleteByPrimaryKey(String id);

//    int deleteByPrimaryPath(String filePath);

    int insert(FileInfo record);

    FileInfo getById(String id);

    List<FileInfo> selectByIds(@Param("ids") String[] ids);

    int updateByPrimaryKey(FileInfo record);
}