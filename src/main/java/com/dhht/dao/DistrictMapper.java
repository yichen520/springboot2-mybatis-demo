package com.dhht.dao;

import com.dhht.model.District;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(District record);

    int updateByPrimaryKey(District record);

    District getByNo(String districtId);

    District getById(String districtId);

    //查询所有的地区信息
    List<District> selectAllDistrict();

    //查询全部Id
    List<District> selectAllById(@Param("districtId") String districtId);

    //添加
    int insertCity(@Param("districtId") String districtId,@Param("districtName") String districtName);

    //查找区域下所有的省市
    List<District> findByDistrictId(District district);

    //删除
    int delete(District district);

    //查找指定Id的区域
    List<District> selectById(@Param("id") String id);

}