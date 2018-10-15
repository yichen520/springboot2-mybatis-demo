package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.model.pojo.SuspiciousPO;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentIncidenceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.suspicious.SuspiciousService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/suspicious")
public class SuspiciousController  {
    @Autowired
    private SuspiciousService suspiciousService;

    private static Logger logger = LoggerFactory.getLogger(SuspiciousController.class);
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private EmployeeService employeeService;


    @Log("获取可疑情况")
    @RequestMapping(value = "/info")
    public JsonObjectBO selectByDepartmentCode(@RequestBody SuspiciousPO map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        int pageSize =  map.getPageSize();
        int pageNum =  map.getPageNum();
        String districtId = map.getDistrictId();
        if(districtId==""||districtId==null) {
            districtId = user.getDistrictId();
        }
        String dis = StringUtil.getDistrictId(districtId);
        map.setDistrictId(dis);
        JSONObject jsonObject = new JSONObject();
        List<Suspicious> list = new ArrayList<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            list = suspiciousService.selectInfo(map);
            PageInfo<Suspicious> result = new PageInfo<>(list);
            jsonObject.put("suspiciousList", result);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取可疑情况列表失败！");
        }finally {
            PageHelper.clearPage();
        }
        return JsonObjectBO.success("获取可疑情况成功", jsonObject);
    }



    @Log("可疑情况添加")
    @RequestMapping(value = "/add")
    public JsonObjectBO add(@RequestBody Suspicious suspicious, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(suspiciousService.insertsuspicious(suspicious, user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("添加失败！");
        }
    }

    @Log("修改可疑情况")
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Suspicious suspicious, HttpServletRequest httpServletRequest) {
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(suspiciousService.updatesuspicious(suspicious,user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("修改失败");
        }
    }


    @Log("删除可疑情况")
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map,HttpServletRequest httpServletRequest) {
        String id = (String) map.get("id");
        try {
            return ResultUtil.getResult(suspiciousService.deletesuspicious(id));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("删除失败！");
        }
    }

    @Log("查看制作单位列表")
    @RequestMapping(value = "/makeDepartment")
    public JsonObjectBO commoninfo(HttpServletRequest httpServletRequest,@RequestBody Map map){
        try {
            User user =(User)httpServletRequest.getSession().getAttribute("user");
            String name = (String)map.get("name");

            List<MakeDepartmentSimple> makeDepartmentSimples = makeDepartmentService.selectSimpleByDepartmentName(user.getDistrictId(),name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makeDepartment",makeDepartmentSimples);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取制作单位列表失败");
        }

    }

    @RequestMapping(value = "/employee")
    public JsonObjectBO employee(HttpServletRequest httpServletRequest,@RequestBody Map map){
       // User user =(User)httpServletRequest.getSession().getAttribute("user");
        String departmentCode = (String) map.get("departmentCode");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Employee> employees = employeeService.selectAllByDepartmentCode(departmentCode);
            jsonObject.put("employees",employees);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }



}
