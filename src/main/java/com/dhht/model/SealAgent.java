package com.dhht.model;


import com.dhht.annotation.EntityComment;
import lombok.Data;

/**
 * 类注释
 * SealAgent
 * 数据库表：seal_agent
 */
@Data
public class SealAgent {

    @EntityComment(value = "")
    private String id;

    @EntityComment(value = "经办人姓名")
    private String name;

    @EntityComment(value = "证件类型")
    private String certificateType;

    @EntityComment(value = "证件号码")
    private String certificateNo;

    @EntityComment(value = "联系电话")
    private String telphone;

    @EntityComment(value = "经办人照片")
    private String agentPhotoId;

    @EntityComment(value = "身份证正面")
    private String idCardFrontId;

    @EntityComment(value = "身份证反面")
    private String idCardReverseId;

    @EntityComment(value = "授权委托书")
    private String proxyId;

    @EntityComment(value = "类型  0-申请  1-领取 2")
    private String businessType;

    @EntityComment(value = "人证合一记录id")
    private String faceCompareRecordId;

}