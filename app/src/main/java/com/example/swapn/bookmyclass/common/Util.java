package com.example.swapn.bookmyclass.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.swapn.bookmyclass.models.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by swapn on 11/20/2016.
 */

public class Util {
    public static final String MyPREFERENCES = "BMCPrefs" ;
    private static final String TAG = "UtilClass";
    private  static Util u;
    public static Util getInstance() {
        if(u != null) {
            return u;
        }
        else {
            return new Util();
        }
    }

    public void setSharedPreferences (Context context, User user) {
        SharedPreferences mPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("user", json);
        prefsEditor.commit();
    }


    public User getUserFromSHaredPreference (Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public static String getStringDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String dateTemp = df.format(date).toString().trim();
        return dateTemp;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Util.context = context;
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, name +".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        ContextWrapper cw = new ContextWrapper(context);
        FileInputStream fis;
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, picName +".jpg");
        try {
            fis = new FileInputStream(mypath);
            b = BitmapFactory.decodeStream(fis);
            fis.close();
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "file not found");
           // e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(TAG, "io exception");
           // e.printStackTrace();
        }
        return b;
    }

    public static Context context;
}
