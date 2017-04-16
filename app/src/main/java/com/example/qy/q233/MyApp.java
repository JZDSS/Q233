package com.example.qy.q233;

import android.app.Application;

/**
 * Created by Qi Yao on 17-4-11.
 */

public class MyApp extends Application {
    int apiVersion;
    String url = "";

    public void setApiVersion(int version) {
        apiVersion = version;
    }

    public int getApiVersion() {
        return apiVersion;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
