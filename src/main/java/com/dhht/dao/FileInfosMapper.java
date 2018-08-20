package com.dhht.dao;

import com.dhht.model.FileInfos;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfosMapper {
    int deleteByPrimaryKey(String id);

    int insert(FileInfos record);

    int insertSelective(FileInfos record);

    FileInfos selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FileInfos record);

    int updateByPrimaryKey(FileInfos record);
}