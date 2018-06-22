package com.dhht.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class SMSCode implements Serializable{
	private static final long serialVersionUID = 1L;

    private String id;
    
    //手机号 唯一
    private String phone;
    
    //短信验证码  6位数字
    private String smscode;
    
    //最后保存的时间
    private Long lastTime;

    
}
