package com.dhht.controller;



import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Makedepartment;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.user.UserService;
import com.dhht.util.MD5Util;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/admin")     // 通过这里配置使下面的映射都在/users下，可去除
public class AdminController {


    @Autowired
   private MakeDepartmentService makeDepartmentService;

    //查看所有的制作单位
    @RequestMapping(value="/findallmake",produces="application/json;charset=UTF-8")
    public JsonObjectBO index(@RequestBody Map map) {
        int pageNum =(Integer) map.get("pageNum");
        int pageSize =(Integer)map.get("pageSize");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageInfo<Makedepartment> users = makeDepartmentService.findAllMakeBySize(pageNum,pageSize);
        jsonObject.put("makeDepartment",users);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;
    }

    //增加制作单位
    @RequestMapping(value="/addmake" ,method=RequestMethod.POST)
    public JsonObjectBO addmake(@ModelAttribute("makedepartment")Makedepartment makedepartment) {
        makedepartment.setPassword(MD5Util.toMd5(makedepartment.getPassword()));
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
       // boolean flag = makeDepartmentService.
        int users = makeDepartmentService.addMake(makedepartment);
        if (users>0){
            jsonObjectBO.setCode(1);

        }else {
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

    //删除制作单位
    @RequestMapping(value="/deletemake",method=RequestMethod.POST)
    public JsonObjectBO deletemake( @RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        String makedepartmentCode = map.get("makedepartmentCode").toString();
        int users = makeDepartmentService.deletemake(makedepartmentCode);
        if (users>0){
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("删除成功");
        }else {
            jsonObjectBO.setMessage("删除失败");
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

    //修改制作单位
    @RequestMapping(value="/updatemake" ,method=RequestMethod.POST)
    public JsonObjectBO updatemake( Makedepartment makedepartment) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        int users = makeDepartmentService.updatemake(makedepartment);
        if (users>0){
            jsonObjectBO.setCode(1);

        }else {
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }





}