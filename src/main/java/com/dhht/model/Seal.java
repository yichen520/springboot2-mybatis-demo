package com.dhht.model;

import lombok.Data;

import java.util.Date;

@Data
public class Seal extends SealKey {


    private String sealName;   //印章名称

    private String sealStatusCode;   //印章状态代码

    private String useDepartmentCode;  //印章使用单位编码

    private String useDepartmentName;  //印章使用单位名字

    private String recordDepartmentCode;    //印章备案单位编码

    private String recordDepartmentName;    //印章备案单位名字

    private String makeDepartmentCode;  //印章备案单位编码

    private String makeDepartmentName;  //印章备案单位名字

    private String sealTypeCode;    //印章类型编码

    private String materialsCode;   //印章章面材料代码

    private String mimeographDescription;   //印油说明

    private String sealShapeCode;   //印章图案编码

    private String sealSize;    //章面尺寸

    private String sealCenterImage; //中心图案

    private String sealSpecification;   //规格说明

    private String sealMakeTypeCode;    //印章刻制类别代码

    private String sealRecordTypeCode;  //印章刻制类别代码

    private String remark;//备注

    private Date recordDate;    //备案日期

    private Boolean isRecord;   //是否申请

    private Date makeDate;  //制作日期

    private Boolean isMake; //是否制作

    private Date personalDate;   //个人化日期

    private Boolean isPersonal; //是否个人化

    private Date deliverDate;   //交付日期

    private Boolean isDeliver;  //是否交付

    private Date logoutDate;    //注销日期

    private Boolean isLogout;   //是否注销

    private Date lossDate;  //挂失日期

    private Boolean isLoss; //是否挂失

    private String sealReason; //印章原因

    private String districtId;  //区域id

    private String agentId;   //申请

    private String getterId;    // 领取

    private String lossPersonId;    //挂失

    private String logoutPersonId;    //注销

    private boolean isChipseal;  //是否芯片章

    private boolean isPass;  //是否通过

    private String rejectReason;// 退回理由

    private String rejectRemark;//  退回备注

    private String verifyTypeName;

    private Boolean isEarlywarning;  //是否预警

    private Boolean isApply; //是否备案

    private Date applyDate; //是否备案

    private Date earlywarningDate;  //预警日期

    private String missingpostRemark;  //遗失补刻备注

    private Boolean isUndertake; //是否承接

    private Date undertakeDate;  //承接日期

    private int applySource;

    private Boolean isCencal;  //是否取消

    private Date cencalDate; //取消日期


     private DeliveryExpressInfo deliveryExpressInfo;

    public Boolean getIsCencal() {
        return isCencal;
    }

    public void setIsCencal(Boolean isCencal) {
        this.isCencal = isCencal;
    }

    public Date getCencalDate() {
        return cencalDate;
    }

    public void setCencalDate(Date cencalDate) {
        this.cencalDate = cencalDate;
    }

    public Boolean getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(Boolean isRecord) {
        this.isRecord = isRecord;
    }

    public Boolean getIsMake() {
        return isMake;
    }

    public void setIsMake(Boolean isMake) {
        this.isMake = isMake;
    }

    public Boolean getIsPersonal() {
        return isPersonal;
    }

    public void setIsPersonal(Boolean isPersonal) {
        this.isPersonal = isPersonal;
    }

    public Boolean getIsDeliver() {
        return isDeliver;
    }

    public void setIsDeliver(Boolean isDeliver) {
        this.isDeliver = isDeliver;
    }

    public Boolean getIsLogout() {
        return isLogout;
    }

    public void setIsLogout(Boolean isLogout) {
        this.isLogout = isLogout;
    }

    public Boolean getLoss() {
        return isLoss;
    }

    public void setLoss(Boolean loss) {
        isLoss = loss;
    }

    public void setIsChipseal(boolean isChipseal) {
        this.isChipseal= isChipseal;
    }

    public boolean getIsChipseal() {
        return isChipseal;
    }

    public void setIsPass(boolean isPass) {
        this.isPass= isPass;
    }

    public boolean getIsPass() {
        return isPass;
    }


    public Boolean getIsEarlywarning() {
        return isEarlywarning;
    }

    public void setIsEarlywarning(Boolean isEarlywarning) {
        this.isEarlywarning = isEarlywarning;
    }

    public Boolean getIsApply() {
        return isApply;
    }

    public void setIsApply(Boolean isApply) {
        this.isApply = isApply;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getEarlywarningDate() {
        return earlywarningDate;
    }

    public void setEarlywarningDate(Date earlywarningDate) {
        this.earlywarningDate = earlywarningDate;
    }

    public String getMissingpostRemark() {
        return missingpostRemark;
    }

    public void setMissingpostRemark(String missingpostRemark) {
        this.missingpostRemark = missingpostRemark == null ? null : missingpostRemark.trim();
    }

    public Boolean getIsUndertake() {
        return isUndertake;
    }

    public void setIsUndertake(Boolean isUndertake) {
        this.isUndertake = isUndertake;
    }

    public Date getUndertakeDate() {
        return undertakeDate;
    }

    public void setUndertakeDate(Date undertakeDate) {
        this.undertakeDate = undertakeDate;
    }
}