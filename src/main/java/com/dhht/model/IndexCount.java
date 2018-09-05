package com.dhht.model;

import lombok.Data;

@Data
public class IndexCount {
    private String comment;
    private int value;

    public IndexCount(){

    }

    public IndexCount(String comment, int value) {
        this.comment = comment;
        this.value = value;
    }
}
