package com.dhht.service.punish.impl;

import com.dhht.dao.EmployeePunishRecordMapper;
import com.dhht.dao.MakePunishRecordMapper;
import com.dhht.dao.SMSCodeDao;
import com.dhht.dao.UserDao;
import com.dhht.model.*;
import com.dhht.service.punish.PunishService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserService;
import com.dhht.util.DateUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("punishService")
@Transactional
public class PunishServiceImpl implements PunishService {

    @Autowired
    private MakePunishRecordMapper makePunishRecordMapper;
    @Autowired
    private EmployeePunishRecordMapper employeePunishRecordMapper;
    @Autowired
    private RecordDepartmentService recordDepartmentService;
    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Override
    public boolean insertPunish(User user, MakePunishRecord makePunishRecord) {
        RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(user.getTelphone());
        makePunishRecord.setId(UUIDUtil.generate());
        makePunishRecord.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
        makePunishRecord.setRecordDepartmentName(recordDepartment.getDepartmentName());
        makePunishRecord.setPunishTime(DateUtil.getCurrentTime());
        makePunishRecord.setDistrictId(user.getDistrictId());
        makePunishRecord.setPunisherName(user.getUserName());
        if (makePunishRecordMapper.insertSelective(makePunishRecord)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<MakePunishRecord> findPunish(String makedepartmentName, String startTime, String endTime, String districtId) {
        return makePunishRecordMapper.findPunish(makedepartmentName,startTime,endTime,districtId);
    }

    @Override
    public boolean insertEmployeePunish(User user, EmployeePunishRecord employeePunish) {
        RecordDepartment recordDepartment = recordDepartmentService.selectByPhone(user.getTelphone());
        employeePunish.setId(UUIDUtil.generate());
        employeePunish.setRecordDepartmentCode(recordDepartment.getDepartmentCode());
        employeePunish.setRecordDepartmentName(recordDepartment.getDepartmentName());
        employeePunish.setPunishTime(DateUtil.getCurrentTime());
        employeePunish.setDistrictId(user.getDistrictId());
        employeePunish.setPunisherName(user.getUserName());
        if (employeePunishRecordMapper.insertSelective(employeePunish)== 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<EmployeePunishRecord> findEmployeePunish(String makedepartmentName, String startTime, String endTime, String districtId) {
        return employeePunishRecordMapper.findPunish(makedepartmentName,startTime,endTime,districtId);
    }

    @Override
    public boolean sendcode(String phone, int templateId, ArrayList<String> params) {
        SMSCode smscode= smsCodeDao.getSms(phone);
        if(smscode==null){
            smscode = new SMSCode();
            smscode.setId(UUIDUtil.generate());
            smscode.setLastTime(System.currentTimeMillis());
            smscode.setPhone(phone);
            smscode.setSmscode(params.get(1));
            smsCodeDao.save(smscode);
        }else{
            smscode.setLastTime(System.currentTimeMillis());
            smscode.setSmscode(params.get(1));
            smsCodeDao.update(smscode);
        }
        smsSendService.sendSingleMsgByTemplate(phone,templateId,params);
        return true;
    }

    @Override
    public boolean sendcode1(String phone, int templateId, ArrayList<String> params) {
        SMSCode smscode= smsCodeDao.getSms(phone);
        if(smscode==null){
            smscode = new SMSCode();
            smscode.setId(UUIDUtil.generate());
            smscode.setLastTime(System.currentTimeMillis());
            smscode.setPhone(phone);
            smscode.setSmscode(params.get(0));
            smsCodeDao.save(smscode);
        }else{
            smscode.setLastTime(System.currentTimeMillis());
            smscode.setSmscode(params.get(0));
            smsCodeDao.update(smscode);
        }
        smsSendService.sendSingleMsgByTemplate(phone,templateId,params);
        return true;
    }
}
