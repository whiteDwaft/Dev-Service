package com.whitedwaft.devservice.dao;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "LogPatternDao")
@XmlRootElement
public class LogPatternDao {

    private LogConfigDao.Levels level;
    private String message;
    private String datetime;
    private String src;
    public LogPatternDao(LogConfigDao.Levels level, String message, String datetime, String src)
    {
        this.src = src;
        this.datetime = datetime;
        this.level = level;
        this.message = message;
    }
    public LogPatternDao(){}

    public LogConfigDao.Levels getLevel() {
        return level;
    }

    public void setLevel(LogConfigDao.Levels level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
