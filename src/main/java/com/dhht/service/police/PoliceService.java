package com.dhht.service.police;

import com.dhht.model.RecordPolice;
import com.github.pagehelper.PageInfo;

public interface PoliceService {

    PageInfo<RecordPolice> selectAllPolice(int Sum, int Num);

    PageInfo<RecordPolice> selectByOfficeCode(String code, int Sum, int Num);

    boolean deleteByTelphone(String phone);

    boolean insert(RecordPolice record);

    RecordPolice selectByPoliceCode(String code);

    boolean updateByPrimaryKey(RecordPolice record);

    RecordPolice selectById(String id);
}
