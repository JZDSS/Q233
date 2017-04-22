package com.example.qy.q233.lib;

/**
 * Created by Xu Yining on 2017/4/22.
 */

public class Counter {
    public float norm_h;
    public float norm_l;
    public float norm_r;
    public boolean isPeak_h = false;
    public boolean isPeak_l = false;
    public static int couter = -2;

    public void DetectorPeak(float new_norm, float old_norm){
        if(old_norm > new_norm && old_norm > norm_r){
            isPeak_h = true;
            norm_h = old_norm;
        }else {
            isPeak_h = false;
            norm_r = new_norm;
        }
        if (old_norm < new_norm && old_norm < norm_r){
            isPeak_l = true;
            norm_l = old_norm;
        }else {
            isPeak_l = false;
            norm_r = new_norm;
        }
    }
    public void Counter(){
        if (isPeak_h || isPeak_l){
            if (norm_h - norm_l >= 3){
                couter++;
            }
        }
    }
}
