package com.dhht.service.suspicious.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.IncidenceMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SuspiciousMapper;
import com.dhht.model.*;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.model.pojo.SuspiciousPO;
import com.dhht.service.make.MakeDepartmentIncidenceService;
import com.dhht.service.suspicious.SuspiciousService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;

@Transactional
@Service("suspiciousService")
public class SuspiciousServiceImpl implements SuspiciousService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private SuspiciousMapper suspiciousMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MakeDepartmentIncidenceService suspiciousService;
    @Autowired
    private MakedepartmentMapper makedepartmentMapper;
    @Override
    public int insertsuspicious(Suspicious suspicious, User user) {
        try {
            suspicious.setId(UUIDUtil.generate());
            if (suspicious.getEmployeeCode() != null){
                suspicious.setEmployeeIdcard(employeeDao.selectByCode(suspicious.getEmployeeCode()).getEmployeeId());
            }
            suspicious.setDistrictId(user.getDistrictId());
            suspicious.setCreateTime(DateUtil.getCurrentTime());
            suspicious.setUpdateTime(DateUtil.getCurrentTime());
            suspicious.setRecorder(user.getRealName());
            suspicious.setUpdateUser(user.getRealName());
            int result = suspiciousMapper.insertSelective(suspicious);
            if(result == 1){
                return ResultUtil.isSuccess;
            }else {
                return ResultUtil.isFail;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.isError;
        }

    }

    @Override
    public int updatesuspicious(Suspicious suspicious, User user) {
        try {
            Suspicious suspicious1 = suspiciousMapper.selectByPrimaryKey(suspicious.getId());
            if (suspicious1.getEmployeeName()!=null){
                if (!suspicious1.getEmployeeName().equals(suspicious.getEmployeeName())){
                    suspicious.setEmployeeIdcard(employeeDao.selectByCode(suspicious.getEmployeeCode()).getEmployeeId());
                }
            }else {
                if (suspicious.getEmployeeName()!=null){
                    suspicious.setEmployeeIdcard(employeeDao.selectByCode(suspicious.getEmployeeCode()).getEmployeeId());
                }
            }

            suspicious.setUpdateUser(user.getRealName());
            suspicious.setUpdateTime(DateUtil.getCurrentTime());
            int result = suspiciousMapper.updateByPrimaryKeySelective(suspicious);
            if(result == 1){
                return ResultUtil.isSuccess;
            }else {
                return ResultUtil.isFail;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.isError;
        }
    }

    @Override
    public int deletesuspicious(String id) {
        try {
            int result = suspiciousMapper.deleteByPrimaryKey(id);
            if(result == 1){
                return ResultUtil.isSuccess;
            }else {
                return ResultUtil.isFail;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.isError;
        }
    }

    @Override
    public List<Suspicious> selectInfo(SuspiciousPO map) {
        try {
            List<Suspicious> result = suspiciousMapper.selectInfo(map);
            if(result !=null){
                return result;
            }else {
                return null ;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    @Override
    public int weChatInsertSuspicious(Suspicious suspicious, WeChatUser user, String makeDepartmentCode) {
        try {
            suspicious.setId(UUIDUtil.generate());
            MakeDepartmentSimple makedepartment = makedepartmentMapper.selectByDepartmentCode(makeDepartmentCode);
            if(makedepartment!=null){
                suspicious.setDistrictId(makedepartment.getDepartmentAddress());
            }
            suspicious.setMakeDepartmentCode(makeDepartmentCode);
            suspicious.setCreateTime(DateUtil.getCurrentTime());
            suspicious.setUpdateTime(DateUtil.getCurrentTime());
            suspicious.setRecorder(user.getName());
            suspicious.setUpdateUser(user.getName());
            suspicious.setLoginTelphone(user.getTelphone());
            int result = suspiciousMapper.insertSelective(suspicious);
            if(result == 1){
                return ResultUtil.isSuccess;
            }else {
                return ResultUtil.isFail;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return ResultUtil.isError;
        }

    }

    @Override
    public List<Suspicious> selectAll(String loginTelphone) {
        List<Suspicious> suspicious = suspiciousMapper.selectAll(loginTelphone);
        return suspicious;
    }

    @Override
    public Suspicious selectById(String id) {
        Suspicious suspicious = suspiciousMapper.selectByPrimaryKey(id);
       return suspicious;
    }


}
