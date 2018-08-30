package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.Role;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.user.*;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/sys/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserLockingService userLockingService;

    @Autowired
    private UserPasswordService userPasswordService;


    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    /***
     * 添加用户
     * @param
     * @return
     */
    @Log("添加用户")
    @RequestMapping(value ="/insert", method = RequestMethod.POST)
    public JsonObjectBO add(@RequestBody User user){

        try {
            return ResultUtil.getResult(userService.insert(user.getTelphone(),"GLY",user.getRealName(),user.getDistrictId()));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("添加失败");
        }
    }


    /**
     * 修改用户
     * @param
     * @return
     */
    @Log("修改用户")
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public JsonObjectBO update(@RequestBody User user){
        User user1 = userService.findById(user.getId());
        try {
            return ResultUtil.getResult(userService.update(user1.getTelphone(),user.getTelphone(),"GLY",user.getRealName(),user.getDistrictId()));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("修改用户");
        }

    }

    /**
     * 删除用户
     * @param
     * @return
     */
    @Log("删除用户")
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO delete(@RequestBody User user){
        try {
            return ResultUtil.getResult(userService.delete(user.getId()));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("删除用户");
        }

    }


    /**
     * 模糊查询列表
     * @param map
     * @return
     */
    @Log("模糊查询列表")
    @RequestMapping(value = "/info")
    public JsonObjectBO find(HttpServletRequest httpServletRequest, @RequestBody Map map){
        String realName = (String)map.get("realName");
        String roleId = (String) map.get("roleId");
        String districtId = (String) map.get("districtId");
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");

        JSONObject jsonObject = new JSONObject();
        try {
            PageInfo<User> pageInfo = new PageInfo<>();
            try {
                pageInfo = userService.find(user, realName, roleId, districtId, pageNum, pageSize);
                jsonObject.put("user", pageInfo);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return JsonObjectBO.exception(e.toString());
            }
            return JsonObjectBO.success("查询成功", jsonObject);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查询失败");
        }
    }


    /**
     * 主动加锁
     * @param map
     * @return
     */
    @Log("主动加锁")
    @RequestMapping(value = "/activeLocking")
    public JsonObjectBO activeLocking(@RequestBody Map map){
        String id = (String)map.get("id");
        try {
            return ResultUtil.getResult(userLockingService.activeLocking(id));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("主动枷锁失败");
        }
    }


    /**
     * 主动解锁
     * @param map
     * @return
     */
    @Log("主动解锁")
    @RequestMapping(value = "/activeUnlocking")
    public JsonObjectBO activeUnlocking(@RequestBody Map map){
        String id = (String)map.get("id");
        try {
            return ResultUtil.getResult(userLockingService.activeUnlocking(id));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("主动解锁失败");
        }
    }


    /**
     * 管理员密码重置
     * @param map
     * @return
     */
    @Log("管理员密码重置")
    @RequestMapping(value = "/adminResetPwd")
    public JsonObjectBO adminResetPwd(@RequestBody Map map){
        String id = (String)map.get("id");
        try{
        if(userPasswordService.adminResetPwd(id)){
            return JsonObjectBO.success("重置成功",null);
        }else {
            return JsonObjectBO.error("重置失败");
        }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("重置失败");
        }
    }


    /**
     * 获取验证码
     */
    @Log("获取验证码")
    @RequestMapping(value = "/getCheckCode")
    public JsonObjectBO getCheckCode(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String username = (String)map.get("username");
        try{
        if (userPasswordService.getCheckCode(username)== ResultUtil.isSuccess){
            return JsonObjectBO.success("获取验证码成功",null);
        }else{
            return  JsonObjectBO.error("获取验证码失败");
        }
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取验证码失败");
        }
    }

    /**
     * 忘记密码后的密码重置
     * @param map
     * @return
     */
    @Log("忘记密码后的密码重置")
    @RequestMapping(value = "/resetPwd")
    public JsonObjectBO resetPwd(@RequestBody Map map){
        String username = (String)map.get("username");
        String checkCode = (String)map.get("captcha");
        String password = (String)map.get("password");
        try {
            if (userPasswordService.resetPwd(username, checkCode, password)) {
                return JsonObjectBO.success("重置成功", null);
            } else {
                return JsonObjectBO.error("重置失败");
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("重置失败");
        }
    }

    /**
     * 根据区域查找用户
     * @param map
     * @return
     */
    @Log("根据区域查找用户")
    @RequestMapping(value = "/findByDistrict")
    public JsonObjectBO findByDistrict(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");
        String id = (String) map.get("id");
        try {
            PageInfo<User> user = userService.selectByDistrict(id, pageSize, pageNum);
            jsonObject.put("user", user);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查询失败");
        }
    }

    /**
     * app重置密码
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("app重置密码")
     @RequestMapping(value = "/appResetPwd")
     public JsonObjectBO AppResetPwd(HttpServletRequest httpServletRequest,@RequestBody Map map){
         JsonObjectBO jsonObjectBO = new JsonObjectBO();
         User user = (User) httpServletRequest.getSession().getAttribute("user");
         String uid = user.getId();
         String passWord = (String)map.get("passWord");
        try {
            Boolean b = userPasswordService.appResetPwd(uid, passWord);
            if (b) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("密码已经重置成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("密码重置失败");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("密码重置失败");
        }
     }


    /**
     * app修改密码
     * @param httpServletRequest
     * @param map
     * @return
     */
    @Log("app修改密码")
    @RequestMapping(value = "/appChangePwd")
    public JsonObjectBO AppChangePwd(HttpServletRequest httpServletRequest,@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String phone = user.getTelphone();
        String checkcode = (String)map.get("checkcode");
        String passWord = (String)map.get("passWord");
        try {
            Boolean b = userPasswordService.resetPwd(phone, checkcode, passWord);
            if (b) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("密码修改成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("密码修改失败");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("密码修改失败");
        }
    }

    /**
     * 根据用户获取区域
     * @param httpServletRequest
     * @return
     */
    @Log("获取区域")
    @RequestMapping(value = "/UserDistrict")
    public JsonObjectBO selectDistrictByUser(HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        List<DistrictMenus> district = new ArrayList<>();
        try {
            district = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("District",district);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("查询失败");
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 查询角色
     * @param map
     * @return
     */
    @Log("查询角色")
    @RequestMapping("role/info")
    public JsonObjectBO getList(@RequestBody(required = false) Map map) {

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        if (map == null){
            try {
                List<Role> roles = roleService.getRoleListNopage();
                jsonObject.put("roles", roles);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setMessage("查询角色成功");
                jsonObjectBO.setCode(1);
                return jsonObjectBO;

            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                jsonObjectBO.setMessage("查询角色失败");
                jsonObjectBO.setCode(-1);
                return jsonObjectBO;
            }
        }else {
            int pageNum = (Integer) map.get("pageNum");
            int pageSize = (Integer) map.get("pageSize");
            try {
                PageInfo<Role> roles = roleService.getRoleList(pageNum, pageSize);
                jsonObject.put("roles", roles);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setMessage("查询角色成功");
                jsonObjectBO.setCode(1);
                return jsonObjectBO;

            } catch (Exception e) {
                jsonObjectBO.setMessage("查询角色失败");
                jsonObjectBO.setCode(-1);
                return jsonObjectBO;
            }
        }

    }

    /**
     * 修改密码
     * @param httpServletRequest
     * @return
     */
    @Log("密码修改")
    @RequestMapping(value = "/changePwd")
    public JsonObjectBO changePwd(HttpServletRequest httpServletRequest,@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String username =  user.getUserName();
        String oldPassword = (String)map.get("oldPassword");
        String newPassword = (String)map.get("newPassword");

        try {
            int result =  userPasswordService.changePwd(username,oldPassword,newPassword);
            if(result==ResultUtil.isSuccess){
                jsonObjectBO.setMessage("密码修改成功");
                jsonObjectBO.setCode(1);
            }else if(result==ResultUtil.isFail){
                jsonObjectBO.setMessage("密码修改成功");
                jsonObjectBO.setCode(1);
            }else{
                jsonObjectBO.setMessage("原密码错误");
                jsonObjectBO.setCode(1);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            jsonObjectBO.setMessage("出现未知错误");
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

}
