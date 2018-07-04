package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.Employee;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DistrictService districtService;

    private JSONObject jsonObject = new JSONObject();

    @RequestMapping(value = "/menus")
    public JsonObjectBO selectAllEmployee(@RequestBody Map map){
      String id = (String)map.get("districtId");
      JSONObject jsonObject = new JSONObject();
      try{
          List<DistrictMenus> list = districtService.selectMakeDepartmentMenus(id);
          jsonObject.put("menus",list);
          return JsonObjectBO.success("查询成功",jsonObject);
      }catch (Exception e){
          return JsonObjectBO.exception(e.getMessage());
      }
    }

    @RequestMapping(value = "/info")
    public JsonObjectBO selectByDepartmentCode(@RequestBody Map map){
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        String DepartmentCode = (String) map.get("code");
        try {
             PageHelper.startPage(pageNum,pageSize);
             PageInfo<Employee> pageInfo = new PageInfo<Employee>(employeeService.selectByDepartmentCode(DepartmentCode));
             jsonObject.put("employee",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "insert")
    public JsonObjectBO insertEmployee(@RequestBody Employee employee){
        try {
            return ResultUtil.getResult(employeeService.insertEmployee(employee));
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }



}
