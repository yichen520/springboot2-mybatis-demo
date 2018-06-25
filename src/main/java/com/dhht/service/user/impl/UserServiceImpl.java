package com.dhht.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.SmsController;
import com.dhht.dao.*;
import com.dhht.model.SMSCode;
import com.dhht.model.User;
import com.dhht.model.*;
import com.dhht.service.resource.ResourceService;
import com.dhht.util.MD5Util;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dhht.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private DistrictMapper districtDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private MakedepartmentMapper makedepartmentMapper;

    @Autowired
    private SMSCodeDao smsCodeDao;

    private RoleResourceDao roleResourceDao;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SmsController smsController;


//    @Value("${loginError.Time}")
    private long loginErrorTime = 5;
//
//    @Value("${loginError.Date}")
   private long loginErrorDate = 60;
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
     * 添加用户
     *
     * @param user
     * @return
     */

    public JsonObjectBO addUser(User user) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        if (user.getTelphone() == null) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("请输入手机号");
            return jsonObjectBO;
        }
        User user1 = userDao.findByTelphone(user.getTelphone());
        if (user1 != null) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("该用户已经存在");
            return jsonObjectBO;
        }
        user.setId(UUIDUtil.generate());
        user.setUserName(user.getTelphone());
        String password = MD5Util.toMd5(createRandomVcode());
        user.setPassword(password);
        user.setRoleId("GLY");
        Integer a = userDao.addUser(user);
        if (a != 1) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
            return jsonObjectBO;
        } else {
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
            return jsonObjectBO;
        }

    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public JsonObjectBO Update(User user) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user2 = userDao.findById(user.getId());
        if (!user2.getTelphone().equals(user.getTelphone())) {
            User user1 = userDao.findByTelphone(user.getTelphone());
            if (user1 != null) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该用户已经存在");
                return jsonObjectBO;
            }
            user.setUserName(user.getTelphone());
            Integer a = userDao.update(user);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
                return jsonObjectBO;
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
                return jsonObjectBO;
            }
        } else {
            user.setUserName(user.getTelphone());
            Integer a = userDao.update(user);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
            }
            return jsonObjectBO;
        }


    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public JsonObjectBO deleteuser(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        Integer a = userDao.delete(id);
        if (a != 1) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除失败");

        } else {
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");

        }

        return jsonObjectBO;
    }


    /**
     * 管理员修改密码
     *
     * @param id
     * @param
     * @return
     */
    @Override
    public JsonObjectBO changePwd(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String code = createRandomVcode();
        User user = userDao.findById(id);
        user.setPassword(MD5Util.toMd5(code));
        String phone = user.getTelphone();
        try {
            if (phone == null) {
                return jsonObjectBO.error("没有该手机号");
            } else {
                ArrayList<String> params = new ArrayList<String>();
                params.add(code);
                SMSCode smscode = smsCodeDao.getSms(phone);
                if (smscode == null) {
                    smscode = new SMSCode();
                    smscode.setId(UUIDUtil.generate());
                    smscode.setLastTime(new Date().getTime());
                    smscode.setPhone(phone);
                    smscode.setSmscode(code);
                    smsCodeDao.save(smscode);
                } else {
                    smscode.setLastTime(new Date().getTime());
                    smscode.setSmscode(code);
                    smsCodeDao.update(smscode);
                }
                smsController.sendPhoneMessage(phone, params);
                return jsonObjectBO.ok("短信发送成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return jsonObjectBO.exception("发生异常");
        }
    }


    /**
     * 查询列表 模糊查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(User user,String realName, String roleId, String districtId, int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        String localdistrictId = user.getDistrictId();
        if (realName == null && districtId == null && roleId == null) {
            PageInfo<User> result = selectByDistrict(localdistrictId,pageSize,pageNum);
            jsonObject.put("user", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        } else {
            List<User> list = userDao.find(realName, districtId, roleId);
            PageInfo<User> result = new PageInfo<>(list);
            jsonObject.put("user", result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }
        return jsonObjectBO;
    }

    /**
     * 主动加锁
     *
     * @param id
     * @return
     */
    @Override
    public JsonObjectBO activeLocking(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = userDao.findById(id);
        if (user.getIsLocked()==null||user.getIsLocked().equals("0")||!user.getIsLocked()) {
            Integer a = userDao.updateLock(id);
            if (a == 1) {
                return jsonObjectBO.ok("锁定成功");
            } else {
                return jsonObjectBO.error("锁定失败");
            }
        } else {
            return jsonObjectBO.error("该用户已经锁定,不能重复锁定");
        }
    }

    /**
     * 主动解锁
     *
     * @param
     * @return
     */
    @Override
    public JsonObjectBO activeUnlocking(String id) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = userDao.findById(id);
        if (user.getIsLocked()==null||user.getIsLocked().equals("0")||!user.getIsLocked()) {
            return jsonObjectBO.error("该账号未加锁");
        } else {
            userDao.updateUnLock(id);
            return jsonObjectBO.ok("解锁成功");
        }
    }

    @Override
    public User validate(User user){
        String userAccount = StringUtil.stringNullHandle(user.getUserName());
        String password = StringUtil.stringNullHandle(MD5Util.toMd5(user.getPassword()));
        User user1 = usersMapper.validate(new UserDomain(userAccount,password));
        return user1;
    }








    @Override
    public PageInfo<User> selectByDistrict(String id, int pageSum, int pageNum) {
        List<User> list = new ArrayList<User>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]+districtIds[1]);
        }else {
            list = userDao.selectByDistrict(id.toString());
        }
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<User> result = new PageInfo(list);
        return result;
    }

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


    @Override
    public Map<String, Object> validateUser(HttpServletRequest request, UserDomain userDomain) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
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
                Date c= currentUser.getLoginTime();

                long b =sdf.parse(sdf.format(currentUser.getLoginTime())).getTime();
                long m = a - b;
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

            List<String> id = roleResourceDao.selectMenuResourceByID(user.getRoleId());
            List<String> resourceId = roleResourceDao.selectResourceByID(user.getRoleId());
            List<Menus> menus = resourceService.findMenusByRole(id);
            List<Resource> resources = resourceService.findResourceByRole(resourceId);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("menus", menus);
            request.getSession().setAttribute("resources", resources);
            return map;

        } catch (Exception e) {
            jsonObjectBO.setMessage("登录失败");
            jsonObjectBO.setCode(-1);
            map.put("status", "error");
            map.put("currentAuthority", "guest");
            map.put("message","登录失败！");
            return map;
        }
    }
}
