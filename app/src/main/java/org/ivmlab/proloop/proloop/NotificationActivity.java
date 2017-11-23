package org.ivmlab.proloop.proloop;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.PostCommentsAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.TimeLineAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePost;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.R.attr.id;
import static com.google.android.gms.analytics.internal.zzy.a;

public class NotificationActivity extends AppCompatActivity {

    private String pub_id;
    private String urlPost1;
    private RecyclerView recyclerView;
    private Context context;
    private String getComURL;
    private PostCommentsAdapter recyclerViewAdapter;
    private ArrayList<Comments> comments=new ArrayList<>();
    private TimeLinePost p;
    private EditText cmntToPush;
    private SharedPreferences sharedPreferences;
    private int user_id;
    private String a;
    private String urlPost3;
    private String urlPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        pub_id =intent.getStringExtra("pub_id");
        Log.e("INTENTNOTIF",pub_id);
         urlPost1 = "http://api.ivmlab.org/getPublicationByPubID.php?id=" + pub_id;
        Log.d("url", urlPost1);
        new getPubInfoTask().execute(urlPost1);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTLPosts21);


        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        getComURL = "http://api.ivmlab.org/getCom.php?pub_id=" + pub_id;
        new AsyncTaskNewsParseJson().execute(getComURL);


    }
    private class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            comments=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                Log.d("jsonObject",urlPost.toString());
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectPosts.getJSONObject(""+i);
                    String com_text=user.getString("com_text");
                    String DateTime=user.getString("DateTime");
                    String name=user.getString("name");
                    String id=user.getString("id");
                    String user_id=user.getString("user_id");
                    String profileImg = user.getString("profilePictureImgPath");

                    comments.add(new Comments(com_text,DateTime,name,profileImg,id,user_id));
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


            // Create the recyclerViewAdapter

            recyclerViewAdapter = new PostCommentsAdapter(NotificationActivity.this, comments);
            recyclerView.setAdapter(recyclerViewAdapter);

            //swipeRefreshLayout = (SwipeRefreshLayout) dialog.findViewById(R.id.swipe_container1);
            //swipeRefreshLayout.setRefreshing(false);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.GONE);

        }
    }

    private class getPubInfoTask extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            comments=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectPosts.getInt("count");
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectPosts.getJSONObject(""+i);
                    String pub_id=user.getString("id");
                    String userID=user.getString("userID");
                    String text=user.getString("text");
                    String imgPath=user.getString("imgPath");
                    String tagPub=user.getString("tag_pub");
                    String dateTime=user.getString("DateTime");
                    String name=user.getString("Name");
                    String lastName=user.getString("LastName");
                    String profileImgPath=user.getString("profilePictureImgPath");

                    p = new TimeLinePost(pub_id,userID,text,imgPath,tagPub,dateTime,name,lastName,profileImgPath);
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
            final TextView name = (TextView) findViewById(R.id.profile_name1);

            name.setText(p.getName()+" "+p.getLastName());
            final TextView txt = (TextView) findViewById(R.id.post_txt1);
            txt.setText(p.getText());
            if (p.getProfileImgPath().length()>1){
                new DownloadImageTask((ImageView) findViewById(R.id.profile_img1))
                        .execute(p.getProfileImgPath());
            }

            /*if (p.getTagPub().equalsIgnoreCase("media")){
                final ImageView img = (ImageView) dialog.findViewById(R.id.img_post1);
                Log.d("img_path",p.getImgPath());
                new DownloadImageTask((ImageView) dialog.findViewById(R.id.img_post1))
                        .execute(p.getImgPath());
                img.getLayoutParams().height = 400;
                img.requestLayout();
            }*/

            final ImageView like = (ImageView) findViewById(R.id.like);
            /*like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.setImageDrawable(context.getResources().getDrawable(R.drawable.like_done));

                }
            });*/

            ImageView pushCmnt = (ImageView) findViewById(R.id.pushCmnt);
            cmntToPush = (EditText) findViewById(R.id.cmntToPush);
            pushCmnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    datetime.setTimeZone(TimeZone.getTimeZone("gmt"));
                    String date= datetime.format(new Date());
                    String[] splited = date.split("\\s+");


                    sharedPreferences = NotificationActivity.this.getSharedPreferences("VALUES", NotificationActivity.this.MODE_PRIVATE);
                    user_id = sharedPreferences.getInt("USERID", 0);
                    Log.e("userrrIDNotif",Integer.toString(user_id));

                    String cmntToInsert = cmntToPush.getText().toString();
                    a = cmntToInsert.replaceAll(" ","%20");
                    if (cmntToInsert.equals("")){
                        Toast.makeText(NotificationActivity.this, "Enter some text!",Toast.LENGTH_LONG).show();
                    }else{
                        urlPost3 = "http://api.ivmlab.org/insertCom.php?user_id="+user_id+"&pub_id="+p.getPub_id()+"&cmnt="+a+"&DateTime="+splited[0]+"%20"+splited[1];
                        Log.e("commentaiiiiire",urlPost3);
                        new favPub1().execute(urlPost3);

                        cmntToPush.setText("");
                        urlPost = "http://api.ivmlab.org/getCom.php?pub_id="+ p.getPub_id();
                        new AsyncTaskNewsParseJson().execute(urlPost);

                        /*String nbrCom = String.valueOf(posts.get(positionPub).getNbrOfComs()+1);
                        Log.d("nbrCom",nbrCom);
                        nbrCmnt.setText(nbrCom+" comments");*/
                    }
                }
            });

            //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            //progressBar.setVisibility(View.GONE);

        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    private class favPub1 extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost3 = url[0];
            // HttpURLConnectionExample e = new HttpURLConnectionExample();
            try {
                // e.sendPost(urlPost,String.valueOf(user_id),p.getPub_id(),a,formattedDate);
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost3);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            //progressBar.setVisibility(View.GONE);

        }
    }
}
