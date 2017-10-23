package com.nadoo.tacademy.eunbyul_nanu;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Tacademy on 2017-06-09.
 */

public class NaDooGlideModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        //현재 앱에 전체메모리 사이즈
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        /*
          현재 Glide이 관리하는 캐쉬사이즈에 10%를 증가한다.
         */
        int customMemoryCacheSize = (int) (1.1 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.1 * defaultBitmapPoolSize);
        //편집이 많은 경우는 1.2로한다.

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //더 선명하게 보여줄 팀은 DecodeFormat.PREFER_ARGB_8888로 설정(메모리소모많음)
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);


    }
    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}