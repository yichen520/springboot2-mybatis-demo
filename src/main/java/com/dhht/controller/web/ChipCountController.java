package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.ChipGrant;
import com.dhht.model.ExamineCount;
import com.dhht.model.pojo.ChipCountVO;
import com.dhht.service.chipApply.ChipApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Action;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/count/chip")
public class ChipCountController {

    @Autowired
    private ChipApplyService chipApplyService;

    @RequestMapping(value = "grant/info")
    public JsonObjectBO grant(@RequestBody Map map, HttpServletRequest httpServletRequest){
        JSONObject jsonObject = new JSONObject();
        try {
            List<ChipCountVO> counts = chipApplyService.countGrantInfo(map,httpServletRequest);
            jsonObject.put("count",counts);
            return JsonObjectBO.success("",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception(e.toString());
        }
    }


}
