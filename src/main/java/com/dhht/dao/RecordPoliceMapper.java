package com.dhht.dao;

import com.dhht.model.RecordPolice;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RecordPoliceMapper {

    int deleteByTelphone(String telphone);

    int insert(RecordPolice record);

    RecordPolice selectByPoliceCode(String policeCode);

    int updateByPrimaryKey(RecordPolice record);

    List<RecordPolice> selectByOfficeCode(String officeCode);

    List<RecordPolice> selectAllPolice();

    List<RecordPolice> selectByRole(@Param("officeDistrict") String officeDistrict);


    RecordPolice selectById(String id);

    RecordPolice selectByTelphone(String telphone);

}