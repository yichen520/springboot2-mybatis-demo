package com.dhht.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.ChipApply;
import com.dhht.model.ChipGrant;
import com.dhht.model.User;
import com.dhht.service.chipApply.ChipApplyService;
import com.dhht.util.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
            String addressDetail = (String) map.get("addressDetail");
            String memo = (String) map.get("meomo");
            int a = chipApplyService.apply(chipNum, getTime, address, addressDetail, memo, user);
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

    @Log("芯片申请信息查询")
    @RequestMapping(value = "/getchipApply",method = RequestMethod.POST)
    public JsonObjectBO getchipApplyInfo(HttpServletRequest httpServletRequest, @RequestBody Map map){

        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject= new JSONObject();

        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        int pageSize = (Integer) map.get("pageSize");
        int pageNum = (Integer) map.get("pageNum");
        try {
            PageHelper.startPage(pageNum, pageSize);
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            List<ChipApply> a = chipApplyService.getchipApplyInfo(startTime, endTime, user);

            if (a == null) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("无芯片申请记录");
            } else {
                PageInfo<ChipApply> pageInfo = new PageInfo<ChipApply>(a);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("申请通过");
                jsonObject.put("applyInfo",pageInfo);
                jsonObjectBO.setData(jsonObject);
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
            String receiverTel = (String)map.get("receiverTel");
            String grantWay = (String)map.get("grantWay");
            String memo =(String)map.get("memo");
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            int a = chipApplyService.grant(user ,chipApplyId,grantNum,grantTime,chipCodeStart,chipCodeEnd,receiver,grantWay,memo,receiverTel);
            if (a == ResultUtil.isFail) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("发放失败");
            } else if(a==ResultUtil.isException) {
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("出现异常");
            } else {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("发放芯片成功");
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }

    }

    @Log("查询申请芯片的制作单位")
    @RequestMapping(value = "/getApplyMakeDepartment",method = RequestMethod.POST)
    public JsonObjectBO  getmakeDepartment(HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            List<ChipApply> chipApplyList = chipApplyService.getmakeDepartment(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makeDepartment",chipApplyList);
           return JsonObjectBO.success("查询成功",jsonObject);
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception(e.toString());
        }
    }

    @Log("查询发放芯片的制作单位")
    @RequestMapping(value = "/getGrantMakeDepartment",method = RequestMethod.POST)
    public JsonObjectBO  getGrantMakeDepartment(HttpServletRequest httpServletRequest){
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            List<ChipApply> chipApplyList = chipApplyService.getGrantMakeDepartment(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("makeDepartment",chipApplyList);
            return JsonObjectBO.success("查询发放芯片的制作单位成功",jsonObject);
        }catch (Exception e) {
            e.printStackTrace();
            return JsonObjectBO.exception(e.toString());
        }
    }

    @Log("芯片申请信息")
    @RequestMapping(value = "/getchipApplyInfo",method = RequestMethod.POST)
    public JsonObjectBO getchipGrant(HttpServletRequest httpServletRequest, @RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String makeDepartment = (String)map.get("makeDepartmentName");

             int  grantFlag = (Integer)map.get("grantFlag");
            String startTime = (String) map.get("startTime");
            String endTime = (String) map.get("endTime");
            int pageSize = (Integer) map.get("pageSize");
            int pageNum = (Integer) map.get("pageNum");
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            PageHelper.startPage(pageNum, pageSize);
            List<ChipApply> a = chipApplyService.getchipGrant(user ,makeDepartment,grantFlag,startTime,endTime);
            if (a == null) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("无芯片申请记录");
            } else {
                PageInfo<ChipApply> pageInfo = new PageInfo<ChipApply>(a);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询到芯片申请记录");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("applyInfo",pageInfo);
                jsonObjectBO.setData(jsonObject);
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }


    @Log("芯片发放信息")
    @RequestMapping(value = "/getchipGrantInfo",method = RequestMethod.POST)
    public JsonObjectBO getchipGrantInfo(HttpServletRequest httpServletRequest, @RequestBody Map map){
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        try {
            String makeDepartment = (String)map.get("makeDepartmentName");

            String startTime = (String) map.get("startTime");
            String endTime = (String) map.get("endTime");
            String receiver = (String) map.get("receiver");
            int pageSize = (Integer) map.get("pageSize");
            int pageNum = (Integer) map.get("pageNum");

            PageHelper.startPage(pageNum, pageSize);
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            List<ChipGrant> a = chipApplyService.getchipGrantInfo(user ,makeDepartment,startTime,endTime,receiver);
            if (a == null) {
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("无芯片申请记录");
            } else {
                PageInfo<ChipGrant> pageInfo = new PageInfo<ChipGrant>(a);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询到芯片申请记录");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("applyInfo",pageInfo);
                jsonObjectBO.setData(jsonObject);
            }
            return jsonObjectBO;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception(e.toString());
        }
    }
}
