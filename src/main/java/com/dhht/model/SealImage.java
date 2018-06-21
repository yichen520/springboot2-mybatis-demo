package com.dhht.model;

public class SealImage {
    private String id;

    private String sealId;

    private Double sealImageWidth;

    private Double sealImageHeight;

    private Byte sealImageCompressStatus;

    private String url;

    private byte[] sealImageData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId == null ? null : sealId.trim();
    }

    public Double getSealImageWidth() {
        return sealImageWidth;
    }

    public void setSealImageWidth(Double sealImageWidth) {
        this.sealImageWidth = sealImageWidth;
    }

    public Double getSealImageHeight() {
        return sealImageHeight;
    }

    public void setSealImageHeight(Double sealImageHeight) {
        this.sealImageHeight = sealImageHeight;
    }

    public Byte getSealImageCompressStatus() {
        return sealImageCompressStatus;
    }

    public void setSealImageCompressStatus(Byte sealImageCompressStatus) {
        this.sealImageCompressStatus = sealImageCompressStatus;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public byte[] getSealImageData() {
        return sealImageData;
    }

    public void setSealImageData(byte[] sealImageData) {
        this.sealImageData = sealImageData;
    }
}