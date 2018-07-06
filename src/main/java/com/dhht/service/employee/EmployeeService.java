package com.dhht.service.employee;

import com.dhht.model.Employee;
import com.dhht.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

   // PageInfo<Employee> selectAllEmployee(int pageSum, int pageNum);

    List<Employee> selectByDepartmentCode(String employeeDepartmentCode);

    int insertEmployee(Employee employee, User user);

    int updateEmployee(Map map);

    int deleteEmployee(String id);

    Employee selectEmployeeByID(String employeeCode);

    List<Employee> seletHistory(String flag);

    int deleteByDepartCode(String code);

    int update(Employee employee);

    List<Employee> selectAllEmployee();

    List<Employee> selectDeleteEmployee();
}