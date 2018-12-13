package com.example.amita.simplycs.Fragment;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amita.simplycs.Adapter.RecyclerAdapter;
import com.example.amita.simplycs.R;
import com.example.amita.simplycs.VideoListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TodayFragment extends Fragment
{

    String todayDate;
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
        rootview = inflater.inflate(R.layout.fragment_today, container, false);

        BottomNavigationView navigation= (BottomNavigationView)getActivity().findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        menu.findItem(R.id.navigation_archive).setTitle("Archive");


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        todayDate=df.format(c);
        Test=(TextView)rootview.findViewById(R.id.test);
        Test.setText(todayDate);


        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview1);

//        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);


//         Implementing Click SipAudioCall.Listener on RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });


            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent)
            {


                rootview = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(rootview != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(rootview);

                    FragmentTransaction transection=getFragmentManager().beginTransaction();
                    switch (RecyclerViewItemPosition)
                    {
                        case 0:

//                            Toast.makeText(getActivity(),"Topics",Toast.LENGTH_SHORT).show();

                            TopicListFragment mfragment=new TopicListFragment();
                            transection.replace(R.id.content_frame, mfragment);
                            transection.addToBackStack(null).commit();

                            break;
                        case 1:

                            Intent intent = new Intent(getActivity(), VideoListActivity.class);
                            startActivity(intent);
                           // finish();
                          break;
                        case 2:

                            Toast.makeText(getActivity(),"Current Affairs",Toast.LENGTH_SHORT).show();
                            break;

                        case 3:

                            Toast.makeText(getActivity(),"Practice",Toast.LENGTH_SHORT).show();
                            break;
                        case 4:

                            Toast.makeText(getActivity(),"Revision",Toast.LENGTH_SHORT).show();
                            break;
                        case 5:

                            Toast.makeText(getActivity(),"Other",Toast.LENGTH_SHORT).show();
                            break;
                    }


                    }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



//        rootview.setFocusableInTouchMode(true);
//        rootview.requestFocus();
//        rootview.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    // DO WHAT YOU WANT ON BACK PRESSED
//                    getFragmentManager().popBackStack();
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });




        return rootview;
    }

}
