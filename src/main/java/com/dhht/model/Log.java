package com.dhht.model;

import java.util.Date;

public class Log {
    private int id;
    private String LogType;
    private String LogResult;
    private String LogUser;
    private String LogTime;
    private String LogContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogType() {
        return LogType;
    }

    public void setLogType(String logType) {
        LogType = logType;
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

    public String getLogTime() {
        return LogTime;
    }

    public void setLogTime(String logTime) {
        LogTime = logTime;
    }

    public String getLogContent() {
        return LogContent;
    }

    public void setLogContent(String logContent) {
        LogContent = logContent;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", LogType='" + LogType + '\'' +
                ", LogResult='" + LogResult + '\'' +
                ", LogUser='" + LogUser + '\'' +
                ", LogTime='" + LogTime + '\'' +
                ", LogContent='" + LogContent + '\'' +
                '}';
    }
}
