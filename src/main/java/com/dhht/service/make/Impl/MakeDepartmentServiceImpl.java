package com.dhht.service.make.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.ExamineRecordDetailMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.OperatorRecordDetailMapper;
import com.dhht.dao.OperatorRecordMapper;
import com.dhht.model.*;
import com.dhht.model.pojo.CommonHistoryVO;
import com.dhht.service.employee.EmployeeService;

import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.dhht.service.make.MakeDepartmentService;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 2018/7/2 create by fyc
 */
@Service(value = "makeDepartmentService")
@Transactional
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private ExamineRecordDetailMapper examineRecordDetailMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private OperatorRecordMapper operatorRecordMapper;
    @Autowired
    private OperatorRecordDetailMapper operatorRecordDetailMapper;

    @Autowired
    private EmployeeService employeeService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

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
        return setFileUrlByType(makedepartment,2);
    }

    /**
     * 添加制作单位
     * @param makedepartment
     * @return
     */
    @Override
    public int insert(Makedepartment makedepartment,User updateUser) {
        if(isInsert(makedepartment)){
            return ResultUtil.isHaveCode;
        }
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        makedepartment.setFlag(UUIDUtil.generate());
        makedepartment.setVersion(1);
        makedepartment.setRegisterTime(DateUtil.getCurrentTime());
        OperatorRecord operatorRecord = setOperatorRecord(updateUser,makedepartment.getFlag(),makedepartment.getId(),SyncOperateType.SAVE);
        int m = makedepartmentMapper.insert(setFileUrlByType(makedepartment,1));
        int u = userService.insert(makedepartment.getLegalTelphone(),"ZZDW",makedepartment.getDepartmentName(),makedepartment.getDepartmentAddress());
        int o = operatorRecordMapper.insert(operatorRecord);
        if(m==1&&u==ResultUtil.isSend&&o>0){
            SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.SAVE);
            return ResultUtil.isSuccess;
        }else if(u==ResultUtil.isHave) {
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
    public int update(Makedepartment makedepartment,User updateUser) {
        try {
            Makedepartment oldDate = makedepartmentMapper.selectDetailById(makedepartment.getId());
            List<Employee> employees = employeeService.selectAllByDepartmentCode(oldDate.getDepartmentCode());
            int d = makedepartmentMapper.deleteHistoryByID(oldDate.getId());
            if (d == 0) {
                return 5;
            }
            //User user = userService.findByUserName("ZZDW"+makedepartment.getLegalTelphone());
            makedepartment.setId(UUIDUtil.generate());
            makedepartment.setVersionTime(DateUtil.getCurrentTime());
            makedepartment.setFlag(makedepartment.getFlag());
            makedepartment.setVersion(makedepartment.getVersion() + 1);
            makedepartment.setRegisterTime(oldDate.getRegisterTime());
            if (isInsert(makedepartment)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHaveCode;
            }
            OperatorRecord operatorRecord = setOperatorRecord(updateUser,makedepartment.getFlag(),makedepartment.getId(),SyncOperateType.UPDATE);
            OperatorRecordDetail operatorRecordDetail = compareData(makedepartment,oldDate,operatorRecord.getId());
            int o = operatorRecordMapper.insert(operatorRecord);
            int od = operatorRecordDetailMapper.insert(operatorRecordDetail);
            int m = makedepartmentMapper.insert(setFileUrlByType(makedepartment,1));
            //int e = setEmployeeByDepartment(employees, makedepartment, 2);
            int u = userService.update(oldDate.getLegalTelphone(),makedepartment.getLegalTelphone(),"ZZDW",makedepartment.getDepartmentName(),makedepartment.getDepartmentAddress());
            if (m == 1 && u == ResultUtil.isSuccess&&o>0&&od>0) {
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.UPDATE);
                return ResultUtil.isSuccess;
            } else if (u == 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            } else {

                return ResultUtil.isError;
            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isException;
        }
    }

    /**
     * 删除制作单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id,User updateUser) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        if(makedepartmentMapper.deleteHistoryByID(id)==0){
            return ResultUtil.isError;
        }
        List<Employee> employees = employeeService.selectAllByDepartmentCode(makedepartment.getDepartmentCode());
        makedepartment.setVersion(makedepartment.getVersion()+1);
        makedepartment.setVersionTime(DateUtil.getCurrentTime());
        User user = userService.findByUserName("ZZDW"+makedepartment.getLegalTelphone());
        OperatorRecord operatorRecord = setOperatorRecord(updateUser,makedepartment.getFlag(),makedepartment.getId(),SyncOperateType.DELETE);
        if(user==null){
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e =setEmployeeByDepartment(employees,makedepartment,updateUser);
            int o = operatorRecordMapper.insert(operatorRecord);
            if(m==1&&e==2&&o>0){
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl) AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.DELETE);
                return ResultUtil.isSuccess;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
        }else {
            int u = userService.delete(user.getId());
            makedepartment.setId(UUIDUtil.generate());
            int m = makedepartmentMapper.deleteById(makedepartment);
            int e = setEmployeeByDepartment(employees,makedepartment,updateUser);
            int o = operatorRecordMapper.insert(operatorRecord);
            if (u ==ResultUtil.isSuccess&&m==1&&e==ResultUtil.isSuccess&&o>0) {
                SyncEntity syncEntity = ((MakeDepartmentServiceImpl)AopContext.currentProxy()).getSyncData(makedepartment, SyncDataType.MAKEDEPARTMENT, SyncOperateType.DELETE);
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
     * 新版查询历史
     * @param flag
     * @return
     */
    @Override
    public List<OperatorRecord> showUpdteHistory(String flag) {
        List<OperatorRecord> operatorRecords =  operatorRecordMapper.selectByFlag(flag);
        for(OperatorRecord operatorRecord : operatorRecords){
            List<OperatorRecordDetail> operatorRecordDetails = operatorRecordDetailMapper.selectByOperateId(operatorRecord.getId());
            if(operatorRecordDetails.size()>0){
                operatorRecord.setOperatorRecordDetails(operatorRecordDetails);
            }
        }
        return operatorRecords;
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
     * 处罚查询
     * @param makeDepartmentName
     * @param startTime
     * @param endTime
     * @param districtId
     * @return
     */
    @Override
    public List<Makedepartment> selectPunish(String makeDepartmentName, String startTime, String endTime, String districtId,String localDistrictId) {
            List<Makedepartment> list = new ArrayList<>();
        if (makeDepartmentName == null && districtId == null && startTime == null && endTime ==null) {
             list =  makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, localDistrictId);
            return list;
        } else if (districtId != null) {
            String districtIds[] = StringUtil.DistrictUtil(districtId);
            if (districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime,districtIds[0]);
            } else if (!districtIds[1].equals("00") && districtIds[2].equals("00")) {
                list =selectByTime(makeDepartmentName, startTime, endTime,districtIds[0] + districtIds[1]);
            } else if (!districtIds[1].equals("00") && !districtIds[2].equals("00")) {
                list =selectByTime(makeDepartmentName, startTime, endTime,districtId);
            }
        } else {
            String localdistrictIds[] = StringUtil.DistrictUtil(localDistrictId);
            if (localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime,localdistrictIds[0]);
            } else if (!localdistrictIds[1].equals("00") && localdistrictIds[2].equals("00")) {
                list = selectByTime(makeDepartmentName, startTime, endTime,localdistrictIds[0] + localdistrictIds[1]);
            }

        }
        return list;
    }

    /**
     * 对时间做的判断
     * @param makeDepartmentName
     * @param startTime
     * @param endTime
     * @param districtId
     * @return
     */
    public List<Makedepartment> selectByTime(String makeDepartmentName, String startTime, String endTime, String districtId){
        List<Makedepartment> list = new ArrayList<>();
        //如果时间段为空
        if ((startTime == null || "".equals(startTime))
                && (endTime == null || "".equals(endTime))) {
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, districtId);
        }
        //只有开始时间没有结束时间
        else if((startTime != null || !"".equals(startTime)) && (endTime == null || "".equals(endTime))) {
            String end= simpleDateFormat.format(new Date());
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, end, districtId);
        }else
        {
            list = makedepartmentMapper.selectByNameAndTimeAndDistrict(makeDepartmentName, startTime, endTime, districtId);
        }
        return list;
    }
