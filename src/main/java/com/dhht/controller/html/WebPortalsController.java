package com.dhht.controller.html;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.Cache;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.model.*;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/portal")
public class WebPortalsController extends BaseController {

    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentSealPriceService makeDepartmentSealPriceService;
    @Autowired
    private UseDepartmentService useDepartmentService;
    @Autowired
    private WeChatUserService weChatUserService;
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private PunishService punishService;
    @Value("${sms.template.makedepartmentpunish}")
    private int makedepartmentpunish ;


    /**
     * 省级区域数据
     * @return
     */
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

    /**
     * 制作单位信息
     * @param districtIds
     * @return
     */
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

    /**
     * 制作单位价格数据
     * @param map
     * @return
     */
    @RequestMapping(value = "/sealPriceInfo",method = RequestMethod.POST)
    public JsonObjectBO getSealPrice(@RequestBody Map map){
        try {
            String makeDepartmentFlag = (String)map.get("makeDepartmentFlag");
            JSONObject jsonObject = new JSONObject();
            List<MakeDepartmentSealPrice> makeDepartmentSealPrices = makeDepartmentSealPriceService.selectByMakeDepartmentFlag(makeDepartmentFlag);
            jsonObject.put("sealPrice",makeDepartmentSealPrices);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("查询制作单位价格失败");
        }
    }

    /**
     * 模拟推送工商数据
     * @param useDepartment
     * @return
     */
    @RequestMapping("/pushUseDepartment")
    public JsonObjectBO pushUseDepartment(@RequestBody UseDepartment useDepartment){
        try {
            User user = new User();
            user.setId(UUIDUtil.generate());
            user.setUserName("系统");
            user.setRealName("工商推送");
            //给注册经办人发送验证短信
            weChatUserService.sendMessage(useDepartment.getManagerPhone());
            return useDepartmentService.insert(useDepartment,user);
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("推送失败");
        }
    }
    @Log("验证手机号")
    @RequestMapping(value ="checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode){
        try {
            return userLoginService.checkPhoneAndIDCard(smsCode);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发送短信发生异常");
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

}
