package com.dhht.dao;

import com.dhht.model.District;
import com.dhht.model.DistrictExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictMapper {
    int countByExample(DistrictExample example);

    int deleteByExample(DistrictExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(District record);

    int insertSelective(District district);

    List<District> selectByExample(DistrictExample example);

    District selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") District record, @Param("example") DistrictExample example);

    int updateByExample(@Param("record") District record, @Param("example") DistrictExample example);

    int updateByPrimaryKeySelective(District record);

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

}