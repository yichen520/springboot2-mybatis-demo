package com.dhht.dao;

import com.dhht.model.RecordPolice;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RecordPoliceMapper {

    int deleteByPrimaryKey(String id);

    int insert(RecordPolice record);

    RecordPolice selectByPrimaryKey(String key);

    int updateByPrimaryKey(RecordPolice record);

    List<RecordPolice> selectByOfficeCode(String officeCode);

    List<RecordPolice> selectAllPolice();
}