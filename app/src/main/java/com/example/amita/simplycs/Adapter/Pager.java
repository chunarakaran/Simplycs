package com.example.amita.simplycs.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.amita.simplycs.Fragment.AudioListFragment;
import com.example.amita.simplycs.Fragment.PDFListFragment;
import com.example.amita.simplycs.Fragment.TheoryViewFragment;
import com.example.amita.simplycs.Fragment.VideoListFragment;


//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Bundle bundle;



    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount,Bundle bundle) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.bundle=bundle;
    }


    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                TheoryViewFragment tab1 = new TheoryViewFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                VideoListFragment tab2 = new VideoListFragment();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                AudioListFragment tab3 = new AudioListFragment();
                tab3.setArguments(bundle);
                return tab3;
            case 3:
                PDFListFragment tab4 = new PDFListFragment();
                tab4.setArguments(bundle);
                return tab4;
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