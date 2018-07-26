package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.User;
import com.dhht.service.employee.EmployeeCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "count/seal")
public class SealCountController {

    @Autowired
    private EmployeeCountService employeeCountService;

    @RequestMapping(value = "/info")
    public JsonObjectBO info(@RequestBody Map map, HttpServletRequest httpServletRequest){
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        String district = (String)map.get("districtId");
        User user =(User) httpServletRequest.getSession().getAttribute("user");

        JSONObject jsonObject = new JSONObject();

        try {
            if(district==null) {
                List<Count> counts = employeeCountService.countAllEmployee(user.getDistrictId(),startTime,endTime);
                jsonObject.put("count",counts);
            }else {
                List<Count> counts = employeeCountService.countAllEmployee(district,startTime,endTime);
                jsonObject.put("count",counts);
            }
            return JsonObjectBO.success("查询",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }
}

