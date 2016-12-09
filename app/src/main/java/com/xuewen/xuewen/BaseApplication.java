package com.xuewen.xuewen;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ListenHelper;

import java.io.File;

/**
 * Created by huangyuming on 16-12-9.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();

        // configuration of image loader
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)

                .diskCacheExtraOptions(480, 800, null)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .threadPoolSize(3)

                .defaultDisplayImageOptions(displayOptions)
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(imageLoaderConfiguration);
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

        //科大讯飞整体初始化
        ListenHelper.Init(context);

        CurrentUser.userId = 7;

    }
}
