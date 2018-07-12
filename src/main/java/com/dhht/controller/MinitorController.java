package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Employee;
import com.dhht.model.Minitor;
import com.dhht.service.minitor.MinitorService;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping(value = "/minitor")
public class MinitorController {

    private static Logger logger = LoggerFactory.getLogger(MinitorController.class);

    private JSONObject jsonObject = new JSONObject();
    @Autowired
    private MinitorService minitorService;

    /**
     *新增监督表配置
     * @param minitor 监督表实体
     * @return
     */
    @Log("新增监督表配置")
    @PostMapping("add")
    public JsonObjectBO add(@RequestBody Minitor minitor){
        minitor.setId(UUIDUtil.generate());
        try {
            if (minitorService.add(minitor)){
                return JsonObjectBO.success("新增监督检查表成功",null);
            }else {
                return JsonObjectBO.error("新增监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 查询监督表配置
     * @param map 传入minitor类型（非必选）例如 1代表制作单位配置表等
     * @return
     */
    @RequestMapping("info")
    @Log("查询监督表配置")
    public JsonObjectBO info(@RequestBody Map map){
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        Integer minitor = (Integer) map.get("minitor");
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<Minitor> pageInfo = new PageInfo<Minitor>(minitorService.info(minitor));
            jsonObject.put("employee", pageInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
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
            return JsonObjectBO.exception(e.getMessage());
        }
    }

    /**
     * 修改监督表配置
     * @param minitor 修改的实体
     * @return
     */
    @RequestMapping("update")
    @Log("修改监督表配置")
    public JsonObjectBO update(@RequestBody Minitor minitor){

        try {
            if (minitorService.update(minitor)){
                return JsonObjectBO.success("修改监督检查表成功",null);
            }else {
                return JsonObjectBO.error("修改监督检查表失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.getMessage());
        }
    }

}
