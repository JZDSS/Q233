package com.example.qy.q233;

/**
 * Created by Xu Yining on 2017/4/22.
 */

public class Counter {
//    public float norm_h;
//    public float norm_l;
//    public float norm_r;
//    public boolean isPeak_h = false;
//    public boolean isPeak_l = false;
//    public static int couter = -2;

    public float norm_h = 9.8f;
    public float norm_l = 9.8f;
    public boolean isPeak_h = false;
    public boolean isPeak_l = false;
    public static int couter = 0;
    public float new_norm = 9.8f;
    public float old_norm = 9.8f;
    public float norm_r = 9.8f;
    public boolean isFresh = false;
    public float norm_t = 9.8f;

    public void RefreshNorm(float Acceler_norm){
        float norm = Acceler_norm;
        if ((norm != old_norm) && (norm != norm_r)) {
            norm_r = old_norm;
            old_norm = new_norm;
            new_norm = Acceler_norm;
            isFresh = true;
        }else {
            new_norm = Acceler_norm;
            isFresh = false;
        }
    }

    public void DetectorPeak(float new_n, float old_n){
        if (isFresh) {
            if (old_n > new_n && old_n > norm_r) {
                isPeak_h = true;
                norm_h = old_n;
            } else if (old_n < new_n && old_n < norm_r) {
                isPeak_l = true;
                norm_l = old_n;
            } else {
                isPeak_h = false;
                isPeak_l = false;
                norm_r = old_n;
            }
        }
    }

    public void Counter(){
        //norm_t=
        DetectorPeak(new_norm, old_norm);
        if (isPeak_h || isPeak_l){
            if (norm_h - norm_l >= 4){
                couter++;
            }
        }
    }

//    public void DetectorPeak(float new_norm, float old_norm){
//        if(old_norm > new_norm && old_norm > norm_r){
//            isPeak_h = true;
//            norm_h = old_norm;
//        }else {
//            isPeak_h = false;
//            norm_r = new_norm;
//        }
//        if (old_norm < new_norm && old_norm < norm_r){
//            isPeak_l = true;
//            norm_l = old_norm;
//        }else {
//            isPeak_l = false;
//            norm_r = new_norm;
//        }
//    }
//    public void Counter(){
//        if (isPeak_h || isPeak_l){
//            if (norm_h - norm_l >= 3){
//                couter++;
//            }
//        }
//    }
}
