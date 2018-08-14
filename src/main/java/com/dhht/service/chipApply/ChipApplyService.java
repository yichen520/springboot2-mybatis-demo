package com.dhht.service.chipApply;

import com.dhht.model.User;

import java.util.Date;

/**
 * Created by imac_dhht on 2018/8/11.
 */
public interface ChipApplyService {

    int apply(Integer chipNum,String getTime,String address,String memo,User user);  //申请


    int grant(User user,String chipApplyId, Integer grantNum, String grantTime,String chipCodeStart, String chipCodeEnd, String receiver, String grantWay ,String memo);//发放

}
