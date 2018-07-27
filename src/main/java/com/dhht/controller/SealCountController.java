package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.SealCount;
import com.dhht.model.User;
import com.dhht.service.employee.EmployeeCountService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealCuontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/count/seal")
public class SealCountController {

    @Autowired
    private SealCuontService sealCuontService;

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    /**
     * 根据制作单位查询
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByMakedepartment")
    public JsonObjectBO selectBymakedepartment(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<String> makeDepartmentCodes = (List<String>)map.get("makeDepartmentCodes");
        List<String> sealTypeCodes = (List<String>)map.get("sealTypeCodes") ;
        List<String> Status = (List<String>)map.get("Ststus");
        String districtId = (String)map.get("districtId");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        try {
            List<SealCount> sealCounts = sealCuontService.countByDepartment(makeDepartmentCodes,districtId,sealTypeCodes,Status,startTime,endTime);
            jsonObject.put("sealCounts",sealCounts);
            return JsonObjectBO.success("查询",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }

    }


    /**
     * 根据区域查询
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByDistrictId")
    public JsonObjectBO selectByDistrictId(@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<String> districtIds = (List)map.get("districtIds");
        List<String> sealTypeCodes = (List)map.get("sealTypeCodes") ;
        List<String> Status = (List<String>)map.get("Ststus");
        String districtId = (String)map.get("districtId");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        try {
            List<SealCount> sealCounts = sealCuontService.countByDistrictId(districtIds, sealTypeCodes, Status,startTime, endTime);
            jsonObject.put("sealCounts",sealCounts);

            return JsonObjectBO.success("查询",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }

    }

    /**
     * 根据区域查询制作单位
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectMakeDepartment")
    public JsonObjectBO selectMakeDepartment (@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        String districtId = (String) map.get("districtId");
        String status = "01";

        try {
            List<MakeDepartmentSimple> makeDepartmentSimples = makeDepartmentService.selectInfo(districtId,null,status);
            jsonObject.put("makeDepartmentSimples",makeDepartmentSimples);

            return JsonObjectBO.success("查询",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }

    }
}

