package com.xuewen.xuewen;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

}
