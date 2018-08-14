package com.dhht.service.chipApply;

import com.dhht.model.ChipGrant;
import com.dhht.model.User;
import com.dhht.model.pojo.ChipCountVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by imac_dhht on 2018/8/11.
 */
public interface ChipApplyService {

    int apply(Integer chipNum,String getTime,String address,String memo,User user);  //申请


    int grant(String chipApplyId, Integer grantNum, String grantTime,String chipCodeStart, String chipCodeEnd, String receiver, String grantWay ,String granter,String memo);//发放

    List<ChipCountVO> countGrantInfo(Map map, HttpServletRequest httpServletRequest);

}
