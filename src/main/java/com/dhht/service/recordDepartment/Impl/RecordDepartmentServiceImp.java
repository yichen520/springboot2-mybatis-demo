package com.dhht.service.recordDepartment.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.make.Impl.MakeDepartmentServiceImpl;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.HistoryService;
import com.dhht.service.user.UserService;
import com.dhht.sync.SyncDataType;
import com.dhht.sync.SyncOperateType;
import com.dhht.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.experimental.var;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 2018/6/26 create by fyc
 */

@Service(value = "RecordDepartmentService")
@Transactional
public class RecordDepartmentServiceImp implements RecordDepartmentService{
    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    @Autowired
    private ExamineRecordMapper examineRecordMapper;

    @Autowired
    private ExamineRecordDetailMapper examineRecordDetailMapper;


    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FileService fileService;

    private static final String NOTICE_FILE_UPLOAD = "检查图片上传";


    /**
     * 带分页的查询区域下的备案单位
     * @param id
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordDepartment> selectByDistrictId(String id,int pageSize,int pageNum ) {
        PageHelper.startPage(pageNum,pageSize);
        List<RecordDepartment> recordDepartments = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordDepartments = recordDepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            recordDepartments = recordDepartmentMapper.selectByDistrictId(id);
        }

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>(recordDepartments);
        return pageInfo;
    }

    /**
     * 不带分页的查询区域下的备案单位
     * @param id
     * @return
     */
    @Override
    public List<RecordDepartment> selectByDistrictId(String id) {
        List<RecordDepartment> list = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = recordDepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = recordDepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            list = recordDepartmentMapper.selectByDistrictId(id);
        }
        return list;
    }

    /**
     * 带分页查询所有的备案单位
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordDepartment> selectAllRecordDepartMent(int pageSize, int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<RecordDepartment> recordDepartments= recordDepartmentMapper.selectAllRecordDepartment();
        PageInfo<RecordDepartment> pageInfo = new PageInfo<>(recordDepartments);
        return pageInfo;
    }



    /**
     * 添加备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public int insert(RecordDepartment recordDepartment,User updateUser) {
        if(isInsert(recordDepartment)){
            return ResultUtil.isDistrict;
        }
        if (recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
            return ResultUtil.isHave;
        }
        String uuid =UUIDUtil.generate();
        recordDepartment.setId(uuid);
        recordDepartment.setVersion(1);
        String flag = UUIDUtil.generate10();
        recordDepartment.setFlag(flag);
        recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        int r = recordDepartmentMapper.insert(recordDepartment);
        int u = userService.insert(recordDepartment.getTelphone(),"BADW",recordDepartment.getDepartmentName(),recordDepartment.getDepartmentAddress());

        boolean o = historyService.insertOperateRecord(updateUser,recordDepartment.getFlag(),recordDepartment.getId(),"recordDepartment",SyncOperateType.SAVE,UUIDUtil.generate());
        if(r==1&&u==ResultUtil.isSend&&o){
            SyncEntity syncEntity = ((RecordDepartmentServiceImp) AopContext.currentProxy()).getSyncData(recordDepartment, SyncDataType.RECORDDEPARTMENT, SyncOperateType.SAVE);
            return ResultUtil.isSuccess;
        }else if(u==ResultUtil.isHave){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isHave;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isFail;
        }
    }

    /**
     * 根据备案单位的编号查询备案单位
     * @param code
     * @return
     */
    @Override
    public RecordDepartment selectByCode(String code) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectByCode(code);
        return recordDepartment;
    }

    /**
     * 备案单位数据同步
     * @return
     */
    //@Sync()
    public SyncEntity getSyncData(Object object,int dataType,int operateType ){
        SyncEntity syncEntity = new SyncEntity();
        syncEntity.setObject(object);
        syncEntity.setDataType(dataType);
        syncEntity.setOperateType(operateType);
        return syncEntity;
    }

    /**
     * 根据Id删除备案单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectById(id);
        try {
            User user = userService.findByUserName("BADW"+recordDepartment.getTelphone());
            if(user ==null){
                int re = recordDepartmentMapper.deleteById(id);
                if(re==1) {
                    return ResultUtil.isSuccess;
                }else {
                    return ResultUtil.isFail;
                }
            }
            int r = recordDepartmentMapper.deleteById(id);
            int u = userService.deleteByUserName("BADW",recordDepartment.getTelphone());
            if (r >0&&u==ResultUtil.isSuccess) {
                return ResultUtil.isSuccess;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        } catch (Exception e){
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ResultUtil.isException;
    }
    }

    /**
     * 根据id修改备案单位
     * @param recordDepartment
     * @return
     */
    @Override
    public int updateById(RecordDepartment recordDepartment,User updateUser) {
        try {
            RecordDepartment oldDate = recordDepartmentMapper.selectById(recordDepartment.getId());
            int d = recordDepartmentMapper.deleteById(recordDepartment.getId());
            if(d==0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isError;
            }
            if(recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
                return ResultUtil.isHave;
            }
            recordDepartment.setVersion(recordDepartment.getVersion()+1);
            recordDepartment.setIsDelete(true);
            recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
            String uuid = UUIDUtil.generate();
            recordDepartment.setId(uuid);
            int r = recordDepartmentMapper.insert(recordDepartment);
            int u = userService.update(oldDate.getTelphone(),recordDepartment.getTelphone(),"BADW",recordDepartment.getDepartmentName(),recordDepartment.getDepartmentAddress());
            String[] ignore = new String[]{"id","principalId","departmentAddress","isDelete","version","operator","updateTime"};
            String operateUUid = UUIDUtil.generate();
            boolean o = historyService.insertOperateRecord(updateUser,recordDepartment.getFlag(),recordDepartment.getId(),"recordmentDepartment",SyncOperateType.UPDATE,operateUUid);
            boolean od = historyService.insertUpdateRecord(recordDepartment,oldDate,operateUUid,ignore);
            if (r==1&&u==ResultUtil.isSuccess&&o&&od){
                return ResultUtil.isSuccess;
            }else if(u==1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isHave;
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isException;
        }
    }

    /**
     * 判断该区域下是否有备案单位
     * @param recordDepartment
     * @return
     */
    public boolean isInsert(RecordDepartment recordDepartment){
        List<RecordDepartment> list = recordDepartmentMapper.selectByDistrictId(recordDepartment.getDepartmentAddress());
        if(list.size()>0){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 根据电话查询备案单位
     * @param phone
     * @return
     */
    @Override
    public RecordDepartment selectByPhone(String phone) {
       return recordDepartmentMapper.selectByPhone(phone);
    }

    /**
     * 惩罚查询
     * @param makedepartmentName
     * @param startTime
     * @param endTime
     * @param districtId
     * @return
     */
    @Override
    public List<ExamineRecord> findPunish(String makedepartmentName, String startTime, String endTime, String districtId) {
        return examineRecordMapper.findPunish(makedepartmentName,startTime,endTime,districtId);
    }

    /**
     * 添加惩罚
     * @param user
     * @param examineRecord
     * @return
     */
    @Override
    public boolean insertPunish(User user, ExamineRecord examineRecord) {
        ExamineRecord employeePunishRecord =  ((RecordDepartmentServiceImp) AopContext.currentProxy()).addExamine(user,examineRecord);
        if ( employeePunishRecord==null){
            return false;
        }else {
            return true;
        }
    }


    /**
     * 检查同步到内网
     * @param user
     * @param examineRecord
     * @return
     */
    @Sync(DataType =SyncDataType.EXAMINE,OperateType = SyncOperateType.SAVE)
    public ExamineRecord addExamine(User user, ExamineRecord examineRecord){
        String id = UUIDUtil.generate();
        examineRecord.setId(id);
        examineRecord.setExaminerName(user.getUserName());
        RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(user.getTelphone());
        examineRecord.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
        examineRecord.setRecordDepartmentName(recordDepartment.getDepartmentName());
        examineRecord.setExamineTime(DateUtil.getCurrentTime());
        examineRecord.setDistrictId(user.getDistrictId());
        examineRecordMapper.insertSelective(examineRecord);
        List<ExamineRecordDetail> punishLogs = examineRecord.getExamineRecordDetails();
        boolean flag = false;
        if(punishLogs!=null){
            for (ExamineRecordDetail examineRecordDetail:punishLogs){
                examineRecordDetail.setId(UUIDUtil.generate());
                examineRecordDetail.setExamineRecordId(examineRecord.getId());
                examineRecordDetailMapper.insertSelective(examineRecordDetail);
                flag = true;
            }
        }
       String[] urls =  StringUtil.toStringArray1(examineRecord.getExamineFileUrl());
        for(int i = 0; i < urls.length; i++) {
            fileService.register(urls[i],NOTICE_FILE_UPLOAD);
        }

        if (flag){
            return examineRecord;
        }else {
            return null;
        }
    }

}
