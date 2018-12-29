package com.dhht.dao;

import com.dhht.model.MakeDepartmentAttachInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MakeDepartmentAttachInfoMapper {

    int insert(MakeDepartmentAttachInfo record);

    int updateById(MakeDepartmentAttachInfo record);

    int deleteByMakeDepartmentFlag(@Param("makeDepartmentFlag") String makeDepartmentFlag);

    MakeDepartmentAttachInfo selectById(@Param("id") String id);

    MakeDepartmentAttachInfo selectByMakeDepartmentFlag(@Param("makeDepartmentFlag") String makeDepartmentFlag);



}