package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.MakeDepartmentSealPrice;
import com.dhht.model.User;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/make/price")
public class MakeDepartmentSealPriceController {

    @Autowired
    private MakeDepartmentSealPriceService makeDepartmentSealPriceService;

    @RequestMapping("/insert")
    public JsonObjectBO insert(@RequestBody List<MakeDepartmentSealPrice> makeDepartmentSealPriceList,HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            int result = makeDepartmentSealPriceService.insertSealPriceRecord(makeDepartmentSealPriceList,user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("新增失败");
        }
    }

    @RequestMapping("/update")
    public JsonObjectBO update(@RequestBody List<MakeDepartmentSealPrice> makeDepartmentSealPrices,HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            int result = makeDepartmentSealPriceService.updateRecordsByIds(makeDepartmentSealPrices,user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("修改失败");
        }
    }

    @RequestMapping("/delete")
    public JsonObjectBO delete(HttpServletRequest httpServletRequest){
        try {
            User user = (User)httpServletRequest.getSession().getAttribute("user");
            int result = makeDepartmentSealPriceService.deleteByUser(user);
            return ResultUtil.getResult(result);
        }catch (Exception e){
            return JsonObjectBO.exception("删除失败");
        }
    }

    @RequestMapping("/info")
    public JsonObjectBO info(HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            JSONObject jsonObject = new JSONObject();
            List<MakeDepartmentSealPrice> makeDepartmentSealPrices = makeDepartmentSealPriceService.selectByUser(user);
            jsonObject.put("makeDepartmentSealPrices",makeDepartmentSealPrices);
            return JsonObjectBO.success("查询成功",jsonObject);

        }catch (Exception e){
            return JsonObjectBO.exception("查询失败");
        }
    }

}
