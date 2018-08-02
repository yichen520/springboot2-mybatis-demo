package com.dhht.model;

import lombok.Data;

@Data
public class ExamineCount {
    private String district;

    private  int countNum;

    public ExamineCount() {
    }

    public ExamineCount(String district, int countNum) {
        this.district = district;
        this.countNum = countNum;
    }
}
