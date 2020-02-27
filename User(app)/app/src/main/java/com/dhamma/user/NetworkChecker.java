package com.dhamma.user;

import android.content.*;
import android.net.*;

public class NetworkChecker { 
    public static boolean isInternetOn(Context context) { 
        NetworkInfo networkInfo; 
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity"); 
        return connectivityManager != null && (networkInfo = connectivityManager.getActiveNetworkInfo()) != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable(); 
    } 
} 
