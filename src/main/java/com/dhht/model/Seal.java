package com.dhht.model;

public class Seal extends SealKey {
    private String sealName;

    private String sealStatusCode;

    private String useDepartmentCode;

    private String useDepartmentName;

    private String recordDepartmentCode;

    private String recordDepartmentName;

    private String makeDepartmentCode;

    private String makeDepartmentName;

    private String sealTypeCode;

    private String materialsCode;

    private String mimeographDescription;

    private String sealShapeCode;

    private Double sealSize;

    private String sealCenterImage;

    private String sealSpecification;

    private String sealMakeTypeCode;

    private String sealRecordTypeCode;

    public String getSealName() {
        return sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName == null ? null : sealName.trim();
    }

    public String getSealStatusCode() {
        return sealStatusCode;
    }

    public void setSealStatusCode(String sealStatusCode) {
        this.sealStatusCode = sealStatusCode == null ? null : sealStatusCode.trim();
    }

    public String getUseDepartmentCode() {
        return useDepartmentCode;
    }

    public void setUseDepartmentCode(String useDepartmentCode) {
        this.useDepartmentCode = useDepartmentCode == null ? null : useDepartmentCode.trim();
    }

    public String getUseDepartmentName() {
        return useDepartmentName;
    }

    public void setUseDepartmentName(String useDepartmentName) {
        this.useDepartmentName = useDepartmentName == null ? null : useDepartmentName.trim();
    }

    public String getRecordDepartmentCode() {
        return recordDepartmentCode;
    }

    public void setRecordDepartmentCode(String recordDepartmentCode) {
        this.recordDepartmentCode = recordDepartmentCode == null ? null : recordDepartmentCode.trim();
    }

    public String getRecordDepartmentName() {
        return recordDepartmentName;
    }

    public void setRecordDepartmentName(String recordDepartmentName) {
        this.recordDepartmentName = recordDepartmentName == null ? null : recordDepartmentName.trim();
    }

    public String getMakeDepartmentCode() {
        return makeDepartmentCode;
    }

    public void setMakeDepartmentCode(String makeDepartmentCode) {
        this.makeDepartmentCode = makeDepartmentCode == null ? null : makeDepartmentCode.trim();
    }

    public String getMakeDepartmentName() {
        return makeDepartmentName;
    }

    public void setMakeDepartmentName(String makeDepartmentName) {
        this.makeDepartmentName = makeDepartmentName == null ? null : makeDepartmentName.trim();
    }


    public String getSealTypeCode() {
        return sealTypeCode;
    }

    public void setSealTypeCode(String sealTypeCode) {
        this.sealTypeCode = sealTypeCode;
    }

    public String getMaterialsCode() {
        return materialsCode;
    }

    public void setMaterialsCode(String materialsCode) {
        this.materialsCode = materialsCode == null ? null : materialsCode.trim();
    }

    public String getMimeographDescription() {
        return mimeographDescription;
    }

    public void setMimeographDescription(String mimeographDescription) {
        this.mimeographDescription = mimeographDescription == null ? null : mimeographDescription.trim();
    }

    public String getSealShapeCode() {
        return sealShapeCode;
    }

    public void setSealShapeCode(String sealShapeCode) {
        this.sealShapeCode = sealShapeCode == null ? null : sealShapeCode.trim();
    }

    public Double getSealSize() {
        return sealSize;
    }

    public void setSealSize(Double sealSize) {
        this.sealSize = sealSize;
    }

    public String getSealCenterImage() {
        return sealCenterImage;
    }

    public void setSealCenterImage(String sealCenterImage) {
        this.sealCenterImage = sealCenterImage == null ? null : sealCenterImage.trim();
    }

    public String getSealSpecification() {
        return sealSpecification;
    }

    public void setSealSpecification(String sealSpecification) {
        this.sealSpecification = sealSpecification == null ? null : sealSpecification.trim();
    }

    public String getSealMakeTypeCode() {
        return sealMakeTypeCode;
    }

    public void setSealMakeTypeCode(String sealMakeTypeCode) {
        this.sealMakeTypeCode = sealMakeTypeCode == null ? null : sealMakeTypeCode.trim();
    }

    public String getSealRecordTypeCode() {
        return sealRecordTypeCode;
    }

    public void setSealRecordTypeCode(String sealRecordTypeCode) {
        this.sealRecordTypeCode = sealRecordTypeCode == null ? null : sealRecordTypeCode.trim();
    }
}