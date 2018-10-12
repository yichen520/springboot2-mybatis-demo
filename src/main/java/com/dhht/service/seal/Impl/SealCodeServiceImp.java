package com.dhht.service.seal.Impl;

import com.dhht.dao.SealDao;
import com.dhht.model.Seal;
import com.dhht.model.SealCode;
import com.dhht.service.seal.SealCodeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SealCodeServiceImp implements  SealCodeService {

    @Autowired
    private SealDao sealDao;


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

    @Override
    public List<SealCode> info(String districtId) {
        return null;
    }

    @Override
    public int updateSealCode(SealCode sealCode) {
        return 0;
    }

    @Override
    public boolean lockSealCode(String districtId) {
        return false;
    }
}
