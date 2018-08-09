package com.dhht.service.recordDepartment.Impl;

import com.dhht.annotation.Sync;
import com.dhht.dao.*;
import com.dhht.model.*;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public int insert(RecordDepartment recordDepartment) {
        if(isInsert(recordDepartment)){
            return ResultUtil.isDistrict;
        }
        if (recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
            return ResultUtil.isHave;
        }
        recordDepartment.setId(UUIDUtil.generate());
        User user = setUserByType(recordDepartment,1);
        recordDepartment.setVersion(1);
        recordDepartment.setFlag(UUIDUtil.generate10());
        recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
        int r = recordDepartmentMapper.insert(recordDepartment);
        int u = userService.insert(user);
        if(r==1&&u==ResultUtil.isSend){
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
     * 根据Id删除备案单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        RecordDepartment recordDepartment = recordDepartmentMapper.selectById(id);
        try {
            User user = setUserByType(recordDepartment,3);
            if(user ==null){
                int re = recordDepartmentMapper.deleteById(id);
                if(re==1) {
                    return ResultUtil.isSuccess;
                }else {
                    return ResultUtil.isFail;
                }
            }
            int r = recordDepartmentMapper.deleteById(id);
            int u = userService.deleteByUserName("BADW"+recordDepartment.getTelphone());
            if (r + u == 2) {
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
    public int updateById(RecordDepartment recordDepartment) {
        try {
            int d = recordDepartmentMapper.deleteById(recordDepartment.getId());
            if(d==0){
                return ResultUtil.isError;
            }
            if(recordDepartmentMapper.validateCode(recordDepartment.getDepartmentCode())>0){
                return ResultUtil.isHave;
            }
            int u = userService.update(setUserByType(recordDepartment, 2));
            recordDepartmentMapper.deleteById(recordDepartment.getId());
            recordDepartment.setVersion(recordDepartment.getVersion()+1);
            recordDepartment.setIsDelete(true);
            recordDepartment.setUpdateTime(new Date(System.currentTimeMillis()));
            recordDepartment.setId(UUIDUtil.generate());
            int r = recordDepartmentMapper.insert(recordDepartment);
            if (r + u == 3) {
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
        //return true;
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
         * 设置User
         * @param recordDepartment
         * @param type
         * @return
         */
        public User setUserByType (RecordDepartment recordDepartment,int type){
            User user = new User();
            switch (type) {
                //添加user
                case 1:
                    user.setId(UUIDUtil.generate());
                    user.setUserName("BADW"+recordDepartment.getTelphone());
                    user.setRealName(recordDepartment.getDepartmentName());
                    user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //修改user
                case 2:
                    RecordDepartment oldDate = recordDepartmentMapper.selectById(recordDepartment.getId());
                    user = userService.findByUserName("BADW"+oldDate.getTelphone());
                    user.setUserName("BADW"+recordDepartment.getTelphone());
                    user.setRealName(recordDepartment.getDepartmentName());
                    user.setRoleId("BADW");
                    user.setTelphone(recordDepartment.getTelphone());
                    user.setDistrictId(recordDepartment.getDepartmentAddress());
                    break;
                 //删除user
                case 3:
                    user = userService.findByUserName("BADW"+recordDepartment.getTelphone());
                default:
                    break;
            }
            return user;
        }

    /**
     * 历史记录查询
      * @param flag
     * @return
     */
    @Override
    public List<RecordDepartment> showMore(String flag) {
        return recordDepartmentMapper.selectByFlag(flag);
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
        if (flag){
            return examineRecord;
        }else {
            return null;
        }
    }

}
