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
    public JsonObjectBO addSuser(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int addSuser = userService.addUser(users);
        if(addSuser>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
        }else {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
        }
        return jsonObjectBO;
    }


    /**
     * 修改用户
     * @param
     * @return
     */
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public JsonObjectBO updateSuser(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int update = userService.Update(users);
        if(update>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("修改成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("修改失败");
        }
        return jsonObjectBO;
    }

    /**
     * 删除用户
     * @param
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO deleteSuser(@RequestBody Users users){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String id = users.getId();
        int delete = userService.deleteSuser(id);
        if (delete>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除失败");
        }
        return jsonObjectBO;
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
     * 查询全部用户
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO findAllSuser(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");

        PageInfo<Users> user = userService.findAllSuser(pageNum,pageSize);
        jsonObject.put("user",user);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;

    }

    @RequestMapping(value = "/findByDistrict")
    public JsonObjectBO findByDistrict(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        int pageSize =(Integer) map.get("pageSize");
        int pageNum =(Integer) map.get("pageNum");
        int id = (Integer) map.get("id");

        PageInfo<Users> user = userService.selectByDistrict(id,pageSize,pageNum);
        jsonObject.put("user",user);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }

}
