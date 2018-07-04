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

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/6/22 create by fyc
 */
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

    /**
     * 分页查找所有民警
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordPolice> selectAllPolice(int pageSize, int pageNum) {
        List<RecordPolice> recordPolice = recordPoliceMapper.selectAllPolice();
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RecordPolice> pageInfo = new PageInfo(recordPolice);
        return pageInfo;
    }

    /**
     * 分页根据备案单位编号查找民警
     * @param code
     * @param pageSum
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordPolice> selectByOfficeCode(String code, int pageSum, int pageNum) {
        List<RecordPolice> recordPolice = recordPoliceMapper.selectByOfficeCode(code);
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<RecordPolice> pageInfo = new PageInfo(recordPolice);
        return pageInfo;
    }


    /**
     * 根据权限分页查找备案单位
     * @param officeDistrictId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<RecordPolice> selectByRole(String officeDistrictId, int pageSize, int pageNum) {
        List<RecordPolice> recordPolice = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(officeDistrictId);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordPolice = recordPoliceMapper.selectByRole(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            recordPolice = recordPoliceMapper.selectByRole(districtIds[0]+districtIds[1]);
        }else {
            recordPolice = recordPoliceMapper.selectByRole(officeDistrictId);
        }
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RecordPolice> pageInfo = new PageInfo(recordPolice);
        return pageInfo;
    }

    /**
     * 根据电话删除民警
     * @param phone
     * @return
     */
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

    /**
     * 新增民警
     * @param record
     * @return
     */
    @Override
    public boolean insert(RecordPolice record) {
        record.setId(UUIDUtil.generate());
        RecordDepartment recordDepartment = getRecordDepartment(record.getOfficeCode());
        record.setOfficeName(recordDepartment.getDepartmentName());
        record.setOfficeDistrict(recordDepartment.getDepartmentAddress());
         int r = recordPoliceMapper.insert(record);
         int u = userService.insert(setUser(record,1));
        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;

    }

    /**
     * 根据警号查找民警
     * @param code
     * @return
     */
    @Override
    public RecordPolice selectByPoliceCode(String code) {
        RecordPolice recordPolice = recordPoliceMapper.selectByPoliceCode(code);
        return recordPolice;
    }

    /**
     * 修改民警
     * @param record
     * @return
     */
    @Override
    public boolean updateByPrimaryKey(RecordPolice record) {
        RecordDepartment recordDepartment = getRecordDepartment(record.getOfficeCode());
        record.setOfficeName(recordDepartment.getDepartmentName());
        record.setOfficeDistrict(recordDepartment.getDepartmentAddress());
        int u = userDao.update(setUser(record,2));
        int r = recordPoliceMapper.updateByPrimaryKey(record);

        if(r+u==2){
            return true;
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    /**
     * 根据Id查找民警
     * @param id
     * @return
     */
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
                //System.out.println(oldDate.toString());
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
