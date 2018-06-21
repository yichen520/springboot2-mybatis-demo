package com.dhht.dao;

import com.dhht.model.SealImage;
import com.dhht.model.SealImageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SealImageMapper {
    int countByExample(SealImageExample example);

    int deleteByExample(SealImageExample example);

    int deleteByPrimaryKey(String id);

    int insert(SealImage record);

    int insertSelective(SealImage record);

    List<SealImage> selectByExampleWithBLOBs(SealImageExample example);

    List<SealImage> selectByExample(SealImageExample example);

    SealImage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SealImage record, @Param("example") SealImageExample example);

    int updateByExampleWithBLOBs(@Param("record") SealImage record, @Param("example") SealImageExample example);

    int updateByExample(@Param("record") SealImage record, @Param("example") SealImageExample example);

    int updateByPrimaryKeySelective(SealImage record);

    int updateByPrimaryKeyWithBLOBs(SealImage record);

    int updateByPrimaryKey(SealImage record);
}