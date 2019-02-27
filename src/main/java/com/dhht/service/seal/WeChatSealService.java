package com.dhht.service.seal;

import com.dhht.model.*;
import com.dhht.model.pojo.SealVerificationPO;
import com.dhht.model.pojo.SealWeChatDTO;

import java.util.List;
import java.util.Map;

public interface WeChatSealService {

    //资料更新列表
    List<SealVerificationPO> sealAndVerification(String telphone);

    //根据印章id获取资料信息
    SealVerificationPO selectVerificationById(String id);

    //小程序变更
    int cachetChange(String sealId,String sealAgentId,WeChatUser weChatUser);

    //小程序申请
    int sealWeChatRecord(WeChatUser user, SealWeChatDTO sealDTO, String payOrderId);

    MakeDepartmentSealPrice sealPrice(Map map);


    List<Seal> sealProgress(Map map);

    List<Seal> portalSealInfoByCode(String cdode);

    //进度查询列表
    List<Seal> sealListForWeChat(String telphone);

    //查询印章code
    Map<String,Object> weChatcheckSealCode(String sealCode,String useDepartmentCode,String sealTypeCode);

    //线上快递交付
    int expressdeliver(User user, Seal seal);

    //印章更换的时候查询印章
    int isHaveSeal(String useDepartmentCode,Seal seal);


    //判断是否资料更新
    SealVerificationPO isSealVerification(String sealId);

    //资料更新
    int dateUpdate(WeChatUser weChatUser,String id,SealAgent sealAgent);

}
