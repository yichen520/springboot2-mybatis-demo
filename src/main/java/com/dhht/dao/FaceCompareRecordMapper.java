package com.dhht.dao;

import com.dhht.model.FaceCompareRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceCompareRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(FaceCompareRecord record);

    int insertSelective(FaceCompareRecord record);

    FaceCompareRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FaceCompareRecord record);

    int updateByPrimaryKey(FaceCompareRecord record);
}