package com.example.qy.q233;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Qi Yao on 17-3-17.
 */

public class FileManager {

    private Context mContext;

    FileManager (Context context){
        mContext = context;
    }

    public void Save(String fileName, String cache, String mode){
        try{
            if (mode.equals(R.string.SD_card)){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + fileName;

                    FileOutputStream output = new FileOutputStream(fileName);
                    output.write(cache.getBytes());
                    output.close();
                } else Toast.makeText(mContext, "SD card isn't exist or can't be written!", Toast.LENGTH_SHORT).show();

            }else{
                //Memory
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public String Read(String fileName, String mode){

        StringBuilder mStringBuilder = new StringBuilder("");
        try {
            if (mode.equals(R.string.SD_card)){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + fileName;
                    FileInputStream input = new FileInputStream(fileName);
                    byte[] temp = new byte[1024];
                    int len;

                    while ((len = input.read(temp)) > 0) {
                        mStringBuilder.append(new String(temp, 0, len));
                    }

                    input.close();
                }
            }else{
                //Memory
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return mStringBuilder.toString();
    }

}
