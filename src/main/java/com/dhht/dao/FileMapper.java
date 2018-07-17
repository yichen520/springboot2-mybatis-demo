package com.dhht.dao;

import com.dhht.model.File;
import com.dhht.model.FileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper {

    File selectByPath(@Param("path") String path);

    int deleteByPrimaryKey(String id);

    int deleteByPrimaryPath(String filePath);

    int insert(File record);


    File selectByPrimaryKey(String id);

    int updateByPrimaryKey(File record);
}