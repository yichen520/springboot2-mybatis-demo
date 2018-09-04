package com.dhht.service.suspicious.Impl;

import com.dhht.dao.EmployeeDao;
import com.dhht.dao.IncidenceMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.SuspiciousMapper;
import com.dhht.model.Employee;
import com.dhht.model.Incidence;
import com.dhht.model.Suspicious;
import com.dhht.model.User;
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
            String code =makedepartmentMapper.selectDetailByName(suspicious.getMakeDepartmentName()).getDepartmentCode();
            suspicious.setId(UUIDUtil.generate());
            suspicious.setMakeDepartmentCode(code);
            if (suspicious.getEmployeeName() != null){
                suspicious.setEmployeeCode(employeeDao.selectByName(suspicious.getEmployeeName()).getEmployeeCode());
                suspicious.setEmployeeIdcard(employeeDao.selectByName(suspicious.getEmployeeName()).getEmployeeId());
            }
            suspicious.setDistrictId(user.getDistrictId());
            suspicious.setCreateTime(DateUtil.getCurrentTime());
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
            if (!suspicious1.getEmployeeName().equals(suspicious.getEmployeeName())){
                suspicious.setEmployeeCode(employeeDao.selectByName(suspicious.getEmployeeName()).getEmployeeCode());
                suspicious.setEmployeeIdcard(employeeDao.selectByName(suspicious.getEmployeeName()).getEmployeeId());
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


}
