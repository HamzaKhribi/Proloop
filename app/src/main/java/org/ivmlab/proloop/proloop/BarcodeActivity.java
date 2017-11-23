package org.ivmlab.proloop.proloop;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.Fragments.FragmentTestMySneaker;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.TabBarcodeScan;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ViewPager mViewPager;
    private String urlPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        new IntentIntegrator(this).initiateScan();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //Log.d("result content",result.getContents());
        if(result != null) {
            if(result.getContents() == null) {
                sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tabSelector","bla3");
                editor.commit();
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Intent i = new Intent(BarcodeActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("BarCode", result.getContents());


                editor.commit();
                urlPost = ("http://api.ivmlab.org/getSneakerResultByBarcode.php?barCode=" +  result.getContents());
                new AsyncTaskGetSneakerByBarcode().execute(urlPost);


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public class AsyncTaskGetSneakerByBarcode extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url", urlPost);
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                if(count==0){
                    Log.e("count","0000");
                    Intent i = new Intent(BarcodeActivity.this, MainActivity.class);
                    i.putExtra("addStyle","1");
                    startActivity(i);




                }
                else{
                    JSONObject result=jsonObjectPosts.getJSONObject("result");
                    String styleNum=result.getString("style_num");
                    Intent i = new Intent(BarcodeActivity.this, MainActivity.class);
                    i.putExtra("addStyle","0");
                    i.putExtra("styleNum",styleNum);
                    startActivity(i);




                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {


        }
    }
}
