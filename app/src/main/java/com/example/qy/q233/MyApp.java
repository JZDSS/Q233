package com.example.qy.q233;

import android.app.Application;

/**
 * Created by Qi Yao on 17-4-11.
 */

public class MyApp extends Application {
    String url = "";

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
