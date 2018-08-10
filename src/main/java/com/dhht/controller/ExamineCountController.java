package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.model.ExamineCount;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
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

    @Autowired
    private DistrictService districtService;

    @RequestMapping(value = "examine/makeDepartment/info")
    public JsonObjectBO examineinfo(@RequestBody Map map, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
                List<ExamineCount> counts = minitorService.countExamine(map,httpServletRequest);
                jsonObject.put("count",counts);
            return JsonObjectBO.success("检查统计成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
        }
    }


    @RequestMapping(value = "punish/makeDepartment/info")
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

    @RequestMapping(value = "punish/employee/info")
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

    /**
     * 区域
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "punish/employee/district")
    public JsonObjectBO selectemployeeDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try {
            List<DistrictMenus> districtMenus =districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }

    @RequestMapping(value = "punish/makeDepartment/district")
    public JsonObjectBO selectmakedepartmentDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        try {
            List<DistrictMenus> districtMenus =districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }
    @RequestMapping(value = "examine/makeDepartment/district")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        try {
            List<DistrictMenus> districtMenus =districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }

}

