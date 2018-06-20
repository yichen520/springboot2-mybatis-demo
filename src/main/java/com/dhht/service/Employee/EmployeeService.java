package com.dhht.service.Employee;

import com.dhht.model.Employee;
import com.github.pagehelper.PageInfo;

public interface EmployeeService {

    PageInfo<Employee> selectAllEmployee(int pageSum, int pageNum);

    boolean insertEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(Employee employee);

    Employee selectEmployeeByID(String employeeCode);
}