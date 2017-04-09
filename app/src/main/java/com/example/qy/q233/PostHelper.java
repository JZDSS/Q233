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
import java.util.HashMap;

/**
 * Created by Qi Yao on 17-4-8.
 */

public class PostHelper {

    HashMap<String, Integer> map;
    Handler mHandler;
    HttpPost httpPost;
    HttpClient httpClient;

    /**
     * Constructed function.
     * @param url Url to who deals with the post request.
     * @param mHandler Handler that handles the massage.
     * @param map Mapping from server massage to cases in Handler.
     */
    PostHelper(String url, Handler mHandler, HashMap<String, Integer> map){
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        this.mHandler = mHandler;
        this.map = map;
    }


    /**
     * Start a new thread and post the information.
     * @param pairs
     */
    void post (ArrayList<NameValuePair> pairs){
        new MyTread(pairs).start();
    }


    class MyTread extends Thread{

        ArrayList<NameValuePair> pairs;

        /**
         * Constructed function.
         * @param pairs Massage to post.
         */
        MyTread(ArrayList<NameValuePair> pairs){
            this.pairs = pairs;
        }

        /**
         *
         * @param mHandler Handler that handles the massage.
         * @param id The handler case id.
         */
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

                        sendMessage(mHandler, map.get(result));

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
