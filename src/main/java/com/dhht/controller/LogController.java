package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Log;
import com.dhht.service.Log.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys/log")
public class LogController {
    @Autowired
    private LogService logService;

    @RequestMapping(value = "/info")
    public JsonObjectBO selectLog(@RequestBody Map map){
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");


        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        PageInfo<Log> logs = logService.selectAllLog(pageSize,pageNum);
        jsonObject.put("log",logs);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        return jsonObjectBO;

    }
}
