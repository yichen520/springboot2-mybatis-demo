package com.dhht.service.seal;

import com.dhht.model.pojo.SealVerificationPO;

import java.util.List;

public interface WeChatSealService {

    //资料更新列表
    List<SealVerificationPO> sealAndVerification(String telphone);

    //根据印章id获取资料信息
    SealVerificationPO selectVerificationById(String id);

}
