package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.tools.ShowHistoryService;
import com.dhht.sync.SyncDataType;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/7/2 create by fyc
 */
@RestController
@RequestMapping(value = "make/makeDepartment")
public class MakeDepartmentController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private ShowHistoryService showHistoryService;

    private static Logger logger = LoggerFactory.getLogger(MakeDepartmentController.class);

    /**
     * 展示制作单位的列表
     * @param map
     * @param httpServletRequest
     * @return
     */
    @Log("查看制作单位列表")
    @RequestMapping(value = "/info")
    public JsonObjectBO info(@RequestBody Map map, HttpServletRequest httpServletRequest){
        User user =(User)httpServletRequest.getSession().getAttribute("user");
        String districtId = (String)map.get("districtId");
        String name = (String)map.get("name");
        String status = (String) map.get("status");
        Integer pageNum = (Integer) map.get("pageNum");
        Integer pageSize = (Integer) map.get("pageSize");

        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageNum, pageSize);
        List<MakeDepartmentSimple> list = new ArrayList<>();
        try {

            if(districtId==""||districtId==null) {
                 list = makeDepartmentService.selectInfo(user.getDistrictId(),name,status);
                 PageInfo<MakeDepartmentSimple> result = new PageInfo<>(list);
                jsonObject.put("makeDepartment", result);
            }else {
                list = makeDepartmentService.selectInfo(districtId,name,status);
                PageInfo<MakeDepartmentSimple> result = new PageInfo<>(list);
                jsonObject.put("makeDepartment", result);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

//    /**
//     * 展示修改记录
//     * @param map
//     * @return
//     */
//    @Log("查看修改记录")
//    @RequestMapping(value = "/showHistory")
//    public JsonObjectBO selectHistory(@RequestBody Map map){
//        String flag = (String)map.get("flag");
//        List<Makedepartment> result = new ArrayList<>();
//        JSONObject jsonObject = new JSONObject();
//        try {
//            result = makeDepartmentService.selectHistory(flag);
//            jsonObject.put("makeDepartment",result);
//        }catch (Exception e){
//            logger.error(e.getMessage(),e);
//            return JsonObjectBO.exception(e.toString());
//        }
//        return JsonObjectBO.success("查询成功",jsonObject);
//    }

    @Log("查看修改记录")
    @RequestMapping(value = "/showHistory")
    public JsonObjectBO selectHistory(@RequestBody Map map){
        String flag = (String)map.get("flag");
        List<OperatorRecord> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try {
            result = showHistoryService.showUpdteHistory(flag, SyncDataType.MAKEDEPARTMENT);
            jsonObject.put("makeDepartment",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }


    /**
     * 查看制作单位的详情
     * @param map
     * @return
     */
    @Log("查看制作单位详情")
    @RequestMapping(value = "/selectDetailById")
    public JsonObjectBO selectDetailById(@RequestBody Map map){
        String id = (String)map.get("id");
        Makedepartment makedepartment = new Makedepartment();
        JSONObject jsonObject = new JSONObject();
        try{
            makedepartment = makeDepartmentService.selectDetailById(id);
            jsonObject.put("makedepartment",makedepartment);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 添加制作单位
     * @param makedepartment
     * @return
     */
    @Log("添加制作单位")
    @RequestMapping(value = "insert")
    public JsonObjectBO insert(@RequestBody Makedepartment makedepartment,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        try {
            int result = makeDepartmentService.insert(makedepartment,user);
            return ResultUtil.getResult(result);
        }catch (DuplicateKeyException d){
            return JsonObjectBO.exception("该用户已经存在");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 注销制作单位
     * @param map
     * @return
     */
    @Log("注销制作单位")
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        String id = (String) map.get("id");
        int result ;
        try{
            result = makeDepartmentService.deleteById(id,user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 修改制作单位
     * @param makedepartment
     * @return
     */
    @Log("修改制作单位")
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Makedepartment makedepartment,HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");

        try {
            int result = makeDepartmentService.update(makedepartment,user);
            return ResultUtil.getResult(result);
        }catch (DuplicateKeyException d){
            return JsonObjectBO.error("该用户已存在");
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
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();
        try {
            districtMenus = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("districtMenus",districtMenus);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
           return JsonObjectBO.exception(e.toString());
        }
    }



    /**
     * 制作单位处罚记录查询
     * @param map
     * @return
     */
    @Log("制作单位处罚记录查询")
    @RequestMapping(value = "/selectPunish")
    public JsonObjectBO selectPunish(@RequestBody Map map){
        String makeDepartmentName = (String)map.get("MakeDepartmentName");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        String districtId = (String)map.get("districtId");
        User user = (User)map.get("user");
        String localDistrictId = user.getDistrictId();
        List<Makedepartment> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        try {
            result = makeDepartmentService.selectPunish(makeDepartmentName,startTime,endTime,districtId,localDistrictId);
            jsonObject.put("makeDepartment",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            JsonObjectBO.exception(e.toString());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }
}
