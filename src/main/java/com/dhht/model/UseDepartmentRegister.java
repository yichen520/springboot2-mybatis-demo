package com.dhht.model;

import lombok.Data;

/**
 * @author 徐正平
 * @Date 2019/1/29 11:10
 */
@Data
public class UseDepartmentRegister {
    private String legalName;
    private String legalId;
    private String code;
    private String name;
    private String mobilePhone;
    private String captcha;
}
