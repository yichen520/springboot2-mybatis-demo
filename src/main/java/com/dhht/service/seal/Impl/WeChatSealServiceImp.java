package com.dhht.service.seal.Impl;

import com.dhht.dao.SealDao;
import com.dhht.dao.SealVerificationMapper;
import com.dhht.model.Seal;
import com.dhht.model.SealVerification;
import com.dhht.model.pojo.SealVerificationPO;
import com.dhht.service.seal.WeChatSealService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("weChatSealService")
@Transactional
public class WeChatSealServiceImp implements WeChatSealService {

    @Autowired
    private SealVerificationMapper sealVerificationMapper;

    @Autowired
    private SealDao sealDao;


    /**
     * 资料更新列表
     * @param telphone
     * @return
     */
    @Override
    public List<SealVerificationPO> sealAndVerification(String telphone) {
        List<Seal> seals = sealDao.selectSealByTelphone(telphone);
        List<SealVerificationPO> sealVerificationPOS = new ArrayList<>();
        for(Seal seal:seals){
            SealVerificationPO sealVerificationPO =new SealVerificationPO();
            sealVerificationPO.setSeal(seal);
            String sealId = seal.getId();
            SealVerification sealVerification = sealVerificationMapper.selectBySealId(sealId);
            sealVerificationPO.setSealVerification(sealVerification);
            sealVerificationPOS.add(sealVerificationPO);
        }
        return sealVerificationPOS;
    }
}
