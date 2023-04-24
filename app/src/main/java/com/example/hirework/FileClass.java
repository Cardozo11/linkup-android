package com.example.hirework;

public class FileClass {
public String name,url;
public int fin;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int orderId;

    public FileClass() {
    }

    public FileClass(String name, String url,int orderId) {
        this.name = name;
        this.url = url;
        this.orderId=orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
