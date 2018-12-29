package com.dhht.model.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommonHistoryVO {
    private Date updateTime;

    private String operator;

    private List<DataHistory> dataHistories;

    public CommonHistoryVO(Date updateTime, String operator, List<DataHistory> dataHistories) {
        this.updateTime = updateTime;
        this.operator = operator;
        this.dataHistories = dataHistories;
    }
    public CommonHistoryVO() {
    }


}
