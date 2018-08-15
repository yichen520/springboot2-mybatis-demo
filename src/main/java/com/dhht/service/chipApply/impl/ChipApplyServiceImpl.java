package com.dhht.service.chipApply.impl;

import com.dhht.dao.ChipApplyMapper;
import com.dhht.dao.ChipGrantMapper;
import com.dhht.model.*;
import com.dhht.model.pojo.ChipCountVO;
import com.dhht.service.District.DistrictService;
import com.dhht.service.chipApply.ChipApplyService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private DistrictService districtService;


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
    public int apply(Integer chipNum, String getTime, String address, String memo,User user) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ChipApply chipApply = new ChipApply();
            String telphone = user.getTelphone();
            MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
            if (makeDepartmentSimple == null) {
                return ResultUtil.isNoDepartment;
            }
            chipApply.setId(UUIDUtil.generate());
            chipApply.setAddress(address);
            chipApply.setChipNum(chipNum);
            chipApply.setGetTime(sdf.parse(getTime));
            chipApply.setMemo(memo);
            chipApply.setApplyTime(DateUtil.getCurrentTime());
            chipApply.setMakeDepartmentCode(makeDepartmentSimple.getDepartmentCode());
            chipApply.setMakeDepartmentName(makeDepartmentSimple.getDepartmentName());
            int applyResult = chipApplyMapper.insert(chipApply);
            if (applyResult < 0) {
                return ResultUtil.isFail;
            } else {
                return ResultUtil.isSuccess;
            }
        }catch (Exception e){
            return ResultUtil.isException;
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
     * @param memo
     * @return
     */
    @Override
    public int grant(User user,String chipApplyId, Integer grantNum, String grantTime, String chipCodeStart, String chipCodeEnd, String receiver, String grantWay,String memo) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ChipGrant chipGrant = new ChipGrant();
            chipGrant.setId(UUIDUtil.generate());
            chipGrant.setMemo(memo);
            chipGrant.setChipApplyId(chipApplyId);
            chipGrant.setGrantNum(grantNum);
            chipGrant.setGrantTime(sdf.parse(grantTime));
            chipGrant.setChipCodeStart(chipCodeStart);
            chipGrant.setChipCodeEnd(chipCodeEnd);
            chipGrant.setReceiver(receiver);
            chipGrant.setGranter(user.getRealName());
            chipGrant.setGrantWay(grantWay);
            chipGrant.setGranterId(UUIDUtil.generate());
            chipGrant.setRecordTime(DateUtil.getCurrentTime());
            int chipGrantResult = chipGrantMapper.insert(chipGrant);
            if (chipGrantResult < 0) {
                return ResultUtil.isFail;
            } else {
                return ResultUtil.isSuccess;
            }
        }catch (Exception e){
            return ResultUtil.isException;
        }
    }

    @Override
    public List<ChipCountVO> countGrantInfo(Map map, HttpServletRequest httpServletRequest) {
        String department = (String)map.get("makeDepartment");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        String district;
        district= (String)map.get("districtId");
        User user= (User)httpServletRequest.getSession().getAttribute("user");
        if (district == null){
            district = user.getDistrictId();
        }
        String districtid = StringUtil.getDistrictId(district);
        List<ChipCountVO> grants =  chipGrantMapper.selectGrantRecord(department,startTime,endTime,districtid);
        return grants;
    }
}
