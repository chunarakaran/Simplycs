package com.example.amita.simplycs.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.amita.simplycs.R;

import java.util.Calendar;

public class ArchiveFragment extends Fragment
{

    View rootview;

    BottomNavigationView navigation;
    Menu menu;

    private Calendar mcalendar;
    private int day,month,year;
    String aDate;

    RecyclerView recyclerView;

    int RecyclerViewItemPosition ;

    GridLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_archive, container, false);



        navigation = (BottomNavigationView)getActivity().findViewById(R.id.navigation);
        menu= navigation.getMenu();


        mcalendar=Calendar.getInstance();
        day=mcalendar.get(Calendar.DAY_OF_MONTH);
        year=mcalendar.get(Calendar.YEAR);
        month=mcalendar.get(Calendar.MONTH);

        DateDialog();

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview1);

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);





        return rootview;
    }

    public void DateDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                int Month;
                Month=monthOfYear+1;
                aDate=dayOfMonth+"-"+Month+"-"+year;
                menu.findItem(R.id.navigation_archive).setTitle(aDate);

            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(mcalendar.getTimeInMillis());
        dpDialog.show();
    }

}
