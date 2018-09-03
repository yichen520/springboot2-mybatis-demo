package com.dhht.service.make.Impl;

import com.dhht.dao.IncidenceMapper;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.model.Incidence;
import com.dhht.model.User;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.service.make.MakeDepartmentIncidenceService;
import com.dhht.service.make.MakeDepartmentService;
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
@Service("incidenceService")
public class MakeDepartmentIncidenceServiceImpl implements MakeDepartmentIncidenceService {

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;
    @Autowired
    private IncidenceMapper incidenceMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MakeDepartmentIncidenceService incidenceService;

    @Override
    public int insertincidence(Incidence incidence, User user) {
        try {
            String code =makedepartmentMapper.selectDetailByName(incidence.getDepartmentName()).getDepartmentCode();
            incidence.setId(UUIDUtil.generate());
            incidence.setDepartmentCode(code);
            incidence.setSerialCode(getSerialCode(incidence.getDepartmentCode()));
            incidence.setDistrictId(user.getDistrictId());
            incidence.setCreateTime(DateUtil.getCurrentTime());
            incidence.setRecorder(user.getRealName());
            int result = incidenceMapper.insertSelective(incidence);
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
    public int updateincidence(Incidence incidence, User user) {
        try {
            incidence.setUpdateUser(user.getRealName());
            incidence.setUpdateTime(DateUtil.getCurrentTime());
            int result = incidenceMapper.updateByPrimaryKeySelective(incidence);
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
    public int deleteincidence(String id) {
        try {
            int result = incidenceMapper.deleteByPrimaryKey(id);
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
    public List<Incidence> selectInfo(IncidencePO map) {
        try {
            List<Incidence> result = incidenceMapper.selectInfo(map);
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
    public String selectMaxSerialCode() {
        return incidenceMapper.selectMaxSerialCode();
    }

    /**
     * 编号
     * @param makeDepartmentCode
     * @return
     */
    public String getSerialCode(String makeDepartmentCode){
        String code = "";
        Jedis jedis = new Jedis();
        if(redisTemplate.hasKey("serialCode")){
            code = jedis.incrBy("serialCode",1).toString();
        }else{
            String serialCode = incidenceService.selectMaxSerialCode();
            redisTemplate.opsForValue().set("serialCode",0);
            String temp = serialCode.substring(19);
            redisTemplate.opsForValue().set("serialCode",Integer.parseInt(temp));
            code = jedis.incrBy("serialCode",1).toString();
        }
        int length = code.length();
        StringBuffer stringBuffer = new StringBuffer();
        if(length<4){
            for(int i = 0;i<4-length;i++){
                stringBuffer.append("0");
            }
        }
        return makeDepartmentCode+stringBuffer.toString()+code;
    }
}
