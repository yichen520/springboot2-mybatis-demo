package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.UseDepartment;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/useDepartment")
public class UseDepartmentController {


    @Autowired
   private UseDepartmentService useDepartmentService;

    /***
     * 添加
     * @param
     * @return
     */
    @Log("添加用户")
    @RequestMapping(value ="/insert", method = RequestMethod.POST)
    public JsonObjectBO insert(@RequestBody UseDepartment useDepartment){
        JsonObjectBO jsonObjectBO = useDepartmentService.insert(useDepartment);
        return jsonObjectBO;


    }

    /**
     * 修改
     * @param useDepartment
     * @return
     */
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public JsonObjectBO update(@RequestBody UseDepartment useDepartment){
        JsonObjectBO jsonObjectBO = useDepartmentService.update(useDepartment);
        return jsonObjectBO;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find" , method = RequestMethod.POST)
    public JsonObjectBO find(@RequestBody Map map){
        String code = (String)map.get("code");
        String name = (String)map.get("name");
        String districtId = (String)map.get("districtId");
        String departmentStatus = (String)map.get("departmentStatus");

        int pageNum = (int) map.get("pageNum");
        int pageSize = (int) map.get("pageSize");
        JsonObjectBO jsonObjectBO = useDepartmentService.find(code, name, districtId, departmentStatus, pageNum, pageSize);
        return jsonObjectBO;
    }


    /**
     * 删除
     * @param useDepartment
     * @return
     */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO delete(@RequestBody UseDepartment useDepartment){
        JsonObjectBO jsonObjectBO = useDepartmentService.delete(useDepartment);
        return jsonObjectBO;
    }


    /**
     * 查询详情
     * @param map
     * @return
     */
    @RequestMapping(value = "/showMore" , method = RequestMethod.POST)
    public JsonObjectBO showMore(@RequestBody Map map){
        String flag = (String)map.get("flag");
        JsonObjectBO jsonObjectBO = useDepartmentService.showMore(flag);
        return jsonObjectBO;
    }

    /**
     * 根据名字进行了查询
     */
    @RequestMapping(value = "/selectByName" ,method = RequestMethod.POST)
    public JsonObjectBO selectByName(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String name = (String)map.get("name");
        UseDepartment useDepartment = useDepartmentService.selectUseDepartment(name);
        jsonObject.put("useDepartment",useDepartment);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setData(jsonObject);
        return jsonObjectBO;

    }
}