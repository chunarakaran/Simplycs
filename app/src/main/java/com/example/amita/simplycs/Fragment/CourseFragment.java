package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amita.simplycs.R;

public class CourseFragment extends Fragment
{

    TextView View_courselist;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_course, container, false);

        View_courselist=(TextView)rootview.findViewById(R.id.view_courselist);

        View_courselist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection=getFragmentManager().beginTransaction();
                CourseListFragment mfragment=new CourseListFragment();
                transection.replace(R.id.content_frame, mfragment);
                transection.addToBackStack(null).commit();
            }
        });

        return rootview;
    }

}
