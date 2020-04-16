package com.example.test;

public class Log {
    public String logId;
    public String logUsername;
    public String logDate;
    public String logAction;
    public Log() {
    }

    public Log(String logId, String logUsername, String logDate, String logAction) {
        this.logId = logId;
        this.logUsername = logUsername;
        this.logDate = logDate;
        this.logAction = logAction;

    }
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogUsername() {
        return logUsername;
    }

    public void setLogUsername(String logUsername) {
        this.logUsername = logUsername;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogAction() {
        return logAction;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }
}
