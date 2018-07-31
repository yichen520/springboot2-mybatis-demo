package com.dhht.dao;

import com.dhht.model.FileInfo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {

    FileInfo selectByPath(@Param("path") String path);

    int deleteByPrimaryKey(String id);

    int deleteByPrimaryPath(String filePath);

    int insert(FileInfo record);


    FileInfo selectByPrimaryKey(String id);

    int updateByPrimaryKey(FileInfo record);
}