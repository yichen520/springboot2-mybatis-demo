package com.dhht.service.seal;

import com.dhht.model.SealAgent;

import java.util.List;

public interface SealAgentWeChatService {
    //新增
    int insertSealAgent(SealAgent sealAgent,String telPhone);

    //更新
    int updateSealAgent(SealAgent sealAgent);

    //列表
    List<SealAgent> sealAgentList(String telPhone);

    //查询详情
    SealAgent sealAgentDetails(String id);

    //删除
    int deleteSealAgent(String id);
}
