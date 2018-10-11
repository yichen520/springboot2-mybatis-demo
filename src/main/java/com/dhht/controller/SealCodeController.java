package com.dhht.controller;

import com.dhht.model.SealCode;
import com.dhht.service.seal.SealCodeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SealCodeController implements InitializingBean {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SealCodeService sealCodeService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SealCode> sealCodes = sealCodeService.selectRecordDepartmentCode();
        for(SealCode sealCode : sealCodes){
            String code = sealCodeService.selectSealCode(sealCode.getRecordDepartmentCode());
            if(code ==null){
                //redisTemplate.opsForValue().set(code.substring(0,6),0);
                return;
            }
            Integer temp = Integer.parseInt(code.substring(6));
            redisTemplate.opsForValue().set(code.substring(0,6),temp);
        }
    }
}
