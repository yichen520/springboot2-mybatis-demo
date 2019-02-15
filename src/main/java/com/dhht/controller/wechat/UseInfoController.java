package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Seal;
import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.service.seal.SealService;
import com.dhht.service.seal.WeChatSealService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/useDepartment")
public class UseInfoController {
    @Autowired
    private UseDepartmentService useDepartmentService;
    @Autowired
    private SealService sealService;
    @Autowired
    private WeChatSealService weChatSealService;

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
            UseDepartment useDepartment = useDepartmentService.selectActiveUseDepartmentByCode(socialCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useDepartment",useDepartment);
            return JsonObjectBO.success("查询企业信息成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询企业信息失败");
        }
    }

    @RequestMapping(value = "/useInfo",method = RequestMethod.POST)
    public JsonObjectBO useInfo(@RequestBody Map map){
        try {
            String name = (String)map.get("name");
            List<UseDepartment> useDepartments = useDepartmentService.selectUseDepartment(name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useInfo",useDepartments);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取使用单位失败");
        }
    }

    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(@RequestBody Map map) {
        String code = (String) map.get("code");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Seal> seal = weChatSealService.portalSealInfoByCode(code);
            jsonObject.put("seals",seal);
            return JsonObjectBO.success("查询成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"印章信息获取失败");
        }
    }

    @RequestMapping("/checkSealCode")
    public JsonObjectBO checkSealCode(@RequestBody Map map) {
        try {
            String sealCode = (String)map.get("sealCode");
            String useDepartmentCode = (String)map.get("useDepartmentCode");
            String sealTypeCode = (String)map.get("sealTypeCode");
            System.out.println(sealCode+"------"+useDepartmentCode+"---------"+sealTypeCode);
            int result = sealService.checkSealCode(sealCode,useDepartmentCode,sealTypeCode);
            if(result== ResultUtil.isSuccess){
                return JsonObjectBO.ok("核验成功");
            }else {
                return JsonObjectBO.error("核验失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询失败");
        }
    }
}