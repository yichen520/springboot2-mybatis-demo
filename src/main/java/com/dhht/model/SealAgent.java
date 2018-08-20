package com.dhht.model;


import com.dhht.annotation.EntityComment;

/**
 * 类注释
 * SealAgent
 * 数据库表：seal_agent
 */
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
    private String idcardFrontId;

    @EntityComment(value = "身份证反面")
    private String idcardReverseId;

    @EntityComment(value = "授权委托书")
    private String proxyId;

    @EntityComment(value = "类型  0-申请  1-领取 2")
    private String businessType;

    @EntityComment(value = "人证合一记录id")
    private String faceCompareRecordId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType == null ? null : certificateType.trim();
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo == null ? null : certificateNo.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    public String getAgentPhotoId() {
        return agentPhotoId;
    }

    public void setAgentPhotoId(String agentPhotoId) {
        this.agentPhotoId = agentPhotoId == null ? null : agentPhotoId.trim();
    }

    public String getIdcardFrontId() {
        return idcardFrontId;
    }

    public void setIdcardFrontId(String idcardFrontId) {
        this.idcardFrontId = idcardFrontId == null ? null : idcardFrontId.trim();
    }

    public String getIdcardReverseId() {
        return idcardReverseId;
    }

    public void setIdcardReverseId(String idcardReverseId) {
        this.idcardReverseId = idcardReverseId == null ? null : idcardReverseId.trim();
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId == null ? null : proxyId.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getFaceCompareRecordId() {
        return faceCompareRecordId;
    }

    public void setFaceCompareRecordId(String faceCompareRecordId) {
        this.faceCompareRecordId = faceCompareRecordId == null ? null : faceCompareRecordId.trim();
    }
}