package com.thephiloswap.thephiloswapbeta;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "1E1F1846-2227-DC19-FFEB-E0954203DF00";
    public static final String API_KEY = "B4C42F06-F43D-4CC0-8FDB-D359417FFD98";
    public static final String SERVER_URL = "https://api.backendless.com";

    //static varibale to represent the user once logged in

    public static BackendlessUser user;

    //list to hold a list of all available books to be swapped

    public static List<Book> books;

    @Override
    public void onCreate() {
        super.onCreate();


        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
        Backendless.setUrl(SERVER_URL);

    }
}
