package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;

import java.util.ArrayList;

/**
 * Created by cuiyang on 2018/7/3.
 */
public interface UserPasswordService {

    void  sendPhoneMessage(String phone,ArrayList<String> params);

    JsonObjectBO sendMessage(String phone, String code);
}
