package com.dhht.model;

import lombok.Data;

@Data
public class MakedepartmentCount {
    private String countName;
    private int departmentAllSum;
    private int departmentWorkSum;
    private int departmentDelSum;
    private int addCount;
    private int deleteCount;

    public MakedepartmentCount(){

    }

    public MakedepartmentCount(String countName, int departmentAllSum, int departmentWorkSum, int departmentDelSum, int addCount, int deleteCount) {
        this.countName = countName;
        this.departmentAllSum = departmentAllSum;
        this.departmentWorkSum = departmentWorkSum;
        this.departmentDelSum = departmentDelSum;
        this.addCount = addCount;
        this.deleteCount = deleteCount;
    }
}
