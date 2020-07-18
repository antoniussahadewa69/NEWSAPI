package com.example.newsapi.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by antoniuskrisnasahadewa on 17/06/2020.
 */

public class CoreApp {

    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<=0.1){
            return d<0 ? -i : i;
        }else{
            return d<0 ? -(i+1) : i+1;
        }
    }

    public static String reformatDate(String input) {
        SimpleDateFormat input_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output_format = new SimpleDateFormat("dd-MMMM-yyyy");
        Date before_format;
        String after_format = null;
        try {
            before_format = input_format.parse(input);
            after_format = output_format.format(before_format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return after_format;
    }
}
