package com.dhht.controller.wechat;

import com.dhht.annotation.Log;
import com.dhht.common.JsonObjectBO;
import com.dhht.model.Suspicious;
import com.dhht.model.User;
import com.dhht.service.suspicious.SuspiciousService;
import com.dhht.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/weChat/suspicious")
public class SuspiciousWeChatController {

    @Autowired
    private SuspiciousService suspiciousService;

    /**
     * 可疑情况添加
     * @param
     * @return
     */
//    @Log("可疑情况添加")
//    @RequestMapping(value = "/add")
//    public JsonObjectBO add(@RequestBody Suspicious suspicious, HttpServletRequest httpServletRequest) {
//        User user = (User) httpServletRequest.getSession().getAttribute("user");
//        try {
//            return ResultUtil.getResult(suspiciousService.insertsuspicious(suspicious, user));
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return JsonObjectBO.exception("添加失败！");
//        }
//    }
}
