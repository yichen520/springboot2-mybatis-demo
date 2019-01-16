package com.dhht.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.controller.web.SuspiciousController;
import com.dhht.model.Suspicious;
import com.dhht.model.User;
import com.dhht.model.WeChatUser;
import com.dhht.model.pojo.SuspiciousPO;
import com.dhht.service.suspicious.SuspiciousService;
import com.dhht.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/weChat/suspicious")
public class SuspiciousWeChatController {

    @Autowired
    private SuspiciousService suspiciousService;

    private static Logger logger = LoggerFactory.getLogger(SuspiciousWeChatController.class);

    /**
     * 可疑情况添加
     *
     * @param
     * @return
     */
    @Log("可疑情况添加")
    @RequestMapping(value = "/add")
    public JsonObjectBO add(@RequestBody Suspicious suspicious, HttpServletRequest httpServletRequest) {
        WeChatUser user = (WeChatUser) httpServletRequest.getSession().getAttribute("user");
        try {
            return ResultUtil.getResult(suspiciousService.weChatInsertSuspicious(suspicious, user));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("添加失败！");
        }
    }

    /**
     * 可疑情况列表
     *
     * @param
     * @return
     */
    @Log("可疑情况添加")
    @RequestMapping(value = "/suspiciouslist")
    public JsonObjectBO suspiciouslist(HttpServletRequest httpServletRequest) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        WeChatUser user = (WeChatUser) httpServletRequest.getSession().getAttribute("user");
        String loginTelphone = user.getTelphone();
        try {
            List<Suspicious> suspicious = suspiciousService.selectAll(loginTelphone);
            jsonObject.put("suspicious", suspicious);
            jsonObjectBO.setData(jsonObject);
            jsonObjectBO.setCode(1);
            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查询失败！");
        }
    }

    /**
     * 可疑情况详情
     *
     * @param
     * @return
     */
    @Log("可疑情况详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonObjectBO info(String id ) {
        JsonObjectBO jsonObjectBO = new JsonObjectBO();
        JSONObject jsonObject = new JSONObject();
        try {
            Suspicious suspicious = suspiciousService.selectById(id);
            if (suspicious==null){
                jsonObject.put("suspicious", suspicious);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(-1);
                jsonObjectBO.setMessage("暂无数据");
            }else{
                jsonObject.put("suspicious", suspicious);
                jsonObjectBO.setData(jsonObject);
                jsonObjectBO.setCode(1);
                jsonObjectBO.setMessage("查询成功");
            }

            return jsonObjectBO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonObjectBO.exception("查询失败！");
        }
    }


}
