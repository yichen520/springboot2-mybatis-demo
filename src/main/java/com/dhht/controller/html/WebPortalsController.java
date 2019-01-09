package com.dhht.controller.html;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.Cache;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.model.*;
import com.dhht.model.pojo.SealDTO;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.punish.PunishService;
import com.dhht.service.seal.SealService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.WeChatUserService;
import com.dhht.util.ResultUtil;
import com.dhht.util.StringUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private SealService sealService;

    /**
     * 省级区域数据
     * @return
     */
    @RequestMapping(value = "/districtInfo",method = RequestMethod.GET)
    public JsonObjectBO getDistrictId(){
        try {
            List<DistrictMenus> districtMenus = (List<DistrictMenus>) Cache.get("tempProvinceDistrict");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("districtMenus", districtMenus);
            return JsonObjectBO.success("获取区域成功", jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取区域失败");
        }
    }

    /**
     * 制作单位信息
     * @param districtId
     * @return
     */
    @RequestMapping(value = "/makeDepartmentInfo",method = RequestMethod.GET)
    public JsonObjectBO getMakeDepartmentByDistrictId(@RequestParam String districtId){
        try {
            //String districtId = districtIds.get(2);
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

    @RequestMapping(value = "/useInfo",method = RequestMethod.GET)
    public JsonObjectBO useInfo(@RequestParam String name){
        try {
            List<UseDepartment> useDepartments = useDepartmentService.selectUseDepartment(name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useInfo",useDepartments);
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取使用单位失败");
        }
    }

    @Log("印章信息")
    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(String code) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();

        User user =null;
        try {
            List<Seal> seal = sealService.portalSealInfoByCode(code);
            jsonObject.put("seal", seal);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"印章信息获取失败");
        }
    }

    /**
     * 印章核验
     * @param map
     * @return
     */
    @Log("印章核验")
    @RequestMapping("/checkSealCode")
    public JsonObjectBO checkSealCode(@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String sealCode = (String)map.get("sealCode");
            String useDepartmentCode = (String)map.get("useDepartmentCode");
            String sealTypeCode = (String)map.get("sealTypeCode");
            int result = sealService.checkSealCode(sealCode,useDepartmentCode,sealTypeCode);
            if(result==ResultUtil.isSuccess){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询成功");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("无该枚印章");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询失败");
        }
    }



}
