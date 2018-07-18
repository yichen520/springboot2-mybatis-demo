package com.dhht.dao;

import com.dhht.model.ExamineDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ExamineDetail record);

    int insertSelective(ExamineDetail record);

    ExamineDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ExamineDetail record);

    int updateByPrimaryKey(ExamineDetail record);

    List<ExamineDetail> selectByExamineId(String examineTypeId);
}