package com.thephiloswap.thephiloswapbeta;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "1E1F1846-2227-DC19-FFEB-E0954203DF00";
    public static final String API_KEY = "B4C42F06-F43D-4CC0-8FDB-D359417FFD98";
    public static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();


        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
        Backendless.setUrl(SERVER_URL);

    }
}
