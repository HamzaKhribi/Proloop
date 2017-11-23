package org.ivmlab.proloop.proloop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class AddPostActivity extends AppCompatActivity {

    private EditText postET;
    private ImageView closeBtn,addImageBtn,addedImg;
    private Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeProfile);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        AddPostActivity.this.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);

        postET = (EditText) findViewById(R.id.postET);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(postET.getId(), InputMethodManager.SHOW_FORCED);

        closeBtn = (ImageView) findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shareBtn = (Button) findViewById(R.id.shareBtn);
        addImageBtn = (ImageView) findViewById(R.id.addImageBtn);
        addedImg = (ImageView) findViewById(R.id.addedImg);
    }
}
