package org.ivmlab.proloop.proloop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.MessageAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.MessageSearchAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.User;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.Messages;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchFriendsActivity extends AppCompatActivity {
    RecyclerView listView;
    EditText editText;
    private String urlPost;
    private SharedPreferences sharedPreferences;
    private ArrayList<User> users;
    JSONObject jsonObjectUsers;
    private MessageSearchAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        editText= (EditText) findViewById(R.id.searchForFriendEditText);
        listView= (RecyclerView) findViewById(R.id.resultLV);
        listView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchFriendsActivity.this);
        listView.setLayoutManager(linearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int userId=sharedPreferences.getInt("USERID", 0);
        urlPost = "http://api.ivmlab.org/getFollowersByUserId.php?user_id="+userId;
        Log.e("urlpost",urlPost);

        new AsyncTaskNewsParseJson().execute(urlPost);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.e("charsequence", s.toString());
            }
        });

    }
    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            users=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url",urlPost);
            try {
                jsonObjectUsers = JsonParser.readJsonFromUrl(urlPost);
                int count = jsonObjectUsers.getInt("count");
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectUsers.getJSONObject(""+i);
                    String usr_id=user.getString("id");
                    String name=user.getString("Name");
                    String lastName=user.getString("LastName");
                    String country=user.getString("country");
                    String age=user.getString("age");
                    String gender=user.getString("gender");
                    String about_user=user.getString("about_user");
                    String bio=user.getString("bio");
                    String profilePictureImgPath=user.getString("profilePictureImgPath");

                    users.add(new User(usr_id,name,lastName,country,age,gender,about_user,profilePictureImgPath,bio));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String result) {

            // Create the recyclerViewAdapter
            Log.e("numuser",users.size()+"");
            adapter = new MessageSearchAdapter(SearchFriendsActivity.this, users);
            listView.setAdapter(adapter);


        }
    }
}
