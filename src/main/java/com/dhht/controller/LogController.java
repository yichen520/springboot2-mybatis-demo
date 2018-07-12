package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.SysLog;
import com.dhht.service.Log.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/sys/log")
public class LogController {
    @Autowired
    private LogService logService;


    @RequestMapping(value = "info")
    public JsonObjectBO findLog(@RequestBody Map map){
        Integer pageSize = (Integer) map.get("pageSize");
        Integer pageNum = (Integer) map.get("pageNum");
        String start = (String) map.get("start");
        String end = (String) map.get("end");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        PageInfo<SysLog> logs = logService.findLog(start,end,pageNum,pageSize);
        jsonObject.put("log",logs);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }
}
