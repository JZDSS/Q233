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
     * true for SD card, false for memory.
     */
    private boolean mode;

    private boolean append;

    /**
     * Initialize a FileManager.
     *
     * @param context Context.
     */
    FileManager(Context context) {
        mContext = context;
        mode = true;
        append = true;
    }

    /**
     * Set file name.
     *
     * @param fileName File name string.
     */
    void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Save data.
     *
     * @param content Data string;
     */
    public void save(String content) {
        try {
            if (mode) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    fileName = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + fileName;
                    FileOutputStream output = new FileOutputStream(fileName, append);
                    output.write(content.getBytes());
                    output.close();
                } else
                    Toast.makeText(mContext, "SD card isn't exist or can't be written!", Toast.LENGTH_SHORT).show();

            } else {
                //Memory
                mode = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read File
     *
     * @return File content.
     */
    public String read() {

        StringBuilder mStringBuilder = new StringBuilder("");
        try {
            if (mode) {
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
            } else {
                //Memory
                mode = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mStringBuilder.toString();
    }
}
