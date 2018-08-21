package com.dhht.model;

import java.util.Date;

import com.dhht.annotation.EntityComment;


/**
 * 类注释
 * FaceCompareRecord
 * 数据库表：face_compare_record
 */
public class FaceCompareRecord {

    @EntityComment(value = "")
    private String id;

    @EntityComment(value = "经办人姓名")
    private String name;

    @EntityComment(value = "经办人身份证号码")
    private String certificateNo;

    @EntityComment(value = "身份证上的照片")
    private String certificatePhotoId;

    @EntityComment(value = "现场照片")
    private String filedPhotoId;

    @EntityComment(value = "裁剪过后的照片")
    private String facePhotoId;

    @EntityComment(value = "相似度")
    private int confidence;

    @EntityComment(value = "记录时间")
    private Date recordTime;

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

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo == null ? null : certificateNo.trim();
    }

    public String getCertificatePhotoId() {
        return certificatePhotoId;
    }

    public void setCertificatePhotoId(String certificatePhotoId) {
        this.certificatePhotoId = certificatePhotoId == null ? null : certificatePhotoId.trim();
    }

    public String getFiledPhotoId() {
        return filedPhotoId;
    }

    public void setFiledPhotoId(String filedPhotoId) {
        this.filedPhotoId = filedPhotoId == null ? null : filedPhotoId.trim();
    }

    public String getFacePhotoId() {
        return facePhotoId;
    }

    public void setFacePhotoId(String facePhotoId) {
        this.facePhotoId = facePhotoId == null ? null : facePhotoId.trim();
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}