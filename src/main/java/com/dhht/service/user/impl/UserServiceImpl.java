package com.dhht.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.dao.*;
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

    @Autowired
    private RoleResourceDao roleResourceDao;

    @Autowired
    private ResourceService resourceService;


//    @Value("${loginError.Time}")
    private long loginErrorTime = 5;
//
//    @Value("${loginError.Date}")
   private long loginErrorDate = 60;
    /**
     * 6位简单密码
     * @return
     */
    public static String createRandomVcode(){
        //密码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }

    /**
     * 添加用户
     * @param users
     * @return
     */

    public JsonObjectBO addUser(Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        if (users.getTelphone()==null){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("请输入手机号");
            return jsonObjectBO;
        }
        Users users1 = userDao.findByTelphone(users.getTelphone());
        if (users1!=null){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("该用户已经存在");
            return jsonObjectBO;
        }
        users.setId(UUIDUtil.generate());
        users.setUserName(users.getTelphone());
        String password = MD5Util.toMd5(createRandomVcode());
        users.setPassword(password);
        users.setRoleId("GLY");
        Integer a = userDao.addUser(users);
        if (a!=1){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
            return jsonObjectBO;
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
            return jsonObjectBO;
        }

    }
    /**
     * 修改用户
     * @param users
     * @return
     */
    @Override
    public JsonObjectBO Update(Users users) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        Users users2 = userDao.findById(users.getId());
        if (!users2.getTelphone().equals(users.getTelphone())) {
            Users users1 = userDao.findByTelphone(users.getTelphone());
            if (users1 != null) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该用户已经存在");
                return jsonObjectBO;
            }
            users.setUserName(users.getTelphone());
            Integer a = userDao.update(users);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
                return jsonObjectBO;
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
                return jsonObjectBO;
            }
        }else{
            users.setUserName(users.getTelphone());
            Integer a = userDao.update(users);
            if (a != 1) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("修改失败");
                return jsonObjectBO;
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("修改成功");
                return jsonObjectBO;
            }
        }


    }


    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public JsonObjectBO deleteuser(String id){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        Integer a = userDao.delete(id);
        if (a!=1){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除失败");

        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");

        }

        return jsonObjectBO;
    }




    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public int changePwd(String id, String password) {
        String password1 = MD5Util.toMd5(password);
        return userDao.changePwd(id,password1);
    }


    /**
     * 查询列表 模糊查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public JsonObjectBO find(String realName,String roleId,String regionId,int pageNum, int pageSize) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum,pageSize);
        if(realName==null&&regionId==null&&roleId==null){
            List<Users> userList = userDao.findAllSuser();
            PageInfo<Users> result = new PageInfo<>(userList);
            jsonObject.put("user",result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }else{
            List<Users> list = userDao.find(realName,regionId,roleId);
            PageInfo<Users> result = new PageInfo<>(list);
            jsonObject.put("user",result);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
        }
        return jsonObjectBO;
    }

    @Override
    public JsonObjectBO activeLocking(String loginTime) {
        return null;
    }




    @Override
    public Users validate(Users users){
        String userAccount = StringUtil.stringNullHandle(users.getUserName());
        String password = StringUtil.stringNullHandle(MD5Util.toMd5(users.getPassword()));
        Users user = usersMapper.validate(new UserDomain(userAccount,password));
        return user;
    }

    @Override
    public PageInfo<Users> selectByDistrict(Integer id,int pageSum,int pageNum) {
        List<Users> list = new ArrayList<Users>();
        String districtIds[] = StringUtil.DistrictUtil(id);
        if(districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]);
        }else if(!districtIds[1].equals("00")&&districtIds[2].equals("00")){
            list = userDao.selectByDistrict(districtIds[0]+districtIds[1]);
        }else {
            list = userDao.selectByDistrict(id.toString());
        }
        PageHelper.startPage(pageSum,pageNum);
        PageInfo<Users> result = new PageInfo(list);
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
            Users user1= new Users();
            user1.setPassword(userDomain.getPassword());
            user1.setUserName(userDomain.getUsername());
            Users user = validate(user1);
            Users currentUser = usersMapper.validateCurrentuser(userDomain.getUsername());

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
            if (user.getLocked()){
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
            Users user2 =new Users();
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
