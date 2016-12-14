package com.example.swapn.bookmyclass.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by swapn on 12/4/2016.
 */

public class BookMyClassFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "BMCInstanceIDService";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token " + refreshedToken);
    }
}
