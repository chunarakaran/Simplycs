package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.Adapter.DataAdapter;
import com.example.amita.simplycs.Adapter.RecyclerViewAdapter;
import com.example.amita.simplycs.Database.DatabaseHelper;
import com.example.amita.simplycs.Database.SectionTable;
import com.example.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopicListFragment extends Fragment
{

    private DatabaseHelper db;
    private ProgressDialog pDialog;

    List<DataAdapter> ListOfdataAdapter;

    RecyclerView recyclerView;

    String orderid;
    final ArrayList<DataAdapter> Orderid = new ArrayList<>();
    int RecyclerViewItemPosition ;
    LinearLayoutManager layoutManagerOfrecyclerView;

    RecyclerView.Adapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_topiclist, container, false);

//        getActivity().onBackPressed();

        db = new DatabaseHelper(getActivity());

        String Url = getResources().getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        ImageTitleNameArrayListForClick = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        String HTTP_JSON_URL = "https://api.androidhive.info/contacts/";

        JSON_HTTP_CALL(HTTP_JSON_URL);

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

                    orderid=Orderid.get(RecyclerViewItemPosition).getId();

//                    FragmentTransaction transection=getFragmentManager().beginTransaction();
//                    OrderDetailFragment mfragment=new OrderDetailFragment();
//
//                    Bundle bundle=new Bundle();
//                    bundle.putString("orderid",orderid);
//                    mfragment.setArguments(bundle);
//
//                    transection.replace(R.id.content_frame, mfragment);
//                    transection.addToBackStack(null).commit();


                    Toast.makeText(getActivity(), "You clicked " + orderid, Toast.LENGTH_SHORT).show();


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

//    private void createNote(String note) {
//        // inserting note in db and getting
//        // newly inserted note id
//        long id = db.insertNote(note);
//
//        // get the newly inserted note from db
//        SectionTable n = db.getNote(id);
//
//        if (n != null) {
//            // adding new note to array list at 0 position
//            notesList.add(0, n);
//
//            // refreshing the list
//            mAdapter.notifyDataSetChanged();
//
//        }
//    }

    private void JSON_HTTP_CALL(String url)
    {

        pDialog.setMessage("Please Wait ...");
        showDialog();

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("contacts");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        DataAdapter GetDataAdapter2 = new DataAdapter();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);


                        GetDataAdapter2.setId(jsonObject1.getString("id"));
                        GetDataAdapter2.setImageTitle(jsonObject1.getString("name"));

                        Orderid.add(GetDataAdapter2);

                        ListOfdataAdapter.add(GetDataAdapter2);
                    }

                    recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter,getActivity());
                    recyclerView.setAdapter(recyclerViewadapter);

                    hideDialog();
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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
