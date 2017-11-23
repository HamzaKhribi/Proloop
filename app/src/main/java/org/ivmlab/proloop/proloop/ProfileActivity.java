package org.ivmlab.proloop.proloop;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.TimeLineAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Favourites;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePosts;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.User;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Item;
import org.ivmlab.proloop.proloop.Tabs.TabsUtils.SlidingTabLayout;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.Timeline;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.messaging_package2.MessagingActivity2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String urlPost;
    String urlPost1;
    String urlPost2;
    String urlPost3;
    ArrayList<Item> timeLinePosts =new ArrayList<>();
    private ArrayList<Item> timeLinePostsTmp;
    SharedPreferences sharedPreferences;
    private int user_id;
    private TimeLineAdapter mAdapter;
    User userInfo;
    TextView user_profile_name;
    TextView user_profile_short_bio;
    CircleImageView user_profile_photo;
    TextView followingTV,followersTV;
    private String count_followers,count_following;
    ImageView followButton, messageButton;
    private int userId;
    private int follow=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeProfile);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        followingTV = (TextView) findViewById(R.id.followingTV);
        followersTV = (TextView) findViewById(R.id.followersTV);
        followButton = (ImageView) findViewById(R.id.followButton);
        messageButton = (ImageView) findViewById(R.id.messageButton);


        sharedPreferences = ProfileActivity.this.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("USERID", 0);

        Bundle b = getIntent().getExtras();
        user_id = Integer.parseInt(b.getString("user_id"));

        urlPost1="http://api.ivmlab.org/getProfile.php?user_id="+user_id;
        new AsyncTaskGetProfileInfo().execute(urlPost1);

        urlPost2="http://api.ivmlab.org/getUserFollowers.php?user_id="+user_id+"&followerID="+userId;
        new AsyncTaskGetFollowCount().execute(urlPost2);


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow==0){
                    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    datetime.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String date= datetime.format(new Date());

                    String[] splited = date.split("\\s+");
                    urlPost3="http://api.ivmlab.org/follow.php?follower_id="+userId+"&followed_id="+user_id+"&DateTime="+splited[0]+"%20"+splited[1];
                    new AsyncTaskFollowUnfollow().execute(urlPost3);
                    follow=1;
                    followButton.setImageResource(R.drawable.followed);

                    followButton.setEnabled(false);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            ProfileActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    followButton.setEnabled(true);

                                }
                            });
                        }
                    }).start();
                }
                else if (follow==1){
                    urlPost3="http://api.ivmlab.org/unfollow.php?follower_id="+userId+"&followed_id="+user_id;
                    new AsyncTaskFollowUnfollow().execute(urlPost3);
                    follow=0;
                    followButton.setImageResource(R.drawable.follow);

                    followButton.setEnabled(false);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            ProfileActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    followButton.setEnabled(true);

                                }
                            });
                        }
                    }).start();
                }
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MessagingActivity2.class);
                Bundle b = new Bundle();
                b.putString("chatting_with",userInfo.getName()+" "+userInfo.getLastName());
                b.putString("chatting_with_id", Integer.toString(user_id));

                intent.putExtras(b);
                startActivity(intent);
            }
        });

        recyclerViewDesign();
    }

    private void recyclerViewDesign() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPosts);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TimeLineAdapter(this, timeLinePosts);
        recyclerView.setAdapter(mAdapter);

        sharedPreferences = this.getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        urlPost="http://api.ivmlab.org/getProfilePub.php?user_id="+user_id;
        Log.e("urlpost",urlPost);

        new AsyncTaskGetPosts().execute(urlPost);



    }

    public class AsyncTaskGetProfileInfo extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost1 = url[0];
            Log.v("url1",urlPost1);
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost1);
                JSONArray user_info = jsonObjectPosts.getJSONArray("user_info");
                String Name=user_info.getString(1);
                String LastName=user_info.getString(2);
                String country=user_info.getString(3);
                String age=user_info.getString(4);
                String profilePic=user_info.getString(6);
                Log.e("Name",Name);
                String bio=user_info.getString(5);

                userInfo = new User(Name,LastName, country, age, profilePic, bio);
                Log.e("userInfo", userInfo.toString());

            } catch (IOException | JSONException e) {
                Log.e("fil catch","nghanni");
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            Log.e("userInfo", userInfo.toString());
            user_profile_name = (TextView) findViewById(R.id.user_profile_name);
            user_profile_short_bio = (TextView) findViewById(R.id.user_profile_short_bio);

            user_profile_name.setText(userInfo.getName()+" "+userInfo.getLastName());
            user_profile_short_bio.setText(userInfo.getBio()+", "+userInfo.getAge()+", "+userInfo.getCountry());

            new DownloadImageTask((ImageView) findViewById(R.id.user_profile_photo))
                    .execute(userInfo.getProfilePic());
        }
    }

    public class AsyncTaskGetFollowCount extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost1 = url[0];
            Log.v("url1",urlPost1);
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost1);
                count_followers = jsonObjectPosts.getString("count_followers");
                count_following = jsonObjectPosts.getString("count_following");
                follow = jsonObjectPosts.getInt("follow");


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {
            followingTV.setText("Following: "+count_following);
            followersTV.setText("Followers: "+count_followers);

            if (userId == user_id){
                followButton.setVisibility(View.GONE);
                messageButton.setVisibility(View.GONE);
            } else {
                if (follow == 1){
                    followButton.setImageResource(R.drawable.followed);
                }
                else if (follow == 0){
                    followButton.setImageResource(R.drawable.follow);
                }
            }
        }
    }

    public class AsyncTaskGetPosts extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            timeLinePostsTmp = new ArrayList<>();
            //timeLinePosts.add(new Footer());
            // recyclerView.getAdapter().notifyItemInserted(timeLinePosts.size() - 1);
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url",urlPost);
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
                    String name=user.getString("name");
                    String lastName=user.getString("LastName");
                    int nbrOfComs=user.getInt("nbrOfComs");
                    String profileImgPath=user.getString("profilePictureImgPath");
                    JSONObject faves = user.getJSONObject("faves");
                    ArrayList<Favourites> favourites= new ArrayList<>();
                    int countFaves = faves.getInt("count");
                    for (int j=1;j<=countFaves;j++){
                        JSONObject favesId = faves.getJSONObject(""+j);
                        String user_id=favesId.getString("user_id");
                        String username=favesId.getString("name");
                        String userLastname=favesId.getString("LastName");
                        favourites.add(new Favourites(user_id,username,userLastname));

                    }
                    String faved = user.getString("faved");

                    timeLinePostsTmp.add(new TimeLinePosts(pub_id,userID,text,imgPath,tagPub,dateTime,name,lastName,profileImgPath,favourites,faved,countFaves,nbrOfComs));
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

            timeLinePosts.addAll(timeLinePostsTmp);
            recyclerView.getAdapter().notifyDataSetChanged();

        }
    }

    public class AsyncTaskFollowUnfollow extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            Log.v("url",urlPost);
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String result) {

            //////////////

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
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in,null,options);
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
}
