package com.dhht.model;

import java.util.Date;

import org.mybatis.generator.annotation.Entity;


/**
 * 类注释
 * FaceCompareRecord
 * 数据库表：face_compare_record
 */
public class FaceCompareRecord {

    @Entity(value = "")
    private String id;

    @Entity(value = "经办人姓名")
    private String name;

    @Entity(value = "经办人身份证号码")
    private String certificateNo;

    @Entity(value = "身份证上的照片")
    private String certificatePhotoId;

    @Entity(value = "现场照片")
    private String filedPhotoId;

    @Entity(value = "裁剪过后的照片")
    private String facePhotoId;

    @Entity(value = "相似度")
    private Float confidence;

    @Entity(value = "记录时间")
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

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}