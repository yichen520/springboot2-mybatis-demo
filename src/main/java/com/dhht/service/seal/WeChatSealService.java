package com.dhht.service.seal;

import com.dhht.model.pojo.SealVerificationPO;

import java.util.List;

public interface WeChatSealService {

    //资料更新列表
    List<SealVerificationPO> sealAndVerification(String telphone);
}
