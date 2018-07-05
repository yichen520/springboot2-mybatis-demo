package com.dhht.dao;

import com.dhht.model.Employee;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao {

    int delete(Employee employee);

    int deleteById(@Param("id") String id);

    int selectCountEmployeeCode(@Param("employeeCode") String employeeCode);

    int insert(Employee employee);

    Employee selectById(@Param("id") String id);


    int update(Employee record);

    //查询某个制作单位下的从业人员
    List<Employee> selectByDepartmentCode(@Param("employeeDepartmentCode") String employeeDepartmentCode);
}