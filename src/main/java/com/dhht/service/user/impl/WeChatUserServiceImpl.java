package com.dhht.service.user.impl;

import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
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

/**
 * @author fyc 2018/12/29
 */

@Service("WeChatUserService")
public class WeChatUserServiceImpl implements WeChatUserService {

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${sms.template.insertUser}")
    private int userCode ;

    @Override
    public int sendMessage(String mobilePhone) {
        Map<String,Object> map = new HashMap<>();
        String code = StringUtil.createRandomVcode();
        ArrayList<String> params = new ArrayList<String>();
        params.add(mobilePhone);
        params.add(code);
        if(!stringRedisTemplate.hasKey(mobilePhone)){
            stringRedisTemplate.opsForValue().append(mobilePhone,code);

        }else {
            stringRedisTemplate.delete(mobilePhone);
            stringRedisTemplate.opsForValue().append(mobilePhone,code);
        }
        expire(mobilePhone);
        boolean result = smsSendService.sendSingleMsgByTemplate(mobilePhone,150656,params);
        if(result){
           return ResultUtil.isSendVerificationCode;
        }else {
            return ResultUtil.isError;
        }
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
            return map;
        }else {
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message","登录失败！请核对验证码！");
            return map;
        }
    }

    /**
     * 设置过期时间五分钟
     * @param key
     */
    private void expire(String key){
        Jedis jedis = new Jedis();
        jedis.expire(key,300);
    }
}
