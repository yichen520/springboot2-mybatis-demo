package com.dhht.controller;


import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.tools.HistoryService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/seal/useDepartment")
public class UseDepartmentController  {


    @Autowired
   private UseDepartmentService useDepartmentService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private HistoryService historyService;

    private static Logger logger = LoggerFactory.getLogger(UseDepartmentController.class);



    /***
     * 添加
     * @param
     * @return
     */
    @Log("添加用户")
    @RequestMapping(value ="/insert", method = RequestMethod.POST)
    public JsonObjectBO insert(@RequestBody UseDepartment useDepartment,HttpServletRequest httpServletRequest){
        try{
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        JsonObjectBO jsonObjectBO = useDepartmentService.insert(useDepartment,user);
        return jsonObjectBO;}
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 修改
     * @param useDepartment
     * @return
     */
    @Log("使用单位修改")
    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public JsonObjectBO update(@RequestBody UseDepartment useDepartment,HttpServletRequest httpServletRequest){
        try {
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JsonObjectBO jsonObjectBO = useDepartmentService.update(useDepartment,user);
        return jsonObjectBO;
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 模糊查找
     * @param
     * @return
     */
    @Log("使用单位模糊查找")
    @RequestMapping(value = "/find" , method = RequestMethod.POST)
    public JsonObjectBO find(HttpServletRequest httpServletRequest,@RequestBody Map map){
        String code = (String)map.get("code");
        String name = (String)map.get("name");
        String districtId = (String)map.get("districtId");
        String departmentStatus = (String)map.get("status");
        int pageNum = (int) map.get("pageNum");
        int pageSize = (int) map.get("pageSize");
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String localDistrictId = StringUtil.DistrictUtil(user.getDistrictId())[0]+"0000";
        try{
        JsonObjectBO jsonObjectBO = useDepartmentService.find(localDistrictId,code, name, districtId, departmentStatus, pageNum, pageSize);
        return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    /**
     * 删除
     * @param useDepartment
     * @return
     */
    @Log("使用单位删除")
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public JsonObjectBO delete(@RequestBody UseDepartment useDepartment,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            JsonObjectBO jsonObjectBO = useDepartmentService.delete(useDepartment,user);
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    /**
     * 查看历史
     * @param map
     * @return
     */
    @Log("查看历史")
    @RequestMapping(value = "/showHistory" , method = RequestMethod.POST)
    public JsonObjectBO showMore(@RequestBody Map map){
        String flag = (String)map.get("flag");
        try {
            //List<OperatorRecord> operatorRecords = historyService.showUpdteHistory(flag, SyncDataType.USERDEPARTMENT);
           return useDepartmentService.showHistory(flag);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 查看制作单位的详情
     * @param map
     * @return
     */
    @Log("查看使用单位详情")
    @RequestMapping(value = "/showMore")
    public JsonObjectBO selectDetailById(@RequestBody Map map){
        String id = (String)map.get("id");
        UseDepartment useDepartment = new UseDepartment();
        JSONObject jsonObject = new JSONObject();
        try{
            useDepartment = useDepartmentService.selectDetailById(id);
            jsonObject.put("useDepartment",useDepartment);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }



    /**
     * 获取区域列表
     * @param httpServletRequest
     * @return
     */
    @Log("获取区域列表")
    @RequestMapping(value = "/districtInfo")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String localDistrictId = StringUtil.DistrictUtil(user.getDistrictId())[0]+"0000";
        try {
            districtMenus = districtService.selectOneDistrict(localDistrictId);
            jsonObject.put("districtMenus",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

}