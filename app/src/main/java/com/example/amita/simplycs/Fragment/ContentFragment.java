package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amita.simplycs.Adapter.Pager;
import com.example.amita.simplycs.R;

public class ContentFragment extends Fragment implements TabLayout.OnTabSelectedListener
{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String Topic_id,SubTopic_id,TodayDate;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_content, container, false);

        Bundle bundle1=getArguments();
        Topic_id=String.valueOf(bundle1.getString("topic_id"));
        SubTopic_id=String.valueOf(bundle1.getString("subtopic_id"));
        TodayDate=String.valueOf(bundle1.getString("TodayDate"));


        tabLayout = (TabLayout)rootview.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Theory"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Audio"));
        tabLayout.addTab(tabLayout.newTab().setText("PDF"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)rootview.findViewById(R.id.pager);

        Bundle bundle=new Bundle();
        bundle.putString("topic_id",Topic_id);
        bundle.putString("subTopic_id",SubTopic_id);
        bundle.putString("TodayDate",TodayDate);


        Pager adapter = new Pager(getFragmentManager(), tabLayout.getTabCount(),bundle);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);




        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // DO WHAT YOU WANT ON BACK PRESSED
                    getFragmentManager().popBackStack();
                    return true;
                } else {
                    return false;
                }
            }
        });




        return rootview;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
