package com.dhht.service.District;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.District;
import com.dhht.model.DistrictMenus;

import java.util.List;

public interface DistrictService {
    //查询所有地区
    List<DistrictMenus> selectAllDistrict();

    //查询指定区域
    List<DistrictMenus> selectOneDistrict(String id);

    //增加区域
    JsonObjectBO insert(String districtId, String parentId, String districtName);

    //删除
    JsonObjectBO delete(String districtId);

    //生成区域下带制作单位单位列表
    List<DistrictMenus> selectMakeDepartmentMenus(String id);

    //选择多区域生成区域列表
    List<DistrictMenus> selectDistrictByArray(List<String> DistrictIds);

    List<DistrictMenus> selectDistrictMakeDepartmentByArray(List<String> DistrictIds);

    String selectByDistrictId(String districtId);
}
