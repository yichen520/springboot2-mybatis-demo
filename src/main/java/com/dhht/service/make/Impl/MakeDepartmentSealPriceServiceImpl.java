package com.dhht.service.make.Impl;

import com.dhht.dao.MakeDepartmentSealPriceMapper;
import com.dhht.model.MakeDepartmentSealPrice;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.model.User;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service("MakeDepartmentSealPriceService")
@Transactional
public class MakeDepartmentSealPriceServiceImpl implements MakeDepartmentSealPriceService {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private MakeDepartmentSealPriceMapper makeDepartmentSealPriceMapper;

    @Override
    public int insertSealPriceRecord(List<MakeDepartmentSealPrice> makeDepartmentSealPrices, User user) {
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
        for(MakeDepartmentSealPrice makeDepartmentSealPrice:makeDepartmentSealPrices){
            makeDepartmentSealPrice.setId(UUIDUtil.generate());
            makeDepartmentSealPrice.setIsDelete(false);
            makeDepartmentSealPrice.setUpdateTime(DateUtil.getCurrentTime());
            makeDepartmentSealPrice.setMakeDepartmentFlag(makeDepartmentSimple.getFlag());
            int i = makeDepartmentSealPriceMapper.insert(makeDepartmentSealPrice);
            if(i<1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultUtil.isFail;
            }
        }
        return ResultUtil.isSuccess;
    }

    @Override
    public int updateRecordsByIds(List<MakeDepartmentSealPrice> makeDepartmentSealPrices, User user) {
       for(MakeDepartmentSealPrice makeDepartmentSealPrice : makeDepartmentSealPrices){
           makeDepartmentSealPrice.setUpdateTime(DateUtil.getCurrentTime());
           int i = makeDepartmentSealPriceMapper.updateById(makeDepartmentSealPrice);
           if(i<1){
               return ResultUtil.isFail;
           }
       }
       return ResultUtil.isSuccess;
    }

    @Override
    public int updateById(MakeDepartmentSealPrice makeDepartmentSealPrice, User user) {
        makeDepartmentSealPrice.setUpdateTime(DateUtil.getCurrentTime());
        int i = makeDepartmentSealPriceMapper.updateById(makeDepartmentSealPrice);
        if(i<1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.isFail;
        }else {
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public int deleteByUser(User user) {
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
        int i = makeDepartmentSealPriceMapper.deleteByMakeDepartmentFlag(makeDepartmentSimple.getFlag());
        if(i<1){
            return ResultUtil.isFail;
        }else {
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public int deleteByMakeDepartFlag(String makeDepartmentFlag) {
        int i = makeDepartmentSealPriceMapper.deleteByMakeDepartmentFlag(makeDepartmentFlag);
        if(i<1){
            return ResultUtil.isFail;
        }else {
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public List<MakeDepartmentSealPrice> selectByUser(User user) {
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
        List<MakeDepartmentSealPrice> makeDepartmentSealPrices = makeDepartmentSealPriceMapper.selectByMakeDepartmentFlag(makeDepartmentSimple.getFlag());
        return makeDepartmentSealPrices;
    }

    @Override
    public List<MakeDepartmentSealPrice> selectByMakeDepartmentFlag(String flag) {
        return makeDepartmentSealPriceMapper.selectByMakeDepartmentFlag(flag);
    }

    @Override
    public MakeDepartmentSealPrice selectByMakeDepartmentFlagAndType(String makeDepartmentFlag, String sealType) {
        MakeDepartmentSealPrice makeDepartmentSealPrice = new MakeDepartmentSealPrice();
        makeDepartmentSealPrice.setMakeDepartmentFlag(makeDepartmentFlag);
        makeDepartmentSealPrice.setSealType(sealType);
        return makeDepartmentSealPriceMapper.selectBySealTypeAndMakeDepartment(makeDepartmentSealPrice);
    }
}
