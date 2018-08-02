package com.dhht.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.CurrentUser;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.examine.MinitorService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/2 create by xzp
 */
@RestController
@RequestMapping(value = "examine/makeDepartment")
public class MakeDepartmentExamineController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private MinitorService minitorService;

    @Autowired
    private RecordDepartmentService recordDepartmentService;

    private static Logger logger = LoggerFactory.getLogger(MakeDepartmentExamineController.class);


    private JSONObject jsonObject = new JSONObject();
    /**
     * 展示制作单位的列表
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO info(@RequestBody Map map, HttpServletRequest httpServletRequest){
        User user =(User)httpServletRequest.getSession().getAttribute("user");
        if (!CurrentUser.validatetoken(httpServletRequest)){
          return   JsonObjectBO.sessionLose("session失效");
        }
        String name = (String)map.get("name");
        JSONObject jsonObject = new JSONObject();
        List<MakeDepartmentSimple> list = new ArrayList<>();
        try {
                 list = makeDepartmentService.selectInfo(user.getDistrictId(),name,"01");
                jsonObject.put("makeDepartment", list);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


    /**
     * 获取此民警区域和上级区域调查表
     * @return
     */
    @RequestMapping(value = "/getexamineform")
    public JsonObjectBO punish(HttpServletRequest httpServletRequest){
        User user =(User)httpServletRequest.getSession().getAttribute("user");
        if (!CurrentUser.validatetoken(httpServletRequest)){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String districtId = user.getDistrictId();
        try {
            List<Examine> survey = minitorService.selectExamineForm(districtId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("examineform",survey);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }



    /**
     *对制作单位进行检查操作
     * @param httpServletRequest
     * @param examineRecord
     * @return
     */
    @RequestMapping(value = "/punish")
    public JsonObjectBO punish(HttpServletRequest httpServletRequest,@RequestBody ExamineRecord examineRecord){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        if (!CurrentUser.validatetoken(httpServletRequest)){
            return   JsonObjectBO.sessionLose("session失效");
        }
        try {
              if (recordDepartmentService.insertPunish(user,examineRecord)){
                  return JsonObjectBO.success("制作单位检查成功",null);
              }else {
                  return JsonObjectBO.error("制作单位检查失败");
              }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    @RequestMapping(value = "/getpunishinfo")
    public JsonObjectBO find(HttpServletRequest httpServletRequest, @RequestBody Map map){
        if (!CurrentUser.validatetoken(httpServletRequest)){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String makedepartmentName = (String)map.get("makedepartmentName");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String districtId = (String) map.get("districtId");
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        if (districtId == null || districtId == ""){
             districtId = StringUtil.getDistrictId(user.getDistrictId());
        }else{
             districtId = StringUtil.getDistrictId(districtId);
        }

        Integer pageSize =(Integer) map.get("pageSize");
        Integer pageNum =(Integer) map.get("pageNum");

        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<ExamineRecord>  pageInfo =new PageInfo<ExamineRecord> (recordDepartmentService.findPunish(makedepartmentName,startTime,endTime,districtId));
            jsonObject.put("punishInfo",pageInfo);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 展示通过id来
     * @param map

     * @return
     */
    @RequestMapping(value = "/examinedetail")
    public JsonObjectBO examinedetail(HttpServletRequest httpServletRequest,@RequestBody Map map){
        if (!CurrentUser.validatetoken(httpServletRequest)){
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
