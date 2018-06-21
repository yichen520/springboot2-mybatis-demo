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

    private JSONObject jsonObject = new JSONObject();

    @RequestMapping(value = "/selectAll")
    public JsonObjectBO SelectAllEmployee(@RequestBody Map map){
        int pageSum = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");


        PageInfo<Employee> pageInfo = new PageInfo<Employee>();
        try {
            pageInfo = employeeService.selectAllEmployee(pageSum,pageNum);
            jsonObject.put("Eemployee",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.error(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "/selectByDepartmentCode")
    public JsonObjectBO SelectByDepartmentCode(@RequestBody Map map){
        int pageSum = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        String DepartmentCode = (String) map.get("Code");

        PageInfo<Employee> pageInfo = new PageInfo<Employee>();
        try {
             pageInfo = employeeService.selectByDepartmentCode(pageSum, pageNum, DepartmentCode);
            jsonObject.put("Eemployee",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.error(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

}
