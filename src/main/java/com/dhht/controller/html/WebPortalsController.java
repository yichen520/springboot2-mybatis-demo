package com.dhht.controller.html;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.Cache;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.BaseController;
import com.dhht.model.*;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.user.UserLoginService;
import com.dhht.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/web")
public class WebPortalsController extends BaseController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private PunishService punishService;
    @Value("${sms.template.makedepartmentpunish}")
    private int makedepartmentpunish ;

    @RequestMapping(value = "/districtInfo",method = RequestMethod.GET)
    public JsonObjectBO getDistrictId(){
        try {
            List<DistrictMenus> districtMenus = (List<DistrictMenus>) Cache.get("provinceDistrict");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("districtMenus", districtMenus);
            return JsonObjectBO.success("获取区域成功", jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }

    @RequestMapping(value = "/makeDepartmentInfo",method = RequestMethod.POST)
    public JsonObjectBO getMakeDepartmentByDistrictId(@RequestBody List<String> districtIds){
        try {
            String districtId = districtIds.get(2);
            JSONObject jsonObject = new JSONObject();
            List<MakeDepartmentSimple> makeDepartmentSimples = makeDepartmentService.selectInfo(districtId,null,"01");
            jsonObject.put("makeDepartment",makeDepartmentSimples);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取制作单位信息失败");
        }
    }


    @RequestMapping(value = "/sendCode")
    public JsonObjectBO punishEmployeeCode( @RequestBody Map map){
        User user = currentUser();
        if (user == null){
            return   JsonObjectBO.sessionLose("session失效");
        }
        String phone = (String)map.get("telphone");
        String code = StringUtil.createRandomVcode();
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
        if (punishService.sendcode1(phone,makedepartmentpunish,params)){
            return  JsonObjectBO.ok("获取验证码成功");
        }else{
            return JsonObjectBO.error("获取验证码失败");
        }
    }

    @Log("处罚验证手机号")
    @RequestMapping(value ="checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode){
        try {
            return userLoginService.checkAPPPhoneAndIDCard(smsCode);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发送短信发生异常");
        }
    }

}
