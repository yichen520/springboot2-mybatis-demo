package com.dhht.dao;

import com.dhht.model.JsonData;
import com.dhht.model.JsonDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JsonDataMapper {
    int countByExample(JsonDataExample example);

    int deleteByExample(JsonDataExample example);

    int deleteByPrimaryKey(String id);

    int insert(JsonData record);

    int insertSelective(JsonData record);

    List<JsonData> selectByExampleWithBLOBs(JsonDataExample example);

    List<JsonData> selectByExample(JsonDataExample example);

    JsonData selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") JsonData record, @Param("example") JsonDataExample example);

    int updateByExampleWithBLOBs(@Param("record") JsonData record, @Param("example") JsonDataExample example);

    int updateByExample(@Param("record") JsonData record, @Param("example") JsonDataExample example);

    int updateByPrimaryKeySelective(JsonData record);

    int updateByPrimaryKeyWithBLOBs(JsonData record);

    int updateByPrimaryKey(JsonData record);
}