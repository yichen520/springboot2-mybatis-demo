package com.dhht.service.seal.Impl;

import com.dhht.dao.ResourceMapper;
import com.dhht.dao.SealMapper;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.Menus;
import com.dhht.model.Resource;
import com.dhht.model.Seal;
import com.dhht.model.UseDepartment;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.seal.SealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("sealService")
public class SealServiceImpl implements SealService {
    @Autowired
    private SealMapper sealMapper;

    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Override
   public UseDepartment isrecord(String useDepartmentCode){
     return useDepartmentDao.selectByCode(useDepartmentCode);
   }

   @Override
   @Transactional
   public int insert(Seal seal){
        //增加文件上传，操作记录和印章本身表
       return 1;
   }



}
