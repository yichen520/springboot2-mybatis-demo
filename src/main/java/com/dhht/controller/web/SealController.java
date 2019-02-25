package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.ImageGenerate;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.*;

import com.dhht.model.pojo.*;
import com.dhht.service.employee.EmployeeService;
import com.dhht.service.seal.SealService;
import com.dhht.service.seal.WeChatSealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import com.github.pagehelper.PageInfo;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
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

    @Autowired
    private WeChatSealService weChatSealService;

    private static Logger logger = LoggerFactory.getLogger(SealController.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        Seal seal = sealService.selectLastSeal();
        if(seal== null) {
            redisTemplate.opsForValue().set("SealSerialNum",0);
            return;
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
        String captcha =sealDTO.getCaptcha();
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
             fieldPhotoId,entryType,captcha);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("添加成功");
            } else if (a == ResultUtil.isHaveSeal) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该公司的法务印章或者单位章已经存在");
            } else if(a==ResultUtil.isNoDepartment){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("备案单位或制作单位不存在");
            }else if(a==ResultUtil.isNoEmployee){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("从业人员不存在");
            } else if(a==ResultUtil.isNoProxy){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("缺少授权委托书");
            } else if(a==ResultUtil.isCodeError){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("验证码错误,请重新输入");
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("备案失败");
        }
    }

    /**
     * 印章交付后备案
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("印章交付后备案")
    @RequestMapping("/newRecord")
    public JsonObjectBO newRecord(HttpServletRequest httpServletRequest, @RequestBody Map map) {
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String sealId = (String)map.get("id");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.newsealRecord(user,sealId);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("备案成功");
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
     * 承接
     *
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("印章承接")
    @RequestMapping("/underTake")
    public JsonObjectBO underTake(HttpServletRequest httpServletRequest, @RequestBody Map map) {
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String sealId = (String)map.get("id");

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.underTake(user,sealId);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("承接成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("承接失败");
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

        String useDepartmentName = sealDTO.getSeal().getUseDepartmentName();
        String useDepartmentCode = sealDTO.getSeal().getUseDepartmentCode();
        String status = sealDTO.getSeal().getSealStatusCode();
        String sealType = sealDTO.getSeal().getSealTypeCode();
        String recordDepartmentName = sealDTO.getSeal().getRecordDepartmentName();
        String sealCode = sealDTO.getSeal().getSealCode();
        int pageNum = sealDTO.getPageNum();
        int pageSize = sealDTO.getPageSize();
        try {
            PageInfo<Seal> seal = sealService.sealInfo(user,useDepartmentName, useDepartmentCode, status, pageNum, pageSize,sealType,recordDepartmentName,sealCode);
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
        String imageDataId = (String)map.get("sealedDataId");//印章图像数据
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
            } if (a == ResultUtil.isNoChipSeal) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该印章不是芯片章");
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
     * 线上快递交付
     * @param httpServletRequest
     * @param
     * @return
     */
    @Log("交付")
    @RequestMapping("/expressdeliver")
    public JsonObjectBO expressdeliver(HttpServletRequest httpServletRequest, @RequestBody Seal seal) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");

        try {
            int a = weChatSealService.expressdeliver( user,  seal);
            return ResultUtil.getResult(a);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("出现未知错误");
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
        String useDepartmentCode = sealDTO.getUseDepartmentCode();
        String agentPhotoId = sealDTO.getAgentPhotoId();
        String idcardFrontId = sealDTO.getIdCardFrontId();
        String idcardReverseId = sealDTO.getIdCardReverseId();
        String entryType = sealDTO.getEntryType();
        int confidence = sealDTO.getConfidence();
        String fieldPhotoId = sealDTO.getFieldPhotoId();
        String idCardPhotoId = sealDTO.getIdCardPhotoId();
        try {
            int a = sealService.deliver( user, id, useDepartmentCode, proxyId, name,
                     certificateType, certificateNo, agentTelphone, agentPhotoId, idcardFrontId, idcardReverseId,
                     entryType, confidence, fieldPhotoId, idCardPhotoId);
            if (a==ResultUtil.isLogout) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该印章已被注销");
            } else if(a==ResultUtil.isLoss) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该印章已被挂失");
            }else if(a==ResultUtil.isNoProxy) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("缺少授权委托书");
            }else if(a==ResultUtil.faceCompare) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("该印章已被挂失");
            }else if(a==ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("交付成功");
            }else if(a==ResultUtil.isNoSealVerification){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("未核验通过，请重新核验");
            }else{
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("交付失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("出现未知错误");
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
            String entryType = sealDTO.getEntryType();
            String fieldPhotoId = sealDTO.getFieldPhotoId();
            int confidence = sealDTO.getConfidence();
            String idCardPhotoId =sealDTO.getAgentPhotoId();

            int a = sealService.loss(user,id,name,agentPhotoId,proxyId,certificateNo,certificateType,
                     localDistrictId, businesslicenseId, idCardFrontId, idCardReverseId,agentTelphone,entryType,idCardPhotoId,confidence,fieldPhotoId);
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
        try {
            User user = (User) httpServletRequest.getSession(true).getAttribute("user");
            String telphone = user.getTelphone();
            String localDistrictId = user.getDistrictId();
            Employee employee = employeeService.selectByPhone(telphone);
            String id = sealDTO.getId();
            String agentPhotoId = sealDTO.getAgentPhotoId();
            String proxyId = sealDTO.getProxyId();
            String certificateNo = sealDTO.getCertificateNo();
            String certificateType = sealDTO.getCertificateType();
            String idCardFrontId = sealDTO.getIdCardFrontId();
            String idCardReverseId = sealDTO.getIdCardReverseId();
            String agentTelphone = sealDTO.getTelphone();
            String entryType = sealDTO.getEntryType();
            String fieldPhotoId = sealDTO.getFieldPhotoId();
            int confidence = sealDTO.getConfidence();
            String idCardPhotoId =sealDTO.getAgentPhotoId();
            String name = sealDTO.getName();
            String businesslicenseId = sealDTO.getBusinessLicenseId();
            int a = sealService.logout(user,id,name,agentPhotoId,proxyId,certificateNo,certificateType,
                    localDistrictId, businesslicenseId, idCardFrontId, idCardReverseId,agentTelphone,entryType,idCardPhotoId,confidence,fieldPhotoId);
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



    @Log("核验")
    @RequestMapping(value = "/verifySeal",method = RequestMethod.POST)
    public JsonObjectBO verifySeal(HttpServletRequest httpServletRequest,@RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String id = (String) map.get("id");
        String verify_type_name = (String) map.get("status");
        String rejectReason = (String) map.get("reason");
        String rejectRemark = (String) map.get("remark");
        try{
            int a  = sealService.verifySeal(user,id,rejectReason,rejectRemark,verify_type_name);
            if(a==ResultUtil.isSuccess){
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("核验成功");
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("核验失败");
            }
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("核验失败");
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

    @Log("查找印章备案信息")
    @RequestMapping(value = "selectSealByDistrictId")
    public JsonObjectBO selectSealByDistrictId(@RequestBody Map map){
        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        String districtId = (String) map.get("districtId");
        try{
            List<Seal> seals = sealService.selectSealByDistrictId(districtId);
            jsonObject.put("seals", seals);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setData(jsonObject);
            return jsonObjectBO;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查看印章信息失败");
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
        JSONObject jsonObject = new JSONObject();
        TrustedIdentityAuthenticationVO result = new TrustedIdentityAuthenticationVO();
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
            result.setFieldPhotoId(fieldPhotoId);
            result.setCertificateNo(certificateNo);
            result.setName(name);
            result.setIsPass(identifyResult.isPassed());
            result.setMessage(identifyResult.getMessage());
            jsonObject.put("identifyResult",result);
            if(identifyResult.isPassed()){
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage(identifyResult.getMessage());
            }else {
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("验证失败");
            }
        }
        return jsonObjectBO;
    }

    /**
     * 制作单位印章退回
     */
    @RequestMapping(value = "/makeDepartmentUntread", method = RequestMethod.POST)
    public JsonObjectBO makeDepartmentUntread(HttpServletRequest httpServletRequest,@RequestBody SealVerificationPO sealVerificationPO){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            User user = (User) httpServletRequest.getSession(true).getAttribute("user");
            String sealId = sealVerificationPO.getSeal().getId();
            SealVerification sealVerification = sealVerificationPO.getSealVerification();
            int makeDepartmentUntread = sealService.makeDepartmentUntread(user,sealId, sealVerification);
            if (makeDepartmentUntread == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("退回成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("退回失败");
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("请求失败");
        }
        return jsonObjectBO;
    }


}
