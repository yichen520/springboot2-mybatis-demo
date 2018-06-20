package com.dhht.service.District;

import com.dhht.model.District;
import com.dhht.model.DistrictMenus;

import java.util.List;

public interface DistrictService {
    //查询所有地区
    List<DistrictMenus> selectAllDistrict();

    //查询指定区域
    List<DistrictMenus> selectOneDistrict(String id);
}
