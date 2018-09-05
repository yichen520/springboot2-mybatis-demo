package com.dhht.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.ImageGenerate;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;

import com.dhht.model.pojo.FileInfoVO;
import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealVO;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.Base64Util;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dhht.idcard.trusted.identify.GuangRayIdentifier;
import dhht.idcard.trusted.identify.IdentifyResult;
import sun.misc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/seal/record")
public class SealController implements InitializingBean {
    @Autowired
    private SealService sealService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UseDepartmentService useDepartmentService;

    @Autowired
    private FileService fileService;

    private static Logger logger = LoggerFactory.getLogger(SealController.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Seal seal = sealService.selectLastSeal();
        if(seal== null) {
            redisTemplate.opsForValue().set("SealSerialNum",0);
        }
        if(!redisTemplate.hasKey("SealSerialNum")){
            redisTemplate.opsForValue().set("SealSerialNum", Integer.parseInt(seal.getSealCode().substring(6)));
        }

    }


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
        String useDepartmentCode =sealDTO.getUseDepartmentCode();
        String entryType = sealDTO.getEntryType();
        int confidence = sealDTO.getConfidence();
        List<Seal> seals = sealDTO.getSeals();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.sealRecord(seals,user,useDepartmentCode,districtId, agentTelphone,
                    agentName,certificateNo, certificateType,
                    agentPhotoId,  idCardFrontId,  idCardReverseId,   proxyId,  idCardPhotoId, confidence,
             fieldPhotoId,entryType);

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
            } else if(a==ResultUtil.isNoProxy){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("缺少授权委托书");
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
            String name = sealDTO.getName();
            String agentPhotoId = sealDTO.getAgentPhotoId();
            String proxyId = sealDTO.getProxyId();
            String certificateType = sealDTO.getCertificateType();
            String certificateNo = sealDTO.getCertificateNo();
            String idCardFrontId = sealDTO.getIdCardFrontId();
            String idCardReverseId = sealDTO.getIdCardReverseId();
            String businesslicenseId = sealDTO.getBusinessLicenseId();
            String agentTelphone = sealDTO.getTelphone();
            int a = sealService.loss(  user, id, name, agentPhotoId,   proxyId , certificateNo, certificateType,
                     localDistrictId, businesslicenseId, idCardFrontId, idCardReverseId,agentTelphone);
            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("挂失成功");
            } else if(a==ResultUtil.isNoProxy){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("请提供授权委托书");
            }else if(a==ResultUtil.isNoName){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("请输入名字");
            } else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("挂失失败");
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
            String agentTelphone = sealDTO.getTelphone();

            String name = sealDTO.getName();
            String businesslicenseId = sealDTO.getBusinessLicenseId();
            int a = sealService.logout( user, id,  name,agentPhotoId,   proxyId , certificateNo, certificateType, businesslicenseId,idCardFrontId,idCardReverseId,agentTelphone);
            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("注销成功");
            } else if(a==ResultUtil.isNoProxy){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("请提供授权委托书");
            }else if(a==ResultUtil.isNoName){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("请输入名字");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("注销失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("注销失败");
        }
    }


    /**
     * 挂失和注销详情
     */
    @Log("挂失和注销详情")
    @RequestMapping(value = "/LossAndLogoutDetail", method = RequestMethod.POST)
    public JsonObjectBO LossAndLogoutDetail(@RequestBody Map map) {
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String id = (String) map.get("id");
        try {
            SealVO sealVO = sealService.lossAndLogoutDetail(id);
            jsonObject.put("sealVO", sealVO);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查看挂失和注销详情失败");
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


    /**
     * 下载文件
     * @param id
     * @return
     */
    @Log("下载文件")
    @RequestMapping(value="/download",produces="application/json;charset=UTF-8")
    public ResponseEntity<byte[]> download(@RequestParam("id") String id) {
        FileInfoVO fileInfoVO = sealService.download(id);
        try {
            Pattern pattern = Pattern.compile("[^\u4E00-\u9FA5]");

            Matcher matcher = pattern.matcher(fileInfoVO.getFileName());
            String fileName = matcher.replaceAll("");
            fileName = URLEncoder.encode(fileName,"utf-8");
            //请求头
            HttpHeaders headers = new HttpHeaders();

            //解决文件名乱码
//            String fileName = new String((fileInfoVO.getFileName()).getBytes("UTF-8"),"iso-8859-1");

            //通知浏览器以attachment（下载方式）打开
            headers.setContentDispositionFormData("attachment", fileName+"."+fileInfoVO.getFileExt());

            //application/octet-stream二进制流数据（最常见的文件下载）。
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(fileInfoVO.getFileData(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Log("是否是法人")
    @RequestMapping(value = "/isLegalPerson", method = RequestMethod.POST)
    public JsonObjectBO isLegalPerson(@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String name = (String) map.get("name");
        String certificateNo = (String) map.get("certificateNo");
        String useDepartmentCode = (String) map.get("useDepartmentCode");
        try {
            Boolean isLegalPerson = sealService.isLegalPerson(certificateNo,name,useDepartmentCode);
            if (isLegalPerson) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("是法人");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("不是法人");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("请求失败");
        }
    }


    /**
     * 可信身份认证
     * @param map
     * @return
     */
    @Log("可信身份认证")
    @RequestMapping(value = "/TrustedIdentityAuthentication", method = RequestMethod.POST)
    public JsonObjectBO TrustedIdentityAuthentication(@RequestBody Map map) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String certificateNo = (String) map.get("certificateNo");
        String name = (String) map.get("name");
        String fieldPhotoId = (String) map.get("fieldPhotoId");
        FileInfoVO fieldFileInfo = fileService.readFile(fieldPhotoId);
        byte[] fileDate = fieldFileInfo.getFileData();
        float fileDatetoKb =fileDate.length/1024;
        if(fileDatetoKb>25||fileDatetoKb<10){
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("重新上传，图片大小请小于25kb大于10kb");
        }else {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String photoDate = base64Encoder.encode(fileDate);
            IdentifyResult identifyResult = GuangRayIdentifier.identify(certificateNo, name, photoDate);
            if(identifyResult.isPassed()){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage(identifyResult.getMessage());
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage(identifyResult.getMessage());
            }
        }
        return jsonObjectBO;
    }



}
