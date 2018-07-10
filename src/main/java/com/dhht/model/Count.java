package com.dhht.model;

import lombok.Data;

@Data
public class Count {
    private String countName;
    private int AllSum;
    private int WorkSum;
    private int DelSum;
    private int addCount;
    private int deleteCount;

    public Count(){

    }

    public Count(String countName, int allSum, int workSum, int delSum, int addCount, int deleteCount) {
        this.countName = countName;
        AllSum = allSum;
        WorkSum = workSum;
        DelSum = delSum;
        this.addCount = addCount;
        this.deleteCount = deleteCount;
    }
}
