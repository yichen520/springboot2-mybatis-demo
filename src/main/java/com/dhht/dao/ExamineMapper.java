package com.dhht.dao;

import com.dhht.model.Examine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineMapper {
    int deleteByPrimaryKey(String id);

    int insert(Examine record);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Examine record);

    int updateByPrimaryKey(Examine record);

    List<Examine> selectExamine();

    List<Examine> selectExamineForm(@Param("dis1")String dis1,@Param("dis2")String dis2,@Param("dis3")String dis3);
}