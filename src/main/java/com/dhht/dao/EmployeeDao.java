package com.dhht.dao;

import com.dhht.model.Employee;

import java.util.List;

import com.dhht.model.MakeDepartmentSimple;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao {

    int delete(Employee employee);

    int deleteById(@Param("id") String id);

    int selectCountEmployeeId(@Param("employeeId") String employeeId);

    List<Employee> selectAllEmployee(@Param("code") String code,@Param("name") String name);

    List<Employee> selectWorkEmployee(@Param("code") String code,@Param("name") String name);

    List<Employee> selectDeleteEmployee(@Param("code") String code,@Param("name") String name);

    int insert(Employee employee);

    Employee selectById(@Param("id") String id);

    int deleteByDepartmentCode(@Param("employeeDepartmentCode") String code);

    int update(Employee record);

    String selectMaxEmployeeCode(@Param("code") String code);

    //查询某个制作单位下的从业人员
    List<Employee> selectByDepartmentCode(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    List<Employee> operationByDepartmentCode(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    List<Employee> selectHistory(@Param("flag") String flag);

    List<Employee> selectWorkEmployee(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    Employee selectByPhone(@Param("phone") String phone);

    Employee selectByEmployeeId(@Param("employeeId") String employeeId);

    Employee selectByName(@Param("name") String name);

    Employee selectByCode(@Param("code") String code);

    int updateHeadById(@Param("id") String id,@Param("employeeImage") String image);

    int updateMakeDepartment(@Param("id") String id,@Param("employeeDepartmentCode") String employeeDepartmentCode);

    List<Employee> selectAllByDepartmentCode(@Param("employeeDepartmentCode") String employeeDepartmentCode);

    List<Employee> selectEmployeeInfo(@Param("name") String name, @Param("status") int status, @Param("list") List<MakeDepartmentSimple> departmentSimples);

    List<Employee> selectAllEmployeeInfo(@Param("name") String name,@Param("list") List<MakeDepartmentSimple> departmentSimples);

    //---------------统计部分--------------------//
    int indexCountAdd(@Param("districtId") String districtId);

    int indexCountDel(@Param("districtId") String districtId);

    int countAllEmployee(@Param("districtId") String districtId);

    int countWorkEmployee(@Param("districtId") String districtId);

    int countDelEmployee(@Param("districtId") String districtId);

    int countAddByStartTime(@Param("districtId") String districtId ,@Param("startTime") String startTime );

    int countAddByEndTime(@Param("districtId") String districtId ,@Param("endTime") String endTime );

    int countAddByTime(@Param("districtId") String districtId ,@Param("startTime") String startTime, @Param("endTime") String endTime);

    int countDelByStartTime(@Param("districtId") String districtId ,@Param("startTime") String startTime );

    int countDelByEndTime(@Param("districtId") String districtId ,@Param("endTime") String endTime );

    int countDelByTime(@Param("districtId") String districtId ,@Param("startTime") String startTime, @Param("endTime") String endTime);

}