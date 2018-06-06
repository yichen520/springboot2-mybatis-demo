package com.dhht.service.make.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.UserInfoMapper;
import com.dhht.model.Makedepartment;

import com.dhht.model.UserDomain;
import com.dhht.model.UserInfo;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service(value = "makeDepartmentService")
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public List<Makedepartment> findAllMake(){
        return makedepartmentMapper.findAllMake();
    }

    @Override
    @Transactional
    public int addMake ( Makedepartment makedepartment){

      int flag1 = makedepartmentMapper.insert(makedepartment);

      UserInfo userInfo = new UserInfo();
       userInfo.setName(makedepartment.getDepartmentName());
       userInfo.setPassword(makedepartment.getPassword());
       userInfo.setState(makedepartment.getDepartmentStatus());
        userInfo. setUsername(makedepartment.getMakedepartmentCode());
        userInfo.setType("1");
        int flag2 = userInfoMapper.insert(userInfo);
        if (flag1 == 1 && flag2 ==1){
            return 1;
        }else {
            return 0;
        }


    }
    @Override
    public int deletemake(String code){
        return makedepartmentMapper.deleteByPrimaryKey(code);
    }

    @Override
    public int updatemake(Makedepartment makedepartment){
        return makedepartmentMapper.updateByPrimaryKeySelective(makedepartment);
    }

    @Override
    public int validateUserAccout(String code){
        return makedepartmentMapper.validateUserAccout(code);
    }


    @Override
    public PageInfo<Makedepartment> findAllMakeBySize(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Makedepartment> userDomains = makedepartmentMapper.findAllMake();
        PageInfo result = new PageInfo(userDomains);
        return result;
    }
}
