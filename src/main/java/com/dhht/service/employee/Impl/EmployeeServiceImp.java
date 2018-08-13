package com.dhht.service.employee.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.EmployeeDao;
import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.*;

import com.dhht.service.employee.EmployeeService;

import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;


    /**
     * 添加从业人员
     * @param employee
     * @return
     */
    @Override
    public int insertEmployee(Employee employee,User user) {
        try {
            MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(user.getTelphone());
            List<RecordDepartment> recordDepartments =recordDepartmentService.selectByDistrictId(user.getDistrictId());
            if(recordDepartments.size()==0){
                return ResultUtil.noRecordDepartment;
            }
            RecordDepartment recordDepartment = recordDepartments.get(0);
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
            OperatorRecord operatorRecord = setOperatorRecord(user,employee.getFlag(),employee.getId(),SyncOperateType.SAVE);
            if (isInsert(employee.getEmployeeId())) {
                return ResultUtil.isWrongId;
            }
            int o = operatorRecordMapper.insert(operatorRecord);
            int u = userService.insert(employee.getTelphone(),"CYRY",employee.getEmployeeName(),employee.getDistrictId());
            int e = employeeDao.insert(employee);
            if (u==ResultUtil.isSend&&e==1&&o>0) {
                SyncEntity syncEntity =  ((EmployeeServiceImp) AopContext.currentProxy()).getSyncDate(employee, SyncDataType.EMPLOYEE, SyncOperateType.SAVE);
                return ResultUtil.isSuccess;
            } else if (u == 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.isError;
        }
    }

    /**
     * 更新从业人员
     * @param employee
     * @return
     */
    @Override
    public int updateEmployee(Employee employee,User updateUser) {
        try {
            //Employee employee = employeeDao.selectById((String) map.get("id"));
            Employee oldDate = employeeDao.selectById(employee.getId());
            String oldTelphone = oldDate.getTelphone();
            int d = employeeDao.deleteById(employee.getId());
            if (d == 0) {
                return ResultUtil.isError;
            }
            if (isInsert(employee.getEmployeeId())) {
                return ResultUtil.isWrongId;
            }employee.setEmployeeDepartmentCode(oldDate.getEmployeeDepartmentCode());
            employee.setEmployeeCode(oldDate.getEmployeeCode());
            employee.setOfficeCode(oldDate.getOfficeCode());
            employee.setEmployeeImage(oldDate.getEmployeeImage());
            employee.setOfficeName(oldDate.getOfficeName());
            employee.setRegisterName(oldDate.getRegisterName());
            employee.setRegisterTime(oldDate.getRegisterTime());
            employee.setVersion(employee.getVersion() + 1);
            employee.setVersionTime(DateUtil.getCurrentTime());
            employee.setId(UUIDUtil.generate());
            OperatorRecord operatorRecord = setOperatorRecord(updateUser,employee.getFlag(),employee.getId(),SyncOperateType.UPDATE);
            OperatorRecordDetail operatorRecordDetail = compareData(employee,oldDate,operatorRecord.getId());
            int u = userService.update(oldTelphone,employee.getTelphone(),"CYRY",employee.getEmployeeName(),employee.getDistrictId());
            int e = employeeDao.insert(employee);
            int o = operatorRecordMapper.insert(operatorRecord);
            int od = operatorRecordDetailMapper.insert(operatorRecordDetail);
            if (u == ResultUtil.isSuccess && e == 1&&o>0&&od>0) {
                SyncEntity syncEntity =  ((EmployeeServiceImp) AopContext.currentProxy()).getSyncDate(employee, SyncDataType.EMPLOYEE, SyncOperateType.UPDATE);
                return ResultUtil.isSuccess;
            } else if (u == 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isException;
        }
    }




    /**
     * 删除从业人员
     * @param id
     * @return
     */
    @Override
    public int deleteEmployee(String id,User updateUser) {
       Employee employee = employeeDao.selectById(id);
       User user = userService.findByUserName("CYRY"+employee.getTelphone());
       employee.setId(UUIDUtil.generate());
       employee.setLogoutOfficeCode(employee.getOfficeCode());
       employee.setLogoutOfficeName(employee.getOfficeName());
       employee.setLogoutName(employee.getRegisterName());
       employee.setVersion(employee.getVersion()+1);
       employee.setVersionTime(DateUtil.getCurrentTime());
       OperatorRecord operatorRecord = setOperatorRecord(updateUser,employee.getFlag(),employee.getEmployeeId(),SyncOperateType.DELETE);
       if(user==null){
           int d = employeeDao.deleteById(id);
           int e = employeeDao.delete(employee);
           int o = operatorRecordMapper.insert(operatorRecord);
           if(e==1&&d==1&&o>0){
               SyncEntity syncEntity =  ((EmployeeServiceImp) AopContext.currentProxy()).getSyncDate(employee,SyncDataType.EMPLOYEE,SyncOperateType.DELETE);
               return ResultUtil.isSuccess;
           }else {
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
               return ResultUtil.isFail;
           }
       }else {
           int u = userService.deleteByUserName("CYRY",employee.getTelphone());
           int d = employeeDao.deleteById(id);
           int e = employeeDao.delete(employee);
           int o = operatorRecordMapper.insert(operatorRecord);
           if (u==ResultUtil.isSuccess && e> 0&&d>0&&o>0) {
               SyncEntity syncEntity =  ((EmployeeServiceImp) AopContext.currentProxy()).getSyncDate(employee,SyncDataType.EMPLOYEE,SyncOperateType.DELETE);
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
        employee.setVersion(employee.getVersion()+1);
        employee.setVersionTime(DateUtil.getCurrentTime());
        employee.setId(UUIDUtil.generate());
        return employeeDao.insert(employee);
    }

    /**
     * 获取当前单位下所有版本的员工
     * @param employeeDepartmentCode
     * @return
     */
    @Override
    public List<Employee> selectAllByDepartmentCode(String employeeDepartmentCode) {
        return employeeDao.selectAllByDepartmentCode(employeeDepartmentCode);
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
     * 操作从业人员
     * @param code
     * @return
     */
    @Override
    public List<Employee> operationByDepartmentCode(String code) {
        return employeeDao.operationByDepartmentCode(code);
    }

    /**
     * 更新制作单位的编号
     * @param id
     * @param code
     * @return
     */
    @Override
    public int updateMakeDepartment(String id, String code) {
        return employeeDao.updateMakeDepartment(id,code);
    }

    /**
     * 查询模块查询从业人员信息
     * @param code
     * @param status
     * @param name
     * @return
     */
    @Override
    public PageInfo selectEmployeeInfo(String code, int status, String name, int pageNum,int pageSize) {
        List<Employee> employees = new ArrayList<>();

        if(code.length()==6){
            List<MakeDepartmentSimple> makedepartments = makeDepartmentService.selectInfo(code,"","01");
            if(makedepartments.size()==0){
                return new PageInfo(employees);
            }
            PageHelper.startPage(pageNum,pageSize);
            if(status==1){
                employees = employeeDao.selectEmployeeInfo(name,0,makedepartments);
            }else if(status==2){
                employees = employeeDao.selectEmployeeInfo(name,1,makedepartments);
            }else {
                employees = employeeDao.selectAllEmployeeInfo(name,makedepartments);
            }
        }else {
            if(status==1){
                employees = employeeDao.selectWorkEmployee(code,name);
            }else if(status==2){
                employees = employeeDao.selectDeleteEmployee(code,name);
            }else {
                employees = employeeDao.selectAllByDepartmentCode(code);
            }
        }
        PageInfo pageInfo = new PageInfo(employees);
        return pageInfo;
    }

    /**
     * 数据操作类的设定
     * @return
     */
    public OperatorRecord setOperatorRecord(User user,String flag,String uuid,int type){
        OperatorRecord operatorRecord = new OperatorRecord();
        operatorRecord.setFlag(flag);
        operatorRecord.setId(UUIDUtil.generate());
        operatorRecord.setOperateUserId(user.getId());
        operatorRecord.setOperateUserRealname(user.getRealName());
        operatorRecord.setOperateEntityId(uuid);
        operatorRecord.setOperateEntityName("Employee");
        operatorRecord.setOperateType(type);
        operatorRecord.setOperateTypeName(SyncOperateType.getOperateTypeName(type));
        operatorRecord.setOperateTime(DateUtil.getCurrentTime());
        return operatorRecord;
    }

    /**
     * 修改时比较数据
     * @param newData
     * @param oldDate
     * @param operatorRecordId
     * @return
     */
    public OperatorRecordDetail compareData(Employee newData,Employee oldDate,String operatorRecordId) {
        String ignore[] = new String[]{"id", "employeeDepartmentCode", "version", "flag", "versionTime", "registerTime","versionStatus","effectiveTime"};
        Map<String, List<Object>> compareResult = CompareFieldsUtil.compareFields(oldDate, newData, ignore);
        Set<String> keySet = compareResult.keySet();
        OperatorRecordDetail operatorRecordDetail = new OperatorRecordDetail();
        if(keySet.size()==0){
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            operatorRecordDetail.setPropertyName("无任何数据修改！");
            return operatorRecordDetail;
        }
        for (String key : keySet) {
            List<Object> list = compareResult.get(key);
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            if(list.get(1)==null||list.get(1)==""||list.get(1).toString()==null||list.get(1).toString()==""){
                operatorRecordDetail.setNewValue("未填写任何值！");
            }else {
                operatorRecordDetail.setNewValue(list.get(1).toString());
            }
            if(list.get(0)==null||list.get(0)==""||list.get(0).toString()==null||list.get(0).toString()==""){
                operatorRecordDetail.setOldValue("未填写任何值！");
            }else {
                operatorRecordDetail.setOldValue(list.get(0).toString());
            }
            operatorRecordDetail.setPropertyName(key);
        }
        return operatorRecordDetail;
    }


//    /**
//     * 设置用户
//     * @param employee
//     * @param type
//     * @return
//     */
//    public User setUserByType(Employee employee, int type){
//        User user = new User();
//        switch (type){
//            //新增用户
//            case 1:
//                user.setUserName("CYRY"+employee.getTelphone());
//                user.setRealName(employee.getEmployeeName());
//                user.setTelphone(employee.getTelphone());
//                user.setDistrictId(employee.getDistrictId());
//                user.setRoleId("CYRY");
//                break;
//            //修改用户
//            case 2:
//                Employee oldDate = employeeDao.selectById(employee.getId());
//                user = userService.findByUserName("CYRY"+oldDate.getTelphone());
//                user.setUserName("CYRY"+employee.getTelphone());
//                user.setRealName(employee.getEmployeeName());
//                user.setTelphone(employee.getTelphone());
//                user.setDistrictId(employee.getDistrictId());
//                break;
//            case 3:
//                user = userService.findByUserName("CYRY"+employee.getTelphone());
//        }
//        return user;
//    }

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


    /**
     * 数据同步
     * @param object
     * @param dataType
     * @param operateType
     * @return
     */
    @Sync()
     public SyncEntity getSyncDate(Object object,int dataType,int operateType){
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
     }
}
