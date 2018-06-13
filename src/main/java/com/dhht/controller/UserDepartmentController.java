package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Makedepartment;
import com.dhht.model.UserDepartment;
import com.dhht.model.Users;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.userDepartment.UserDepartmentService;
import com.dhht.util.MD5Util;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value="/userDepartment")     // 通过这里配置使下面的映射都在/users下，可去除
public class UserDepartmentController {


    @Autowired
   private UserDepartmentService userDepartmentService;

    /***
     * 添加使用单位
     * @param
     * @return
     */
    @RequestMapping(value ="/addUserDepartment", method = RequestMethod.POST)
    public JsonObjectBO addUserDepartment(@RequestBody UserDepartment userDepartment){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int addUserDepartment = userDepartmentService.insert(userDepartment);
        if(addUserDepartment>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
        }else {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
        }
        return jsonObjectBO;
    }


    /**
     * 修改使用单位
     * @param
     * @return
     */
    @RequestMapping(value ="/updateUserDepartment",method = RequestMethod.POST)
    public JsonObjectBO updateUserDepartment(@RequestBody UserDepartment userDepartment){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int update = userDepartmentService.update(userDepartment);
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
     * 删除用使用单位
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteUserDepartment" , method = RequestMethod.POST)
    public JsonObjectBO deleteUserDepartment(@RequestBody UserDepartment userDepartment){
        String userdepartmentCode = userDepartment.getUserdepartmentCode();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int delete = userDepartmentService.delete(userdepartmentCode);
        if (delete>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("删除失败");
        }
        return jsonObjectBO;
    }

    /**
     * 查询全部用户
     */
    @RequestMapping(value = "/findAll" , method = RequestMethod.GET)
    public JsonObjectBO findAll(@RequestParam Integer pageNum, Integer pageSize){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserDepartment> userDepartmentPageInfo = userDepartmentService.findAllMakeBySize(pageNum,pageSize);
        jsonObject.put("UserDepartment",userDepartmentPageInfo);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;

    }





}