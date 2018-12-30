package com.dhht.service.employee;

import com.dhht.model.Employee;
import com.dhht.model.User;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

   // PageInfo<Employee> selectAllEmployee(int pageSum, int pageNum);
    List<Employee> selectAllByDepartmentCode(String employeeDepartmentCode);

    List<Employee> selectByDepartmentCode(String employeeDepartmentCode);

    int insertEmployee(Employee employee, User user);

   // int insertEmployeeImport(Employee employee);

    int updateEmployee(Employee employee,User user);

    int deleteEmployee(String id,User user);

    Employee selectEmployeeByID(String id);

    Employee selectEmployeeByEmployeeID(String employeeId);

    List<Employee> seletHistory(String flag);

    int deleteByDepartCode(String code);

    int update(Employee employee);

    List<Employee> selectAllEmployee(String code,String name);

    List<Employee> selectDeleteEmployee(String code,String name);

    List<Employee> selectWorkEmployee(String code,String name);

    Employee selectByPhone(String phone);

    int updateHeadById(String id,String image,User updateUser);

    List<Employee> operationByDepartmentCode(String code);

    int updateMakeDepartment(String id,String district);

    PageInfo selectEmployeeInfo(String code,int status,String name,int pageNum,int pageSize);

    String selectMaxEmployeeCode(String code);


}