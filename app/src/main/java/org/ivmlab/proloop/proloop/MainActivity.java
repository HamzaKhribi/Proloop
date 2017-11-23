package org.ivmlab.proloop.proloop;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.location.LocationManager;

import org.ivmlab.proloop.proloop.Fragments.FragmentFitRoom;
import org.ivmlab.proloop.proloop.Fragments.FragmentShops;
import org.ivmlab.proloop.proloop.Fragments.FragmentTestMySneaker;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import org.ivmlab.proloop.proloop.Utils.JsonParser;

import org.ivmlab.proloop.proloop.Fragments.FragmentContainer;
import org.ivmlab.proloop.proloop.Fragments.FragmentAllUsers;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewAdapters.DrawerAdapter;
import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.DrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.squareup.otto.Subscribe;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.squareup.otto.Subscribe;
import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.event.EventBus;
import org.ivmlab.proloop.proloop.event.PageChangedEvent;
import org.ivmlab.proloop.proloop.view.VerticalPager;

/**
 * Manages start screen of the application.
 */
public class MainActivity extends FragmentActivity implements LocationListener  {

    final Context context = this;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;
    public String tabSelector;
    private ViewPager mViewPager;
    public double lat=0.0;
    public double longi=0.0;
    boolean locationFound=false;
    private String urlPost;
    private JSONObject jsonObjectDesignPosts;
    private JSONArray jsonArrayDesignContent;
    private String adress;
    private String adress1;
    RelativeLayout home, find_friends, store_locator, test_my_sneaker;
    RelativeLayout efit_room;
    private ImageView profilePictureDrawer;

    int theme;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // -----------------------------------------------------------------------
    //
    // Statics
    //
    // -----------------------------------------------------------------------
    /**
     * Start page index. 0 - top page, 1 - central page, 2 - bottom page.
     */
    private static final int CENTRAL_PAGE_INDEX = 1;

    // -----------------------------------------------------------------------
    //
    // Fields
    //
    // -----------------------------------------------------------------------
    public VerticalPager mVerticalPager;

    // -----------------------------------------------------------------------
    //
    // Methods
    //
    // -----------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        sharedPreferences=getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme();

        findViews();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
                }, 10);
            }
            return;
        }
       locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,200000,10000, this);
    }


    public void theme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("THEME", 0);
        settingTheme(theme);
    }

    public void settingTheme(int theme) {
        switch (theme) {
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            case 7:
                setTheme(R.style.AppTheme7);
                break;
            case 8:
                setTheme(R.style.AppTheme8);
                break;
            case 9:
                setTheme(R.style.AppTheme9);
                break;
            case 10:
                setTheme(R.style.AppTheme10);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }

    private void findViews() {
        mVerticalPager = (VerticalPager) findViewById(R.id.activity_main_vertical_pager);
        initViews();
    }

    private void initViews() {
        snapPageWhenLayoutIsReady(mVerticalPager, CENTRAL_PAGE_INDEX);
    }

    private void snapPageWhenLayoutIsReady(final View pageView, final int page) {
		/*
		 * VerticalPager is not fully initialized at the moment, so we want to snap to the central page only when it
		 * layout and measure all its pages.
		 */
        pageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                mVerticalPager.snapToPage(page, VerticalPager.PAGE_SNAP_DURATION_INSTANT);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    // recommended removeOnGlobalLayoutListener method is available since API 16 only
                    pageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    removeGlobalOnLayoutListenerForJellyBean(pageView);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            private void removeGlobalOnLayoutListenerForJellyBean(final View pageView) {
                pageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getInstance().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onLocationChanged(PageChangedEvent event) {
        mVerticalPager.setPagingEnabled(event.hasVerticalNeighbors());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        Log.e("MyLoc",Double.toString(lat));
        longi=location.getLongitude();
        Log.e("Mylocc",Double.toString(longi));
        sharedPreferences = getSharedPreferences("VALUES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lat", Double.toString(lat));
        editor.putString("lng",Double.toString(longi));
        editor.commit();




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

    }
    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];
            Log.v("url",urlPost);
            try {
                jsonObjectDesignPosts = JsonParser.readJsonFromUrl(urlPost);
                jsonArrayDesignContent = jsonObjectDesignPosts.getJSONArray("results");
                int size = jsonArrayDesignContent.length();

                JSONObject place = jsonArrayDesignContent.getJSONObject(size-3);
                adress = place.getString("formatted_address").toString();
                String stateName=adress;
                int iend = stateName.indexOf(","); //this finds the first occurrence of "."
                if (iend != -1){
                    String stateName1= stateName.substring(0 , iend); //this will give abc
                    Log.i("stateName1",stateName1);
                    sharedPreferences =getSharedPreferences("VALUES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Country", stateName1);
                    editor.commit();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {

            startActivity(getIntent());
        } else {
            Log.d("getFrag",Integer.toString(getFragmentManager().getBackStackEntryCount()));
        }
    }
}

