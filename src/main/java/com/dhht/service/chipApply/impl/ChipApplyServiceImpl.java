package com.dhht.service.chipApply.impl;

import com.dhht.dao.ChipApplyMapper;
import com.dhht.dao.ChipGrantMapper;
import com.dhht.model.ChipApply;
import com.dhht.model.ChipGrant;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.User;
import com.dhht.service.chipApply.ChipApplyService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by imac_dhht on 2018/8/11.
 */
@Service(value = "chipApplyServiceImpl")
@Transactional
public class ChipApplyServiceImpl implements ChipApplyService {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private ChipApplyMapper chipApplyMapper;

    @Autowired
    private ChipGrantMapper chipGrantMapper;


    /**
     * 申请
     * @param chipNum
     * @param getTime
     * @param address
     * @param memo
     * @param user
     * @return
     */
    @Override
    public int apply(Integer chipNum, Date getTime, String address, String memo,User user) {
        ChipApply chipApply = new ChipApply();
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
        if(makeDepartmentSimple==null){
            return ResultUtil.isNoDepartment;
        }
        chipApply.setId(UUIDUtil.generate());
        chipApply.setAddress(address);
        chipApply.setChipNum(chipNum);
        chipApply.setGetTime(getTime);
        chipApply.setMemo(memo);
        chipApply.setApplyTime(DateUtil.getCurrentTime());
        chipApply.setMakeDepartmentCode(makeDepartmentSimple.getDepartmentCode());
        chipApply.setMakeDepartmentName(makeDepartmentSimple.getDepartmentName());
        int applyResult = chipApplyMapper.insert(chipApply);
        if(applyResult<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }

    }

    /**
     * 发放
     * @param chipApplyId
     * @param grantNum
     * @param grantTime
     * @param chipCodeStart
     * @param chipCodeEnd
     * @param receiver
     * @param grantWay
     * @param granter
     * @param memo
     * @return
     */
    @Override
    public int grant(String chipApplyId, Integer grantNum, Date grantTime, String chipCodeStart, String chipCodeEnd, String receiver, String grantWay ,String granter,String memo) {
        ChipGrant chipGrant = new ChipGrant();
        chipGrant.setId(UUIDUtil.generate());
        chipGrant.setMemo(memo);
        chipGrant.setChipApplyId(chipApplyId);
        chipGrant.setGrantNum(grantNum);
        chipGrant.setGrantTime(grantTime);
        chipGrant.setChipCodeStart(chipCodeStart);
        chipGrant.setChipCodeEnd(chipCodeEnd);
        chipGrant.setReceiver(receiver);
        chipGrant.setGranter(granter);
        chipGrant.setGrantWay(grantWay);
        chipGrant.setGranterId(UUIDUtil.generate());
        chipGrant.setRecordTime(DateUtil.getCurrentTime());
        int chipGrantResult = chipGrantMapper.insert(chipGrant);
        if(chipGrantResult<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }
}
