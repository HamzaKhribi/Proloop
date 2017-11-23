package org.ivmlab.proloop.proloop.firebase_services;

/**
 * Created by mal21 on 21/08/2016.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


import android.content.SharedPreferences;

import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.messaging_package2.MessagingActivity2;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ResourceBundle;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public String urlPost;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    public void onTokenRefresh() {

        //Getting registration token

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sharedPreferences = getApplicationContext().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcmToken",refreshedToken);
        editor.commit();
        Log.e("refreshed token",refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }

}