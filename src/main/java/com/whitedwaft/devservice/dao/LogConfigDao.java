package com.whitedwaft.devservice.dao;

public class LogConfigDao {

    public enum Levels {ERROR,WARN,INFO,DEBUG}
    public enum Formats {JSON,XML,TEXT}

    public String pattern;
    public String size;
    public Levels level;
    public Formats format;

    public Levels fromStringToLevel(String level)
    {
        switch (level)
        {
            case "INFO":
                return Levels.INFO;
            case "ERROR":
                return Levels.ERROR;
            case "WARN":
                return Levels.WARN;
            case "DEBUG":
                return Levels.DEBUG;
        }
        return null;
    }
    public Formats fromStringToFormat(String format)
    {
        switch (format)
        {
            case "JSON":
                return Formats.JSON;
            case "XML":
                return Formats.XML;
            case "TEXT":
                return Formats.TEXT;
        }
        return null;
    }




}
