package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amita.simplycs.Adapter.RecyclerAdapter;
import com.example.amita.simplycs.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PreviousFragment extends Fragment
{

    String prevDate;

    TextView Test;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    int RecyclerViewItemPosition ;

    GridLayoutManager mLayoutManager;


    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_previous, container, false);


        BottomNavigationView navigation= (BottomNavigationView)getActivity().findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        menu.findItem(R.id.navigation_archive).setTitle("Archive");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todayDate=df.format(c);

        Date date = null;
        try {
            date = df.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        prevDate = df.format(calendar.getTime());

        Test=(TextView)rootview.findViewById(R.id.test);
        Test.setText(prevDate);


        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview1);

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);



        return rootview;
    }

}
