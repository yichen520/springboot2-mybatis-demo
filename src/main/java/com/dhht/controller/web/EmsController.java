package com.dhht.controller.web;

import com.dhht.common.JsonObjectBO;
import com.dhht.model.Ems;
import com.dhht.service.ems.EmsService;
import com.dhht.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public JsonObjectBO selectDistrict(@RequestBody Ems ems){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {

            int result = emsService.insertEms(ems);
            if(result==ResultUtil.isError){
                jsonObjectBO.setMessage("导出失败");
                jsonObjectBO.setCode(-1);
            }else{
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("导出成功");
            }
        }catch (Exception e){
            jsonObjectBO.setCode(-1);
            return JsonObjectBO.exception("导出失败");
        }
        return jsonObjectBO;
    }
}
