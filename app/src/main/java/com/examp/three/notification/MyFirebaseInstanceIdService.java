package com.examp.three.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Murugan on 23-10-2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

private static String TAG =MyFirebaseInstanceIdService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG,refreshToken);
    }
}
