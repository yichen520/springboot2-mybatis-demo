package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;

import java.util.ArrayList;

/**
 * Created by cuiyang on 2018/7/3.
 */
public interface UserPasswordService {

    int sendMessage(String phone, String code,int smsmesscode);

    boolean adminResetPwd(String id);

    int getCheckCode(String username);

    boolean resetPwd(String username,String checkCode,String password);

    boolean appResetPwd(String id,String passWord);

    int changePwd(String username,String oldPassword,String newPassword);

    int getCheckAgentCode(String telphone);
}
