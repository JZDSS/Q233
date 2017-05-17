package com.example.qy.q233;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Qi Yao on 17-3-17.
 */

public class FileManager {

    private Context context;

    public FileManager(Context context) {
        super();
        this.context = context;
        File file = new File("/storage/0000-0000/com.example.qy.q233");
        if (!file.exists())
        {
            try {
                file.mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    //往SD卡写入文件的方法
    public void save(String filename, String filecontent, boolean append) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename =  "/storage/0000-0000/com.example.qy.q233/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename,append);
            output.write(filecontent.getBytes());
            //将String字符串以字节流的形式写入到输出流中
            output.close();
            //关闭输出流
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

    //读取SD卡中文件的方法
    //定义读取文件的方法:
    String read(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = "/storage/0000-0000/com.example.qy.q233/" + filename;
            //打开文件输入流
            FileInputStream input = new FileInputStream(filename);
            byte[] temp = new byte[1024];

            int len;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
        }
        return sb.toString();
    }


}