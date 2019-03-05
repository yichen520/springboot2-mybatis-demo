package com.dhht.controller.web;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.Ems;
import com.dhht.service.ems.EmsService;
import com.dhht.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/ems")
public class EmsController {
    @Resource
    private EmsService emsService;


    /**
     * ems
     * @return
     */
    @RequestMapping(value = "/ems")
    public Map<String,Object>  selectDistrict(@RequestBody Ems ems){
        Map<String,Object> resultMap = new HashMap<>();
        try {

            resultMap = emsService.insertEms(ems);
            return resultMap;
        }catch (Exception e){
            resultMap.put("status", "error");
            resultMap.put("message","出现异常");
            return resultMap;
        }
    }

    /**
     * 下载ems文件
     * @param map
     * @return
     */
    @RequestMapping(value = "/downloadEms")
    public Map<String,Object>  downLoadEms(@RequestBody Map map){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String id = (String) map.get("id");
            resultMap = emsService.downLoad(id);
            return resultMap;
        }catch (Exception e){
            resultMap.put("status", "error");
            resultMap.put("message","出现异常");
            return resultMap;
        }
    }
}
