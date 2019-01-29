package com.dhht.service.user.impl;

import com.dhht.common.JsonObjectBO;
import com.dhht.dao.MakedepartmentMapper;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.dao.WeChatUserMapper;
import com.dhht.model.UseDepartment;
import com.dhht.model.UseDepartmentRegister;
import com.dhht.model.WeChatUser;
import com.dhht.dao.SMSCodeDao;
import com.dhht.model.SMSCode;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.DateUtil;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author fyc 2018/12/29
 */

@Service("WeChatUserService")
public class WeChatUserServiceImpl implements WeChatUserService {

    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private UseDepartmentDao useDepartmentDao;

    @Value("${sms.template.insertUser}")
    private int userCode ;



    @Override
    public int sendMessage(String mobilePhone,int param) {
        Map<String,Object> map = new HashMap<>();
        String code = StringUtil.createRandomVcode();
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
        if(!stringRedisTemplate.hasKey(mobilePhone)){
            stringRedisTemplate.opsForValue().append(mobilePhone,code);
        }else {
            stringRedisTemplate.delete(mobilePhone);
            stringRedisTemplate.opsForValue().append(mobilePhone,code);
        }
        expire(mobilePhone);
        boolean result = smsSendService.sendSingleMsgByTemplate(mobilePhone,param,params);
        if(result){
//            SMSCode smscode= smsCodeDao.getSms(mobilePhone);
//            if(smscode==null){
//                smscode = new SMSCode();
//                smscode.setId(UUIDUtil.generate());
//                smscode.setLastTime(System.currentTimeMillis());
//                smscode.setPhone(mobilePhone);
//                smscode.setSmscode(code);
//                smsCodeDao.save(smscode);
//            }else{
//                smscode.setLastTime(System.currentTimeMillis());
//                smscode.setSmscode(code);
//                smsCodeDao.update(smscode);
//            }
           return ResultUtil.isSendVerificationCode;
        }else {
            return ResultUtil.isError;
        }
    }

    public String testNum(int num) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    @Override
    public Map<String,Object> isLogin(String mobilePhone, String inputVerificationCode,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        String verificationCode = stringRedisTemplate.opsForValue().get(mobilePhone);
        if(verificationCode.equals(inputVerificationCode)){
            map.put("status", "ok");
            map.put("message","登录成功");
            map.put("mobilePhone",mobilePhone);
            request.getSession().setAttribute("mobilePhone",mobilePhone);

            WeChatUser weChatUser = weChatUserMapper.selectByTelPhone(mobilePhone);
            if(weChatUser==null) {
                WeChatUser weChatUser1 = new WeChatUser();
                weChatUser1.setId(UUIDUtil.generate());
                weChatUser1.setName(testNum(6));
                weChatUser1.setTelphone(mobilePhone);
                weChatUser1.setCreateTime(DateUtil.getCurrentTime());
                weChatUserMapper.insertSelective(weChatUser1);
                map.put("weChatUser", weChatUser1);
            }
            else {
                map.put("weChatUser", weChatUser);
            }
            request.getSession().setAttribute("weChatUser",weChatUser);
            return map;
        }else {
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message","登录失败！请核对验证码！");
            return map;
        }
    }

    /**
     * 用户修改
     * @param weChatUser
     * @param id
     * @return
     */
    @Override
    public int updateWeChatUser(WeChatUser weChatUser, String id) {
       int weChatUserUpdate = weChatUserMapper.updateByPrimaryKeySelective(weChatUser);
       if(weChatUserUpdate<0){
           return ResultUtil.isFail;
       }else{
           return ResultUtil.isSuccess;
       }
    }

    /**
     * 用户查询
     * @param telphone
     * @return
     */
    @Override
    public Map<String,Object>  selectWeChatUser(String telphone) {
        Map<String,Object> map = new HashMap<>();
        WeChatUser weChatUser = weChatUserMapper.selectByTelPhone(telphone);
        if(weChatUser==null){
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message","数据不存在");
            return map;
        }
        map.put("status", "ok");
        map.put("message","查询成功");
        map.put("weChatUser",weChatUser);
        return map;
    }


    /**
     * 设置过期时间五分钟
     * @param key
     */
    private void expire(String key){
        Jedis jedis = new Jedis();
        jedis.expire(key,300);
    }

    @Override
    public UseDepartment bindCompany(UseDepartment useDepartment) {
        return useDepartmentDao.selectCompany(useDepartment);
    }

    @Override
    public int updateWeChatUserInfo(WeChatUser weChatUser) {
        return weChatUserMapper.updateByPrimaryKeySelective(weChatUser);
    }

    @Override
    public WeChatUser isExistTelphone(String telphone) {
        return weChatUserMapper.selectByTelPhone(telphone);
    }

    @Override
    public int companyRegister(UseDepartmentRegister useDepartmentRegister) {

        UseDepartment useDepartment = useDepartmentDao.selectCompanyInfo(useDepartmentRegister);
        if(useDepartment.getName()!=null){
            //新增到用户表
            WeChatUser weChatUser =new WeChatUser();
            weChatUser.setCreateTime(DateUtil.getCurrentTime());
            weChatUser.setId(UUIDUtil.generate());
            weChatUser.setTelphone(useDepartmentRegister.getMobliePhone());
            weChatUser.setCompany(useDepartment.getId());
            weChatUser.setCompanyName(useDepartment.getName());
          int result =  weChatUserMapper.insertSelective(weChatUser);
          //插入到一张新表（如果做企业单位添加员工时，可用）
           if(result>0){
               return ResultUtil.isSuccess;
           }else {
               return ResultUtil.isFail;
           }
        }
        else {
            return ResultUtil.isNoMatchUseDepartment;
        }
    }
}
