package com.xuewen.utility;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

    private GlobalUtil() {
        circleBitmapOptions = new DisplayImageOptions.Builder()
                .displayer(new CircleBitmapDisplayer())  // rounded corner bitmap
                .build();
    }

}
