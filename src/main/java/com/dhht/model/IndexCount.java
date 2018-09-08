package com.dhht.model;

import lombok.Data;

@Data
public class IndexCount {
    private String comment;
    private int value;
    private double percent;
    private String temp;

    public IndexCount(){

    }

    public IndexCount(String comment, int value) {
        this.comment = comment;
        this.value = value;
    }

    public IndexCount(String comment, int value, double percent) {
        this.comment = comment;
        this.value = value;
        this.percent = percent;
    }
}
