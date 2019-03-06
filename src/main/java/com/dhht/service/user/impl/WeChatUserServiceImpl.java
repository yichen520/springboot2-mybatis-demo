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
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author fyc 2018/12/29
 */

@Service("WeChatUserService")
@Transactional
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
    private int userCode;


    @Override
    public int sendMessage(String mobilePhone, int param) {
        Map<String, Object> map = new HashMap<>();
        String code = StringUtil.createRandomVcode();
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
        if (!stringRedisTemplate.hasKey(mobilePhone)) {
            stringRedisTemplate.opsForValue().append(mobilePhone, code);
        } else {
            stringRedisTemplate.delete(mobilePhone);
            stringRedisTemplate.opsForValue().append(mobilePhone, code);
        }
        stringRedisTemplate.expire(mobilePhone, 300, TimeUnit.SECONDS);
        // expire(mobilePhone);
        boolean result = smsSendService.sendSingleMsgByTemplate(mobilePhone, param, params);
        if (result) {
            return ResultUtil.isSendVerificationCode;
        } else {
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
    public Map<String, Object> isLogin(String mobilePhone, String inputVerificationCode, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String verificationCode = stringRedisTemplate.opsForValue().get(mobilePhone);
        if (verificationCode.equals(inputVerificationCode)) {
            map.put("status", "ok");
            map.put("message", "登录成功");
            map.put("mobilePhone", mobilePhone);
            request.getSession().setAttribute("mobilePhone", mobilePhone);
            WeChatUser weChatUser = weChatUserMapper.selectByTelPhone(mobilePhone);
            if (weChatUser == null) {
                WeChatUser weChatUser1 = new WeChatUser();
                weChatUser1.setId(UUIDUtil.generate());
                weChatUser1.setName(testNum(6));
                weChatUser1.setTelphone(mobilePhone);
                weChatUser1.setCreateTime(DateUtil.getCurrentTime());
                weChatUserMapper.insertSelective(weChatUser1);
                map.put("weChatUser", weChatUser1);
                request.getSession().setAttribute("weChatUser", weChatUser1);
            } else {
                map.put("weChatUser", weChatUser);
                request.getSession().setAttribute("weChatUser", weChatUser);
            }

            return map;
        } else {
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message", "登录失败！请核对验证码！");
            return map;
        }
    }

    /**
     * 用户修改
     *
     * @param weChatUser
     * @param id
     * @return
     */
    @Override
    public int updateWeChatUser(WeChatUser weChatUser, String id) {
        int weChatUserUpdate = weChatUserMapper.updateByPrimaryKeySelective(weChatUser);
        if (weChatUserUpdate < 0) {
            return ResultUtil.isFail;
        } else {
            return ResultUtil.isSuccess;
        }
    }

    /**
     * 用户查询
     *
     * @param telphone
     * @return
     */
    @Override
    public Map<String, Object> selectWeChatUser(String telphone) {
        Map<String, Object> map = new HashMap<>();
        WeChatUser weChatUser = weChatUserMapper.selectByTelPhone(telphone);
        if (weChatUser == null) {
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message", "数据不存在");
            return map;
        }
        map.put("status", "ok");
        map.put("message", "查询成功");
        map.put("weChatUser", weChatUser);
        return map;
    }

    @Override
    public int isRightVerificationCode(String phone, String inputVerificationCode) {
        try {
            String verificationCode = stringRedisTemplate.opsForValue().get(phone);
            if(verificationCode!=null){
                if(verificationCode.equals(inputVerificationCode)){
                    return ResultUtil.isRightCode;
                }else {
                    return ResultUtil.isCodeError;
                }
            }else {
                return ResultUtil.isCodeError;
            }
        }catch (Exception e){
            return ResultUtil.isCodeError;
        }
    }

    @Override
    public WeChatUser selectById(String id) {
        return weChatUserMapper.selectByPrimaryKey(id);
    }


    /**
     * 设置过期时间五分钟
     *
     * @param key
     */
    private void expire(String key) {
        Jedis jedis = new Jedis();
        jedis.expire(key, 300);
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
        String rightCaptcha = stringRedisTemplate.opsForValue().get(useDepartmentRegister.getMobilePhone());
        if (!useDepartmentRegister.getCaptcha().equals(rightCaptcha)) {
            return ResultUtil.isCodeError;
        }

        WeChatUser tempWeChatUser = weChatUserMapper.selectByTelPhone(useDepartmentRegister.getMobilePhone());
        UseDepartment useDepartment = useDepartmentDao.selectCompanyInfo(useDepartmentRegister);

        if (useDepartment != null && useDepartment.getName() != null) {
            if(tempWeChatUser==null) {
                WeChatUser weChatUser = new WeChatUser();
                weChatUser.setCreateTime(DateUtil.getCurrentTime());
                weChatUser.setId(UUIDUtil.generate());
                weChatUser.setTelphone(useDepartmentRegister.getMobilePhone());
                weChatUser.setName(useDepartmentRegister.getLegalName());
                weChatUser.setCompany(useDepartment.getFlag());
                weChatUser.setCompanyName(useDepartment.getName());
                weChatUser.setCompanyAccout(true);
                weChatUser.setCertificateNo(useDepartmentRegister.getLegalId());
                int result = weChatUserMapper.insert(weChatUser);
                if (result > 0) {
                    return ResultUtil.isSuccess;
                } else {
                    return ResultUtil.isFail;
                }
            }else{
                if(tempWeChatUser.isCompanyAccout()){
                    return ResultUtil.isRepairCompany;
                }else if(tempWeChatUser.getCompany()==null||tempWeChatUser.getCompany().equals("")){
                    tempWeChatUser.setName(useDepartmentRegister.getLegalName());
                    tempWeChatUser.setCompany(useDepartment.getFlag());
                    tempWeChatUser.setCompanyName(useDepartment.getName());
                    tempWeChatUser.setCompanyAccout(true);
                    tempWeChatUser.setCertificateNo(useDepartmentRegister.getLegalId());
                    int result = weChatUserMapper.updateByPrimaryKey(tempWeChatUser);
                    if (result > 0) {
                        return ResultUtil.isSuccess;
                    } else {
                        return ResultUtil.isFail;
                    }
                }else if(!tempWeChatUser.isCompanyAccout()&&tempWeChatUser.getCompany()!=null||tempWeChatUser.getCompany()!=""){
                    if(tempWeChatUser.getCompany().equals(useDepartment.getFlag())) {
                        tempWeChatUser.setName(useDepartmentRegister.getLegalName());
                        tempWeChatUser.setCompanyAccout(true);
                        tempWeChatUser.setCertificateNo(useDepartmentRegister.getLegalId());
                        int result = weChatUserMapper.updateByPrimaryKey(tempWeChatUser);
                        if (result > 0) {
                            return ResultUtil.isSuccess;
                        } else {
                            return ResultUtil.isFail;
                        }
                    }else {
                        return ResultUtil.isBindingOtherCompay;
                    }
                }else {
                    return ResultUtil.isError;
                }
            }
        } else {
            return ResultUtil.isNoMatchUseDepartment;
        }
    }
}
