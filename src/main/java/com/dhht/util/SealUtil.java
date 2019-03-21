package com.dhht.util;

import org.springframework.beans.factory.annotation.Autowired;

public class SealUtil {


    public static final String  SEAL_TYPE_CODE_01 = "01";//单位专用章
    public static final String  SEAL_TYPE_CODE_02 = "02";//财务专用章
    public static final String  SEAL_TYPE_CODE_03 = "03";//税务专用章
    public static final String  SEAL_TYPE_CODE_04 = "04";//合同专用章
    public static final String  SEAL_TYPE_CODE_05 = "05";//法人章
    public static final String  SEAL_TYPE_CODE_99 = "99";//其他章

    public static final String  SEAL_REASON_01 = "01";//新增印章
    public static final String  SEAL_REASON_02 = "02";//更换印章
    public static final String  SEAL_REASON_03 = "03";//遗失补课

    public static final String SEAL_STATUS_CODE_00 ="00";//未交付
    public static final String SEAL_STATUS_CODE_01 ="01";//已制作
    public static final String SEAL_STATUS_CODE_02 ="02";//已个人化
    public static final String SEAL_STATUS_CODE_03 ="03";//已备案
    public static final String SEAL_STATUS_CODE_04 ="04";//已交付
    public static final String SEAL_STATUS_CODE_05 ="05";//已挂失
    public static final String SEAL_STATUS_CODE_06 ="06";//已注销
    public static final String SEAL_STATUS_CODE_07 ="07";//待交付
    public static final String SEAL_STATUS_CODE_08 ="08";//承接
    public static final String SEAL_STATUS_CODE_09 ="09";//交付后的备案
    public static final String SEAL_STATUS_CODE_10 ="10";//已退回
    public static final String SEAL_STATUS_CODE_11 ="11";//资料更新

    public static final String SEAL_AGENT_BUSINESS_TYPE_0="00";//经办人申请
    public static final String SEAL_AGENT_BUSINESS_TYPE_1="01";//经办人领取
    public static final String SEAL_AGENT_BUSINESS_TYPE_2="02";//经办人挂失
    public static final String SEAL_AGENT_BUSINESS_TYPE_3="03";//经办人注销
    public static final String SEAL_AGENT_BUSINESS_TYPE_000="000";//小程序申请

    public static final String SEALMATERIAL_TYPE_1="01";//挂失营业执照
    public static final String SEALMATERIAL_TYPE_2="02";//印鉴留存卡
    public static final String SEALMATERIAL_TYPE_3="03";//印章图像数据
    public static final String SEALMATERIAL_TYPE_4="04";//印模图像
    public static final String SEALMATERIAL_TYPE_5="05";//印模二维数据
    public static final String SEALMATERIAL_TYPE_6="06";//印模图片缩略图
    public static final String SEALMATERIAL_TYPE_7="07";//注销营业执照


}
