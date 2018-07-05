package com.dhht.service.make.Impl;

import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.UserDao;
import com.dhht.model.*;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.service.user.UserService;
import com.dhht.util.DateUtil;
import com.dhht.util.MD5Util;
import com.dhht.util.UUIDUtil;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dhht.service.user.impl.UserServiceImpl.createRandomVcode;


/**
 * 2018/7/2 create by fyc
 */
@Service(value = "makeDepartmentService")
@Transactional
public class MakeDepartmentServiceImpl implements MakeDepartmentService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPasswordService userPasswordService;

    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");

    private String code = null;

    /**
     * 根据区域ID查询制作单位
     * @param districtId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<MakeDepartmentSimple> selectByDistrictId(String districtId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<MakeDepartmentSimple> list = new ArrayList<>();
        String districtIds[] = StringUtil.DistrictUtil(districtId);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = makedepartmentMapper.selectByDistrictId(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = makedepartmentMapper.selectByDistrictId(districtIds[0]+districtIds[1]);
        }else {
            list = makedepartmentMapper.selectByDistrictId(districtId);
        }
        PageInfo<MakeDepartmentSimple> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 根据Id查询详细的制作单位资料
     * @param id
     * @return
     */
    @Override
    public Makedepartment selectDetailById(String id) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        return makedepartment;
    }

    /**
     * 添加制作单位
     * @param makedepartment
     * @return
     */
    @Override
    public int insert(Makedepartment makedepartment) {
        if(isInsert(makedepartment)){
            return 3;
        }
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(simpleDateFormat.format(System.currentTimeMillis()));
        makedepartment.setFlag(UUIDUtil.generate());
        makedepartment.setVersion(1);
        User user =setUserByType(makedepartment,1);
        int m = makedepartmentMapper.insert(makedepartment);
        int u = userDao.addUser(user);
        if(m+u==2){
            userPasswordService.sendMessage(user.getTelphone(),code);
            return 1;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 2;
        }
    }

    /**
     * 更新制作单位
     * @param makedepartment
     * @return
     */
    @Override
    public int update(Makedepartment makedepartment) {
        int d = makedepartmentMapper.deleteHistoryByID(makedepartment.getId());
        if(d==0){
            return 5;
        }
        if(isInsert(makedepartment)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 3;
        }
        makedepartment.setId(UUIDUtil.generate());
        makedepartment.setVersionTime(simpleDateFormat.format(System.currentTimeMillis()));
        makedepartment.setFlag(makedepartment.getFlag());
        makedepartment.setVersion(makedepartment.getVersion()+1);
        User user =setUserByType(makedepartment,2);
        int m = makedepartmentMapper.insert(makedepartment);
        int u = userDao.update(setUserByType(makedepartment,2));
        if(m+d+u==3){
            return 1;
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 2;
        }
    }

    /**
     * 删除制作单位
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        Makedepartment makedepartment = makedepartmentMapper.selectDetailById(id);
        makedepartment.setLogoutTime(DateUtil.getCurrentTime());
        if(setUserByType(makedepartment,3)==null){
            int m = makedepartmentMapper.deleteById(makedepartment);
            if(m==1){
                return 1;
            }else {
                return 2;
            }
        }else {
            int u = userDao.deleteByTelphone(makedepartment.getLegalTelphone());
            int m = makedepartmentMapper.deleteById(makedepartment);
            if (u + m == 2) {
                return 1;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 2;
            }
        }
    }

    /**
     * 查看操作历史
     * @param flag
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Makedepartment> selectHistory(String flag,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Makedepartment> makedepartments = makedepartmentMapper.selectByFlag(flag);
        PageInfo<Makedepartment> result = new PageInfo<>(makedepartments);
        return result;
    }

    /**
     * 设置user
     * @param makedepartment
     * @param type
     * @return
     */
    public User setUserByType(Makedepartment makedepartment,int type){
        User user = new User();
        switch (type){
            case 1:
                user.setId(UUIDUtil.generate());
                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
                user.setRoleId("ZZDW");
                user.setDistrictId(makedepartment.getDepartmentAddress());
                user.setRealName(makedepartment.getDepartmentName());
                code = createRandomVcode();
                user.setPassword(MD5Util.toMd5(code));
                user.setTelphone(makedepartment.getLegalTelphone());
                break;
            case 2:
                Makedepartment oldDate =makedepartmentMapper.selectDetailById(makedepartment.getId());
                user = userDao.findByTelphone(makedepartment.getLegalTelphone());
                user.setUserName("ZZDW"+makedepartment.getLegalTelphone());
                user.setDistrictId(makedepartment.getDepartmentAddress());
                user.setRealName(makedepartment.getDepartmentName());
                user.setTelphone(makedepartment.getLegalTelphone());
                break;
            case 3:
                user = userDao.findByTelphone(makedepartment.getLegalTelphone());
                break;
        }
        return user;
    }

    /**
     * 判断是否有重复Code
     * @param makedepartment
     * @return
     */
    public boolean isInsert(Makedepartment makedepartment){
        List<MakeDepartmentSimple> list = makedepartmentMapper.selectByCode(makedepartment);
        if(list.size()>0){
            return true;
        }
        return false;
    }

}
