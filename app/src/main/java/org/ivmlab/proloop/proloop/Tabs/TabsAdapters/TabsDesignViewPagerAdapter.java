package org.ivmlab.proloop.proloop.Tabs.TabsAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.ivmlab.proloop.proloop.R;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.Messages;
import org.ivmlab.proloop.proloop.Tabs.TabsViews.Timeline;

import java.util.ArrayList;

public class TabsDesignViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<CharSequence> titles;
    int tabNumber;
    private int tabIcons[] = {R.drawable.cmnnt, R.drawable.mssg};


    // Constructor
    public TabsDesignViewPagerAdapter (FragmentManager fragmentManager, ArrayList<CharSequence> titles, int tabNumber) {
        super(fragmentManager);

        this.titles = titles;
        this.tabNumber = tabNumber;

    }

    // Return Fragment for each position
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:{
                Timeline timeLine = new Timeline();
                return  timeLine;
            }
            case 1:{
                Messages messages = new Messages();
                return  messages;
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