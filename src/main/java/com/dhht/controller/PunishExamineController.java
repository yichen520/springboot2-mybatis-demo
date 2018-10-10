package com.dhht.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.user.UserLoginService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 2018/7/2 create by xzp
 */
@RestController
public class PunishExamineController {



    @Autowired
    private PunishService punishService;
    @Autowired
    private RecordDepartmentService recordDepartmentService;
    @Autowired
    private MakeDepartmentService makeDepartmentService;


    @Value("${sms.template.employeepunish}")
    private int employeepunish ;
    @Value("${sms.template.makedepartmentpunish}")
    private int makedepartmentpunish ;
    @Value("${expireTime}")
    private long expireTime;

    private static Logger logger = LoggerFactory.getLogger(PunishExamineController.class);



    @RequestMapping(value = "makedepartment/punish/info")
    public JsonObjectBO find(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("districtId");
        if (startTime !=null){
            startTime =startTime+" 00:00:00";
        }
        if (endTime !=null){
            endTime =endTime+" 23:59:59";
        }
        if (districtId == null || districtId == ""){
            districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
            districtId = StringUtil.getDistrictId(districtId);
        }

        Integer pageSize =(Integer) map.get("pageSize");
        Integer pageNum =(Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<MakePunishRecord>  pageInfo =new PageInfo<MakePunishRecord> (punishService.findPunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("punishInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


    /**
     *对从业人员处罚进行查询操作
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "employee/punish/info")
    public JsonObjectBO employeefind(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        if (startTime !=null){
            startTime =startTime+" 00:00:00";
        }
        if (endTime !=null){
            endTime =endTime+" 23:59:59";
        }
        String districtId = (String) map.get("districtId");
        if (districtId == null || districtId == ""){
            districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
            districtId = StringUtil.getDistrictId(districtId);
        }
        Integer pageSize =(Integer) map.get("pageSize");

        Integer pageNum =(Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<EmployeePunishRecord>  pageInfo =new PageInfo<EmployeePunishRecord> (punishService.findEmployeePunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("punishInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }
    @RequestMapping(value = "makedepartment/examine/info")
    public JsonObjectBO getpunishinfo(HttpServletRequest httpServletRequest, @RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("districtId");
        if (startTime !=null){
            startTime =startTime+" 00:00:00";
        }
        if (endTime !=null){
            endTime =endTime+" 23:59:59";
        }
        if (districtId == null || districtId == ""){
            districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
            districtId = StringUtil.getDistrictId(districtId);
        }

        Integer pageSize =(Integer) map.get("pageSize");
        Integer pageNum =(Integer) map.get("pageNum");
        JSONObject jsonObject = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<ExamineRecord>  pageInfo =new PageInfo<ExamineRecord> (recordDepartmentService.findPunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("examineInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "makedepartment/examine/examinedetail")
    public JsonObjectBO examinedetail(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String id = (String)map.get("id");
        JSONObject jsonObject = new JSONObject();
        try {
            List<ExamineRecordDetail> list= makeDepartmentService.selectExamineDetailByID(id);
            jsonObject.put("makeDepartment", list);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

}
