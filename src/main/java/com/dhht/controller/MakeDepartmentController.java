package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.DistrictMenus;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.model.User;
import com.dhht.service.District.DistrictService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
@RequestMapping(value = "seal/makeDepartment")
public class MakeDepartmentController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;

    @Autowired
    private DistrictService districtService;

    /**
     * 展示制作单位的列表
     * @param map
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/info")
    public JsonObjectBO info(@RequestBody Map map, HttpServletRequest httpServletRequest){
        User user =(User)httpServletRequest.getSession().getAttribute("user");
        String districtId = (String)map.get("districtId");
        String name = (String)map.get("name");
        String status = (String) map.get("status");
        int pageNum = (Integer) map.get("pageNum");
        int pageSize = (Integer) map.get("pageSize");


        JSONObject jsonObject = new JSONObject();
        List<MakeDepartmentSimple> list = new ArrayList<>();
        try {
            PageHelper.startPage(pageNum, pageSize);
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
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }



    /**
     * 展示修改记录
     * @param map
     * @return
     */
    @RequestMapping(value = "/showHistory")
    public JsonObjectBO selectHistory(@RequestBody Map map){
        String flag = (String)map.get("flag");

        List<Makedepartment> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        try {
            result = makeDepartmentService.selectHistory(flag);
            jsonObject.put("makeDepartment",result);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 查看制作单位的详情
     * @param map
     * @return
     */
    @RequestMapping(value = "/selectDetailById")
    public JsonObjectBO selectDetailById(@RequestBody Map map){
        String id = (String)map.get("id");

        Makedepartment makedepartment = new Makedepartment();
        JSONObject jsonObject = new JSONObject();
        try{
            makedepartment = makeDepartmentService.selectDetailById(id);
            jsonObject.put("makedepartment",makedepartment);
        }catch (Exception e){
            JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    /**
     * 添加制作单位
     * @param makedepartment
     * @return
     */
    @RequestMapping(value = "insert")
    public JsonObjectBO insert(@RequestBody Makedepartment makedepartment){
        JSONObject jsonObject = new JSONObject();
        int result = 0;

        try {
            result = makeDepartmentService.insert(makedepartment);
            return ResultUtil.getResult(result);
        }catch (DuplicateKeyException d){
            return JsonObjectBO.exception("该用户已经存在");
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 注销制作单位
     * @param map
     * @return
     */
    @RequestMapping(value = "/delete")
    public JsonObjectBO delete(@RequestBody Map map){
        String id = (String) map.get("id");

        JSONObject jsonObject = new JSONObject();
        int result = 0;

        try{
            result = makeDepartmentService.deleteById(id);
            return ResultUtil.getResult(result);
        }catch (Exception e){
           return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 修改制作单位
     * @param makedepartment
     * @return
     */
    @RequestMapping(value = "/update")
    public JsonObjectBO update(@RequestBody Makedepartment makedepartment){
        JSONObject jsonObject = new JSONObject();
        int result = 0;

        try {
            result = makeDepartmentService.update(makedepartment);
            return ResultUtil.getResult(result);
        }catch (DuplicateKeyException d){
            return JsonObjectBO.error("该用户已存在");
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 获取区域列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/districtInfo")
    public JsonObjectBO selectDistrict(HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        List<DistrictMenus> districtMenus = new ArrayList<>();
        try {
            districtMenus = districtService.selectOneDistrict(user.getDistrictId());
            jsonObject.put("districtMenus",districtMenus);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }
}
