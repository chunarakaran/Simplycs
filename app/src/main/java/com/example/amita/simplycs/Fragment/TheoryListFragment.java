package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.Adapter.TheoryListDataAdapter;
import com.example.amita.simplycs.Adapter.TheoryListRecyclerViewAdapter;
import com.example.amita.simplycs.R;
import com.example.amita.simplycs.TheoryViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class TheoryListFragment extends Fragment
{

    List<TheoryListDataAdapter> ListOfdataAdapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String CDate,Topic_id,SubTopic_id,content_id,content_Data;

    final ArrayList<TheoryListDataAdapter> Contentid = new ArrayList<>();

    final ArrayList<TheoryListDataAdapter> ContentData = new ArrayList<>();

    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    PromptDialog promptDialog;



    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_theorylist, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");


        Bundle bundle=getArguments();
        Topic_id=String.valueOf(bundle.getString("topic_id"));
        SubTopic_id=String.valueOf(bundle.getString("subTopic_id"));
        CDate=String.valueOf(bundle.getString("CDate"));


        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(getActivity());

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        GetContentList();


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

                    content_id=Contentid.get(RecyclerViewItemPosition).getId();

                    content_Data=ContentData.get(RecyclerViewItemPosition).getContentData();


                    startActivity(new Intent(getActivity(), TheoryViewActivity.class)
                            .putExtra("content_Data", content_Data));


//                    Toast.makeText(getActivity(), "You clicked " + content_id, Toast.LENGTH_SHORT).show();


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





        return rootview;
    }


    public void GetContentList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetTopicContent",
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
                                    TheoryListDataAdapter GetDataAdapter2=new TheoryListDataAdapter();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("content_name"));
                                    GetDataAdapter2.setContentData(jsonObject1.getString("content_data"));




                                    Contentid.add(GetDataAdapter2);

                                    ContentData.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }


                                adapter = new TheoryListRecyclerViewAdapter(ListOfdataAdapter,getActivity());
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
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("TopicId", Topic_id);
                params.put("SubTopicId", SubTopic_id);
                params.put("Date", CDate);

                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }




    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

        return isConnected;
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
