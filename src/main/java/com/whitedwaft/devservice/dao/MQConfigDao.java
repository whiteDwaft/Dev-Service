package com.whitedwaft.devservice.dao;


import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("MQ_Config")
public class MQConfigDao implements Serializable {

    private String id;
    private String host;
    private String port;
    private String channel;
    private String qmgr;
    private String app_user;
    private String app_password;
    private String queue_name;
    private String message;
    private String time;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getQmgr() {
        return qmgr;
    }

    public void setQmgr(String qmgr) {
        this.qmgr = qmgr;
    }

    public String getApp_user() {
        return app_user;
    }

    public void setApp_user(String app_user) {
        this.app_user = app_user;
    }

    public String getApp_password() {
        return app_password;
    }

    public void setApp_password(String app_password) {
        this.app_password = app_password;
    }

    public String getQueue_name() {
        return queue_name;
    }

    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
