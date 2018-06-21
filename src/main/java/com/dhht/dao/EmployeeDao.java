package com.dhht.dao;

import com.dhht.model.Employee;
import com.dhht.model.EmployeeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao {
    int countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(String employeeCode);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(String employeeCode);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    //查询所有的从业人员
    List<Employee> selectAllEmployee();

    //查询某个制作单位下的从业人员
    List<Employee> selectByDepartmentCode(String employeeDepartmentCode);
}