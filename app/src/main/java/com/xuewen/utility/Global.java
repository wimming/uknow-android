package com.xuewen.utility;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

/**
 * Created by ym on 16-11-2.
 */

public class Global {

    private static Global mInstance = null;
    public static Global getInstance(){
        if(mInstance == null)
        {
            mInstance = new Global();
        }
        return mInstance;
    }

    public DisplayImageOptions circleBitmapOptions;
    public String baseUrl = "http://123.207.233.226:5000/";
//    public String baseUrl = "https://172.19.50.181:5050/";
//    public String baseUrl = "http://192.168.1.106:5000/";

    public String baseAvatarUrl = baseUrl+"static/avatar/";
    public String baseAudioUrl = baseUrl+"static/audio/";

    private Global() {
        circleBitmapOptions = new DisplayImageOptions.Builder()
                .displayer(new CircleBitmapDisplayer())  // rounded corner bitmap
                .cacheOnDisk(true)
//                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

}
