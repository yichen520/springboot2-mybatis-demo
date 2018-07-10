package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.seal.SealService;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value="/seal")
public class SealController  {
    @Autowired
    private SealService sealService;

    @Autowired
    private EmployeeService employeeService;

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
            User user = (User)request.getSession(true).getAttribute("user");
            seal.setId(UUIDUtil.generate());
            seal.setRecordDepartmentCode(user.getUserName());
            seal.setRecordDepartmentName(user.getRealName());
            //从从业人员查找制作单位   稍后做

//            sealService.insert(seal);
            return JsonObjectBO.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("，添加失败发生异常");
        }
    }

    /**
     * 备案
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(HttpServletRequest httpServletRequest,@RequestBody Map map){
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String districtId = user.getDistrictId();
        String operatorTelphone = (String) map.get("operatorTelphone");
        String operatorName = (String) map.get("operatorName");
        String operatorCertificateCode = (String) map.get("operatorCertificateCode");
        String operatorCrtificateType = (String) map.get("operatorCrtificateType");
        String operatorPhoto = (String) map.get("operatorPhoto");
        String idCardScanner = (String) map.get("idCardScanner");
        String proxy = (String) map.get("proxy");
        Seal seal = (Seal)map.get("seal");
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        int a = sealService.sealRecord(seal,user,districtId,operatorTelphone,operatorName,operatorCertificateCode,operatorCrtificateType,operatorPhoto,idCardScanner,proxy);
        if(a==1) {
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
        }
        return jsonObjectBO;

    }

    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(HttpServletRequest httpServletRequest,@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        String recordCode = employee.getOfficeCode();
        String useDepartmentName = (String)map.get("useDepartmentName");
        String useDepartmentCode = (String)map.get("useDepartmentCode");
        String status = (String)map.get("status");
        int pageNum = (int)map.get("pageNum");
        int pageSize = (int)map.get("pageSize");
        PageInfo<Seal> seal = sealService.sealInfo(recordCode,useDepartmentName,useDepartmentCode,status,pageNum,pageSize);
        jsonObject.put("seal",seal);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
    }


    /**
     * 上传印模
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping("sealUpload")
    public JsonObjectBO sealUpload(HttpServletRequest httpServletRequest,@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = (Seal)map.get("seal");
        String electronicSealURL = (String)map.get("electronicSealURL");
        String sealScannerURL = (String)map.get("sealScannerURL");
        int a =sealService.sealUpload(user,seal,electronicSealURL, sealScannerURL);
        if(a<0){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("上传印模失败");
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("上传印模成功");
        }
        return jsonObjectBO;
    }


    /**
     * 个人化
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping("sealPersonal")
    public JsonObjectBO sealPersonal(HttpServletRequest httpServletRequest,@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = (Seal)map.get("seal");
        int a = sealService.sealPersonal (seal, user);
        if(a<0){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("个人化失败");
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("个人化成功");
        }
        return jsonObjectBO;
    }

    /**
     * 交付
     * @param httpServletRequest
     * @param map
     * @return
     */
    @RequestMapping("deliver")
    public JsonObjectBO deliver (HttpServletRequest httpServletRequest,@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = (Seal)map.get("seal");
        SealGetPerson sealGetPerson = (SealGetPerson)map.get("sealGetPerson");
        boolean a = sealService.deliver(user,seal,sealGetPerson);
        if(a){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("交付成功");
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("交付失败");
        }
        return jsonObjectBO;
    }
}
