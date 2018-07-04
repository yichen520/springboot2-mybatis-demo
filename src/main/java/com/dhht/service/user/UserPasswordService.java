package com.dhht.service.user;

import com.dhht.common.JsonObjectBO;

import java.util.ArrayList;

/**
 * Created by cuiyang on 2018/7/3.
 */
public interface UserPasswordService {

    JsonObjectBO sendMessage(String phone, String code);
}
