package com.dhht.service.Employee.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.Employee;
import com.dhht.model.User;
import com.dhht.service.Employee.EmployeeService;
import com.dhht.util.DateUtil;
import com.dhht.util.MD5Util;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Service(value = "EmployeeService")
@Transactional
public class EmployeeServiceImp implements EmployeeService {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:hh:ss");
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<Employee> selectAllEmployee(int pageSum,int pageNum ) {
        List<Employee> employees = employeeDao.selectAllEmployee();
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<Employee> result = new PageInfo(employees);
        return result;
    }

    @Override
    public PageInfo<Employee> selectByDepartmentCode(int pageSum, int pageNum, String employeeDepartmentCode) {
        List<Employee> employees = employeeDao.selectByDepartmentCode(employeeDepartmentCode);
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<Employee> result = new PageInfo(employees);
        return result;
    }

    @Override
    public boolean insertEmployee(Employee employee) {
        employee.setRegisterTime(DateUtil.getCurrentTime());
        int e = employeeDao.insert(employee);
        int u = userDao.addUser(setUserByType(employee,1));
        if(e+u==2){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        int e = employeeDao.updateByPrimaryKey(employee);
        int u = userDao.update(setUserByType(employee,1));
        if(e+u==2){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(Employee employee) {
        employee.setLogoutTime(DateUtil.getCurrentTime());
        int e = employeeDao.updateByPrimaryKey(employee);
        int u = userDao.update(setUserByType(employee,3));
        if (e+u==2){
            return true;
        }
        return false;
    }

    @Override
    public Employee selectEmployeeByID(String employeeCode) {

        return employeeDao.selectByPrimaryKey(employeeCode);
    }

    public User setUserByType(Employee employee, int type){
        User user = new User();
        switch (type){
            //新增用户
            case 1:
                user.setId(UUIDUtil.generate());
                user.setUserName(employee.getEmployeeCode());
                user.setRealName(employee.getEmployeeName());
                user.setPassword(MD5Util.toMd5("123456"));
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getFamilyAddress());
                user.setRoleId("CYRY");
                break;
            //修改用户
            case 2:
                user = userDao.findByUserName(employee.getEmployeeCode());
                user.setRealName(employee.getEmployeeName());
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getFamilyAddress());
                break;
            //删除用户
            case 3:
               user = userDao.findByUserName(employee.getEmployeeCode());
               user.setDeleted(true);
               break;
        }
        return user;
    }
}
