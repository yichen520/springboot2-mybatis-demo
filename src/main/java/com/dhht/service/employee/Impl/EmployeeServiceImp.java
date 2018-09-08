package com.dhht.service.employee.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.EmployeeDao;
import com.dhht.model.*;

import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;

import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.HistoryService;
import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.Jedis;

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
    private HistoryService historyService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DistrictService districtService;

    private final static String EMPLOYEE_HEAD_UPLOAD = "从业人员头像上传";


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
            employee.setEmployeeCode(getEmployeeCode(makeDepartmentSimple.getDepartmentCode()));
            employee.setId(UUIDUtil.generate());
            employee.setFamilyDistrictId(employee.getFamilyDistrictIds().get(2));
            employee.setNowDistrictId(employee.getNowDistrictIds().get(2));
            employee.setEmployeeImage("");
            employee.setEmployeeDepartmentCode(makeDepartmentSimple.getDepartmentCode());
            employee.setOfficeCode(recordDepartment.getDepartmentCode());
            employee.setOfficeName(recordDepartment.getDepartmentName());
            employee.setRegisterName(recordDepartment.getPrincipalName());
            employee.setRegisterTime(DateUtil.getCurrentTime());
            employee.setFlag(UUIDUtil.generate());
            employee.setVersion(1);
            employee.setVersionTime(DateUtil.getCurrentTime());
            String operateUUid = UUIDUtil.generate();
            boolean o = historyService.insertOperateRecord(user,employee.getFlag(),employee.getId(),"employee",SyncOperateType.SAVE,operateUUid);
            if (isRepeatEmployeeId(employee.getEmployeeId())) {
                return ResultUtil.isWrongId;
            }
            int u = userService.insert(employee.getTelphone(),"CYRY",employee.getEmployeeName(),employee.getDistrictId());
            int e = employeeDao.insert(employee);
            if (u==ResultUtil.isSend&&e==1&&o) {
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
            if (isRepeatEmployeeId(employee.getEmployeeId())) {
                return ResultUtil.isWrongId;
            }
            employee.setEmployeeDepartmentCode(oldDate.getEmployeeDepartmentCode());
            if(employee.getFamilyDistrictIds()!=null&&employee.getNowDistrictIds()!=null){
                employee.setFamilyDistrictId(employee.getFamilyDistrictIds().get(2));
                employee.setNowDistrictId(employee.getNowDistrictIds().get(2));
            }else {
                employee.setFamilyDistrictId(updateUser.getDistrictId());
                employee.setNowDistrictId(updateUser.getDistrictId());
            }
            employee.setEmployeeCode(oldDate.getEmployeeCode());
            employee.setOfficeCode(oldDate.getOfficeCode());
            employee.setEmployeeImage(oldDate.getEmployeeImage());
            employee.setOfficeName(oldDate.getOfficeName());
            employee.setRegisterName(oldDate.getRegisterName());
            employee.setRegisterTime(oldDate.getRegisterTime());
            employee.setVersion(employee.getVersion() + 1);
            employee.setVersionTime(DateUtil.getCurrentTime());
            employee.setId(UUIDUtil.generate());
            String ignore[] = new String[]{"id", "employeeDepartmentCode", "version", "flag", "versionTime", "registerTime","versionStatus","effectiveTime","familyDistrictIds","familyDistrictName","nowDistrictIds","nowDistrictName"};
            String operateUUid = UUIDUtil.generate();
            boolean o = historyService.insertOperateRecord(updateUser,employee.getFlag(),employee.getId(),"employee",SyncOperateType.UPDATE,operateUUid);
            boolean od = historyService.insertUpdateRecord(employee,oldDate,operateUUid,ignore);
            int u = userService.update(oldTelphone,employee.getTelphone(),"CYRY",employee.getEmployeeName(),employee.getDistrictId());
            int e = employeeDao.insert(employee);
            if (u == ResultUtil.isSuccess && e == 1&&o&&od) {
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
       String operateUUid = UUIDUtil.generate();

       if(user==null){
           int d = employeeDao.deleteById(id);
           int e = employeeDao.delete(employee);
           boolean o = historyService.insertOperateRecord(updateUser,employee.getFlag(),employee.getEmployeeId(),"employee",SyncOperateType.DELETE,operateUUid);
           if(e==1&&d==1&&o){
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
           boolean o = historyService.insertOperateRecord(updateUser,employee.getFlag(),employee.getEmployeeId(),"employee",SyncOperateType.DELETE,operateUUid);
           if (u==ResultUtil.isSuccess && e> 0&&d>0&&o) {
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
        Employee employee = employeeDao.selectById(id);
        return setEmployeeDistrict(employee);
    }

    @Override
    public Employee selectEmployeeByEmployeeID(String employeeId) {
        Employee employee = employeeDao.selectByEmployeeId(employeeId);
        return employee;
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
        List<Employee> employees =  employeeDao.selectAllByDepartmentCode(employeeDepartmentCode);
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        return employees;
    }

    /**
     * 查询某制作单位下在职的从业人员
     * @param employeeDepartmentCode
     * @return
     */
    @Override
    public List<Employee> selectByDepartmentCode(String employeeDepartmentCode) {
        List<Employee> employees = employeeDao.selectByDepartmentCode(employeeDepartmentCode);
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        return employees;
    }

    /**
     * 查询制作单位下所有的从业人员
     * @return
     */
    @Override
    public List<Employee> selectAllEmployee(String code,String name) {
        List<Employee> employees =  employeeDao.selectAllEmployee(code,name);
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        return employees;
    }


    /**
     * 查询离职的从业人员
     * @return
     */
    @Override
    public List<Employee> selectDeleteEmployee(String code,String name) {
        List<Employee> employees =  employeeDao.selectDeleteEmployee(code,name);
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        return employees;
    }

    /**
     * 查询制作单位在职人员
     * @param code
     * @param name
     * @return
     */
    @Override
    public List<Employee> selectWorkEmployee(String code, String name) {
        List<Employee> employees =  employeeDao.selectWorkEmployee(code,name);
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        return employees;
    }

    /**
     * 根据电话获取从业人员信息
     * @param phone
     * @return
     */
    @Override
    public Employee selectByPhone(String phone) {

        Employee employee =  employeeDao.selectByPhone(phone);
        return setEmployeeDistrict(employee);
    }

    /**
     * 存储图像上传的URL路径
     * @param id
     * @param image
     * @return
     */
    @Override
    public int updateHeadById(String id, String image,User updateUser) {
        Employee employee = employeeDao.selectById(id);
        String operateUUid = UUIDUtil.generate();
        boolean o = historyService.insertOperateRecord(updateUser,employee.getFlag(),employee.getId(),"employee",SyncOperateType.UPDATE,operateUUid);
        OperatorRecordDetail operatorRecordDetail = new OperatorRecordDetail();
        operatorRecordDetail.setId(UUIDUtil.generate());
        operatorRecordDetail.setPropertyType(2);
        operatorRecordDetail.setPropertyName("从业人员头像");
        if(employee.getEmployeeImage()!=null) {
            operatorRecordDetail.setOldValue(employee.getEmployeeImage());
        }
        operatorRecordDetail.setNewValue(image);
        operatorRecordDetail.setEntityOperateRecordId(operateUUid);
        boolean b = historyService.insertOperateDetail(operatorRecordDetail);
        if(employee.getEmployeeImage()!=null){
            fileService.getFileInfo(employee.getEmployeeImage());
        }
        int e = employeeDao.updateHeadById(id,image);
        if(e==1){
            fileService.register(image,EMPLOYEE_HEAD_UPLOAD);
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
        for(Employee employee:employees){
            String familyDistrictId = employee.getFamilyDistrictId();
            employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
            String nowDistrictId = employee.getNowDistrictId();
            employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
            String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
            String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
            employee.setFamilyDistrictName(familyDistrictName);
            employee.setNowDistrictName(nowDistrictName);
        }
        PageInfo pageInfo = new PageInfo(employees);
        return pageInfo;
    }

    public Employee setEmployeeDistrict(Employee employee){
        String familyDistrictId = employee.getFamilyDistrictId();
        employee.setFamilyDistrictIds(StringUtil.getDistrictIds(familyDistrictId));
        String nowDistrictId = employee.getNowDistrictId();
        employee.setNowDistrictIds(StringUtil.getDistrictIds(nowDistrictId));
        String nowDistrictName = districtService.selectByDistrictId(employee.getNowDistrictId());
        String familyDistrictName = districtService.selectByDistrictId(employee.getFamilyDistrictId());
        employee.setFamilyDistrictName(familyDistrictName);
        employee.setNowDistrictName(nowDistrictName);
        return employee;
    }

    /**
     * 查询最大的从业人员你的编号，用于redis
     * @return
     */
    @Override
    public String selectMaxEmployeeCode() {
        try {
            return employeeDao.selectMaxEmployeeCode();
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 判断身份证号是否是否重复
     * @param employeeId
     * @return
     */
    public boolean isRepeatEmployeeId(String employeeId){
        if(employeeDao.selectCountEmployeeId(employeeId)>0){
            return true;
        }
        return false;
    }

    /**
     * 生成从业人员编号
     * @param makeDepartmentCode
     * @return
     */
    public String getEmployeeCode(String makeDepartmentCode){
        String code = "";
        Jedis jedis = new Jedis();
        if(redisTemplate.hasKey("employeeCode")){
            code = jedis.incrBy("employeeCode",1).toString();
        }else{
            redisTemplate.opsForValue().set("employeeCode",0);
            code = redisTemplate.opsForValue().get("employeeCode").toString();
        }
        int length = code.length();
        StringBuffer stringBuffer = new StringBuffer();
        if(length<4){
            for(int i = 0;i<4-length;i++){
                stringBuffer.append("0");
            }
        }
        return makeDepartmentCode+stringBuffer.toString()+code;
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
