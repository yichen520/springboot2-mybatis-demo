package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.RecordPolice;
import com.dhht.service.police.PoliceService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/record/police")
public class PoliceController {

    @Autowired
    private PoliceService policeService;

    private JSONObject jsonObject = new JSONObject();

    @RequestMapping(value = "info")
    public JsonObjectBO selectAllPolice(@RequestBody Map map){
        int pageSum = (Integer)map.get("pageSize");
        int pageNum = (Integer)map.get("pageNum");

        PageInfo<RecordPolice> pageInfo = new PageInfo<RecordPolice>();
        try{
            pageInfo = policeService.selectAllPolice(pageSum,pageNum);
            jsonObject.put("Police",pageInfo);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功",jsonObject);
    }

    @RequestMapping(value = "/selectByOfficeCode")
    public JsonObjectBO selectByOfficeCode(@RequestBody Map map) {
        int pageSum = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        String Code = (String) map.get("code");

        PageInfo<RecordPolice> recordPolice = new PageInfo<RecordPolice>();
        try {
            recordPolice = policeService.selectByOfficeCode(Code, pageSum, pageNum);
            jsonObject.put("Police", recordPolice);
        } catch (Exception e) {
            return JsonObjectBO.exception(e.getMessage());
        }
        return JsonObjectBO.success("查询成功", jsonObject);
    }

    @RequestMapping(value = "/delete")
    public JsonObjectBO deleteById(@RequestBody Map map){
        String id = (String) map.get("id");
       boolean result = false;
        try {
            result = policeService.deleteByPrimaryKey(id);
        }catch (Exception e){
            return JsonObjectBO.exception(e.getMessage());
        }
        if(result){
            return JsonObjectBO.ok("删除成功");
        }else {
            return JsonObjectBO.error("删除失败");
        }
    }



}
