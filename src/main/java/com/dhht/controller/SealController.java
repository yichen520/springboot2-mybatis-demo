package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.ImageGenerate;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;

import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealVO;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.seal.SealService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/seal/record")
public class SealController {
    @Autowired
    private SealService sealService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UseDepartmentService useDepartmentService;

    private static Logger logger = LoggerFactory.getLogger(SealController.class);

    @Log("查询使用单位是否备案")
    @RequestMapping("/isrecord")
    public JsonObjectBO isrecord(@RequestBody Map map) {

        JSONObject jsonObject = new JSONObject();
        try {
            String useDepartmentCode = (String) map.get("useDepartmentCode");
            UseDepartment useDepartment = sealService.isrecord(useDepartmentCode);
            if (useDepartment == null) {
                return JsonObjectBO.error("该使用单位没有印章备案资格");
            } else {
                jsonObject.put("useDepartment", useDepartment);
                return JsonObjectBO.success("查询成功", jsonObject);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查询失败");
        }
    }

    @Log("印章刻制添加")
    @RequestMapping("add")
    public JsonObjectBO add(@RequestBody Seal seal) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            User user = (User) request.getSession(true).getAttribute("user");
            seal.setId(UUIDUtil.generate());
            seal.setRecordDepartmentCode(user.getUserName());
            seal.setRecordDepartmentName(user.getRealName());
            return JsonObjectBO.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("添加失败发生异常");
        }
    }

    /**
     * 备案
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("印章备案")
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String districtId = user.getDistrictId();
        String agentTelphone = sealDTO.getTelphone();
        String agentName = sealDTO.getName();
        String certificateNo = sealDTO.getCertificateNo();
        String certificateType = sealDTO.getCertificateType();
        String agentPhotoId = sealDTO.getAgentPhotoId();
        String idCardFrontId = sealDTO.getIdCardFrontId();//身份证正面扫描件
        String idCardReverseId = sealDTO.getIdCardReverseId();//身份证反面扫描件
        String proxyId = sealDTO.getProxyId();
        String faceCompareRecordId = sealDTO.getFaceCompareRecordId();
        String idCardPhotoId =sealDTO.getAgentPhotoId();
        String fieldPhotoId = sealDTO.getFieldPhotoId();
        int confidence = sealDTO.getConfidence();
        Seal seal = sealDTO.getSeal();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.sealRecord(seal,user,districtId, agentTelphone,
                    agentName,certificateNo, certificateType,
                    agentPhotoId,  idCardFrontId,  idCardReverseId,   proxyId,  idCardPhotoId, confidence,
             fieldPhotoId);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("添加成功");
            } else if (a == ResultUtil.isHaveSeal) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("法定章已经存在");
            } else if(a==ResultUtil.isNoDepartment){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("备案单位或制作单位不存在");
            }else if(a==ResultUtil.isNoEmployee){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("从业人员不存在");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("备案失败");
        }


    }

    /**
     * 印章信息

     */
    @Log("印章信息")
    @RequestMapping("/sealInfo")
    public JsonObjectBO sealInfo(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        String useDepartmentName = sealDTO.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealDTO.getSeal().getUseDepartmentCode();
        String status = sealDTO.getSeal().getSealStatusCode();
        int pageNum = sealDTO.getPageNum();
        int pageSize = sealDTO.getPageSize();
        try {
            PageInfo<Seal> seal = sealService.sealInfo(user,useDepartmentName, useDepartmentCode, status, pageNum, pageSize);
            jsonObject.put("seal", seal);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("查询成功");
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("印章信息获取失败");
        }
    }


    /**
     * 上传印模
     *
     * @param httpServletRequest
     * @return
     * @paraml
     */
    @Log("上传印模")
    @RequestMapping("/sealUpload")
    public JsonObjectBO sealUpload(HttpServletRequest httpServletRequest, @RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
//        Seal seal = sealOperator.getSeal();
        String id = (String)map.get("id");
        String sealedCardId = (String)map.get("sealedCardId");//印鉴留存卡
        String imageDataId = (String)map.get("imageDataId");//印章图像数据
        try {
            int a = sealService.sealUpload(user, id,  sealedCardId,  imageDataId);
            if (a < 0) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("上传印模失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("上传印模成功");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("印模上传失败");
        }
    }


    /**
     * 个人化
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("个人化")
    @RequestMapping("/sealPersonal")
    public JsonObjectBO sealPersonal(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
//        Seal seal = sealOperator.getSeal();
        String id = sealDTO.getId();
        try {
            int a = sealService.sealPersonal(id, user);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("个人化失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("个人化成功");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("个人化失败");
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
    public JsonObjectBO deliver(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String id = sealDTO.getId();
        String proxyId = sealDTO.getProxyId();
        String name = sealDTO.getName();
        String certificateType = sealDTO.getCertificateType();
        String certificateNo = sealDTO.getCertificateNo();
        String agentTelphone = sealDTO.getTelphone();
        Boolean isSame = sealDTO.getIsSame();
        try {
            boolean a = sealService.deliver(user, id, proxyId, name, certificateType, certificateNo, agentTelphone, isSame);
            if (a) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("交付成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("交付失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("交付失败");
        }
    }

    /**
     * 挂失
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("挂失")
    @RequestMapping("/loss")
    public JsonObjectBO loss(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();

        try {
            User user = (User) httpServletRequest.getSession(true).getAttribute("user");
            String telphone = user.getTelphone();
            String localDistrictId = user.getDistrictId();
            String id = sealDTO.getId();
            String agentPhotoId = sealDTO.getAgentPhotoId();
            String proxyId = sealDTO.getProxyId();
            String certificateType = sealDTO.getCertificateType();
            String certificateNo = sealDTO.getCertificateNo();
            String idCardFrontId = sealDTO.getIdCardFrontId();
            String idCardReverseId = sealDTO.getIdCardReverseId();
            String businesslicenseId = sealDTO.getBusinesslicenseId();
            int a = sealService.loss(  user, id,  agentPhotoId,   proxyId , certificateNo, certificateType,
                     localDistrictId, businesslicenseId, idCardFrontId, idCardReverseId);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("挂失失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("挂失成功");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("挂失失败");
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
    public JsonObjectBO logout(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String telphone = user.getTelphone();
        try {
            Employee employee = employeeService.selectByPhone(telphone);
            String id = sealDTO.getId();
            String agentPhotoId = sealDTO.getAgentPhotoId();
            String proxyId = sealDTO.getProxyId();
            String certificateNo = sealDTO.getCertificateNo();
            String certificateType = sealDTO.getCertificateType();
            String idCardFrontId = sealDTO.getIdCardFrontId();
            String idCardReverseId = sealDTO.getIdCardReverseId();
            String businesslicenseId = sealDTO.getBusinesslicenseId();
            int a = sealService.logout( user, id,  agentPhotoId,   proxyId , certificateNo, certificateType, businesslicenseId,idCardFrontId,idCardReverseId);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("注销失败");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("注销成功");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("注销失败");
        }
    }


    /**
     * 根据名字进行了查询
     */
    @Log("根据名字进行查询")
    @RequestMapping(value = "/selectByName", method = RequestMethod.POST)
    public JsonObjectBO selectByName(@RequestBody Map map) {
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String name = (String) map.get("name");
        try {
            List<UseDepartment> useDepartment = useDepartmentService.selectUseDepartment(name);
            jsonObject.put("useDepartment", useDepartment);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查询使用单位失败");
        }
    }


    /**
     * 查看印章详情
     *
     * @param map
     * @return
     */
    @Log("查看印章详情")
    @RequestMapping(value = "selectDetailById")
    public JsonObjectBO selectDetailById(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String id = (String) map.get("id");
        try{
            SealVO sealVo = sealService.selectDetailById(id);
            jsonObject.put("sealVo", sealVo);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查看印章详情失败");
        }
    }

    /**
     * 人证合一
     *
     * @param map
     * @return
     */
    @Log("人证合一")
    @RequestMapping(value = "/facecheck")
    public JsonObjectBO checkface(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        String idCardId = (String) map.get("idCardPhoto");
        String fieldId = (String) map.get("fieldPhoto");
        try{
            FaceCompareResult face = sealService.faceCompare(idCardId,fieldId);
            if(face==null){
                return JsonObjectBO.error("对比失败");
            }
            jsonObject.put("face",face);
            return JsonObjectBO.success("对比成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("对比失败");
        }
    }

    @Log("印模模板生成")
    @RequestMapping(value = "sealtemplate")
    public JsonObjectBO sealtemplate(@RequestBody Map map){
        String sealTemplatePath =  new ImageGenerate().seal(map);
        int[][] a =new ImageGenerate().moulageData(map);
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sealTemplatePath",sealTemplatePath);
            jsonObject.put("a",a);
            return JsonObjectBO.success("印模模板生成成功",jsonObject);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("印模模板生成失败");
        }
    }





}
