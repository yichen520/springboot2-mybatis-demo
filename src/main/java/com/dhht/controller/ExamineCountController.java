package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.ExamineCount;
import com.dhht.model.User;
import com.dhht.service.employee.EmployeeCountService;
import com.dhht.service.examine.MinitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "count")
public class ExamineCountController {

    @Autowired
    private MinitorService minitorService;

    @RequestMapping(value = "examine/info")
    public JsonObjectBO examineinfo(@RequestBody Map map, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
                List<ExamineCount> counts = minitorService.countExamine(map,httpServletRequest);
                jsonObject.put("count",counts);
            return JsonObjectBO.success("检查统计成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }
    @RequestMapping(value = "punish/makedepartment")
    public JsonObjectBO punishmakedepartmentinfo(@RequestBody Map map, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
            List<ExamineCount> counts = minitorService.countPunish(map,httpServletRequest);
            jsonObject.put("count",counts);
            return JsonObjectBO.success("",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    @RequestMapping(value = "punish/employee")
    public JsonObjectBO punishemployeeinfo(@RequestBody Map map, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
            List<ExamineCount> counts = minitorService.countemployeePunish(map,httpServletRequest);
            jsonObject.put("count",counts);
            return JsonObjectBO.success("",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }
}

