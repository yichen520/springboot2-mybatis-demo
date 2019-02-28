package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealCuontService;
import com.dhht.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private static Logger logger = Logger.getLogger(SealCountController.class);


    /**
     * 根据制作单位查询
     * @param map
     * @return
     */
    @Log("根据制作单位统计")
    @RequestMapping(value = "/selectByMakedepartment")
    public JsonObjectBO selectBymakedepartment(@RequestBody Map map,HttpServletRequest httpServletRequest) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            List<String> sealTypeCodes = (List<String>) map.get("sealTypeCodes");
            List<String> districtIds = (List<String>) map.get("districtIds");
            String startTime = (String) map.get("startTime");
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String endTime1 = (String) map.get("endTime");
            Date date = fmt.parse(endTime1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            String  endTime = calendar.getTime().toString();
            List<SealCount> sealCounts = sealCuontService.countByDepartment( user,districtIds, sealTypeCodes, startTime, endTime);
            jsonObject.put("sealCounts", sealCounts);
            return JsonObjectBO.success("查询", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("统计失败");
        }

    }


    /**
     * 根据区域查询
     *
     * @param map
     * @return
     */
    @Log("根据区域统计")
    @RequestMapping(value = "/selectByDistrictId")
    public JsonObjectBO selectByDistrictId(@RequestBody Map map, HttpServletRequest httpServletRequest) {
        try {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        List<String> districtIds = (List) map.get("districtIds");
        List<String> sealTypeCodes = (List) map.get("sealTypeCodes");
        List<String> Status = (List<String>) map.get("Ststus");
        String districtId = (String) map.get("districtId");
        String startTime = (String) map.get("startTime");
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String endTime1 = (String) map.get("endTime");
        Date date = fmt.parse(endTime1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        String  endTime = calendar.getTime().toString();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");

            List<SealCount> sealCounts = sealCuontService.countByDistrictId(user, districtIds, sealTypeCodes, startTime, endTime);
            jsonObject.put("sealCounts", sealCounts);

            return JsonObjectBO.success("查询", jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("统计失败");
        }

    }

    /**
     * 根据区域查询制作单位
     *
     * @param map
     * @return
     */
    @Log("根据区域查制作单位")
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
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("统计失败");
        }

    }

    /**
     * 区域
     * @param httpServletRequest
     * @return
     */
    @Log("获取区域")
    @RequestMapping(value = "/district")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();

        try {
            List<DistrictMenus> districtMenus = districtService.selectOneDistrict(user.getDistrictId()) ;
            jsonObject.put("district",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取区域失败");
        }
    }
}

