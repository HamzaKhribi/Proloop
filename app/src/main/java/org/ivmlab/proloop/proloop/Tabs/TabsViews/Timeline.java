package org.ivmlab.proloop.proloop.Tabs.TabsViews;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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

import org.ivmlab.proloop.proloop.AddTLPostActivity;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Item;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.TimeLineAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Favourites;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.TimeLinePosts;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.Tabs.TabsUtils.SlidingTabLayout;
import org.ivmlab.proloop.proloop.Utils.JsonParser;
import org.ivmlab.proloop.proloop.Utils.ScrollManagerToolbarTabs;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class Timeline extends Fragment  {


    String urlPost;
    JSONObject jsonObjectDesignPosts;
    ArrayList<Item> timeLinePosts =new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    View view;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    SlidingTabLayout tabs;
    int recyclerViewPaddingTop;
    private int user_id;
    private TimeLineAdapter mAdapter;
    private boolean hasMore;
    private ArrayList<Item> timeLinePostsTmp;
    private FloatingActionButton mFab;
    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab_design, container, false);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);



        recyclerViewDesign(view);
        swipeToRefresh(view);





        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_add_post);

                dialog.show();*/

                Intent intent = new Intent(getActivity(),AddTLPostActivity.class);
                getActivity().startActivity(intent);

            }
        });

        return view;
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
        user_id = sharedPreferences.getInt("USERID", 0);
        urlPost="http://api.ivmlab.org/getTL2.php?user_id="+user_id;
        Log.e("urlpost",urlPost);

        new AsyncTaskNewsParseJson().execute(urlPost);


        /*   recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        //position starts at 0

                            new AsyncTaskNewsParseJson().execute(urlPost);

                }
            });*/



    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
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
                            String followedId=user.getString("followed_id");
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

                            timeLinePostsTmp.add(new TimeLinePosts(followedId,pub_id,userID,text,imgPath,tagPub,dateTime,name,lastName,profileImgPath,favourites,faved,countFaves,nbrOfComs));
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


          /*  int size = timeLinePosts.size();
            timeLinePosts.remove(size-1);//removes footer*/
            timeLinePosts.addAll(timeLinePostsTmp);
            recyclerView.getAdapter().notifyDataSetChanged();

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setRefreshing(false);

            //ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.GONE);

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
                timeLinePosts.clear();
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

