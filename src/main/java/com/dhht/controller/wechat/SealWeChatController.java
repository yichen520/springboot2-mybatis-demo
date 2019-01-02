package com.dhht.controller.wechat;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.BaseController;
import com.dhht.model.*;
import com.dhht.model.pojo.SealDTO;
import com.dhht.model.pojo.SealWeChatDTO;
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
public class SealWeChatController extends BaseController {

    @Autowired
    private SealService sealService;
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


}
