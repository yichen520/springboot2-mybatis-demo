package com.dhht.controller.html;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.Cache;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.service.make.MakeDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/web")
public class WebPortalsController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @RequestMapping(value = "/districtInfo",method = RequestMethod.GET)
    public JsonObjectBO getDistrictId(){
        try {
            List<DistrictMenus> districtMenus = (List<DistrictMenus>) Cache.get("provinceDistrict");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("districtMenus", districtMenus);
            return JsonObjectBO.success("获取区域成功", jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }

    @RequestMapping(value = "/makeDepartmentInfo",method = RequestMethod.POST)
    public JsonObjectBO getMakeDepartmentByDistrictId(@RequestBody List<String> districtIds){
        try {
            String districtId = districtIds.get(2);
            JSONObject jsonObject = new JSONObject();
            List<MakeDepartmentSimple> makeDepartmentSimples = makeDepartmentService.selectInfo(districtId,null,"01");
            jsonObject.put("makeDepartment",makeDepartmentSimples);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取制作单位信息失败");
        }
    }

}
