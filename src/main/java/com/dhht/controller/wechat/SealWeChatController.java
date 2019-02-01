package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;

import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.dao.MakeDepartmentSealPriceMapper;
import com.dhht.dao.SealDao;
import com.dhht.dao.UseDepartmentDao;
import com.dhht.model.*;
import com.dhht.model.pojo.*;
import com.dhht.service.make.MakeDepartmentSealPriceService;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealAgentWeChatService;
import com.dhht.service.seal.SealService;
import com.dhht.service.seal.WeChatSealService;
import com.dhht.service.tools.FileService;
import com.dhht.service.useDepartment.UseDepartmentService;
import com.dhht.service.user.UserLoginService;
import com.dhht.service.user.UserPasswordService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import dhht.idcard.trusted.identify.GuangRayIdentifier;
import dhht.idcard.trusted.identify.IdentifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐正平
 * @Date 2018/12/31 14:06
 */
@RestController
@RequestMapping("/weChat")
public class SealWeChatController extends WeChatBaseController {

    @Autowired
    private SealService sealService;
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentSealPriceMapper makeDepartmentSealPriceMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private WeChatSealService weChatSealService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserPasswordService userPasswordService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UseDepartmentService useDepartmentService;


    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(@RequestBody SealWeChatDTO sealDTO, HttpServletResponse httpServletResponse) {
        WeChatUser user = getcurrentUserByTelphone(sealDTO.getTelphone());

        init(httpServletRequest, httpServletResponse);
        try {
            JSONObject jsonObject = new JSONObject();
            String payOrderId = UUIDUtil.generate();
            int a = sealService.sealWeChatRecord(user, sealDTO, payOrderId);
            if (a == 1) {
                jsonObject.put("payOrderId", payOrderId);
                return JsonObjectBO.success("印章申请成功", jsonObject);
            } else {
                return ResultUtil.getResult(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "申请失败");
        }
    }

    @RequestMapping("/sealPrice")
    public JsonObjectBO sealPrice(@RequestBody Map map) {

        try {
            String sealType = (String) map.get("sealType");
            String makeDepartmentFlag = (String) map.get("makeDepartmentFlag");
            JSONObject jsonObject = new JSONObject();
            if (sealType == null) {
                List<MakeDepartmentSealPrice> makeDepartmentSealPrices = makeDepartmentSealPriceMapper.selectByMakeDepartmentFlag(makeDepartmentFlag);
                jsonObject.put("makeDepartmentSealPrices", makeDepartmentSealPrices);
            } else {
                MakeDepartmentSealPrice makeDepartmentSealPrice = sealService.sealPrice(map);
                jsonObject.put("makeDepartmentSealPrice", makeDepartmentSealPrice);
            }
            return JsonObjectBO.success("价格获取成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "价格获取失败");
        }
    }


    @RequestMapping("/sealProgress")
    public JsonObjectBO sealProgress(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        init(httpServletRequest, httpServletResponse);
        try {
            List<Seal> sealOperationRecords = sealService.sealProgress(map);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("seals", sealOperationRecords);
            return JsonObjectBO.success("印章进度查询成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "印章进度查询失败");
        }
    }

    @RequestMapping("/makeDepartmentSort")
    public JsonObjectBO makeDepartmentSort(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            List<Makedepartment> makedepartments = makeDepartmentService.makeDepartmentSort(map);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makedepartments", makedepartments);
            return JsonObjectBO.success("刻制企业排名查询成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "刻制企业排名查询失败");
        }
    }


    @RequestMapping("/cachetChange")
    public JsonObjectBO cachetChange(@RequestBody SealWeChatDTO sealDTO, HttpServletResponse httpServletResponse) {
        init(httpServletRequest, httpServletResponse);
        WeChatUser user = currentUser();

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.cachetChange(sealDTO, user);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("变更成功");
            } else if (a == ResultUtil.isNoDepartment) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("备案单位或制作单位不存在");
            } else if (a == ResultUtil.isNoEmployee) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("从业人员不存在");
            } else if (a == ResultUtil.isNoProxy) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("缺少授权委托书");
            } else if (a == ResultUtil.isCodeError) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("验证码错误,请重新输入");
            } else if (a == ResultUtil.isNoSeal) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("印章刻制原因选择错误,该企业还没有公章");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("变更失败");
        }
    }

    /**
     * 可信身份认证
     *
     * @param map
     * @return
     */

    @RequestMapping(value = "/TrustedIdentityAuthentication", method = RequestMethod.POST)
    public JsonObjectBO TrustedIdentityAuthentication(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        init(httpServletRequest, httpServletResponse);
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        TrustedIdentityAuthenticationVO result = new TrustedIdentityAuthenticationVO();
        String certificateNo = (String) map.get("certificateNo");
        String name = (String) map.get("name");
        String fieldPhotoId = (String) map.get("fieldPhotoId");
        FileInfoVO fieldFileInfo = fileService.readFile(fieldPhotoId);
        byte[] fileDate = fieldFileInfo.getFileData();
        float fileDatetoKb = fileDate.length / 1024;
        if (fileDatetoKb > 25 || fileDatetoKb < 10) {
            jsonObjectBO.setCode(-1);
            jsonObjectBO.setMessage("重新上传，图片大小请小于25kb大于10kb");
        } else {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String photoDate = base64Encoder.encode(fileDate);
            IdentifyResult identifyResult = GuangRayIdentifier.identify(certificateNo, name, photoDate);
            result.setFieldPhotoId(fieldPhotoId);
            result.setCertificateNo(certificateNo);
            result.setName(name);
            result.setIsPass(identifyResult.isPassed());
            result.setMessage(identifyResult.getMessage());
            jsonObject.put("identifyResult", result);
            if (identifyResult.isPassed()) {
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage(identifyResult.getMessage());
            } else {
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("验证失败");
            }
        }
        return jsonObjectBO;
    }

    @RequestMapping(value = "/sealList", method = RequestMethod.POST)
    public JsonObjectBO sealList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            String telphone = (String) httpServletRequest.getSession().getAttribute("mobilePhone");
            JSONObject jsonObject = new JSONObject();
            List<Seal> seals = sealService.sealListForWeChat(telphone);
            jsonObject.put("seals", seals);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询失败");
        }
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    public JsonObjectBO singleFileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse httpServletResponse) {
        init(httpServletRequest, httpServletResponse);
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            byte[] fileBuff = null;
            InputStream inputStream = file.getInputStream();
            if (inputStream != null) {
                int len1 = inputStream.available();
                fileBuff = new byte[len1];
                inputStream.read(fileBuff);
            }
            inputStream.close();

            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            User user = new User();
            user.setId(UUIDUtil.generate());
            user.setRealName("微信小程序可信身份");
            FileInfo fileInfo = fileService.save(fileBuff, fileName, ext, "", FileService.CREATE_TYPE_UPLOAD, user.getId(), user.getRealName());

            if (fileInfo != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("file", fileInfo);
                return JsonObjectBO.success("文件上传成功", jsonObject);
            } else {
                return JsonObjectBO.error("文件上传失败");
            }
        } catch (Exception e) {
            return JsonObjectBO.exception("上传文件失败");
        }
    }


    @RequestMapping(value = "/getCheckCode")
    public JsonObjectBO getCheckCode(HttpServletRequest request, @RequestBody Map map, HttpServletResponse httpServletResponse) {
        String telphone = (String) map.get("telphone");
        init(httpServletRequest, httpServletResponse);
        try {
            if (userPasswordService.getCheckCode(telphone) == ResultUtil.isSuccess) {
                return JsonObjectBO.success("获取验证码成功", null);
            } else {
                return JsonObjectBO.error("获取验证码失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("获取验证码失败");
        }
    }

    @RequestMapping(value = "/sealInfo", method = RequestMethod.POST)
    public JsonObjectBO sealInfo(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            JSONObject jsonObject = new JSONObject();
            String id = (String) map.get("sealId");
            SealVO seal = sealService.selectDetailById(id);
            jsonObject.put("seal", seal);
            return JsonObjectBO.success("查询成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询失败");
        }
    }


    @RequestMapping(value = "checkPhone", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SMSCode smsCode, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            return userLoginService.checkAPPPhoneAndIDCard(smsCode);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("发送短信发生异常");
        }
    }

    @RequestMapping(value = "updatePay", method = RequestMethod.POST)
    public JsonObjectBO checkPhone(@RequestBody SealPayOrder sealPayOrder, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            return ResultUtil.getResult(sealService.updatePay(sealPayOrder));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("支付更改不成功");
        }
    }


    /**
     * 印章核验
     *
     * @param map
     * @return
     */
    @RequestMapping("/weChatCheckSealCode")
    public Map<String, Object> weChatCheckSealCode(@RequestBody Map map) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String sealCode = (String) map.get("sealCode");
            String useDepartmentCode = (String) map.get("useDepartmentCode");
            resultMap = sealService.weChatcheckSealCode(sealCode, useDepartmentCode, "01");
            return resultMap;
        } catch (Exception e) {
            resultMap.put("status", "error");
            resultMap.put("message", "查询失败");
            return resultMap;
        }
    }

    @RequestMapping(value = "/download", produces = "application/json;charset=UTF-8")
    public ResponseEntity<byte[]> download(@RequestParam("id") String id) {
        FileInfoVO fileInfoVO = fileService.readFile(id);

        try {
            //请求头
            HttpHeaders headers = new HttpHeaders();

            //解决文件名乱码
//            String fileName = new String((fileInfoVO.getFileName()).getBytes("UTF-8"),"iso-8859-1");

            //通知浏览器以attachment（下载方式）打开
//            headers.setContentDispositionFormData("attachment", fileInfoVO.getFileName());

            //application/octet-stream二进制流数据（最常见的文件下载）。
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(fileInfoVO.getFileData(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 法人章和公章
     *
     * @param
     * @return
     */
    @RequestMapping("/isHaveSeal")
    public JsonObjectBO isHaveSeal(@RequestBody Seal seal) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String useDepartmentCode = seal.getUseDepartmentCode();
            int result = sealService.isHaveSeal(useDepartmentCode, seal);
            if (result == ResultUtil.isSuccess) {
                jsonObjectBO.setMessage("可以添加");
                jsonObjectBO.setCode(1);
            } else {
                jsonObjectBO.setMessage("不可以添加");
                jsonObjectBO.setCode(-1);
            }
        } catch (Exception e) {
            jsonObjectBO.setMessage("不可以添加");
            jsonObjectBO.setCode(-1);
        }
        return jsonObjectBO;
    }

    /**
     * 根据session中的用户查询订单
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public JsonObjectBO order(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        try {
            String type = (String) map.get("type");
            init(httpServletRequest, httpServletResponse);
            JSONObject jsonObject = new JSONObject();
            WeChatUser weChatUser = currentUser();
            List<SealOrder> sealorders = sealService.selectOrder(type, weChatUser.getTelphone());
            jsonObject.put("sealorders", sealorders);
            return JsonObjectBO.success("查询订单成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询订单失败");
        }
    }

    /**
     * 订单详细
     */
    @RequestMapping(value = "/orderDetail", method = RequestMethod.POST)
    public JsonObjectBO orderDetail(@RequestBody Map map, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            String SealId = (String) map.get("id");
            JSONObject jsonObject = new JSONObject();
            SealOrder sealOrder = sealService.selectOrderDetail(SealId);
            jsonObject.put("sealOrder", sealOrder);
            return JsonObjectBO.success("查询订单详细成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "查询订单详细失败");
        }
    }

    /**
     * 获取cookie
     *
     * @return
     */
    @RequestMapping(value = "/getcookie")
    public WeChatUser getcookie() {
        WeChatUser weChatUser = currentUser();
        return weChatUser;
    }

    @RequestMapping(value = "/bindCompany", method = RequestMethod.POST)
    public JsonObjectBO bindCompany(@RequestBody UseDepartment useDepartment, HttpServletResponse httpServletResponse) {
        try {
            init(httpServletRequest, httpServletResponse);
            UseDepartment useDepartment1 = weChatUserService.bindCompany(useDepartment);
            JSONObject jsonObject = new JSONObject();
            if (useDepartment1.getName() != null) {
                WeChatUser weChatUser = currentUser();
                weChatUser.setCompany(useDepartment1.getId());
                if (weChatUserService.updateWeChatUserInfo(weChatUser) < 1) {
                    return JsonObjectBO.error("绑定用章单位失败");
                }
            }
            jsonObject.put("useDepartment", useDepartment1);
            return JsonObjectBO.success("绑定成功", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(), "绑定用章单位失败，请补全正确的信息");
        }
    }

    /**
     * 印章退回
     *
     * @param httpServletRequest
     * @param sealVerificationPO
     * @return
     */
    @RequestMapping(value = "/verifySeal", method = RequestMethod.POST)
    public JsonObjectBO verifySeal(HttpServletRequest httpServletRequest, @RequestBody SealVerificationPO sealVerificationPO) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        WeChatUser weChatUser = currentUser();
        String id = sealVerificationPO.getSeal().getId();
        String verify_type_name = sealVerificationPO.getSealVerification().getVerifyTypeName();
        String rejectReason = sealVerificationPO.getSealVerification().getRejectReason();
        String rejectRemark = sealVerificationPO.getSealVerification().getRejectRemark();
        try {
            User user = new User();
            user.setRealName(weChatUser.getName());
            user.setTelphone(weChatUser.getTelphone());
            int a = sealService.verifySeal(user, id, rejectReason, rejectRemark, verify_type_name);
            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("核验成功");
            } else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("核验失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            return JsonObjectBO.exception("核验失败");
        }

    }

    @RequestMapping("/getCompany")
    public JsonObjectBO getCompany( HttpServletResponse httpServletResponse){
        try {
            init(httpServletRequest,httpServletResponse);
            WeChatUser weChatUser = currentUser();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company",  useDepartmentService.selectDetailById(weChatUser.getCompany()));
            return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e){
            e.printStackTrace();

            return JsonObjectBO.exception("评价失败");
        }
    }


    /**
     * 资料更新列表
     */
    @RequestMapping(value = "/verificationList" , method = RequestMethod.GET)
    public JsonObjectBO verificationList(HttpServletRequest httpServletRequest){

//        WeChatUser weChatUser = (WeChatUser)httpServletRequest.getSession().getAttribute("weChatUser");
        String telphone = currentUserMobilePhone();

        JSONObject jsonObject = new JSONObject();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try{
            List<SealVerificationPO> list = weChatSealService.sealAndVerification(telphone);
            jsonObject.put("sealVerification",list);
            jsonObjectBO.setCode(1);
            jsonObjectBO.setMessage("获取成功");
            jsonObjectBO.setData(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return JsonObjectBO.exception("获取列表失败");
        }
        return jsonObjectBO;

    }

//        WeChatUser weChatUser = (WeChatUser)httpServletRequest.getSession().getAttribute("weChatUser");



//    @RequestMapping(value = "/unbindCompany", method = RequestMethod.POST)
//    public JsonObjectBO unbindCompany(HttpServletResponse httpServletResponse) {
//        try{
//            init(httpServletRequest,httpServletResponse);
//            UseDepartment useDepartment1 =  weChatUserService.unbindCompany();
//            JSONObject jsonObject = new JSONObject();
//            if(useDepartment1.getName()!=null){
//                WeChatUser weChatUser =currentUser();
//                weChatUser.setCompany(useDepartment1.getId());
//                if(weChatUserService.updateWeChatUserInfo(weChatUser)<1){
//                    return JsonObjectBO.error("绑定用章单位失败");
//                }
//            }
//            jsonObject.put("useDepartment",useDepartment1);
//            return JsonObjectBO.success("解绑成功",jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"绑定用章单位失败，请补全正确的信息");
//        }
//    }

    /**
     * 企业账号注册
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/companyAccoutRegister", method = RequestMethod.POST)
public JsonObjectBO companyRegister(@RequestBody UseDepartmentRegister useDepartmentRegister,HttpServletResponse httpServletResponse) {
    try{
        init(httpServletRequest,httpServletResponse);
        int result =  weChatUserService.companyRegister(useDepartmentRegister);
        return ResultUtil.getResult(result);
    } catch (Exception e) {
        e.printStackTrace();
        return JsonObjectBO.exceptionWithMessage(e.getMessage(),"企业账户注册失败，请填写正确的信息");
    }
}

    /**
     * 根据id找资料信息
     */
    @RequestMapping(value = "/selectVerificationById" ,method = RequestMethod.GET)
    public JsonObjectBO selectVerificationById(@RequestParam String id){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try{
            SealVerificationPO sealVerificationPO =  weChatSealService.selectVerificationById(id);
            jsonObject.put("sealVerification",sealVerificationPO);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setMessage("查询成功");
            jsonObjectBO.setCode(1);

        }catch(Exception e){
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"获取失败");
        }
        return jsonObjectBO;
    }


    /**
     * 资料更新
     */
    @RequestMapping(value = "/dateUpdate" ,method = RequestMethod.POST)
    public JsonObjectBO dateUpdate(@RequestBody weChatSealVerificationPO weChatSealVerificationPO){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try{
            WeChatUser weChatUser = currentUser();
            String id = weChatSealVerificationPO.getId();
            SealAgent sealAgent = weChatSealVerificationPO.getSealAgent();
            int result =  sealService.dateUpdate(weChatUser,id,sealAgent);
            if(result==ResultUtil.isFail){
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setMessage("修改失败");
                jsonObjectBO.setCode(-1);
            }else{
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setMessage("修改成功");
                jsonObjectBO.setCode(1);
            }


        }catch(Exception e){
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"修改失败");
        }
        return jsonObjectBO;
    }
}
