package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Employee;
import com.dhht.service.Employee.EmployeeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/make/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/selectAll")
    public JsonObjectBO SelectAllEmployee(@RequestBody Map map){
        int pageSum = (Integer) map.get("pageSum");
        int pageNum = (Integer) map.get("pageNum");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        PageInfo<Employee> pageInfo = employeeService.selectAllEmployee(pageSum,pageNum);
        jsonObject.put("Eemployee",pageInfo);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setData(jsonObject);
        return jsonObjectBO;
    }

}
