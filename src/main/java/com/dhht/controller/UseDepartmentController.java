package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.UseDepartment;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value="/useDepartment")     // 通过这里配置使下面的映射都在/users下，可去除
public class UseDepartmentController {


    @Autowired
   private UseDepartmentService useDepartmentService;

//    /***
//     * 添加使用单位
//     * @param
//     * @return
//     */
//    @RequestMapping(value ="/add", method = RequestMethod.POST)
//    public JsonObjectBO addUserDepartment(@RequestBody UseDepartment useDepartment){
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        int addUseDepartment = useDepartmentService.insert(useDepartment);
//        if(addUseDepartment>0){
//            jsonObjectBO.setCode(1);
//            jsonObjectBO.setMessage("添加成功");
//        }else {
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("添加失败");
//        }
//        return jsonObjectBO;
//    }
//
//
//    /**
//     * 修改使用单位
//     * @param
//     * @return
//     */
//    @RequestMapping(value ="/update",method = RequestMethod.POST)
//    public JsonObjectBO updateUserDepartment(@RequestBody UseDepartment useDepartment){
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        int update = useDepartmentService.update(useDepartment);
//        if(update>0){
//            jsonObjectBO.setCode(1);
//            jsonObjectBO.setMessage("修改成功");
//        }else{
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("修改失败");
//        }
//        return jsonObjectBO;
//    }
//
//    /**
//     * 删除用使用单位
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "/deleteUserDepartment" , method = RequestMethod.POST)
//    public JsonObjectBO deleteUserDepartment(@RequestBody UseDepartment useDepartment){
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        int delete = useDepartmentService.delete(useDepartment);
//        if (delete>0){
//            jsonObjectBO.setCode(1);
//            jsonObjectBO.setMessage("删除成功");
//        }else{
//            jsonObjectBO.setCode(-1);
//            jsonObjectBO.setMessage("删除失败");
//        }
//        return jsonObjectBO;
//    }
//
//    /**
//     * 查询全部用户
//     */
//    @RequestMapping(value = "/findAll" , method = RequestMethod.POST)
//    public JsonObjectBO findAll(@RequestBody Map map){
//        JsonObjectBO jsonObjectBO = new JsonObjectBO();
//        JSONObject jsonObject = new JSONObject();
//        int pageSize =(Integer) map.get("pageSize");
//        int pageNum =(Integer) map.get("pageNum");
//        PageInfo<UseDepartment> useDepartmentPageInfo = useDepartmentService.findAllMakeBySize(pageNum,pageSize);
//        jsonObject.put("UserDepartment",useDepartmentPageInfo);
//        jsonObjectBO.setData(jsonObject);
//        jsonObjectBO.setCode(1);
//        jsonObjectBO.setMessage("查询成功");
//        return jsonObjectBO;
//
//    }
//
//



}