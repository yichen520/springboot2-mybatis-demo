package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.RecordDepartment;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "record/department")
public class RecordDepartmentController {

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    private JSONObject jsonObject = new JSONObject();
    @RequestMapping(value = "/selectByDistrict")
    public JsonObjectBO selectByDistrict(@RequestBody Map map){
        String id = (String) map.get("id");
        List<RecordDepartment> list = new ArrayList<RecordDepartment>();
        try {
             list = recordDepartmentService.selectByDistrictId(id);
             jsonObject.put("recordDepartment",list);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }
}
