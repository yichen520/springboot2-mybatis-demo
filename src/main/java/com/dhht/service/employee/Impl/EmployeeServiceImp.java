package com.dhht.service.employee.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.Employee;

import com.dhht.model.User;

import com.dhht.service.employee.EmployeeService;

import com.dhht.service.user.UserService;
import com.dhht.util.DateUtil;
import com.dhht.util.MD5Util;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 2018/7/2 create by fyc
 */
@Service(value = "EmployeeService")
@Transactional
public class EmployeeServiceImp implements EmployeeService {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:hh:ss");
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserService userService;

    /**
     * 查询某制作单位下的从业人员
     * @param employeeDepartmentCode
     * @return
     */
    @Override
    public List<Employee> selectByDepartmentCode(String employeeDepartmentCode) {
        List<Employee> employees = employeeDao.selectByDepartmentCode(employeeDepartmentCode);
        return employees;
    }

    @Override
    public int insertEmployee(Employee employee) {
        if(isInsert(employee.getEmployeeCode())){
            return ResultUtil.isHave;
        }
        int u = userService.insert(setUserByType(employee,1));
        employee.setId(UUIDUtil.generate());
        employee.setRegisterTime(DateUtil.getCurrentTime());
        employee.setFlag(UUIDUtil.generate());
        employee.setVersion(1);
        employee.setVersionTime(DateUtil.getCurrentTime());
        int e = employeeDao.insert(employee);

        if(u+e==3){
            return ResultUtil.isSuccess;
        }else if(u==1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isFail;

        }

    }

    @Override
    public int updateEmployee(Employee employee) {
        return 0;
    }

    @Override
    public int deleteEmployee(String id) {
        return 0;
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
                user.setUserName("YG"+employee.getTelphone());
                user.setRealName(employee.getEmployeeName());
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getNowAddress());
                user.setRoleId("CYRY");
                break;
            //修改用户
            case 2:
               /* user = userDao.findByUserName(employee.getEmployeeCode());
                user.setRealName(employee.getEmployeeName());
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getFamilyAddress());
                break;*/
            //删除用户
            case 3:
               /*user = userDao.findByUserName(employee.getEmployeeCode());
               user.setIsDeleted(true);
               break;*/
        }
        return user;
    }

    public boolean isInsert(String code){
        if(employeeDao.selectCountEmployeeCode(code)>0){
            return true;
        }
        return false;
    }
}
