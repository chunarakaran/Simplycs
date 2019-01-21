package com.example.amita.simplycs.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.amita.simplycs.Fragment.AudioListFragment;
import com.example.amita.simplycs.Fragment.TheoryListFragment;
import com.example.amita.simplycs.Fragment.VideoListFragment;


//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                TheoryListFragment tab1 = new TheoryListFragment();
                return tab1;
            case 1:
                VideoListFragment tab2 = new VideoListFragment();
                return tab2;
            case 2:
                AudioListFragment tab3 = new AudioListFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}