//    /**
//     * 设置user
//     * @param makedepartment
//     * @param type
//     * @return
//     */
//    public User setUserByType(Makedepartment makedepartment,int type){
//        User user = new User();
//        switch (type){
//            case 1:
//                user.setId(UUIDUtil.generate());
//                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
//                user.setRoleId("ZZDW");
//                user.setDistrictId(makedepartment.getDepartmentAddress());
//                user.setRealName(makedepartment.getDepartmentName());
//                user.setTelphone(makedepartment.getLegalTelphone());
//                break;
//            case 2:
//                Makedepartment oldDate =makedepartmentMapper.selectDetailById(makedepartment.getId());
//                user = userService.findByUserName("ZZDW"+oldDate.getLegalTelphone());
//                user.setRoleId("ZZDW");
//                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
//                user.setDistrictId(makedepartment.getDepartmentAddress());
//                user.setRealName(makedepartment.getDepartmentName());
//                user.setTelphone(makedepartment.getLegalTelphone());
//                break;
//            case 3:
//                user = userService.findByUserName("ZZDW"+makedepartment.getLegalTelphone());
//                break;
//        }
//        return user;
//    }

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
     * 对制作单位操作时同时操作从业人员
     * @param employees
     * @return
     */
    public int setEmployeeByDepartment(List<Employee> employees,Makedepartment makedepartment,User user) {
        for (Employee emp : employees) {
            int e = employeeService.deleteEmployee(emp.getId(),user);
            if (e != 2) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
        }
        return ResultUtil.isSuccess;
    }


    /**
     * 检查详情
     * @param id
     * @return
     */
    @Override
    public List<ExamineRecordDetail> selectExamineDetailByID(String id) {
        return examineRecordDetailMapper.selectExamineDetailByID(id);
    }


    /**
     * 设置url字段
     * @param makedepartment
     * @return
     */
    public Makedepartment setFileUrlByType(Makedepartment makedepartment,int type){
        String businessLicenseUrl = makedepartment.getBusinessLicenseUrl();
        String specialLicenseUrl = makedepartment.getSpecialLicenseUrl();
        String legalDocumentUrl = makedepartment.getLegalDocumentUrl();
        switch (type) {
            case 1:
                if (businessLicenseUrl != null) {
                    makedepartment.setBusinessLicenseUrl(StringUtil.getRelativePath(businessLicenseUrl));
                }
                if (specialLicenseUrl != null) {
                    makedepartment.setSpecialLicenseUrl(StringUtil.getRelativePath(specialLicenseUrl));
                }
                if (legalDocumentUrl != null) {
                    makedepartment.setLegalDocumentUrl(StringUtil.getRelativePath(legalDocumentUrl));
                }
            case 2:
                if (businessLicenseUrl != null) {
                    makedepartment.setBusinessLicenseUrl(StringUtil.getAbsolutePath(businessLicenseUrl));
                }
                if (specialLicenseUrl != null) {
                    makedepartment.setSpecialLicenseUrl(StringUtil.getAbsolutePath(specialLicenseUrl));
                }
                if (legalDocumentUrl != null) {
                    makedepartment.setLegalDocumentUrl(StringUtil.getAbsolutePath(legalDocumentUrl));
                }
             default:
                 break;
        }
        return makedepartment;
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
        operatorRecord.setOperateEntityName("makeDepartment");
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
    public OperatorRecordDetail compareData(Makedepartment newData,Makedepartment oldDate,String operatorRecordId) {
        String ignore[] = new String[]{"id", "departmentStatus", "deleteStatus", "version", "flag", "versionTime", "registerTime"};
        Map<String, List<Object>> compareResult = CompareFieldsUtil.compareFields(oldDate, newData, ignore);
        Set<String> keySet = compareResult.keySet();
        OperatorRecordDetail operatorRecordDetail = new OperatorRecordDetail();
        if(keySet.size()==0){
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            operatorRecordDetail.setPropertyName("无任何数据修改！");
        }
        for (String key : keySet) {
            List<Object> list = compareResult.get(key);
            operatorRecordDetail.setId(UUIDUtil.generate());
            operatorRecordDetail.setEntityOperateRecordId(operatorRecordId);
            if(list.get(1)==null||list.get(1)==""){
                operatorRecordDetail.setNewValue("未填写任何值！");
            }else {
                operatorRecordDetail.setNewValue(list.get(1).toString());
            }
            if(list.get(0)==null||list.get(0)==""){
                operatorRecordDetail.setOldValue("未填写任何值！");
            }else {
                operatorRecordDetail.setOldValue(list.get(0).toString());
            }
            operatorRecordDetail.setPropertyName(key);
        }
        return operatorRecordDetail;
    }

    /**
     * 数据同步
     * @return
     */
    @Sync()
    public SyncEntity getSyncData(Object object,int dataType,int operateType ){
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }
}
