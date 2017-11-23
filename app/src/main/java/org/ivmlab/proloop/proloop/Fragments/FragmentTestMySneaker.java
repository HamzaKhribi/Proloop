package org.ivmlab.proloop.proloop.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.Tabs.TabsAdapters.TabsDesignViewPagerAdapter2;
import org.ivmlab.proloop.proloop.Tabs.TabsUtils.SlidingTabLayout;

import java.util.ArrayList;
public class FragmentTestMySneaker extends Fragment {

    View view;
    public ViewPager pager;
    public TabsDesignViewPagerAdapter2 tabsDesignViewPagerAdapter2;
    public SlidingTabLayout tabs;
    public static ArrayList<CharSequence> titles=new ArrayList<>();
    int tabNumber = titles.size();
    int tabsPaddingTop;
    SharedPreferences sharedPreferences;
    String tabSelector;
    private String urlPost;
   /* public int tabSelector;

    public int getTabSelector() {
        return tabSelector;
    }

    public void setTabSelector(int tabSelector) {
        this.tabSelector = tabSelector;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_test_my_sneaker, container, false);

        if (titles.size()==0){
            titles.add("Scan Product");
            titles.add("Result");
        }

        //  Setup tabs
        setupTabs();

        return view;
    }

    public void setupTabs() {

        tabsDesignViewPagerAdapter2 = new TabsDesignViewPagerAdapter2(getFragmentManager(), titles, 2);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(tabsDesignViewPagerAdapter2);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);

        tabs.setPadding(convertToPx(0), tabsPaddingTop, convertToPx(0), 0);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        tabSelector=sharedPreferences.getString("tabSelector","bla3");
        urlPost=sharedPreferences.getString("URL","bla");
        Log.e("sharedddddPrefff",tabSelector);
        Log.e("URLLLLLL",urlPost);
        if (!tabSelector.equals("bla3")  ){

            pager.setCurrentItem(1);
            Log.e("Tab changed ResultTab",tabSelector);
        }

        tabs.setViewPager(pager);
    }

    public int convertToPx(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

