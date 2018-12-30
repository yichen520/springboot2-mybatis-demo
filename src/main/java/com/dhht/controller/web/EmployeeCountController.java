package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Count;
import com.dhht.model.DistrictMenus;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "count/employee")
public class EmployeeCountController {

    @Autowired
    private EmployeeCountService employeeCountService;
    @Autowired
    private DistrictService districtService;

    private static Logger logger = LoggerFactory.getLogger(EmployeeCountController.class);

    /**
     * 数据展示接口
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/info")
    @Log("获取从业人员统计列表")
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
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("发生异常！");
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

