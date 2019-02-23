package com.example.amita.simplycs.Fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.Adapter.DataAdapter1;
import com.example.amita.simplycs.Adapter.RecyclerViewAdapter1;
import com.example.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodayFragment extends Fragment
{

    String todayDate;
    TextView Test;

    List<DataAdapter1> ListOfdataAdapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String topic_id;
    final ArrayList<DataAdapter1> Topicid = new ArrayList<>();
    int RecyclerViewItemPosition ;

    GridLayoutManager mLayoutManager;


    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;


    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_today, container, false);


        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        BottomNavigationView navigation= (BottomNavigationView)getActivity().findViewById(R.id.navigation);
        Menu menu= navigation.getMenu();
        menu.findItem(R.id.navigation_archive).setTitle("Archive");


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        todayDate=df.format(c);


        ListOfdataAdapter = new ArrayList<>();
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview1);


        mLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(mLayoutManager);

        GetTopicList();


        // Implementing Click Listener on RecyclerView.
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

                    topic_id=Topicid.get(RecyclerViewItemPosition).getId();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();
                    TopicListFragment mfragment=new TopicListFragment();

                    Bundle bundle=new Bundle();
                    bundle.putString("topic_id",topic_id);
                    bundle.putString("todayDate",todayDate);
                    mfragment.setArguments(bundle);

                    transection.replace(R.id.content_frame, mfragment);
                    transection.addToBackStack(null).commit();


//                    Toast.makeText(getActivity(), "You clicked " + topic_id, Toast.LENGTH_SHORT).show();


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


    public void GetTopicList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetTopics",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    DataAdapter1 GetDataAdapter2=new DataAdapter1();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setImageTitle(jsonObject1.getString("topic_name"));
                                    GetDataAdapter2.setImageUrl(jsonObject1.getString("image"));



                                    Topicid.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }

                                Collections.reverse(ListOfdataAdapter);
                                Collections.reverse(Topicid);

                                adapter = new RecyclerViewAdapter1(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);


                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();


                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
//                            Toast.makeText(getActivity(), "Time Out",Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            //TODO
                        } else if (volleyError instanceof ServerError) {
                            //TODO
                        } else if (volleyError instanceof NetworkError) {
                            //TODO
                        } else if (volleyError instanceof ParseError) {
                            //TODO
                        }

                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
