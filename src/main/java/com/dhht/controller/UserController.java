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

    private JSONObject jsonObject = new JSONObject();


    /***
     * 添加用户
     * @param
     * @return
     */
    @Log("添加用户")
    @RequestMapping(value ="/insert", method = RequestMethod.POST)
    public JsonObjectBO add(@RequestBody User user){
        user.setRoleId("GLY");
        user.setUserName("GLY"+user.getTelphone());
        return ResultUtil.getResult(userService.insert(user));
    }


    /**
     * 修改用户
     * @param
     * @return
     */
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public JsonObjectBO update(@RequestBody User user){
        user.setUserName("GLY"+user.getTelphone());
        return ResultUtil.getResult(userService.update(user));

    }

    /**
     * 删除用户
     * @param
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO delete(@RequestBody User user){
        return ResultUtil.getResult(userService.delete(user.getId()));

    }

//    /**
//     * 重置密码
//     * @param user
//     * @return
//     */
//    @RequestMapping(value = "/resetPwd" , method = RequestMethod.POST)
//    public JsonObjectBO changePwd(@RequestBody User user){
//        String id = user.getId();
//        JsonObjectBO jsonObjectBO = userPasswordService.resetPwd(id);
//        return jsonObjectBO;
//
//    }

    /**
     * 模糊查询列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO find(HttpServletRequest httpServletRequest, @RequestBody Map map){
        String realName = (String)map.get("realName");
        String roleId = (String) map.get("roleId");
        String districtId = (String) map.get("districtId");
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");

        JSONObject jsonObject = new JSONObject();
        PageInfo<User> pageInfo = new PageInfo<>();
        try {
            pageInfo = userService.find(user,realName,roleId,districtId,pageNum,pageSize);
            jsonObject.put("user",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


    /**
     * 主动加锁
     * @param map
     * @return
     */
    @RequestMapping(value = "/activeLocking")
    public JsonObjectBO activeLocking(@RequestBody Map map){
        String id = (String)map.get("id");
        return ResultUtil.getResult(userLockingService.activeLocking(id));
    }


    /**
     * 主动解锁
     * @param map
     * @return
     */
    @RequestMapping(value = "/activeUnlocking")
    public JsonObjectBO activeUnlocking(@RequestBody Map map){
        String id = (String)map.get("id");
        return ResultUtil.getResult(userLockingService.activeUnlocking(id));
    }


    /**
     * 管理员密码重置
     * @param map
     * @return
     */
    @RequestMapping(value = "/adminResetPwd")
    public JsonObjectBO adminResetPwd(@RequestBody Map map){
        String id = (String)map.get("id");
        if(userPasswordService.adminResetPwd(id)){
            return JsonObjectBO.success("重置成功",null);
        }else {
            return JsonObjectBO.error("重置失败");
        }
    }


    /**
     * 获取验证码
     */
    @RequestMapping(value = "/getCheckCode")
    public JsonObjectBO getCheckCode(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String phone = (String)map.get("telphone");

        if (userPasswordService.getCheckCode(phone)==1){
            jsonObjectBO.setMessage("获取验证码成功");
            jsonObjectBO.setCode(1);
        }else{
            jsonObjectBO.setMessage("获取验证码成功");
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

    /**
     * 忘记密码后的密码重置
     * @param map
     * @return
     */
    @RequestMapping(value = "/resetPwd")
    public JsonObjectBO resetPwd(@RequestBody Map map){
        String phone = (String)map.get("telphone");
        String checkCode = (String)map.get("checkCode");
        String passWord = (String)map.get("passWord");

        if(userPasswordService.resetPwd(phone, checkCode, passWord)){
            return JsonObjectBO.success("重置成功",null);
        }else {
            return JsonObjectBO.error("重置失败");
        }
    }

    /**
     * 根据区域查找用户
     * @param map
     * @return
     */
    @RequestMapping(value = "/findByDistrict")
    public JsonObjectBO findByDistrict(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");
        String id = (String) map.get("id");

        PageInfo<User> user = userService.selectByDistrict(id,pageSize,pageNum);
        jsonObject.put("user",user);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }

    @RequestMapping(value = "/UserDistrict")
    public JsonObjectBO selectDistrictByUser(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        List<DistrictMenus> district = new ArrayList<>();
        try {
            district = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("District",district);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

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


}
