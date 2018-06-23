package com.dhht.service.police.Imp;

import com.dhht.dao.RecordPoliceMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.police.PoliceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "PoliceService")
@Transactional
public class PoliceServiceImp implements PoliceService{

    @Autowired
    private RecordPoliceMapper recordPoliceMapper;
    @Autowired
    private UserDao userDao;
    @Override
    public PageInfo<RecordPolice> selectAllPolice(int pageSum, int pageNum) {
        List<RecordPolice> recordPolice = recordPoliceMapper.selectAllPolice();
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<RecordPolice> pageInfo = new PageInfo(recordPolice);
        return pageInfo;
    }

    @Override
    public PageInfo<RecordPolice> selectByOfficeCode(String code, int pageSum, int pageNum) {
        List<RecordPolice> recordPolice = recordPoliceMapper.selectByOfficeCode(code);
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<RecordPolice> pageInfo = new PageInfo(recordPolice);
        return pageInfo;
    }

    @Override
    public boolean deleteByPrimaryKey(String id) {
       int p =  recordPoliceMapper.deleteByPrimaryKey(id);
       int u =  userDao.delete(id);
       if(p+u==2){
           return true;
       }
       return false;
    }

    @Override
    public int insert(RecordPolice record) {
       return recordPoliceMapper.insert(record);
    }

    @Override
    public RecordPolice selectByPrimaryKey(String key) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(RecordPolice record) {
        return 0;
    }

    public User setUserByType(RecordPolice recordPolice, int type){
        User user = new User();
        switch (type){
            //添加用户
            case 1:

            case 2:

        }
        return user;
    }
}
