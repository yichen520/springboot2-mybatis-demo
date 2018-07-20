package com.dhht.dao;

import com.dhht.model.ExamineRecordDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineRecordDetailMapper {
    int insert(ExamineRecordDetail record);

    int insertSelective(ExamineRecordDetail record);

    List<ExamineRecordDetail> selectExamineDetailByID(@Param("id") String id);
}