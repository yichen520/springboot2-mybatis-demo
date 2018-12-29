package com.dhht.dao;

import com.dhht.model.MakeDepartmentSealPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeDepartmentSealPriceMapper {
    int deleteById(@Param("id") String id);

    int insert(MakeDepartmentSealPrice makeDepartmentSealPrice);

    int updateById(MakeDepartmentSealPrice makeDepartmentSealPrice);

    int deleteByMakeDepartmentFlag(@Param("makeDepartmentFlag") String makeDepartmentFlag);

    MakeDepartmentSealPrice selectById(@Param("makeDepartmentFlag") String id);

    List<MakeDepartmentSealPrice> selectByMakeDepartmentFlag(@Param("makeDepartmentFlag") String makeDepartmentFlag);
}