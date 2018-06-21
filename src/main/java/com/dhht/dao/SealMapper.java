package com.dhht.dao;

import com.dhht.model.Seal;
import com.dhht.model.SealKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SealMapper {


    int deleteByPrimaryKey(SealKey key);

    int insert(Seal record);

    int insertSelective(Seal record);

    Seal selectByPrimaryKey(SealKey key);



    int updateByPrimaryKeySelective(Seal record);

    int updateByPrimaryKey(Seal record);
}