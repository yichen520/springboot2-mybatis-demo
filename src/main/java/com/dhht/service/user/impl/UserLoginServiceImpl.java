package com.dhht.service.user.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smutil.SM3Util;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 崔杨 on 2017/8/16.
 */
@Service(value = "userLoginService")
@Transactional
public class UserLoginServiceImpl implements UserLoginService {

    private static Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private APKVersionMapper apkVersionMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private AppReportLogMapper appReportLogMapper;

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Autowired
    private StringRedisTemplate template;
    @Value("${loginError.Time}")
    private long loginErrorTime ;
    @Value("${loginError.Date}")
   private long loginErrorDate;
    @Value("${expireTime}")
    private long expireTime;
    @Value("${smsexpireTime}")
    private long smsexpireTime;

    /**
     * 6位简单密码
     *
     * @return
     */
    public static String createRandomVcode() {
        //密码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }

    /**
     * 验证账号和密码
     * @param user
     * @return
     */
    @Override
    public User validate(User user){
        String userAccount = StringUtil.stringNullHandle(user.getUserName());
        String password = StringUtil.stringNullHandle(user.getPassword());
        User loginUser = userDao.findByUserName(userAccount);
        boolean result = SM3Util.verify(password,loginUser.getPassword());
            if(result){
                if(shilUtil.shiled(loginUser.getCarand(),12345,12345,12345,12345)==user.getCarand()) {
                    return loginUser;
                }
            }
        return new User();
    }


    /**
     * 检查手机号和ID
     * @param smsCode
     * @return
     */
    @Override
    public JsonObjectBO checkPhoneAndIDCard(SMSCode smsCode) {
        Long nowtime =System.currentTimeMillis();

        int count = userDao.findByPhone(smsCode.getPhone());
        if(count>0){
            return JsonObjectBO.error("手机号或身份证已被注册");
        }
        SMSCode sms = smsCodeDao.getSms(smsCode.getPhone());
        if (sms ==null){
            return JsonObjectBO.error("请点击发送验证码");
        }
        if(!sms.getSmscode().equals(smsCode.getSmscode())){
            return JsonObjectBO.error("验证码错误");
        }
        if(nowtime-sms.getLastTime()>smsexpireTime){
            return JsonObjectBO.error("验证码超时，请重新发送");
        }
        return JsonObjectBO.ok("效验通过");

    }
    @Override
    public JsonObjectBO checkPhone(SMSCode smsCode) {
        Long nowtime =System.currentTimeMillis();

        SMSCode sms = smsCodeDao.getSms(smsCode.getPhone());
        if (sms ==null){
            return JsonObjectBO.error("请点击发送验证码");
        }
        if(!sms.getSmscode().equals(smsCode.getSmscode())){
            return JsonObjectBO.error("验证码错误");
        }
        if(nowtime-sms.getLastTime()>smsexpireTime){
            return JsonObjectBO.error("验证码超时，请重新发送");
        }
        return JsonObjectBO.ok("效验通过");

    }

    /**
     * ca随机数
     * @param username
     * @return
     */
    @Override
    public int caRand(String username) {
        int rand = shilUtil.rand();
        User user = usersMapper.validateCurrentuser(username);
        if(user==null){
            return ResultUtil.isFail;
        }else{
            user.setCarand(rand);
            int updateUser = usersMapper.updateByPrimaryKeySelective(user);
            if(updateUser<0){
                return ResultUtil.isFail;
            }else{
                return rand;
            }
        }
    }

    /**
     * 验证用户 输入错误次数等
     * @param request
     * @param userDomain
     * @return
     */
    @Override
    public Map<String, Object> validateUser(HttpServletRequest request, UserDomain userDomain) {
        Map<String,Object> map=new HashMap<>();
        try {
            User user1= new User();
            user1.setPassword(userDomain.getPassword());
            user1.setUserName(userDomain.getUsername());
            user1.setCarand(userDomain.getCaNum());
            User user = validate(user1);
            User currentUser = usersMapper.validateCurrentuser(userDomain.getUsername());

            if(user==null && currentUser!=null ){
                //更新登录错误次数
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long  a = sdf.parse(sdf.format(new Date())).getTime();
                long b =sdf.parse(sdf.format(currentUser.getLoginTime())).getTime();
                long m = a - b;
                //如果现在的登录时间大于数据库最后登录时间60分钟   则错误登录次数是1
                long errorTimes ;
                if (( m / (1000 * 60  )>loginErrorDate)){
                    userDao.updateErrorTimesZero(userDomain.getUsername());
                   errorTimes = 4;
                }else {
                    userDao.updateErrorTimes(userDomain.getUsername());
                    errorTimes = loginErrorTime - (currentUser.getLoginErrorTimes()+1);
                }
                if (errorTimes<1){
                    map.put("status", "error");
                    map.put("currentAuthority", "guest");
                    map.put("message","该用户登录错误超过5次，请1小时后重试！");
                    return map;
                }
                map.put("status", "error");
                map.put("currentAuthority","guest");
                map.put("message","账号密码错误,你还可以输入"+errorTimes+"次");
                return map;
            }
            if (user.getIsLocked()){
                map.put("status", "error");
                map.put("currentAuthority", "guest");
                map.put("message","该用户已被锁定，请联系管理员！");
                return map;
            }else {
                if (currentUser.getLoginErrorTimes()>=loginErrorTime){
                    map.put("status", "error");
                    map.put("currentAuthority", "guest");
                    map.put("message","该用户登录错误超过5次，请1小时后重试！");
                    return map;
                }
            }
            User user2 =new User();
            user2.setLoginTime(new Date());
            user2.setUserName(userDomain.getUsername());
            user2.setLoginErrorTimes(0);
            userDao.updateUser(user2);
            map.put("status", "ok");
            map.put("currentAuthority", user.getRoleId());
            map.put("message","登录成功");

            List<String> ids = roleResourceDao.selectMenuResourceByID(user.getRoleId());
            List<String> resourceId = roleResourceDao.selectResourceByID(user.getRoleId());
            List<Menus> menus = resourceService.findMenusByRole(ids);
            List<Resource> resources = resourceService.findResourceByRole(resourceId);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("menus", menus);
            request.getSession().setAttribute("resources", resources);
            return map;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message","登录失败！请核对用户名和账号！");
            return map;
        }
    }

    @Override
    public JsonObjectBO validateAppUser(HttpServletRequest request, UserDomain userDomain) {
        try {
            User user1= new User();
            user1.setPassword(userDomain.getPassword());
            user1.setUserName(userDomain.getUsername());
            User user = validate(user1);
            User currentUser = usersMapper.validateCurrentuser(userDomain.getUsername());

            if(user==null && currentUser!=null ){
                //更新登录错误次数
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long  a = sdf.parse(sdf.format(new Date())).getTime();
                long b =sdf.parse(sdf.format(currentUser.getLoginTime())).getTime();
                long m = a - b;
                long errorTimes ;
                //如果现在的登录时间大于数据库最后登录时间60分钟   则错误登录次数是1
                if (( m / (1000 * 60  )>loginErrorDate)){
                    userDao.updateErrorTimesZero(userDomain.getUsername());
                    errorTimes = 4;
                }else {
                    userDao.updateErrorTimes(userDomain.getUsername());
                    errorTimes = loginErrorTime - (currentUser.getLoginErrorTimes()+1);
                }
                if (errorTimes > 0){
                    return JsonObjectBO.error("账号密码错误,你还可以输入"+errorTimes+"次");
                }else {
                    return JsonObjectBO.error("该用户登录错误超过5次，请1小时后重试！！");
                }

            }
            if (currentUser==null){
                return JsonObjectBO.error("账号账号或密码错误,请重新输入");
            }
            if (user.getIsLocked()){
                return JsonObjectBO.error("该用户已被锁定，请联系管理员！");
            }else {
                if (currentUser.getLoginErrorTimes()>=loginErrorTime){
                     return JsonObjectBO.error("该用户登录错误超过5次，请1小时后重试！");
                }
            }
            if(currentUser.getRoleId().equals("BADW")||currentUser.getRoleId().equals("ZZDW")||currentUser.getRoleId().equals("CYRY")){
                User user2 =new User();
                user2.setLoginTime(new Date());
                user2.setUserName(userDomain.getUsername());
                user2.setLoginErrorTimes(0);
                userDao.updateUser(user2);
                User currentUser1 = usersMapper.validateCurrentuser(userDomain.getUsername());
                currentUser1.setPassword(null);
                JSONObject jsonObject = new JSONObject();
                String token= UUIDUtil.generate();
                jsonObject.put("currentUser",currentUser1);
                jsonObject.put("token",token);
//                List<String> resourceId = roleResourceDao.selectResourceByID(user.getRoleId());
//                List<Resource> resources = resourceService.findResourceByRole(resourceId);
//                request.getSession().setAttribute("user", currentUser1);
//                request.getSession().setAttribute("resources", resources);
//                request.getSession().setAttribute("token", token);

                template.opsForValue().set(token, JSON.toJSONString(currentUser1),expireTime,TimeUnit.SECONDS);
                return JsonObjectBO.success("登录成功",jsonObject);
            }else {
                return JsonObjectBO.error("此用户不是app端用户");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.error("账号账号或密码错误,请重新输入");
        }
    }

    @Override
    public APKVersion versionupdate() {
        return apkVersionMapper.selectNewVersion();
    }

    @Override
    public boolean addversion(HttpServletRequest request,APKVersion apkVersion) {
        User user = (User)request.getSession().getAttribute("user");
        apkVersion.setUserId(user.getId());
        apkVersion.setUserName(user.getUserName());
        apkVersion.setCreatetime(new Date(System.currentTimeMillis()));
        apkVersion.setId(UUIDUtil.generate());
        if(apkVersionMapper.insertSelective(apkVersion)==1){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public List<APKVersion> getAllApk() {
        return apkVersionMapper.getAllApk();
    }

    @Override
    public boolean insertlog(HttpServletRequest request,Map map) {

        AppReportLog appReportLog = new AppReportLog();
        String reportLog = (String)map.get("log");
        String phoneType = (String) map.get("deviceInfo");
        String remark = (String) map.get("remark");
        String appversion = (String) map.get("appVersion");
        String systemVersion = (String) map.get("systemVersion");
        User user = (User)request.getSession().getAttribute("user");
        appReportLog.setId(UUIDUtil.generate());
        appReportLog.setCreatetime(new Date(System.currentTimeMillis()));
        appReportLog.setDeviceInfo(phoneType);
        appReportLog.setReportlog(reportLog);
        appReportLog.setRemark(remark);
        appReportLog.setAppVersion(appversion);
        appReportLog.setSystemVersion(systemVersion);
        appReportLog.setUserId(user.getUserName());
        if(appReportLogMapper.insertSelective(appReportLog)==1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public JsonObjectBO checkAPPPhoneAndIDCard(SMSCode smsCode) {
        Long nowtime =System.currentTimeMillis();

        SMSCode sms = smsCodeDao.getSms(smsCode.getPhone());
        if (sms ==null){
            return JsonObjectBO.error("请发送验证码");
        }
        if(!sms.getSmscode().equals(smsCode.getSmscode())){
            return JsonObjectBO.error("验证码错误");
        }
        if(nowtime-sms.getLastTime()>300000){
            return JsonObjectBO.error("验证码超时，请重新发送");
        }
        return JsonObjectBO.ok("效验通过");
    }



}
