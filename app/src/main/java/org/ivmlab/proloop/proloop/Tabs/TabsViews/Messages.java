package org.ivmlab.proloop.proloop.Tabs.TabsViews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.MessageAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.TimeLineAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Develop;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.User;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Footer;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Item;
import org.ivmlab.proloop.proloop.SearchFriendsActivity;
import org.ivmlab.proloop.proloop.Tabs.TabsUtils.SlidingTabLayout;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.Utils.ScrollManagerToolbarTabs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class Messages extends Fragment  {


    JSONObject jsonObjectDesignPosts;
    ArrayList<Item> timeLinePosts =new ArrayList<>();
    RecyclerView.Adapter recyclerViewAdapter;
    SlidingTabLayout tabs;
    private int user_id;
    private TimeLineAdapter mAdapter;
    private boolean hasMore;
    private ArrayList<Item> timeLinePostsTmp;String urlPost;
    JSONObject jsonObjectUsers;
    JSONArray jsonArrayDevelopContent;
    ArrayList<Develop> develop;
    ArrayList<User> users=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    String[] developTitle, developExcerpt, developImage, developImageFull;
    int postNumber = 99;
    SharedPreferences sharedPreferences;
    Boolean error = false;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    View view;
    int recyclerViewPaddingTop;
    TypedValue typedValueToolbarHeight = new TypedValue();
    Toolbar toolbar;
    FrameLayout statusBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_design, container, false);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        FloatingActionButton newMessage = (FloatingActionButton) view.findViewById(R.id.fab);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getContext(), SearchFriendsActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
               getContext().startActivity(myIntent);

            }
        });

        hasMore = true;


        recyclerViewDesign(view);

        swipeToRefresh(view);

        return view;
    }

    private boolean hasFooter() {
        return timeLinePosts.get(timeLinePosts.size() - 1) instanceof Footer;
    }


    private void recyclerViewDesign(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDesign);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TimeLineAdapter(getActivity(), timeLinePosts);
        recyclerView.setAdapter(mAdapter);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int userId=sharedPreferences.getInt("USERID", 0);
        urlPost = "http://api.ivmlab.org/getLastMessagesByUserId.php?user_id="+userId;
        Log.e("urlpost",urlPost);

        new AsyncTaskNewsParseJson().execute(urlPost);

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
                developTitle = new String[0];
                error = true;
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String result) {

            // Create the recyclerViewAdapter
            adapter = new MessageAdapter(getActivity(), users);
            recyclerView.setAdapter(adapter);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void swipeToRefresh(View view) {
        //timeLinePosts=new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        int start = recyclerViewPaddingTop - convertToPx(48), end = recyclerViewPaddingTop + convertToPx(16);
        swipeRefreshLayout.setProgressViewOffset(true, start, end);
        TypedValue typedValueColorPrimary = new TypedValue();
        TypedValue typedValueColorAccent = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValueColorPrimary, true);
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValueColorAccent, true);
        final int colorPrimary = typedValueColorPrimary.data, colorAccent = typedValueColorAccent.data;
        swipeRefreshLayout.setColorSchemeColors(colorPrimary, colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timeLinePosts =new ArrayList<>();
                new AsyncTaskNewsParseJson().execute(urlPost);
            }
        });
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    public void toolbarHideShow() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                ScrollManagerToolbarTabs manager = new ScrollManagerToolbarTabs(getActivity());
                manager.attach(recyclerView);
                manager.addView(toolbar, ScrollManagerToolbarTabs.Direction.UP);
                manager.setInitialOffset(toolbar.getHeight());
            }
        });
    }
}

