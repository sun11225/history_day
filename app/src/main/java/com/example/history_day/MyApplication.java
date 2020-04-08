package com.example.history_day;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //第一：默认初始化
        Bmob.initialize(this, "eaca57c90f764cbe5607070fdfee2bfb");

        //xutil框架初始化
        x.Ext.init(this);

//        JVerificationInterface.setDebugMode(true);
//        final long start = System.currentTimeMillis();
//        JVerificationInterface.init(this, new RequestCallback<String>() {
//            @Override
//            public void onResult(int code, String result) {
//                Log.d("MyApp", "[init] code = " + code + " result = " + result + " consists = " + (System.currentTimeMillis() - start));
//            }
//        });
//    }

    //开启dubug日志
//        x.Ext.setDebug(BuildConfig.DEBUG);

}
}
