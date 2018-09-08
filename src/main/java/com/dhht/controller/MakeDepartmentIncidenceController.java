package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.IncidencePO;
import com.dhht.service.District.DistrictService;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.make.MakeDepartmentIncidenceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.recordDepartment.RecordDepartmentService;
import com.dhht.service.tools.FileService;
import com.dhht.service.tools.HistoryService;
import com.dhht.sync.SyncDataType;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/incidence")
public class MakeDepartmentIncidenceController implements InitializingBean {
    @Autowired
    private MakeDepartmentIncidenceService incidenceService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    private static Logger logger = LoggerFactory.getLogger(MakeDepartmentIncidenceController.class);



    @Log("获取制作单位发案")
    @RequestMapping(value = "/info")
    public JsonObjectBO selectByDepartmentCode(@RequestBody IncidencePO map, HttpServletRequest httpServletRequest) {
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
        List<Incidence> list = new ArrayList<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
            list = incidenceService.selectInfo(map);
            PageInfo<Incidence> result = new PageInfo<>(list);
            jsonObject.put("incidenceList", result);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("获取制作单位发案列表失败！");
        }finally {
            PageHelper.clearPage();
        }
        return JsonObjectBO.success("查询制作单位发案成功", jsonObject);
    }



    @Log("制作单位发案添加")
    @RequestMapping(value = "/add")
    public JsonObjectBO add(@RequestBody Incidence incidence, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(incidenceService.insertincidence(incidence, user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("添加失败！");
        }
    }

    @Log("修改制作单位发案")
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Incidence incidence, HttpServletRequest httpServletRequest) {
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(incidenceService.updateincidence(incidence,user));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("修改失败");
        }
    }


    @Log("删除制作单位发案")
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map,HttpServletRequest httpServletRequest) {
        String id = (String) map.get("id");
        try {
            return ResultUtil.getResult(incidenceService.deleteincidence(id));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("删除失败！");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String serialCode = incidenceService.selectMaxSerialCode();
        if(serialCode == null) {
            redisTemplate.opsForValue().set("serialCode", 0000);
        }else{
            String temp = serialCode.substring(19);
            Integer code = Integer.parseInt(temp);
            if(!redisTemplate.hasKey("serialCode")){
                redisTemplate.opsForValue().set("serialCode", code);
            }else {
                redisTemplate.opsForValue().getAndSet("serialCode",code);
            }
        }
    }

    @Log("查看制作单位列表")
    @RequestMapping(value = "/makeDepartment")
    public JsonObjectBO commoninfo(HttpServletRequest httpServletRequest){
        User user =(User)httpServletRequest.getSession().getAttribute("user");
//        String districtId = (String)map.get("districtId");
//        String name = (String)map.get("name");
        String status = "01";

        JSONObject jsonObject = new JSONObject();
        List<MakeDepartmentSimple> list = new ArrayList<>();
        try {


                list = makeDepartmentService.selectInfo(user.getDistrictId(),null,status);
                jsonObject.put("makeDepartment", list);

//            if(districtId==""||districtId==null) {}else {
//                list = makeDepartmentService.selectInfo(districtId,name,status);
//                jsonObject.put("makeDepartment", list);
//            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }finally {
            PageHelper.clearPage();
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }



}
