package com.dhht.model;

import java.util.Date;

public class Log {
    private int id;
    private String LogName;
    private String LogResult;
    private String LogUser;
    private Date LogDate;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogName() {
        return LogName;
    }

    public void setLogName(String logName) {
        LogName = logName;
    }

    public String getLogResult() {
        return LogResult;
    }

    public void setLogResult(String logResult) {
        LogResult = logResult;
    }

    public String getLogUser() {
        return LogUser;
    }

    public void setLogUser(String logUser) {
        LogUser = logUser;
    }

    public Date getLogDate() {
        return LogDate;
    }

    public void setLogDate(Date logDate) {
        LogDate = logDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
