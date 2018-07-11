package com.dhht.service.make.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;
import com.dhht.util.*;
import com.dhht.service.make.MakeDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dhht.service.user.impl.UserServiceImpl.createRandomVcode;


/**
 * 2018/7/2 create by fyc
 */
@Service(value = "makeDepartmentService")
@Transactional
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;


    /**
     * 展示制作单位列表
     * @param districtId
     * @param name
     * @param status
     * @return
     */
    @Override
    public List<MakeDepartmentSimple> selectInfo(String districtId, String name, String status) {
        String did = StringUtil.getDistrictId(districtId);
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectInfo(did,status,name);
        return list;
    }

    /**
     * 根据Id查询详细的制作单位资料
     * @param id
     * @return
     */
    @Override
    public Makedepartment selectDetailById(String id) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        return makedepartment;
    }

    /**
     * 添加制作单位
     * @param makedepartment
     * @return
     */
    @Override
    public int insert(Makedepartment makedepartment) {
        if(isInsert(makedepartment)){
            return 3;
        }
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        makedepartment.setFlag(UUIDUtil.generate());
        makedepartment.setVersion(1);
        makedepartment.setRegisterTime(DateUtil.getCurrentTime());
        User user =setUserByType(makedepartment,1);
        int m = makedepartmentMapper.insert(makedepartment);
        int u = userService.insert(setUserByType(makedepartment,1));
        if(m+u==3){
            return ResultUtil.isSuccess;
        }else if(m==1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isError;
        }
    }

    /**
     * 更新制作单位
     * @param makedepartment
     * @return
     */
    @Override
    public int update(Makedepartment makedepartment) {
        Makedepartment oldDate = makedepartmentMapper.selectDetailById(makedepartment.getId());
        List<Employee> employees = employeeService.selectByDepartmentCode(oldDate.getDepartmentCode());
        int d = makedepartmentMapper.deleteHistoryByID(makedepartment.getId());
        if(d==0){
            return 5;
        }
        if(isInsert(makedepartment)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 3;
        }
        User user =setUserByType(makedepartment,2);
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        makedepartment.setFlag(makedepartment.getFlag());
        makedepartment.setVersion(makedepartment.getVersion()+1);
        makedepartment.setRegisterTime(oldDate.getRegisterTime());
        int m = makedepartmentMapper.insert(makedepartment);
        int e = setEmployeeByDepartment(employees,2);
        int u = userService.update(setUserByType(makedepartment,2));
        if(m==1&&u==2&&e==2){
            return ResultUtil.isSuccess;
        }else if(u==1) {
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 2;
        }
    }

    /**
     * 删除制作单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        List<Employee> employees = employeeService.selectByDepartmentCode(makedepartment.getDepartmentCode());
        makedepartment.setVersion(makedepartment.getVersion()+1);
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        if(setUserByType(makedepartment,3)==null){
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e =setEmployeeByDepartment(employees,1);
            if(m==1&&e==2){
                return ResultUtil.isSuccess;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
        }else {
            int u = userService.deleteByTelphone(makedepartment.getLegalTelphone());
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e = setEmployeeByDepartment(employees,1);
            if (u ==1&&m==1&&e==2) {
                return ResultUtil.isSuccess;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }
    }

    /**
     * 查询历史
     * @param flag
     * @return
     */
    @Override
    public List<Makedepartment> selectHistory(String flag) {
        List<Makedepartment> makedepartments = makedepartmentMapper.selectByFlag(flag);
        return makedepartments;
    }

    /**
     * 根据法人电话获取制作单位
     * @param phone
     * @return
     */
    @Override
    public MakeDepartmentSimple selectByLegalTephone(String phone) {
        return makedepartmentMapper.selectByLegalTephone(phone);
    }

    /**
     * 根据制作单位的编号查询
     * @param code
     * @return
     */
    @Override
    public MakeDepartmentSimple selectByDepartmentCode(String code) {
        return makedepartmentMapper.selectByDepartmentCode(code);
    }

    /**
     * 根据法人手机号获取备案单位的编号
     * @param phone
     * @return
     */
    @Override
    public String selectCodeByLegalTelphone(String phone) {
        return makedepartmentMapper.selectCodeByLegalTelphone(phone);
    }

    /**
     * 设置user
     * @param makedepartment
     * @param type
     * @return
     */
    public User setUserByType(Makedepartment makedepartment,int type){
        User user = new User();
        switch (type){
            case 1:
                user.setId(UUIDUtil.generate());
                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
                user.setRoleId("ZZDW");
                user.setDistrictId(makedepartment.getDepartmentAddress());
                user.setRealName(makedepartment.getDepartmentName());
                user.setTelphone(makedepartment.getLegalTelphone());
                break;
            case 2:
                Makedepartment oldDate =makedepartmentMapper.selectDetailById(makedepartment.getId());
                user = userService.findByTelphone(oldDate.getLegalTelphone());
                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
                user.setDistrictId(makedepartment.getDepartmentAddress());
                user.setRealName(makedepartment.getDepartmentName());
                user.setTelphone(makedepartment.getLegalTelphone());
                break;
            case 3:
                user = userService.findByTelphone(makedepartment.getLegalTelphone());
                break;
        }
        return user;
    }

    /**
     * 判断是否有重复Code
     * @param makedepartment
     * @return
     */
    public boolean isInsert(Makedepartment makedepartment){
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectByCode(makedepartment);
        if(list.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 对备案单位操作时同时操作从业人员
     * @param employees
     * @return
     */
    public int setEmployeeByDepartment(List<Employee> employees,int type) {
        switch (type) {
            //执行删除
            case 1:
                for (Employee emp : employees) {
                    int e = employeeService.deleteEmployee(emp.getId());
                    if (e != 2) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResultUtil.isError;
                    }
                }
                return ResultUtil.isSuccess;
            //执行修改
            case 2:
                for(Employee emp : employees){
                    int e = employeeService.update(emp);
                    if(e==0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResultUtil.isError;
                    }
                }
                return ResultUtil.isSuccess;
        }
        return ResultUtil.isSuccess;
    }

}
