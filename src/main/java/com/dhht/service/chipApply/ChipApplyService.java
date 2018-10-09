package com.dhht.service.chipApply;

import com.dhht.model.ChipApply;
import com.dhht.model.ChipGrant;
import com.dhht.model.User;
import com.dhht.model.pojo.ChipCountVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by imac_dhht on 2018/8/11.
 */
public interface ChipApplyService {

    int apply(Integer chipNum,String getTime,String address,String addressDetail,String memo,User user);  //申请


    List<ChipCountVO> countGrantInfo(Map map, HttpServletRequest httpServletRequest);

    List<ChipApply> getchipApplyInfo(String startTime, String endTime ,User user);

    List<ChipApply> getmakeDepartment(User user);

    List<ChipApply> getchipGrant(User user, String makeDepartment, Integer grantFlag, String startTime,String endTime);

    List<ChipApply> getGrantMakeDepartment(User user);

    int grant(User user, String chipApplyId, Integer grantNum, String grantTime, String chipCodeStart, String chipCodeEnd, String receiver, String grantWay, String memo, String receiverTel);

    List<ChipGrant> getchipGrantInfo(User user, String makeDepartment, String startTime, String endTime, String receiver);
}
