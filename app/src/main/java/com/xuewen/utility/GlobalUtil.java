package com.xuewen.utility;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

/**
 * Created by ym on 16-11-2.
 */

public class GlobalUtil {

    private static GlobalUtil mInstance = null;
    public static GlobalUtil getInstance(){
        if(mInstance == null)
        {
            mInstance = new GlobalUtil();
        }
        return mInstance;
    }

    public DisplayImageOptions circleBitmapOptions;
    public String baseUrl = "http://123.207.233.226:5000/";
//    public String baseUrl = "http://192.168.1.106:5000/";
//    public String baseUrl = "http://192.168.1.100:5000/";
//    public String baseUrl = "http://172.22.192.65:5000/";

    public String avatarUrl = baseUrl+"static/avatar/";
    public String audioUrl = baseUrl+"static/audio/";

    private GlobalUtil() {
        circleBitmapOptions = new DisplayImageOptions.Builder()
                .displayer(new CircleBitmapDisplayer())  // rounded corner bitmap
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

}
