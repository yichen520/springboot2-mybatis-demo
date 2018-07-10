package com.dhht.service.employee.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.Employee;

import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;

import com.dhht.service.employee.EmployeeService;

import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private RecordDepartmentService recordDepartmentService;


    /**
     * 添加从业人员
     * @param employee
     * @return
     */
    @Override
    public int insertEmployee(Employee employee,User user) {
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(user.getTelphone());
        RecordDepartment recordDepartment = recordDepartmentService.selectByDistrictId(user.getDistrictId()).get(0);
        employee.setEmployeeCode(CodeUtil.generate());
        employee.setId(UUIDUtil.generate());
        employee.setDistrictId(user.getDistrictId());
        employee.setEmployeeDepartmentCode(makeDepartmentSimple.getDepartmentCode());
        employee.setOfficeCode(recordDepartment.getDepartmentCode());
        employee.setOfficeName(recordDepartment.getDepartmentName());
        employee.setRegisterName(recordDepartment.getPrincipalName());
        employee.setRegisterTime(DateUtil.getCurrentTime());
        employee.setFlag(UUIDUtil.generate());
        employee.setVersion(1);
        employee.setVersionTime(DateUtil.getCurrentTime());
        if(isInsert(employee.getEmployeeId())){
            return ResultUtil.isWrongId;
        }
        int u = userService.insert(setUserByType(employee,1));
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

    /**
     * 更新从业人员
     * @param map
     * @return
     */
    @Override
    public int updateEmployee(Map map) {
        Employee employee = employeeDao.selectById((String) map.get("id"));
        int d = employeeDao.deleteById(employee.getId());
        if (d == 0) {
            return ResultUtil.isError;
        }
        employee.setEmployeeName((String)map.get("employeeName"));
        employee.setEmployeeId((String)map.get("employeeId"));
        employee.setEmployeeJob((String)map.get("employeeJob"));
        employee.setEmployeeNation((String)map.get("employeeNation"));
        employee.setFamilyAddressDetail((String)map.get("familyAddressDetail"));
        employee.setFamilyAddressDetail((String)map.get("nowAddressDetail"));
        employee.setEmployeeImage((String)map.get("employeeImage"));
        employee.setTelphone((String)map.get("telphone"));
        employee.setContactName((String)map.get("contactName"));
        employee.setContactTelphone((String)map.get("contactTelphone"));
        employee.setFlag(employee.getFlag());
        employee.setVersion(employee.getVersion()+1);
        employee.setVersionTime(DateUtil.getCurrentTime());
        employee.setId(UUIDUtil.generate());
        if(isInsert(employee.getEmployeeId())){
            return ResultUtil.isWrongId;
        }
        int u = userService.update(setUserByType(employee,2));
        int e = employeeDao.insert(employee);
        if(u==2&&e==1){
            return ResultUtil.isSuccess;
        }else if(u==1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isFail;
        }
    }




    /**
     * 删除从业人员
     * @param id
     * @return
     */
    @Override
    public int deleteEmployee(String id) {
       Employee employee = employeeDao.selectById(id);
       employee.setId(UUIDUtil.generate());
       employee.setLogoutOfficeCode(employee.getOfficeCode());
       employee.setOfficeName(employee.getOfficeName());
       employee.setLogoutName(employee.getRegisterName());
       employee.setVersion(employee.getVersion()+1);
       employee.setVersionTime(DateUtil.getCurrentTime());
       User user = setUserByType(employee,3);
       if(user==null){
           int d = employeeDao.deleteById(id);
           int e = employeeDao.delete(employee);
           if(e==1&&d==1){
               return ResultUtil.isSuccess;
           }else {
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
               return ResultUtil.isFail;
           }
       }else {
           int u = userService.deleteByTelphone(employee.getTelphone());
           int d = employeeDao.deleteById(id);
           int e = employeeDao.delete(employee);
           if (u == 1 && e == 1&&d==1) {
               return ResultUtil.isSuccess;
           } else {
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
               return ResultUtil.isFail;
           }
       }
    }

    /**
     * 根据Id查找从业人员
     * @param id
     * @return
     */
    @Override
    public Employee selectEmployeeByID(String id) {
        return employeeDao.selectById(id);
    }

    /**
     *历史记录
     * @param flag
     * @return
     */
    @Override
    public List<Employee> seletHistory(String flag) {
        return employeeDao.selectHistory(flag);
    }

    /**
     * 根据制作单位删除
     * @param code
     * @return
     */
    @Override
    public int deleteByDepartCode(String code) {
        return 0;
    }

    /**
     * 其他service 调用的更新方法
     * @param employee
     * @return
     */
    @Override
    public int update(Employee employee) {
        return employeeDao.update(employee);
    }

    /**
     * 查询某制作单位下在职的从业人员
     * @param employeeDepartmentCode
     * @return
     */
    @Override
    public List<Employee> selectByDepartmentCode(String employeeDepartmentCode) {
        List<Employee> employees = employeeDao.selectByDepartmentCode(employeeDepartmentCode);
        return employees;
    }

    /**
     * 查询制作单位下所有的从业人员
     * @return
     */
    @Override
    public List<Employee> selectAllEmployee(String code,String name) {
        return employeeDao.selectAllEmployee(code,name);
    }


    /**
     * 查询离职的从业人员
     * @return
     */
    @Override
    public List<Employee> selectDeleteEmployee(String code,String name) {
        return employeeDao.selectDeleteEmployee(code,name);
    }

    /**
     * 查询制作单位在职人员
     * @param code
     * @param name
     * @return
     */
    @Override
    public List<Employee> selectWorkEmployee(String code, String name) {
        return employeeDao.selectWorkEmployee(code,name);
    }

    /**
     * 根据电话获取从业人员信息
     * @param phone
     * @return
     */
    @Override
    public Employee selectByPhone(String phone) {
        return employeeDao.selectByPhone(phone);
    }

    /**
     * 存储图像上传的URL路径
     * @param id
     * @param image
     * @return
     */
    @Override
    public int updateHeadById(String id, String image) {
        int e = employeeDao.updateHeadById(id,image);
        if(e==1){
            return ResultUtil.isSuccess;
        }else {
            return ResultUtil.isUploadFail;
        }
    }


    /**
     * 设置用户
     * @param employee
     * @param type
     * @return
     */
    public User setUserByType(Employee employee, int type){
        User user = new User();
        switch (type){
            //新增用户
            case 1:
                user.setUserName("YG"+employee.getTelphone());
                user.setRealName(employee.getEmployeeName());
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getDistrictId());
                user.setRoleId("CYRY");
                break;
            //修改用户
            case 2:
                Employee oldDate = employeeDao.selectById(employee.getId());
                user = userService.findByTelphone(oldDate.getTelphone());
                user.setRealName("YG"+employee.getEmployeeName());
                user.setTelphone(employee.getTelphone());
                user.setDistrictId(employee.getDistrictId());
                break;
            case 3:
                user = userService.findByTelphone(employee.getTelphone());
        }
        return user;
    }

    /**
     * 判断身份证号是否是否重复
     * @param employeeId
     * @return
     */
    public boolean isInsert(String employeeId){
        if(employeeDao.selectCountEmployeeId(employeeId)>0){
            return true;
        }
        return false;
    }
}
