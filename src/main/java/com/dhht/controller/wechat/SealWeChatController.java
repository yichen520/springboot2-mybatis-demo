package com.dhht.controller.wechat;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Seal;
import com.dhht.model.User;
import com.dhht.model.pojo.SealDTO;
import com.dhht.service.seal.SealService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 徐正平
 * @Date 2018/12/31 14:06
 */
@RestController
@RequestMapping("/weChat")
public class SealWeChatController {

    @Autowired
    private SealService sealService;
    @Log("印章备案")
    @RequestMapping("/sealRecord")
    public JsonObjectBO sealRecord(HttpServletRequest httpServletRequest, @RequestBody SealDTO sealDTO) {
        User user = (User) httpServletRequest.getSession(true).getAttribute("user");
        String districtId = user.getDistrictId();
        String agentTelphone = sealDTO.getTelphone();
        String  captcha =sealDTO.getCaptcha();
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
            }else {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("添加失败");
            }
            return jsonObjectBO;
        } catch (Exception e) {
            return JsonObjectBO.exception("备案失败");
        }
    }


}
