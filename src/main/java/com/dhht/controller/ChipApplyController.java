package com.dhht.controller;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.User;
import com.dhht.service.chipApply.ChipApplyService;
import com.dhht.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by imac_dhht on 2018/8/11.
 */

@RestController
@RequestMapping(value = "/chip")
public class ChipApplyController {

    @Autowired
    private ChipApplyService chipApplyService;



    private static Logger logger = LoggerFactory.getLogger(ChipApplyController.class);



    /**
     * 申请
     * @return
     */
    @Log("芯片申请")
    @RequestMapping(value = "/chipApply",method = RequestMethod.POST)
    public JsonObjectBO chipApply(HttpServletRequest httpServletRequest, @RequestBody Map map){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            Integer chipNum = (Integer) map.get("chipNum");
            String getTime = (String) map.get("getTime");
            String address = (String) map.get("address");
            String memo = (String) map.get("meomo");
            int a = chipApplyService.apply(chipNum, getTime, address, memo, user);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("申请失败");
            } else if (a == ResultUtil.isNoDepartment) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("制作单位不存在");
            } else if(a==ResultUtil.isException) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("出现异常");
            }else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("申请通过");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }

    }

    @Log("芯片发放")
    @RequestMapping(value = "/chipGrant",method = RequestMethod.POST)
    public JsonObjectBO chipGrant(HttpServletRequest httpServletRequest, @RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String chipApplyId = (String)map.get("chipApplyId");
            Integer grantNum = (Integer)map.get("grantNum");
            String grantTime = (String) map.get("grantTime");
            String chipCodeStart = (String)map.get("chipCodeStart");
            String chipCodeEnd = (String)map.get("chipCodeEnd");
            String receiver = (String)map.get("receiver");
            String grantWay = (String)map.get("grantWay");
            String granter = (String)map.get("granter");
            String memo =(String)map.get("memo");
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            int a = chipApplyService.grant(user ,chipApplyId,grantNum,grantTime,chipCodeStart,chipCodeEnd,receiver,grantWay,memo);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("发放失败");
            } else if(a==ResultUtil.isException) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("出现异常");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("发放通过");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }

    }
}
