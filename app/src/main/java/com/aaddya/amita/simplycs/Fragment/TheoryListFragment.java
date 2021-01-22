package com.aaddya.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Model.Theory_Model_List;
import com.aaddya.amita.simplycs.Adapter.Theory_List_Adapter;
import com.aaddya.amita.simplycs.R;

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

    List<Theory_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String CDate,Category_id,SubCategory_id,SubCategory_name,content_id,content_name,content_Data;

    final ArrayList<Theory_Model_List> Contentid = new ArrayList<>();

    final ArrayList<Theory_Model_List> ContentData = new ArrayList<>();

    final ArrayList<Theory_Model_List> ContentName = new ArrayList<>();

    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id,Course_id;
    public static final String PREFS_NAME = "login";


    String PremiumUser;

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

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        Course_id = sp.getString("Courseid", "");


        Bundle bundle=getArguments();
        Category_id=String.valueOf(bundle.getString("category_id"));
        SubCategory_id=String.valueOf(bundle.getString("SubCategory_id"));
        SubCategory_name=String.valueOf(bundle.getString("SubCategory_name"));
        CDate=String.valueOf(bundle.getString("CDate"));

        toolbar.setTitle(SubCategory_name);


        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(getActivity());

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview1);
        emptyView=(LinearLayout)rootview.findViewById(R.id.empty_view);
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

                    content_name=ContentName.get(RecyclerViewItemPosition).getTitle();

                    content_Data=ContentData.get(RecyclerViewItemPosition).getContentData();


                    FragmentTransaction transection=getFragmentManager().beginTransaction();
                    ContentFragment mfragment=new ContentFragment();

                    Bundle bundle=new Bundle();
                    bundle.putString("category_id",Category_id);
                    bundle.putString("SubCategory_id",SubCategory_id);
                    bundle.putString("SubCategory_name",SubCategory_name);
                    bundle.putString("CDate",CDate);
                    bundle.putString("content_id",content_id);
                    bundle.putString("content_Data",content_Data);
                    bundle.putString("content_name",content_name);
                    bundle.putString("PremiumUser",PremiumUser);
                    mfragment.setArguments(bundle);

                    transection.replace(R.id.content_frame, mfragment);
                    transection.addToBackStack(null).commit();


//                    startActivity(new Intent(getActivity(), TheoryViewActivity.class)
//                            .putExtra("content_Data", content_Data)
//                            .putExtra("content_name",content_name));


//                    Toast.makeText(getActivity(), "You clicked " + PremiumUser, Toast.LENGTH_SHORT).show();


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

    public void GetContentList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetContent",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");
                            PremiumUser=jObj.getString("is_premium_user");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("new_content");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    Theory_Model_List GetDataAdapter2=new Theory_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("content_name"));
                                    GetDataAdapter2.setContentData(jsonObject1.getString("content_data"));




                                    Contentid.add(GetDataAdapter2);
                                    ContentName.add(GetDataAdapter2);

                                    ContentData.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }

                                JSONArray jsonArray1=jObj.getJSONArray("exist_content");
                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    Theory_Model_List GetDataAdapter2=new Theory_Model_List();
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("content_name"));
                                    GetDataAdapter2.setContentData(jsonObject1.getString("content_data"));




                                    Contentid.add(GetDataAdapter2);
                                    ContentName.add(GetDataAdapter2);

                                    ContentData.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }



                                adapter = new Theory_List_Adapter(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);

                                if(adapter.getItemCount()==0)
                                {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                                else{
                                    recyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                }

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
                params.put("CourseId", Course_id);
                params.put("CategoryId", Category_id);
                params.put("SubCategoryId", SubCategory_id);
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
