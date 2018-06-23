package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/sys/district")
public class DistrictController {
    @Autowired
    private DistrictService districtService;



    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public JsonObjectBO selectAllDistrict(){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        List<DistrictMenus> district = districtService.selectAllDistrict();
       // System.out.println(district.size());
        for (DistrictMenus districts:district) {
            System.out.println(districts.toString());
        }
        jsonObject.put("District",district);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;
    }

    @RequestMapping(value = "/select")
    public JsonObjectBO selectByRole(HttpServletRequest httpServletRequest){
       User user = (User) httpServletRequest.getSession().getAttribute("user");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();


        List<DistrictMenus> district = districtService.selectOneDistrict(user.getDistrict().getDistrictId().toString());


        // System.out.println(district.size());
        for (DistrictMenus districts:district) {
            System.out.println(districts.toString());
        }

        jsonObject.put("District",district);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;

    }



}
