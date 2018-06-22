package com.dhht.service.police;

import com.dhht.model.RecordPolice;
import com.github.pagehelper.PageInfo;

public interface PoliceService {

    PageInfo<RecordPolice> selectAllPolice(int Sum, int Num);

    PageInfo<RecordPolice> selectByOfficeCode(String code, int Sum, int Num);

    boolean deleteByPrimaryKey(String key);

    int insert(RecordPolice record);

    RecordPolice selectByPrimaryKey(String key);

    int updateByPrimaryKey(RecordPolice record);
}
