package org.ivmlab.proloop.proloop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.messaging_package2.Message;
import org.ivmlab.proloop.proloop.messaging_package2.MessageDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AddTLPostActivity extends ActionBarActivity {

    private EditText postET;
    private ImageView closeBtn,addImageBtn,addedImg;
    private Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeProfile);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtlpost);

        postET = (EditText) findViewById(R.id.postET);

        closeBtn = (ImageView) findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(postET.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

        shareBtn = (Button) findViewById(R.id.shareBtn);
        addImageBtn = (ImageView) findViewById(R.id.addImageBtn);
        addedImg = (ImageView) findViewById(R.id.addedImg);

    }
}
