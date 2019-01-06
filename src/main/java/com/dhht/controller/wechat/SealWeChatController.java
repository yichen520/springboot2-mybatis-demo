package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.CurrentUser;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.dao.MakeDepartmentSealPriceMapper;
import com.dhht.model.*;
import com.dhht.model.pojo.FileInfoVO;
import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealWeChatDTO;
import com.dhht.model.pojo.TrustedIdentityAuthenticationVO;
import com.dhht.service.make.MakeDepartmentService;
import com.dhht.service.seal.SealService;
import com.dhht.service.tools.FileService;
import com.dhht.util.ResultUtil;
import com.dhht.util.UUIDUtil;
import dhht.idcard.trusted.identify.GuangRayIdentifier;
import dhht.idcard.trusted.identify.IdentifyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author 徐正平
 * @Date 2018/12/31 14:06
 */
@RestController
@RequestMapping("/weChat")
public class SealWeChatController extends BaseController {

    @Autowired
    private SealService sealService;
    @Autowired
    private MakeDepartmentService makeDepartmentService;
    @Autowired
    private MakeDepartmentSealPriceMapper makeDepartmentSealPriceMapper;
    @Autowired
    private FileService fileService;

    @Log("小程序印章申请")
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(@RequestBody SealWeChatDTO sealDTO) {
        User user = currentUser();
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.sealWeChatRecord(user,sealDTO);

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
            } else if(a==ResultUtil.isCodeError){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("验证码错误,请重新输入");
            }else if(a==ResultUtil.isNoSeal){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("印章刻制原因选择错误,该企业还没有公章");
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception("备案失败");
        }
    }
    @Log("获取印章价格")
    @RequestMapping("/sealPrice")
    public JsonObjectBO sealPrice(@RequestBody Map map){
        User user = currentUser();
        try {
            String sealType=(String)map.get("sealType");
            String makeDepartmentFlag=(String)map.get("makeDepartmentFlag");
            JSONObject jsonObject =new JSONObject();
            if (sealType ==null){
                List<MakeDepartmentSealPrice> makeDepartmentSealPrices =makeDepartmentSealPriceMapper.selectByMakeDepartmentFlag(makeDepartmentFlag);
                jsonObject.put("makeDepartmentSealPrices",makeDepartmentSealPrices);
            }else {
                MakeDepartmentSealPrice makeDepartmentSealPrice = sealService.sealPrice(user,map);
                jsonObject.put("makeDepartmentSealPrice",makeDepartmentSealPrice);
            }
            return JsonObjectBO.success("价格获取成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"价格获取失败");
        }
    }
    @Log("印章进度查询")
    @RequestMapping("/sealProgress")
    public JsonObjectBO sealProgress(@RequestBody Map map) {
        User user = currentUser();
        try {
            List<Seal> sealOperationRecords = sealService.sealProgress(user,map);
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("seals",sealOperationRecords);
            return JsonObjectBO.success("印章进度查询成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"印章进度查询失败");
        }
    }
    @Log("刻制企业排名")
    @RequestMapping("/makeDepartmentSort")
    public JsonObjectBO makeDepartmentSort(@RequestBody Map map) {
        try {
            List<Makedepartment> makedepartments = makeDepartmentService.makeDepartmentSort(map);
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("makedepartments",makedepartments);
            return JsonObjectBO.success("刻制企业排名查询成功",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exceptionWithMessage(e.getMessage(),"刻制企业排名查询失败");
        }
    }


    @Log("小程序印章变更")
    @RequestMapping("/cachetChange")
    public JsonObjectBO cachetChange(@RequestBody SealWeChatDTO sealDTO) {
        User user = currentUser();

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            int a = sealService.cachetChange(sealDTO,user);

            if (a == ResultUtil.isSuccess) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("变更成功");
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
            }else if(a==ResultUtil.isNoSeal){
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("印章刻制原因选择错误,该企业还没有公章");
            }else {
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

    @RequestMapping(value = "/sealList", method = RequestMethod.POST)
    public JsonObjectBO sealList(@RequestBody Map map) {
        try{
        JSONObject jsonObject = new JSONObject();
        String useDepartmentCode = (String)map.get("useDepartmentCode");
        List<Seal> seals = sealService.sealListForWeChat(useDepartmentCode);
        jsonObject.put("seals",seals);
        return JsonObjectBO.success("查询成功",jsonObject);
    } catch (Exception e) {
        e.printStackTrace();
        return JsonObjectBO.exceptionWithMessage(e.getMessage(),"查询失败");
    }
    }

    @RequestMapping(value="/upload",produces="application/json;charset=UTF-8")
    public JsonObjectBO singleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return JsonObjectBO.error("请选择上传文件");
        }
        try {
            byte[] fileBuff = null;
            InputStream inputStream = file.getInputStream();
            if(inputStream != null){
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

            if(fileInfo != null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("file",fileInfo);
                return JsonObjectBO.success("文件上传成功",jsonObject);
            }else {
                return JsonObjectBO.error("文件上传失败");
            }
        } catch (Exception e) {
            return JsonObjectBO.exception("上传文件失败");
        }
    }



}
