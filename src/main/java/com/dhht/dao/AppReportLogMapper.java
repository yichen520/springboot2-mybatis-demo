package com.dhht.dao;

import com.dhht.model.AppReportLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AppReportLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(AppReportLog record);

    int insertSelective(AppReportLog record);

    AppReportLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AppReportLog record);

    int updateByPrimaryKeyWithBLOBs(AppReportLog record);

    int updateByPrimaryKey(AppReportLog record);
}