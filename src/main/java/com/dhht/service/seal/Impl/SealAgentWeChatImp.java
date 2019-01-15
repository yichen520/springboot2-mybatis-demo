package com.dhht.service.seal.Impl;

import com.dhht.dao.SealAgentMapper;
import com.dhht.model.SealAgent;
import com.dhht.service.seal.SealAgentWeChatService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.util.UIUtil;

import java.util.List;
import java.util.UUID;

@Service("sealAgentWeChatService")
@Transactional
public class SealAgentWeChatImp implements SealAgentWeChatService {

    @Autowired
    private SealAgentMapper sealAgentMapper;

    @Override
    public int insertSealAgent(SealAgent sealAgent,String telPhone) {
        sealAgent.setId(UUIDUtil.generate());
        sealAgent.setBusinessType("111");
        sealAgent.setLoginTelPhone(telPhone);
        int insertResult = sealAgentMapper.insertSelective(sealAgent);
        if(insertResult<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public int updateSealAgent(SealAgent sealAgent) {
        int updateResult = sealAgentMapper.updateByPrimaryKey(sealAgent);
        if(updateResult<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public List<SealAgent> sealAgentList(String telPhone) {
        List<SealAgent> sealAgents = sealAgentMapper.selectByTelPhone(telPhone);
        return sealAgents;
    }

    @Override
    public SealAgent sealAgentDetails(String id) {
        SealAgent sealAgent = sealAgentMapper.selectByPrimaryKey(id);
        if(sealAgent!=null){
            return sealAgent;
        }else{
            return null;
        }
    }

    @Override
    public int deleteSealAgent(String id) {
        int deleteResult = sealAgentMapper.deleteByPrimaryKey(id);
        if(deleteResult<0){
            return ResultUtil.isFail;
        }else{
            return ResultUtil.isSuccess;
        }
    }

}