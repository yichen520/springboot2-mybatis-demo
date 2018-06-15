package com.dhht.model;

import java.util.Date;

public class SysLog {
    private int id;
    private String LogType;
    private String LogResult;
    private String LogUser;
    private String LogTime;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

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


    @Override
    public String toString() {
        return "SysLog{" +
                "id=" + id +
                ", LogType='" + LogType + '\'' +
                ", LogResult='" + LogResult + '\'' +
                ", LogUser='" + LogUser + '\'' +
                ", LogTime='" + LogTime + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
