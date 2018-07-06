package com.dhht.dao;

import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakedepartmentMapper {


    List<MakeDepartmentSimple> selectByDistrictId(@Param("id") String id);

    Makedepartment selectDetailById(@Param("id") String id);

    int insert(Makedepartment makedepartment);

    int deleteById(Makedepartment makedepartment);

    int deleteHistoryByID(@Param("id") String id);

    String selectCodeByLegalTelphone(@Param("legalPhone") String phone);

    MakeDepartmentSimple selectByLegalTephone(@Param("legalPhone") String phone);

    MakeDepartmentSimple selectByDepartmentCode(@Param("departmentCode") String code);

    List<Makedepartment> selectByFlag(@Param("flag") String flag);

    List<MakeDepartmentSimple> selectByCode(Makedepartment makedepartment);

    List<MakeDepartmentSimple> selectByDistrict(@Param("id") String id);
}