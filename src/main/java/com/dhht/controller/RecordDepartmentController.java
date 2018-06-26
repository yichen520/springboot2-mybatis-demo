package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.RecordDepartment;
import com.dhht.model.User;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/department")
public class RecordDepartmentController {

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    private JSONObject jsonObject = new JSONObject();

    @RequestMapping(value = "/selectByRole")
    public JsonObjectBO selectByRole(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer)map.get("pageNum");

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try {
            pageInfo = recordDepartmentService.selectByDistrictId(user.getDistrictId(),pageSize,pageNum);
            jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "/selectByDistrict")
    public JsonObjectBO selectByDistrict(@RequestBody Map map){
        String id = (String) map.get("id");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer)map.get("pageNum");

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try {
             pageInfo = recordDepartmentService.selectByDistrictId(id,pageSize,pageNum);
             jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "/info")
    public JsonObjectBO selectAllRecordDepartment(@RequestBody Map map){
        int pageSize = (Integer)map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");

        PageInfo<RecordDepartment> pageInfo = new PageInfo<>();
        try{
            pageInfo = recordDepartmentService.selectAllRecordDepartMent(pageSize,pageNum);
            jsonObject.put("recordDepartment",pageInfo);
        }catch (Exception e ){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "/insert")
    public JsonObjectBO insrt(@RequestBody RecordDepartment recordDepartment){
        boolean result = false;
        try {
            result = recordDepartmentService.insert(recordDepartment);

        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        if(result){
            return JsonObjectBO.ok("添加成功");
        }else {
            return JsonObjectBO.error("添加失败");
        }
    }
}
