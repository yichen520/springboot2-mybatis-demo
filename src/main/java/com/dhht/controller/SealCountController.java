package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
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

    @Autowired
    private DistrictService districtService;

    /**
     * 根据制作单位查询
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByMakedepartment")
    public JsonObjectBO selectBymakedepartment(@RequestBody Map map,HttpServletRequest httpServletRequest) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            List<String> sealTypeCodes = (List<String>) map.get("sealTypeCodes");
            List<String> districtIds = (List<String>) map.get("districtIds");
            String startTime = (String) map.get("startTime");
            String endTime = (String) map.get("endTime");
            List<SealCount> sealCounts = sealCuontService.countByDepartment( user,districtIds, sealTypeCodes, startTime, endTime);
            jsonObject.put("sealCounts", sealCounts);
            return JsonObjectBO.success("查询", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.getMessage());
        }

    }


    /**
     * 根据区域查询
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectByDistrictId")
    public JsonObjectBO selectByDistrictId(@RequestBody Map map, HttpServletRequest httpServletRequest) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<String> districtIds = (List) map.get("districtIds");
        List<String> sealTypeCodes = (List) map.get("sealTypeCodes");
        List<String> Status = (List<String>) map.get("Ststus");
        String districtId = (String) map.get("districtId");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        try {
            List<SealCount> sealCounts = sealCuontService.countByDistrictId(user, districtIds, sealTypeCodes, startTime, endTime);
            jsonObject.put("sealCounts", sealCounts);

            return JsonObjectBO.success("查询", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.getMessage());
        }

    }

    /**
     * 根据区域查询制作单位
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectMakeDepartment")
    public JsonObjectBO selectMakeDepartment(@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        String districtId = (String) map.get("districtId");
        String status = "01";

        try {
            List<MakeDepartmentSimple> makeDepartmentSimples = makeDepartmentService.selectInfo(districtId, null, status);
            jsonObject.put("makeDepartmentSimples", makeDepartmentSimples);

            return JsonObjectBO.success("查询", jsonObject);
        } catch (Exception e) {
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
            List<DistrictMenus> districtMenus = districtService.selectOneDistrict(user.getDistrictId()) ;
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }
}

