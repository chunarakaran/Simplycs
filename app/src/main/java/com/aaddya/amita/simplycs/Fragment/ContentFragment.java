package com.aaddya.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaddya.amita.simplycs.Adapter.Pager;
import com.aaddya.amita.simplycs.R;

public class ContentFragment extends Fragment implements TabLayout.OnTabSelectedListener
{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String Category_id,SubCategory_id,CDate,content_id,content_name,content_Data,PremiumUser;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_content, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle1=getArguments();
        Category_id=String.valueOf(bundle1.getString("category_id"));
        SubCategory_id=String.valueOf(bundle1.getString("SubCategory_id"));
        CDate=String.valueOf(bundle1.getString("CDate"));
        content_id=String.valueOf(bundle1.getString("content_id"));
        content_name=String.valueOf(bundle1.getString("content_name"));
        content_Data=String.valueOf(bundle1.getString("content_Data"));
        PremiumUser=String.valueOf(bundle1.getString("PremiumUser"));

        toolbar.setTitle(content_name);

        tabLayout = (TabLayout)rootview.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Theory"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Audio"));
        tabLayout.addTab(tabLayout.newTab().setText("PDF"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)rootview.findViewById(R.id.pager);

        Bundle bundle=new Bundle();
        bundle.putString("Category_id",Category_id);
        bundle.putString("SubCategory_id",SubCategory_id);
        bundle.putString("CDate",CDate);
        bundle.putString("content_id",content_id);
        bundle.putString("content_name",content_name);
        bundle.putString("content_Data",content_Data);
        bundle.putString("PremiumUser",PremiumUser);


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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
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
