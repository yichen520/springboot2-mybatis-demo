package com.dhht.model;

import lombok.Data;

@Data
public class IndexOverview {

    private int Sum;
    private int Add;
    private int Del;

    public IndexOverview(){

    }

    public IndexOverview(int sum, int add, int del) {
        Sum = sum;
        Add = add;
        Del = del;
    }
}
