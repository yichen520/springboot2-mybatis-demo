package com.dhht.model;

public class RecordPolice {

    private String id;

    private String policeCode;

    private String policeName;

    private String telphone;

    private String officeCode;

    private String officeDistrict;

    private String officeName;

    private Boolean isDelete;

    public RecordPolice() {
    }

    /*public RecordPolice(String policeCode, String policeName, String telphone, String officeCode, String officeDistrict, String officeName) {
        this.policeCode = policeCode;
        this.policeName = policeName;
        this.telphone = telphone;
        this.officeCode = officeCode;
        this.officeDistrict = officeDistrict;
        this.officeName = officeName;
    }

    public RecordPolice(String id, String policeCode, String policeName, String telphone, String officeCode, String officeDistrict, String officeName) {
        this.id = id;
        this.policeCode = policeCode;
        this.policeName = policeName;
        this.telphone = telphone;
        this.officeCode = officeCode;
        this.officeDistrict = officeDistrict;
        this.officeName = officeName;
    }
*/
    public String getPoliceName() {
        return policeName;
    }

    public void setPoliceName(String policeName) {
        this.policeName = policeName == null ? null : policeName.trim();
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }


    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode == null ? null : officeCode.trim();
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName == null ? null : officeName.trim();
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    public String getOfficeDistrict() {
        return officeDistrict;
    }

    public void setOfficeDistrict(String officeDistrict) {
        this.officeDistrict = officeDistrict;
    }

    @Override
    public String toString() {
        return "RecordPolice{" +
                "id='" + id + '\'' +
                ", policeCode='" + policeCode + '\'' +
                ", policeName='" + policeName + '\'' +
                ", telphone='" + telphone + '\'' +
                ", officeCode='" + officeCode + '\'' +
                ", officeDistrict='" + officeDistrict + '\'' +
                ", officeName='" + officeName + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}