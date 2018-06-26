package com.dhht.service.police.Imp;

import com.dhht.dao.RecordDepartmentMapper;
import com.dhht.dao.RecordPoliceMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.RecordDepartment;
import com.dhht.model.RecordPolice;
import com.dhht.model.User;
import com.dhht.service.police.PoliceService;
import com.dhht.service.user.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.dhht.util.*;

import java.util.List;

@Service(value = "PoliceService")
@Transactional
public class PoliceServiceImp implements PoliceService{

    @Autowired
    private RecordPoliceMapper recordPoliceMapper;
   @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RecordDepartmentMapper recordDepartmentMapper;

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
    public boolean deleteByTelphone(String phone) {
       int p =  recordPoliceMapper.deleteByTelphone(phone);
       int u =  userDao.deleteByTelphone(phone);
       if(p+u==2){
           return true;
       }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }


    @Override
    public boolean insert(RecordPolice record) {
         record.setId(UUIDUtil.generate());
        RecordDepartment recordDepartment = getRecordDepartment(record.getOfficeCode());
        record.setOfficeName(recordDepartment.getDepartmentName());
        record.setOfficeDistrict(recordDepartment.getDepartmentAddress());
         int r = recordPoliceMapper.insert(record);
         int u = userService.insert(setUser(record,1)).getCode();
        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;

    }

    @Override
    public RecordPolice selectByPoliceCode(String code) {
        RecordPolice recordPolice = recordPoliceMapper.selectByPoliceCode(code);
        return recordPolice;
    }

    @Override
    public boolean updateByPrimaryKey(RecordPolice record) {
        int u = userDao.update(setUser(record,2));
        RecordDepartment recordDepartment = getRecordDepartment(record.getOfficeCode());
        record.setOfficeName(recordDepartment.getDepartmentName());
        record.setOfficeDistrict(recordDepartment.getDepartmentAddress());
        int r = recordPoliceMapper.updateByPrimaryKey(record);

        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    @Override
    public RecordPolice selectById(String id) {
        RecordPolice recordPolice = recordPoliceMapper.selectById(id);
        return recordPolice;
    }

    /**
     * 去设置User
     * @param recordPolice
     * @param type
     * @return
     */
    public User setUser(RecordPolice recordPolice,int type) {
        User user = new User();
        switch (type) {
            case 1:
                System.out.println(1);
                user.setId(UUIDUtil.generate());
                //user.setPassword(MD5Util.toMd5("123456"));
                user.setUserName(recordPolice.getTelphone());
                user.setRealName(recordPolice.getPoliceName());
                user.setTelphone(recordPolice.getTelphone());
                user.setRoleId("MJ");
                user.setDistrictId(recordPolice.getOfficeDistrict());
                break;

            case 2:
                String id = recordPolice.getId();
                RecordPolice oldDate = recordPoliceMapper.selectById(id);
                System.out.println(oldDate.toString());
                user = userDao.findByTelphone(oldDate.getTelphone());
                user.setUserName(recordPolice.getTelphone());
                user.setRealName(recordPolice.getPoliceName());
                user.setTelphone(recordPolice.getTelphone());
                user.setDistrictId(recordPolice.getOfficeDistrict());
                break;

            default:
                break;
        }
        return user;
    }

    /**
     * 获取备案单位信息
     * @param code
     * @return
     */
    public RecordDepartment getRecordDepartment(String code){
        return recordDepartmentMapper.selectByCode(code);
    }
}
