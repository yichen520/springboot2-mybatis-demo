package com.dhht.service.make.Impl;

import com.dhht.dao.MakeDepartmentAttachInfoMapper;
import com.dhht.model.MakeDepartmentAttachInfo;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.User;
import com.dhht.service.make.MakeDepartmentAttacthInfoService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("MakeDepartmentAttactInfoService")
@Transactional
public class MakeDepartmentInfoServiceImpl implements MakeDepartmentAttacthInfoService {

    @Autowired
    private MakeDepartmentAttachInfoMapper makeDepartmentAttachInfoMapper;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Override
    public int insert(MakeDepartmentAttachInfo makeDepartmentAttachInfo, User user) {
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple= makeDepartmentService.selectByLegalTephone(telphone);
        makeDepartmentAttachInfo.setId(UUIDUtil.generate());
        makeDepartmentAttachInfo.setIsDelete(false);
        makeDepartmentAttachInfo.setMakeDepartmentFlag(makeDepartmentSimple.getFlag());
        makeDepartmentAttachInfo.setUpdateTime(DateUtil.getCurrentTime());
        int i = makeDepartmentAttachInfoMapper.insert(makeDepartmentAttachInfo);
        if(i<1){
            return ResultUtil.isFail;
        }
        return ResultUtil.isSuccess;
    }

    @Override
    public int updateById(MakeDepartmentAttachInfo makeDepartmentAttachInfo, User user) {
        int i = makeDepartmentAttachInfoMapper.updateById(makeDepartmentAttachInfo);
        if(i<1){
            return ResultUtil.isFail;
        }
        return ResultUtil.isSuccess;
    }

    @Override
    public int deleteByMakeDepartmentFlag(String makeDepartmentFlag) {
        int i = makeDepartmentAttachInfoMapper.deleteByMakeDepartmentFlag(makeDepartmentFlag);
        if(i<1){
            return ResultUtil.isFail;
        }else {
            return ResultUtil.isSuccess;
        }
    }

    @Override
    public MakeDepartmentAttachInfo selectById(String id) {
        return makeDepartmentAttachInfoMapper.selectById(id);
    }

    @Override
    public MakeDepartmentAttachInfo selectByMakeDepartmentFlag(User user) {
        String telphone = user.getTelphone();
        MakeDepartmentSimple makeDepartmentSimple = makeDepartmentService.selectByLegalTephone(telphone);
        MakeDepartmentAttachInfo makeDepartmentAttachInfo = makeDepartmentAttachInfoMapper.selectByMakeDepartmentFlag(makeDepartmentSimple.getFlag());
        return makeDepartmentAttachInfo;
    }

    @Override
    public MakeDepartmentAttachInfo selectByMakeDepartmentFlag(String makeDepartmentFlag){
        MakeDepartmentAttachInfo makeDepartmentAttachInfo = makeDepartmentAttachInfoMapper.selectByMakeDepartmentFlag(makeDepartmentFlag);
        return makeDepartmentAttachInfo;
    }
}
