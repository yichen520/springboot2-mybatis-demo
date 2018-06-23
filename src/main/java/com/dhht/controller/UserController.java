package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Menus;
import com.dhht.model.Resource;
import com.dhht.model.UserDomain;
import com.dhht.model.Users;
import com.dhht.service.user.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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




    /***
     * 添加用户
     * @param
     * @return
     */
    @RequestMapping(value ="/add", method = RequestMethod.POST)
    public JsonObjectBO adduser(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = userService.addUser(users);
        return jsonObjectBO;


    }


    /**
     * 修改用户
     * @param
     * @return
     */
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public JsonObjectBO updateuser(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = userService.Update(users);
        return jsonObjectBO;

    }

    /**
     * 删除用户
     * @param
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO deleteSuser(@RequestBody Users users){
        String id = users.getId();
        JsonObjectBO jsonObjectBO = userService.deleteuser(id);
        return  jsonObjectBO;

    }

    @RequestMapping(value = "/changePwd" , method = RequestMethod.POST)
    public JsonObjectBO changePwd(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String id = users.getId();
        String password = users.getPassword();
        Integer changepwd = userService.changePwd(id,password);
        if(changepwd>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("修改成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("修改失败");
        }
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

    @RequestMapping(value = "/findByDistrict")
    public JsonObjectBO findByDistrict(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");
        String id = (String) map.get("id");

        PageInfo<Users> user = userService.selectByDistrict(id,pageSize,pageNum);
        jsonObject.put("user",user);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }

}
