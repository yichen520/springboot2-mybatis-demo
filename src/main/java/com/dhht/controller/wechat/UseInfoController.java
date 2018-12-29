package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.UseDepartment;
import com.dhht.service.useDepartment.UseDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/weChat/useDepartment")
public class UseInfoController {
    @Autowired
    private UseDepartmentService useDepartmentService;

//    private String mobilePhone = currentUserMobilePhone();

    /**
     * 根据社会统一信用编码查询企业信息
     * @param map
     * @return
     */
    @RequestMapping("/selectByCode")
    public JsonObjectBO selectByCode(@RequestBody Map map){
        try {
            String socialCode = (String) map.get("code");
            UseDepartment useDepartment = useDepartmentService.selectByCode(socialCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useDepartment",useDepartment);
            return JsonObjectBO.success("查询企业信息成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询企业信息失败");
        }
    }
}