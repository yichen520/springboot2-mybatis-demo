package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Seal;
import com.dhht.model.UseDepartment;
import com.dhht.model.Users;
import com.dhht.service.seal.SealService;
import com.dhht.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value="/seal")
public class SealController  {
    @Autowired
    private SealService sealService;

    private static JSONObject jsonObject = new JSONObject();;

    @Log("查询使用单位是否备案")
    @RequestMapping("isrecord")
    public JsonObjectBO isrecord(@RequestBody Map map){

        try {
            String useDepartmentCode =(String) map.get("useDepartmentCode");
            UseDepartment useDepartment =sealService.isrecord(useDepartmentCode);
            if (useDepartment == null){
                return JsonObjectBO.error("改使用单位没有印章备案资格");
            }else {
                jsonObject.put("useDepartment",useDepartment);
                return JsonObjectBO.success("查询成功",jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发生异常");
        }
    }

    @Log("印章刻制添加")
    @RequestMapping("add")
    public JsonObjectBO add(@RequestBody Seal seal){
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Users users = (Users)request.getSession(true).getAttribute("user");
            seal.setId(UUIDUtil.generate());
            seal.setRecordDepartmentCode(users.getUserName());
            seal.setRecordDepartmentName(users.getRealName());
            //从从业人员查找制作单位   稍后做

            sealService.insert(seal);
            return JsonObjectBO.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("，添加失败发生异常");
        }
    }



}
