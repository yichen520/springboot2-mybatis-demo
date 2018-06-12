package com.dhht.service.Employee.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.Employee;
import com.dhht.model.Users;
import com.dhht.service.Employee.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    public boolean insertEmployee(Employee employee) {
       // return employeeDao.insert(employee);
        return false;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        //return employeeDao.updateByPrimaryKey(employee);
        return false;
    }

    @Override
    public boolean deleteEmployee(Employee employee) {
        Date date = new Date();
        employee.setLogoutTime(date);
        int e = employeeDao.updateByPrimaryKey(employee);
        int u = userDao.update(setUserByType(employee,3));
        if (e+u==2){
            return true;
        }else {
            return false;
        }
    }

    public Users setUserByType(Employee employee,int type){
        Users users = new Users();
        switch (type){
            //新增用户
            case 1:
                break;
            //修改用户
            case 2:
                break;
            //删除用户
            case 3:
               users = userDao.findByUserName(employee.getEmployeeCode());
               users.setDeleted(true);
               break;
        }
        return users;
    }
}
