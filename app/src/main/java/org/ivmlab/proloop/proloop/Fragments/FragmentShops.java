package org.ivmlab.proloop.proloop.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.ivmlab.proloop.proloop.MainActivity;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.ShopAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Shop;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentShops extends Fragment {

    String urlPost;
    JSONObject jsonObjectUsers;
    String country;
    String country1;

    ArrayList<Shop> shops=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    View view;
    int recyclerViewPaddingTop;
    FrameLayout statusBar;
    private JSONArray resultArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shops, container, false);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);

        // Setup RecyclerView News
        recyclerViewDevelop(view);

        // Setup swipe to refresh
        swipeToRefresh(view);


        return view;
    }

    private void recyclerViewDevelop(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDevelop);

        // Divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setPadding(0, recyclerViewPaddingTop, 0, 0);

        country = sharedPreferences.getString("Country", "bla");
        country1 = country.replaceAll(" ", "%20");
        Log.d("Countryyyy", country);
        urlPost = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=Nike%20Store%20"+country1+"&key=AIzaSyAYlT2W02QQKx9nf3YNr9CaG4LES0Nyv-8";
        new AsyncTaskNewsParseJson().execute(urlPost);
    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            shops=new ArrayList<>();
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url", urlPost);
            try {
                jsonObjectUsers = JsonParser.readJsonFromUrl(urlPost);
                resultArray = jsonObjectUsers.getJSONArray("results");
                Log.e("resultArray",resultArray.toString());
                int length = resultArray.length();
                for (int i = 0 ; i<length ; i++){
                    Double lat = Double.parseDouble(resultArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    Double lng = Double.parseDouble( resultArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng"));
                    String shopName = resultArray.getJSONObject(i).getString("name");
                    String adress = resultArray.getJSONObject(i).getString("formatted_address");

                    shops.add(new Shop(lat,lng,adress,shopName));
                    Log.i("shopName",shopName);
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
            adapter = new ShopAdapter(getActivity(), shops);
            recyclerView.setAdapter(adapter);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

            swipeRefreshLayout.setRefreshing(false);

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void swipeToRefresh(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        int start = convertToPx(0), end = recyclerViewPaddingTop + convertToPx(16);
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

    public void animationTranslationY(View view, int duration, int interpolator, int translationY) {
        Animator slideInAnimation = ObjectAnimator.ofFloat(view, "translationY", translationY);
        slideInAnimation.setDuration(duration);
        slideInAnimation.setInterpolator(new AccelerateInterpolator(interpolator));
        slideInAnimation.start();
    }
}