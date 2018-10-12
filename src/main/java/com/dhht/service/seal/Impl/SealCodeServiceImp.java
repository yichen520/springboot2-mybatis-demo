package com.dhht.service.seal.Impl;

import com.dhht.dao.SealDao;
import com.dhht.model.Seal;
import com.dhht.model.SealCode;
import com.dhht.service.District.DistrictService;
import com.dhht.service.seal.SealCodeService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SealCodeServiceImp implements  SealCodeService {

    @Autowired
    private SealDao sealDao;

    @Autowired
    private  RedisTemplate redisTemplate;

    @Autowired
    private DistrictService districtService;

    /**
     * 获取备案单位编号
     * @return
     */
    @Override
    public List<SealCode> selectRecordDepartmentCode() {
        return sealDao.selectRecordDepartmentCode();
    }

    /**
     * 获取印章编号
     * @param code
     * @return
     */
    @Override
    public String selectSealCode(String code) {
        return sealDao.selectSealCode(code);
    }


    /**
     * 展示编号信息
     * @param districtId
     * @return
     */
    @Override
    public List<SealCode> info(String districtId) {
         districtId = StringUtil.getDistrictId(districtId);
         Set<String> keys = redisTemplate.keys("*");
         List<SealCode> sealCodes = new ArrayList<>();
         for(String key : keys){
             if(key.contains(districtId)){
                 String value = redisTemplate.opsForValue().get(key).toString();
                 String districtName = districtService.selectByDistrictId(key);
                 SealCode sealCode = new SealCode(key,value,districtName);
                 sealCodes.add(sealCode);
             }
         }
         return sealCodes;
    }

    /**
     * 修改印章最大编号
     * @param sealCode
     * @return
     */
    @Override
    public int updateSealCode(SealCode sealCode) {
        String key = sealCode.getDistrictId();
        String value = sealCode.getSealCode();
        if(!redisTemplate.hasKey(key)){
            return ResultUtil.isError;
        }
        redisTemplate.opsForValue().set(key,value);
        String newValue = redisTemplate.opsForValue().get(key).toString();
        if(value.equals(newValue)){
            return ResultUtil.isSuccess;
        }
        return ResultUtil.isFail;
    }

    /**
     * 锁定redis 的SealCode
     * @param districtId
     * @return
     */
    @Override
    public boolean lockSealCode(String districtId) {
         if(redisTemplate.hasKey(districtId)){
             redisTemplate.expire(districtId,60, TimeUnit.SECONDS);
             return true;
         }
         return false;
    }

    /**
     * 获取区域下最大的印章号
     * @param districtId
     * @return
     */
    @Override
    public String getMaxSealCode(String districtId){
        String num = "";
        Jedis jedis = new Jedis();
        if(redisTemplate.hasKey(districtId)){
            num = jedis.incrBy("districtId", 1).toString();
        }else {
            redisTemplate.opsForValue().set("districtId",0);
            num = jedis.incrBy("districtId", 1).toString();
        }
        return num;
    }
}
