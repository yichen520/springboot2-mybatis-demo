package com.dhht.model;

import lombok.Data;

@Data
public class SealCount {
    private String countName;
    private String sealType;
    private int newSealNum;
    private int lossSealNum;
    private int logoutSealNum;
//    private int sealNum;

public SealCount(String countName, String sealType, int newSealNum, int lossSealNum, int logoutSealNum) {
    this.countName = countName;
    this.sealType = sealType;
    this.newSealNum = newSealNum;
    this.lossSealNum =lossSealNum;
    this.logoutSealNum = logoutSealNum;
}


    public SealCount() {
    }

}
