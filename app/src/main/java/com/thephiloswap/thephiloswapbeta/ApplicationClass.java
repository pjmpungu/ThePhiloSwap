package com.thephiloswap.thephiloswapbeta;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "1E1F1846-2227-DC19-FFEB-E0954203DF00";
    public static final String API_KEY = "B4C42F06-F43D-4CC0-8FDB-D359417FFD98";
    public static final String SERVER_URL = "https://api.backendless.com";

    //static varibale to represent the user once logged in

    public static BackendlessUser user;

    //list to hold a list of all available books to be swapped

    public static List<Book> books;
    public static List<Book> userBooks = new ArrayList<Book>();
    public static List<Book> swapBooks = new ArrayList<Book>();

    //the next two methods are string methods that will return the email body
    //for sending an email to the owner of a book

    public static String generateEmail(Book book, String address, String phone, String name){

        String email = "Hello " + name + ",\n"
                +"Philo Swap user " + ApplicationClass.user.getProperty("name")
                + " has requested your copy of " + book.getTitle() + "."
                + " Below is their address.\n\n" + address + "\n\n";

                if(!phone.isEmpty())
                    email += "If you'd like to keep in touch, their phone number is " + phone + ".\n";

                email += "Thanks for using The Philo Swap!";

        return email;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
        Backendless.setUrl(SERVER_URL);

    }
}
