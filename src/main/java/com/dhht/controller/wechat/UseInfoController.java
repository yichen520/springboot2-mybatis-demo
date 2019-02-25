package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.common.JsonObjectBO;

import com.dhht.model.Seal;
import com.dhht.model.UseDepartment;
import com.dhht.model.User;
import com.dhht.model.WeChatUser;
import com.dhht.service.seal.SealService;
import com.dhht.service.seal.WeChatSealService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.ResultUtil;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weChat/useDepartment")
public class UseInfoController extends WeChatBaseController {
    @Autowired
    private UseDepartmentService useDepartmentService;
    @Autowired
    private SealService sealService;
    @Autowired
    private WeChatSealService weChatSealService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 根据社会统一信用编码查询企业信息
     *
     * @param map
     * @return
     */
    @RequestMapping("/selectByCode")
    public JsonObjectBO selectByCode(@RequestBody Map map) {
        try {
            String socialCode = (String) map.get("code");
            UseDepartment useDepartment = useDepartmentService.selectActiveUseDepartmentByCode(socialCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useDepartment", useDepartment);
            return JsonObjectBO.success("查询企业信息成功", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception("查询企业信息失败");
        }
    }

    @RequestMapping(value = "/useInfo", method = RequestMethod.POST)
    public JsonObjectBO useInfo(@RequestBody Map map) {
        try {
            String name = (String) map.get("name");
            List<UseDepartment> useDepartments = useDepartmentService.selectUseDepartment(name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useInfo", useDepartments);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception("获取使用单位失败");
        }
    }

    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(@RequestBody Map map) {
        String code = (String) map.get("code");
        JSONObject jsonObject = new JSONObject();
        try {
            List<Seal> seal = weChatSealService.portalSealInfoByCode(code);
            jsonObject.put("seals", seal);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "印章信息获取失败");
        }
    }

    @RequestMapping("/checkSealCode")
    public JsonObjectBO checkSealCode(@RequestBody Map map) {
        try {
            String sealCode = (String) map.get("sealCode");
            String useDepartmentCode = (String) map.get("useDepartmentCode");
            String sealTypeCode = (String) map.get("sealTypeCode");
            System.out.println(sealCode + "------" + useDepartmentCode + "---------" + sealTypeCode);
            int result = sealService.checkSealCode(sealCode, useDepartmentCode, sealTypeCode);
            if (result == ResultUtil.isSuccess) {
                return JsonObjectBO.ok("核验成功");
            } else {
                return JsonObjectBO.error("核验失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询失败");
        }
    }

    @RequestMapping(value = "/companyUser",method = RequestMethod.GET)
    public JsonObjectBO getCompanyUser(HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            WeChatUser weChatUser = currentUser();
            if (!weChatUser.isCompanyAccout()) {
                return JsonObjectBO.error("非企业用户");
            }
            String useDepartmentFlag = weChatUser.getCompany();
            UseDepartment useDepartment = useDepartmentService.selectByFlag(useDepartmentFlag);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useDepartment", useDepartment);
            return JsonObjectBO.success("获取企业用户成功", jsonObject);
        } catch (Exception e) {
            return JsonObjectBO.exception("获取企业用户异常");
        }
    }

    @RequestMapping("/updateCompany")
    public JsonObjectBO updateCompany(@RequestBody UseDepartment useDepartment) {
        try {
            WeChatUser weChatUser = currentUser();
            User user = new User();
            user.setId(weChatUser.getId());
            user.setRealName(weChatUser.getName());
            int result =  useDepartmentService.updateFromWeChatAPP(useDepartment, user);
            return ResultUtil.getResult(result);
        } catch (Exception e) {
            return JsonObjectBO.exception("企业变更异常");
        }
    }


    @RequestMapping("/getCompany")
    public JsonObjectBO getCompanyByWeChatUser(HttpServletRequest httpServletRequest){
        try {
            WeChatUser weChatUser = currentUser();
            String flag = weChatUser.getCompany();
            UseDepartment useDepartment = useDepartmentService.selectByFlag(flag);
            if(useDepartment==null){
                return JsonObjectBO.error("您未设置默认企业");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("useDepartment",useDepartment);
            return JsonObjectBO.success("获取成功",jsonObject);
        }catch (Exception e){
            return JsonObjectBO.exception("获取企业信息失败");
        }
    }
}