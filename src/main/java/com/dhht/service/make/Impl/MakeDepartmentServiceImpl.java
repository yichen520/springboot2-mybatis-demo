package com.dhht.service.make.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.Makedepartment;

import com.dhht.model.UserDomain;
import com.dhht.service.make.MakeDepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "makeDepartmentService")
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Override
    public List<Makedepartment> findAllMake(){
        return makedepartmentMapper.findAllMake();
    }

    @Override
    public int addMake ( Makedepartment makedepartment){
        return makedepartmentMapper.insert(makedepartment);
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
    public PageInfo<Makedepartment> findAllMakeBySize(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Makedepartment> userDomains = makedepartmentMapper.findAllMake();
        PageInfo result = new PageInfo(userDomains);
        return result;
    }
}
