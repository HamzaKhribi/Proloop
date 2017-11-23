package org.ivmlab.proloop.proloop.Tabs.TabsAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.ivmlab.proloop.proloop.Tabs.TabsViews.TabBarcodeScan;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.TabResult;

import java.util.ArrayList;

public class TabsDesignViewPagerAdapter2 extends FragmentStatePagerAdapter {

    ArrayList<CharSequence> titles;
    int tabNumber;

    // Constructor
    public TabsDesignViewPagerAdapter2(FragmentManager fragmentManager, ArrayList<CharSequence> titles, int tabNumber) {
        super(fragmentManager);

        this.titles = titles;
        this.tabNumber = tabNumber;

    }

    // Return Fragment for each position
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:{
                TabBarcodeScan tabBarcodeScan = new TabBarcodeScan();
                return  tabBarcodeScan;
            }
            case 1:{
                TabResult tabResult = new TabResult();
                return  tabResult;
            }
        }
        return null;
    }

    // Return tab titles for each position

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    // Return tab number.
    @Override
    public int getCount() {
        return tabNumber;
    }
}