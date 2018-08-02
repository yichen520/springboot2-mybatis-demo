package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentCuontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by fyc 2018/6/30
 */
@RestController
@RequestMapping("count/makeDepartment")
public class MakeDepartmentCountController {
    @Autowired
     private MakeDepartmentCuontService makeDepartmentCuontService;
    @Autowired
    private DistrictService districtService;

    /**
     * 展示统计列表
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping("/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest, @RequestBody Map map){
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        String district = (String)map.get("districtId");
        User user =(User) httpServletRequest.getSession().getAttribute("user");

        JSONObject jsonObject = new JSONObject();

        try {
            if(district==null) {
                List<Count> counts = makeDepartmentCuontService.countAllDepartment(user.getDistrictId(),startTime,endTime);
                jsonObject.put("count",counts);
            }else {
                List<Count> counts = makeDepartmentCuontService.countAllDepartment(district,startTime,endTime);
                jsonObject.put("count",counts);
            }
            return JsonObjectBO.success("查询",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
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
            List<DistrictMenus> districtMenus =districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }
}
