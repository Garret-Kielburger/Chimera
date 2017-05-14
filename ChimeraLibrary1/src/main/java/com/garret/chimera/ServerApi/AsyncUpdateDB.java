package com.garret.chimera.ServerApi;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Captain on 31/08/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class AsyncUpdateDB extends AsyncTask<Context, String, String>{
    private Context mContext;

    public AsyncUpdateDB(Context context ){
        this.mContext = context;
    }

    @Override
    protected String doInBackground(Context... params) {

        // should call fetchserverdata class on API Util class

        ApiUtilities.fetchServerData(mContext);
        return null;
    }
}
