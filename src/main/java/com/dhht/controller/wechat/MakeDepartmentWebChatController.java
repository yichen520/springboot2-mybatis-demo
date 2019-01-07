package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.MakeDepartmentSealPrice;
import com.dhht.model.MakeDepartmentSimple;
import com.dhht.model.Makedepartment;
import com.dhht.model.pojo.MakedepartmentSimplePO;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/make")
public class MakeDepartmentWebChatController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentSealPriceService makeDepartmentSealPriceService;

    /**
     * 制作单位价格数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/sealPriceInfo",method = RequestMethod.POST)
    public JsonObjectBO getSealPrice(@RequestBody Map map){
        try {
            String makeDepartmentFlag = (String)map.get("makeDepartmentFlag");
            String sealType = (String)map.get("sealType");
            JSONObject jsonObject = new JSONObject();
            MakeDepartmentSealPrice makeDepartmentSealPrice = makeDepartmentSealPriceService.selectByMakeDepartmentFlagAndType(makeDepartmentFlag,sealType);
            jsonObject.put("sealPrice",makeDepartmentSealPrice);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询制作单位价格失败");
        }
    }


    @RequestMapping(value = "/selectMakedePartment",method = RequestMethod.POST)
    public JsonObjectBO selectMakedePartment(@RequestBody MakedepartmentSimplePO makedepartmentSimplePO){
        try {
            JSONObject jsonObject = new JSONObject();
            List<MakedepartmentSimplePO> makedepartmentSimplePOs = makeDepartmentService.selectMakedePartment(makedepartmentSimplePO);
            jsonObject.put("makedepartmentList",makedepartmentSimplePOs);
            return JsonObjectBO.success("查询制作单位成功",jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询制作单位失败");
        }
    }


    @RequestMapping("/detail")
    public JsonObjectBO makeDetail(@RequestBody Map map){
        try {
            String id = (String) map.get("id");
            JSONObject jsonObject = new JSONObject();
            Makedepartment makedepartment = makeDepartmentService.selectDetailById(id);
            jsonObject.put("makeDepartment",makedepartment);
            return JsonObjectBO.success("获取详情成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取制作单位详情失败");
        }
    }
}
