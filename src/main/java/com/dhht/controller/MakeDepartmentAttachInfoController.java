package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.MakeDepartmentAttachInfo;
import com.dhht.model.User;
import com.dhht.service.make.MakeDepartmentAttacthInfoService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("make/attach")
public class MakeDepartmentAttachInfoController {
    @Autowired
    private MakeDepartmentAttacthInfoService makeDepartmentAttacthInfoService;

    @RequestMapping("/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            MakeDepartmentAttachInfo makeDepartmentAttachInfo = makeDepartmentAttacthInfoService.selectByMakeDepartmentFlag(user);
            jsonObject.put("makeDepartmentAttachInfo",makeDepartmentAttachInfo);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
        }
    }

    @RequestMapping("/insert")
    public JsonObjectBO insert(@RequestBody MakeDepartmentAttachInfo makeDepartmentAttachInfo,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            int result = makeDepartmentAttacthInfoService.insert(makeDepartmentAttachInfo,user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("新增失败");
        }
    }

    @RequestMapping("/update")
    public JsonObjectBO update(@RequestBody MakeDepartmentAttachInfo makeDepartmentAttachInfo,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            int result = makeDepartmentAttacthInfoService.updateById(makeDepartmentAttachInfo,user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("修改失败");
        }
    }

    
}
