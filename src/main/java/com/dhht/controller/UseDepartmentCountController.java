package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.useDepartment.UseDepartmentCountService;
import com.dhht.service.useDepartment.UseDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by fyc 2018/7/24
 */
@RestController
@RequestMapping(value = "/count/useDepartment")
public class UseDepartmentCountController {

    @Autowired
    private UseDepartmentCountService departmentCountService;
    @Autowired
    private DistrictService districtService;

    /**
     * 使用单位统计列表
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO info(@RequestBody Map map, HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String districtId = (String)map.get("districtId");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");

        JSONObject jsonObject = new JSONObject();
        List<Count> list = new ArrayList<>();

        try{
            if(districtId==""||districtId==null) {
                list = departmentCountService.countAllUseDepartment(user.getDistrictId(),startTime,endTime);
            }else {
                list = departmentCountService.countAllUseDepartment(districtId,startTime,endTime);
            }
            jsonObject.put("departmentCount",list);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取统计信息失败！");
        }
    }

    /**
     * 区域
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/district")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try {
            List<DistrictMenus> districtMenus = districtService.selectOneDistrict(user.getDistrictId()) ;
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }
}
