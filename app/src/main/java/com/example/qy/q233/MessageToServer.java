package com.example.qy.q233;

import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Xu Yining on 2017/4/7.
 */

public class MessageToServer {

    MyThread mThread;
    long time = System.currentTimeMillis();
    float value = (float) Math.random();
    HttpClient httpClient = new DefaultHttpClient();
    String url = "http://192.168.1.104/q233/index.php";
    HttpPost httpPost = new HttpPost(url);

    MessageToServer(){
        mThread = new MyThread(0);
        mThread.start();
    }


    public void Post(final float norm) {
        mThread.setval(norm);
    }

    class MyThread extends Thread{
        float norm;
        boolean on;
        MyThread(float norm){
            this.norm = norm;
            this.on = false;
        }
        void setval(float norm){
            this.norm = norm;
        }
        void open(){
            this.on=true;
        }

        void close(){
            this.on = false;
        }
        @Override
        public void run() {
            super.run();
            while(true){
                if(on){
                    //NameValuePair对象代表了一个需要发往服务器的键值对
                    NameValuePair pair= new BasicNameValuePair("norm", String.format("%f", norm));
        //                NameValuePair pair1 = new BasicNameValuePair("address", "sichuan"+String.format("%d",(int)(Math.random()*100)));
        //                NameValuePair pair2 = new BasicNameValuePair("latitude", String.format("%f",Math.random()));
        //                NameValuePair pair3 = new BasicNameValuePair("longitude", String.format("%f",Math.random()));
                    //将准备好的键值对对象放置在一个List当中
                    ArrayList<NameValuePair> pairs = new ArrayList<>();
                    pairs.add(pair);
        //                pairs.add(pair1);
        //                pairs.add(pair2);
        //                pairs.add(pair3);
                    try {
                        //创建代表请求体的对象（注意，是请求体）
                        HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
                        //将请求体放置在请求对象当中
                        httpPost.setEntity(requestEntity);
                        //执行请求对象
                        try {
                            //第三步：执行请求对象，获取服务器发还的相应对象
                            HttpResponse response = httpClient.execute(httpPost);
                            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                            if (response.getStatusLine().getStatusCode() == 200) {
                                //第五步：从相应对象当中取出数据，放到entity当中
                                HttpEntity entity = response.getEntity();
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(entity.getContent()));
                                String result = reader.readLine();
                                Log.d("HTTP", "POST:" + result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
