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

    private String fileName;

    /**
     * Initialize a FileManager.
     * @param context Context.
     */
    FileManager (Context context){
        mContext = context;
    }

    /**
     * Set file name.
     * @param fileName File name string.
     */
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    /**
     * Save data.
     * @param content Data string;
     * @param mode "SD_card": save to SD card, else: save to memory;
     * @param append True: write in the end of the file,false: clear/create the file and write.
     */
    public void save(String content, int mode, boolean append){
        try{
            if (mode==R.string.SD_card){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + fileName;
                    FileOutputStream output = new FileOutputStream(fileName, append);
                    output.write(content.getBytes());
                    output.close();
                } else Toast.makeText(mContext, "SD card isn't exist or can't be written!", Toast.LENGTH_SHORT).show();

            }else{
                //Memory
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Read File
     * @param mode SD card or memory.
     * @return File content.
     */
    public String read(String mode){

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
