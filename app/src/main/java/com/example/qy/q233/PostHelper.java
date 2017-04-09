package com.example.qy.q233;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Qi Yao on 17-4-8.
 */

public class PostHelper {
    Handler mHandler;
    HttpPost httpPost;
    HttpClient httpClient;

    PostHelper(String url, Handler mHandler){
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        this.mHandler = mHandler;
    }


    void post (ArrayList<NameValuePair> pairs){
        new MyTread(pairs).start();
    }


    class MyTread extends Thread{
        ArrayList<NameValuePair> pairs;
        MyTread(ArrayList<NameValuePair> pairs){
            this.pairs = pairs;
        }

        private void sendMessage(Handler mHandler, int id) {
            if (mHandler != null) {
                Message message = Message.obtain(mHandler, id);
                mHandler.sendMessage(message);
            }
        }
        @Override
        public void run(){
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                try {
                    HttpResponse response = httpClient.execute(httpPost);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        if (result.equals("p")){
                            sendMessage(mHandler, 3);
                        }
                        else {
                            sendMessage(mHandler, 4);
                        }
                        Log.d("HTTP", "POST:" + result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
