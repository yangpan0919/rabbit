//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.study.rabbit.routing;

import java.util.List;

public class MessageData {
    private String deviceCode;
    private int type;
    private String key;
    private List<String> data;

    public MessageData() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String toString() {
        return "MessageData{deviceCode='" + this.deviceCode + '\'' + ", type=" + this.type + ", key='" + this.key + '\'' + ", data=" + this.data + '}';
    }
}
