package com.dhht.controller.wechat;

import com.dhht.service.suspicious.SuspiciousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weChat/suspicious")
public class SuspiciousWeChatController {

    @Autowired
    private SuspiciousService suspiciousService;


}
