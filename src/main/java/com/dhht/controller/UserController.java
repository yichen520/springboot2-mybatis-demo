package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.Role;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.user.RoleService;
import com.dhht.service.user.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    private JSONObject jsonObject = new JSONObject();


    /***
     * 添加用户
     * @param
     * @return
     */
    @RequestMapping(value ="/add", method = RequestMethod.POST)
    public JsonObjectBO adduser(@RequestBody User user){
        JsonObjectBO jsonObjectBO = userService.addUser(user);
        return jsonObjectBO;


    }


    /**
     * 修改用户
     * @param
     * @return
     */
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public JsonObjectBO updateuser(@RequestBody User user){
        JsonObjectBO jsonObjectBO = userService.Update(user);
        return jsonObjectBO;

    }

    /**
     * 删除用户
     * @param
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO deleteSuser(@RequestBody User user){
        String id = user.getId();
        JsonObjectBO jsonObjectBO = userService.deleteuser(id);
        return  jsonObjectBO;

    }

    @RequestMapping(value = "/changePwd" , method = RequestMethod.POST)
    public JsonObjectBO changePwd(@RequestBody User user){
        String id = user.getId();
        JsonObjectBO jsonObjectBO = userService.changePwd(id);
        return jsonObjectBO;

    }

    /**
     * 迷糊查询列表
     * @param map
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO find(@RequestBody Map map){

        String realName = (String)map.get("realName");
        String roleId = (String) map.get("roleId");
        String regionId = (String) map.get("regionId");

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");

        JsonObjectBO jsonObjectBO = userService.find(realName,roleId,regionId,pageNum,pageSize);
        return jsonObjectBO;

    }


    /**
     * 主动加锁
     * @param map
     * @return
     */
    @RequestMapping(value = "/activeLocking")
    public JsonObjectBO activeLocking(@RequestBody Map map){
        String id = (String)map.get("id");
        JsonObjectBO jsonObjectBO = userService.activeLocking(id);
        return  jsonObjectBO;
    }


    /**
     * 主动解锁
     * @param map
     * @return
     */
    @RequestMapping(value = "/activeUnlocking")
    public JsonObjectBO activeUnlocking(@RequestBody Map map){
        String id = (String)map.get("id");
        JsonObjectBO jsonObjectBO = userService.activeUnlocking(id);
        return  jsonObjectBO;
    }


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
            district = districtService.selectOneDistrict(user.getDistrict().getDistrictId());
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
