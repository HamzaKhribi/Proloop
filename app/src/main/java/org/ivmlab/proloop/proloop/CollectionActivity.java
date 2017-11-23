package org.ivmlab.proloop.proloop;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.CollectionAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.PostCommentsAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.CollectionCard;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Comments;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<CollectionCard> collectionCard;
    private CollectionAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);


        recyclerView = (RecyclerView) findViewById(R.id.collectionRecycler);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        String urlPost = "http://api.ivmlab.org/getCollection.php";
        new AsyncTaskNewsParseJson().execute(urlPost);
    }


    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        private JSONObject jsonObjectPosts;

        @Override
        protected void onPreExecute() {
            collectionCard=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            String urlPost = url[0];
            try {
                jsonObjectPosts = JsonParser.readJsonFromUrl(urlPost);
                Log.v("url",jsonObjectPosts.toString());
                int count = jsonObjectPosts.getInt("count");
                Log.v("url",String.valueOf(count));
                for (int i=1;i<=count;i++){
                    JSONObject user=jsonObjectPosts.getJSONObject(""+i);
                    String id=user.getString("id");
                    String user_id=user.getString("user_id");
                    String sneaker_model=user.getString("sneaker_model");
                    String sneaker_imgPath=user.getString("sneaker_imgPath");
                    String dateOfBuy=user.getString("dateOfBuy");
                    String price=user.getString("price");

                    collectionCard.add(new CollectionCard(id,user_id,sneaker_model,sneaker_imgPath,dateOfBuy,price));
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

            recyclerViewAdapter = new CollectionAdapter(CollectionActivity.this, collectionCard);
            recyclerView.setAdapter(recyclerViewAdapter);

            //swipeRefreshLayout = (SwipeRefreshLayout) dialog.findViewById(R.id.swipe_container1);
            //swipeRefreshLayout.setRefreshing(false);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.GONE);

        }
    }
}
