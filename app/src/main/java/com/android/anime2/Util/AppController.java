package com.android.anime2.Util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    private static final String TAG = AppController.class.getSimpleName();
    private static AppController instance;
    RequestQueue rQ;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static synchronized AppController getInstance(){
        return instance;
    }

    private RequestQueue getRQ(){
        if(rQ == null){
            rQ = Volley.newRequestQueue(getApplicationContext());
        }
        return rQ;
    }

    public <T> void addToRequestQueue (Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRQ().add(req);
    }
    public <T> void addToRequestQueue (Request<T> req)
    {
        req.setTag(TAG);
        getRQ().add(req);
    }
    public void cancelAllRequest(Object req){
        if(rQ == null){
            rQ.cancelAll(req);
        }
    }
}

