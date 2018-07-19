package com.dhht.service.user.impl;


import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.UploadController;
import com.dhht.dao.*;
import com.dhht.model.*;
import com.dhht.service.resource.ResourceService;
import com.dhht.service.tools.SmsSendService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.MD5Util;
import com.dhht.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private SmsSendService smsSendService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private SMSCodeDao smsCodeDao;

    @Autowired
    private UserPasswordService userPasswordService;

    @Autowired
    private RoleResourceDao roleResourceDao;
    @Value("${loginError.Time}")
    private long loginErrorTime ;
    @Value("${loginError.Date}")
   private long loginErrorDate;

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

//    /**
//     * 管理员重置密码
//     *
//     * @param id
//     * @param
//     * @return
//     */
//    @Override
//    public JsonObjectBO resetPwd(String id) {
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        String code = createRandomVcode();
//        User user = userDao.findById(id);
//        user.setPassword(MD5Util.toMd5(code));
//        String phone = user.getTelphone();
//        return userPasswordService.sendMessage(phone,code);
//    }


    /**
     * 验证账号和密码
     * @param user
     * @return
     */
    @Override
    public User validate(User user){
        String userAccount = StringUtil.stringNullHandle(user.getUserName());
        String password = StringUtil.stringNullHandle(MD5Util.toMd5(user.getPassword()));
        User user1 = usersMapper.validate(new UserDomain(userAccount,password));
        return user1;
    }


    /**
     * 检查手机号和ID
     * @param smsCode
     * @return
     */
    @Override
    public JsonObjectBO checkPhoneAndIDCard(SMSCode smsCode) {
        Long nowtime = new Date().getTime();

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
        if(nowtime-sms.getLastTime()>300000){
            return JsonObjectBO.error("验证码超时，请重新发送");
        }
        return JsonObjectBO.ok("效验通过");

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
            User user = validate(user1);
            User currentUser = usersMapper.validateCurrentuser(userDomain.getUsername());

            if(user==null && currentUser!=null ){
                //更新登录错误次数
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long  a = sdf.parse(sdf.format(new Date())).getTime();
                long b =sdf.parse(sdf.format(currentUser.getLoginTime())).getTime();
                long m = a - b;
                //如果现在的登录时间大于数据库最后登录时间60分钟   则错误登录次数是1
                if (( m / (1000 * 60  )>loginErrorDate)){
                    userDao.updateErrorTimesZero(userDomain.getUsername());
                }else {
                    userDao.updateErrorTimes(userDomain.getUsername());
                }

                map.put("status", "error");
                map.put("currentAuthority","guest");
                map.put("message","账号密码错误");
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
                    map.put("message","该用户登录错误超过5次，请稍后重试！");
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
            map.put("message","登录失败！");
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
                //如果现在的登录时间大于数据库最后登录时间60分钟   则错误登录次数是1
                if (( m / (1000 * 60  )>loginErrorDate)){
                    userDao.updateErrorTimesZero(userDomain.getUsername());
                }else {
                    userDao.updateErrorTimes(userDomain.getUsername());
                }

                return JsonObjectBO.error("账号密码错误");
            }
            if (user.getIsLocked()){
                return JsonObjectBO.error("该用户已被锁定，请联系管理员！");
            }else {
                if (currentUser.getLoginErrorTimes()>=loginErrorTime){
                     return JsonObjectBO.error("该用户登录错误超过5次，请稍后重试！");
                }
            }
            if(currentUser.getRoleId().equals("BADW")){
                User user2 =new User();
                user2.setLoginTime(new Date());
                user2.setUserName(userDomain.getUsername());
                user2.setLoginErrorTimes(0);
                userDao.updateUser(user2);
                User currentUser1 = usersMapper.validateCurrentuser(userDomain.getUsername());
                currentUser1.setPassword(null);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("currentUser",currentUser1);
                List<String> resourceId = roleResourceDao.selectResourceByID(user.getRoleId());
                List<Resource> resources = resourceService.findResourceByRole(resourceId);
                request.getSession().setAttribute("user", currentUser1);
                request.getSession().setAttribute("resources", resources);
                return JsonObjectBO.success("登录成功",jsonObject);
            }else {
                return JsonObjectBO.error("此用户不是app端用户");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }
}
