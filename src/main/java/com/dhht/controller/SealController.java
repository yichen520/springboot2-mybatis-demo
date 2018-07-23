package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value="/seal/record")
public class SealController  {
    @Autowired
    private SealService sealService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UseDepartmentService useDepartmentService;

    private static Logger logger = LoggerFactory.getLogger(SealController.class);

    @Log("查询使用单位是否备案")
    @RequestMapping("isrecord")
    public JsonObjectBO isrecord(@RequestBody Map map){

        JSONObject jsonObject = new JSONObject();
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
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
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
    @Log("印章备案")
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String districtId = user.getDistrictId();
        String operatorTelphone = sealOperator.getSealOperationRecord().getOperatorTelphone();
        String operatorName = sealOperator.getSealOperationRecord().getOperatorName();
        String operatorCertificateCode = sealOperator.getSealOperationRecord().getOperatorCertificateCode();
        String operatorCrtificateType = sealOperator.getSealOperationRecord().getOperatorCertificateType();
        String operatorPhoto = sealOperator.getOperatorPhoto();
        String idCardScanner = sealOperator.getIdCardScanner();
        String proxy =  sealOperator.getProxy();
        Seal seal = sealOperator.getSeal();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try{
            int a = sealService.sealRecord(seal,user,districtId,operatorTelphone,operatorName,operatorCertificateCode,operatorCrtificateType,operatorPhoto,idCardScanner,proxy);

        if(a==1) {
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("添加成功");
        }else{
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("添加失败");
        }
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }


    }

    /**
     *印章信息
     * @param httpServletRequest
     * @param sealOperator
     * @return
     */
    @Log("印章信息")
    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
//        Employee employee = employeeService.selectByPhone(telphone);
//        String recordCode = employee.getOfficeCode();
        String useDepartmentName = sealOperator.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealOperator.getSeal().getUseDepartmentCode();
        String status = sealOperator.getSeal().getSealStatusCode();
        int pageNum = sealOperator.getPageNum();
        int pageSize = sealOperator.getPageSize();
        try{
        PageInfo<Seal> seal = sealService.sealInfo(useDepartmentName,useDepartmentCode,status,pageNum,pageSize);
        jsonObject.put("seal",seal);
        jsonObjectBO.setData(jsonObject);
        jsonObjectBO.setCode(1);
        jsonObjectBO.setMessage("查询成功");
        return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    /**
     * 上传印模
     * @param httpServletRequest
     * @paraml
     * @return
     */
    @Log("上传印模")
    @RequestMapping("/sealUpload")
    public JsonObjectBO sealUpload(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
        String electronicSealURL = sealOperator.getElectronicSealURL();
        String sealScannerURL = sealOperator.getSealScannerURL();
        try{
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
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    /**
     * 个人化
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("个人化")
    @RequestMapping("/sealPersonal")
    public JsonObjectBO sealPersonal(HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
        try {
            int a = sealService.sealPersonal(seal, user);
            if (a < 0) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("个人化失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("个人化成功");
            }
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 交付
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("交付")
    @RequestMapping("/deliver")
    public JsonObjectBO deliver (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        Seal seal = sealOperator.getSeal();
        SealGetPerson sealGetPerson = sealOperator.getSealGetPerson();
        try {
            boolean a = sealService.deliver(user, seal, sealGetPerson);
            if (a) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("交付成功");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("交付失败");
            }
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 挂失
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("挂失")
    @RequestMapping("/loss")
    public JsonObjectBO loss (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        try {
            Employee employee = employeeService.selectByPhone(telphone);
            String recordCode = employee.getOfficeCode();
            Seal seal = sealOperator.getSeal();
            SealOperationRecord sealOperationRecord = sealOperator.getSealOperationRecord();
            String operatorPhoto = sealOperator.getOperatorPhoto();
            String businessScanner = sealOperator.getBusinessScanner();
            String proxy = sealOperator.getProxy();
            int a = sealService.loss(user, seal, operatorPhoto, proxy, businessScanner, sealOperationRecord, recordCode);
            if (a < 0) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("挂失失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("挂失成功");
            }
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 注销
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("注销")
    @RequestMapping("/logout")
    public JsonObjectBO logout (HttpServletRequest httpServletRequest,@RequestBody SealOperator sealOperator) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user =(User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        try{
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
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }
    }

    /**
     * 文件上传接口
     * @param request
     * @param file
     * @return
     */
    @Log("文件上传")
    @RequestMapping(value="/upload",produces="application/json;charset=UTF-8")
    public JsonObjectBO singleFileUpload(HttpServletRequest request,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            File uploadFile =fileService.insertFile(request,file);
            if(uploadFile!=null){
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("file",uploadFile);
                return JsonObjectBO.success("文件上传成功",jsonObject);
            }else {
                return JsonObjectBO.error("文件上传失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception("上传文件失败");
        }
    }

    /**
     * 根据名字进行了查询
     */
    @Log("根据名字进行查询")
    @RequestMapping(value = "/selectByName" ,method = RequestMethod.POST)
    public JsonObjectBO selectByName(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String name = (String)map.get("name");
        try {
            UseDepartment useDepartment = useDepartmentService.selectUseDepartment(name);
            jsonObject.put("useDepartment", useDepartment);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonObjectBO.exception(e.toString());
        }

    }
}
