package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Examine;
import com.dhht.model.ExamineDetail;
import com.dhht.model.User;
import com.dhht.service.examine.MinitorService;
import com.dhht.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/examine")
public class ExamineController {

    private static Logger logger = LoggerFactory.getLogger(ExamineController.class);

    @Autowired
    private MinitorService minitorService;

    /**
     *新增监督表配置
     * @param examine 监督表实体
     * @return
     */
    @Log("新增监督表配置")
    @PostMapping("add")
    public JsonObjectBO add(HttpServletRequest httpServletRequest, @RequestBody Examine examine){
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        examine.setDistrictId(user.getDistrictId());
        try {
            if (minitorService.add(examine)){
                return JsonObjectBO.success("新增监督检查表成功",null);
            }else {
                return JsonObjectBO.error("新增监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 查询监督表配置
     * @param map 传入minitor类型（非必选）例如 1代表制作单位配置表等
     * @return
     */
    @RequestMapping("info")
    @Log("查询监督表配置")
    public JsonObjectBO info( @RequestBody Map map,HttpServletRequest httpServletRequest){
        Integer pageSize = (Integer) map.get("pageSize");
        Integer pageNum = (Integer) map.get("pageNum");
        User user =(User)httpServletRequest.getSession().getAttribute("user");
        String districtId =StringUtil.getDistrictId( user.getDistrictId());
        try {
            JSONObject jsonObject = new JSONObject();
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<Examine> pageInfo = new PageInfo<Examine>(minitorService.info(districtId));
            jsonObject.put("examine", pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 查询监督表配置
     * @return
     */
    @RequestMapping("items")
    @Log("查询此表各检查项")
    public JsonObjectBO items( @RequestBody Map map){
        String id = (String) map.get("id");
        try {
            List<ExamineDetail> examineDetails = minitorService.items(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("examineDetails", examineDetails);
            if (examineDetails !=null){
                return JsonObjectBO.success("查询此表各检查项成功",jsonObject);
            }else {
                return JsonObjectBO.error("查询此表各检查项失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    /**
     * 删除监督表配置
     * @param map 传入minitor类型（非必选）例如 1代表制作单位配置表等
     * @return
     */
    @RequestMapping("delete")
    @Log("删除监督表配置")
    public JsonObjectBO delete(@RequestBody Map map){
        String id = (String) map.get("id");
        try {
            if (minitorService.delete(id)){
                return JsonObjectBO.success("删除监督检查表成功",null);
            }else {
                return JsonObjectBO.error("删除监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    @RequestMapping("itemdelete")
    @Log("删除检查项配置")
    public JsonObjectBO itemdelete(@RequestBody Map map){
        String id = (String) map.get("id");
        try {
            if (minitorService.itemdelete(id)){
                return JsonObjectBO.success("删除此检查项成功",null);
            }else {
                return JsonObjectBO.error("删除此检查项失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 修改监督表配置
     * @param examine 修改的实体
     * @return
     */
    @RequestMapping("update")
    @Log("修改监督表配置")
    public JsonObjectBO update(@RequestBody Examine examine){

        try {
            if (minitorService.update(examine)){
                return JsonObjectBO.success("修改监督检查表成功",null);
            }else {
                return JsonObjectBO.error("修改监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 修改检查项配置
     * @param
     * @return
     */
    @RequestMapping("itemupdate")
    @Log("修改监督表配置")
    public JsonObjectBO itemupdate(@RequestBody ExamineDetail examineDetail){

        try {
            if (minitorService.itemupdate(examineDetail)){
                return JsonObjectBO.success("修改监督检查表成功",null);
            }else {
                return JsonObjectBO.error("修改监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }



}
