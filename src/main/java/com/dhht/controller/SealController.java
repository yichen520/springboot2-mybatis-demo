package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.model.pojo.SealOperator;
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
@RequestMapping(value="/seal/record")
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
     * @param
     * @return
     */
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String districtId = user.getDistrictId();
        String operatorTelphone = sealOperator.getSealOperationRecord().getOperatorTelphone();
        String operatorName = sealOperator.getSealOperationRecord().getOperatorName();
        String operatorCertificateCode = sealOperator.getSealOperationRecord().getOperatorCertificateCode();
        String operatorCrtificateType = sealOperator.getSealOperationRecord().getOperatorCrtificateType();
        String operatorPhoto = sealOperator.getOperatorPhoto();
        String idCardScanner = sealOperator.getIdCardScanner();
        String proxy =  sealOperator.getProxy();
        Seal seal = sealOperator.getSeal();
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
    public JsonObjectBO sealInfo(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        String recordCode = employee.getOfficeCode();
        String useDepartmentName = sealOperator.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealOperator.getSeal().getUseDepartmentCode();
        String status = sealOperator.getSeal().getSealStatusCode();
        int pageNum = sealOperator.getPageNum();
        int pageSize = sealOperator.getPageSize();
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
     * @paraml
     * @return
     */
    @RequestMapping("/sealUpload")
    public JsonObjectBO sealUpload(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
        String electronicSealURL = sealOperator.getElectronicSealURL();
        String sealScannerURL = sealOperator.getSealScannerURL();
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
     * @param
     * @return
     */
    @RequestMapping("/sealPersonal")
    public JsonObjectBO sealPersonal(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
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
     * @param
     * @return
     */
    @RequestMapping("/deliver")
    public JsonObjectBO deliver (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
        SealGetPerson sealGetPerson = sealOperator.getSealGetPerson();
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

    /**
     * 挂失
     * @param httpServletRequest
     * @param
     * @return
     */
    @RequestMapping("/loss")
    public JsonObjectBO loss (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        String recordCode = employee.getOfficeCode();
        Seal seal = sealOperator.getSeal();
        SealOperationRecord sealOperationRecord = sealOperator.getSealOperationRecord();
        String operatorPhoto = sealOperator.getOperatorPhoto();
        String businessScanner = sealOperator.getBusinessScanner();
        String proxy = sealOperator.getProxy();
        int a =sealService.loss(user,seal,operatorPhoto,proxy,businessScanner,sealOperationRecord,recordCode);
        if(a<0){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("挂失失败");
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("挂失成功");
        }
        return jsonObjectBO;
    }

    /**
     * 注销
     * @param httpServletRequest
     * @param
     * @return
     */
    @RequestMapping("/logout")
    public JsonObjectBO logout (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        Employee employee = employeeService.selectByPhone(telphone);
        Seal seal = sealOperator.getSeal();
        SealOperationRecord sealOperationRecord = sealOperator.getSealOperationRecord();
        String operatorPhoto = sealOperator.getOperatorPhoto();
        String businessScanner = sealOperator.getBusinessScanner();
        String proxy = sealOperator.getProxy();
        int a = sealService.logout(user,seal,operatorPhoto,proxy,businessScanner,sealOperationRecord);
        if(a<0){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("注销失败");
        }else{
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("注销成功");
        }
        return jsonObjectBO;
    }
}
