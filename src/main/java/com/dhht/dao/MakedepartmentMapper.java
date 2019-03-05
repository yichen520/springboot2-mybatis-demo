package com.dhht.dao;

import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakedepartmentMapper {

   //---------------------------------------查询模块----------------------//
    List<MakeDepartmentSimple> selectInfo(@Param("districtId") String districtId,@Param("departmentStatus") String departmentStatus,@Param("name") String name);

    List<MakeDepartmentSimple> selectAllInfo(@Param("districtId") String districtId);

    Makedepartment selectDetailById(@Param("id") String id);

    Makedepartment selectDetailByCode(@Param("code") String code);

    Makedepartment selectDetailByName(@Param("name") String name);

    List<MakeDepartmentSimple> selectSimpleByName(@Param("departmentName") String makeDepartmentName,@Param("districtId") String districtId);

    int insert(Makedepartment makedepartment);

    int deleteById(Makedepartment makedepartment);

    int deleteHistoryByID(@Param("id") String id);

    String selectCodeByLegalTelphone(@Param("legalPhone") String phone);

    MakeDepartmentSimple selectByLegalTephone(@Param("legalPhone") String phone);

    MakeDepartmentSimple selectByDepartmentCode(@Param("departmentCode") String code);

    List<Makedepartment> selectByName(@Param("name")String name);

    List<Makedepartment> selectByFlag(@Param("flag") String flag);

    Makedepartment selectByCode1(@Param("departmentCode") String departmentCode);

    List<MakeDepartmentSimple> selectByCode(Makedepartment makedepartment);

    List<MakeDepartmentSimple> selectByDistrict(@Param("id") String id);

    List<MakeDepartmentSimple> selectMeumByDistrict(@Param("id") String id);


    List<Makedepartment> selectByNameAndTimeAndDistrict(@Param("departmentName")String departmentName,String startTime,String endTime,@Param("districtId") String districtId);

    List<Makedepartment> selectByNameOrTimeAndDistrict(@Param("departmentName")String departmentName,String startTime,String endTime,@Param("localDistrictId") String localDistrictId);

    List<Makedepartment> selectByMakeDepartmentName(@Param("departmentName")String departmentName,@Param("departmentCode") String code);
    //------------------------------统计模块-------------------------------------//
    int indexCountAdd (@Param("districtId") String districtId);

    int indexCountDel(@Param("districtId") String districtId);

    int countAllDepartment(@Param("districtId") String districtId);

    int countWorkDepartment(@Param("districtId") String districtId);

    int countDeleteDepartment(@Param("districtId") String districtId);

    int countAddDepartmentByStartTime(@Param("districtId") String districtId,@Param("startTime") String startTime);

    int countAddDepartmentByEndTime(@Param("districtId") String districtId,@Param("endTime") String endTime);

    int countAddDepartmentByTime(@Param("districtId") String districtId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int countDelDepartmentByStartTime(@Param("districtId") String districtId,@Param("startTime") String startTime);

    int countDelDepartmentByEndTime(@Param("districtId") String districtId,@Param("endTime") String endTime);

    int countDelDepartmentByTime(@Param("districtId") String districtId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    List<Makedepartment> makeDepartmentSort(@Param("districtId")String districtId);

 List<Makedepartment> makeDepartmentSortBySealNum(@Param("districtId")String districtId);

 List<MakedepartmentSimplePO> selectMakedePartment(MakedepartmentSimplePO makedepartmentSimplePO);

 List<MakedepartmentSimplePO> selectMakedePartmentByEvaluate(MakedepartmentSimplePO makedepartmentSimplePO);

 List<MakedepartmentSimplePO> selectMakedePartmentBySealNum(MakedepartmentSimplePO makedepartmentSimplePO);

    MakedepartmentSimplePO selectMakedepartmentSimplePODetailById(@Param("id") String id);

    int selectCompany(MakedepartmentSimplePO makedepartmentSimplePO);
}