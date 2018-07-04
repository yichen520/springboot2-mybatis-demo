package com.dhht.service.employee;

import com.dhht.model.Employee;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface EmployeeService {

   // PageInfo<Employee> selectAllEmployee(int pageSum, int pageNum);

    List<Employee> selectByDepartmentCode(String employeeDepartmentCode);

    int insertEmployee(Employee employee);

    int updateEmployee(Employee employee);

    int deleteEmployee(String id);

    Employee selectEmployeeByID(String employeeCode);
}