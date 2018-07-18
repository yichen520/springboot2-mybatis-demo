package com.dhht.service.punish.impl;

import com.dhht.dao.MakePunishRecordMapper;
import com.dhht.model.*;
import com.dhht.service.punish.PunishService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.util.DateUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("punishService")
@Transactional
public class PunishServiceImpl implements PunishService {

    @Autowired
    private MakePunishRecordMapper makePunishRecordMapper;
    @Autowired
    private RecordDepartmentService recordDepartmentService;


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
}
