package com.dhht.dao;

import com.dhht.model.ExamineRecordDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamineRecordDetailMapper {
    int insert(ExamineRecordDetail record);

    int insertSelective(ExamineRecordDetail record);
}