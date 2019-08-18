package com.aaddya.amita.simplycs.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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
import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Adapter.SubCategory_List_Adapter;
import com.aaddya.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class SubCategoryListFragment extends Fragment
{



    List<SubCategory_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String Category_id,Category_name;
    String SubCategory_id,SubCategory_name;
    String CDate;
    final ArrayList<SubCategory_Model_List> SubCategoryid = new ArrayList<>();
    final ArrayList<SubCategory_Model_List> SubCategoryName = new ArrayList<>();
    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id,Course_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    PromptDialog promptDialog;


    View rootview;
    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_subcategorylist, container, false);

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
        CDate=String.valueOf(bundle.getString("Date"));
        Category_name=String.valueOf(bundle.getString("Category_name"));

        toolbar.setTitle(Category_name);



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



        if(isNetworkAvailable()){
            GetSubCategoryList();
        }
        else {
            promptDialog.setCancelable(false);
            promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WARNING);
            promptDialog.setAnimationEnable(true);
            promptDialog.setTitleText("Connection Failed");
            promptDialog.setContentText("Please Check Your Internet Connection");
            promptDialog.setPositiveListener("Setting", new PromptDialog.OnPositiveListener() {
                @Override
                public void onClick(PromptDialog dialog) {
                    Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS );
                    startActivity(intent);
                    getActivity().finish();
                }
            }).show();

        }



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

                    SubCategory_id=SubCategoryid.get(RecyclerViewItemPosition).getId();
                    SubCategory_name=SubCategoryName.get(RecyclerViewItemPosition).getImageTitle();

                    if(Category_name.equals("Practice")||Category_name.equals("practice"))
                    {
                        FragmentTransaction transection=getFragmentManager().beginTransaction();
                        TestListFragment mfragment=new TestListFragment();

                        Bundle bundle=new Bundle();
                        bundle.putString("category_id",Category_id);
                        bundle.putString("SubCategory_id",SubCategory_id);
                        bundle.putString("CDate",CDate);
                        bundle.putString("SubCategory_name",SubCategory_name);
                        mfragment.setArguments(bundle);

                        transection.replace(R.id.content_frame, mfragment);
                        transection.addToBackStack(null).commit();

                    }
                    else
                    {
                        FragmentTransaction transection=getFragmentManager().beginTransaction();
                        TheoryListFragment mfragment=new TheoryListFragment();

                        Bundle bundle=new Bundle();
                        bundle.putString("category_id",Category_id);
                        bundle.putString("SubCategory_id",SubCategory_id);
                        bundle.putString("CDate",CDate);
                        bundle.putString("SubCategory_name",SubCategory_name);
                        mfragment.setArguments(bundle);

                        transection.replace(R.id.content_frame, mfragment);
                        transection.addToBackStack(null).commit();

                    }

//                    Toast.makeText(getActivity(), "You clicked " + Category_name, Toast.LENGTH_SHORT).show();






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

    public void GetSubCategoryList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetSubCategory",
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
                                    SubCategory_Model_List subCategory_model_list=new SubCategory_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    subCategory_model_list.setId(jsonObject1.getString("id"));
                                    subCategory_model_list.setImageTitle(jsonObject1.getString("subtopic_name"));
                                    subCategory_model_list.setImageUrl(jsonObject1.getString("image"));
                                    subCategory_model_list.setPaidStatus(jsonObject1.getString("is_paid"));




                                    SubCategoryid.add(subCategory_model_list);

                                    SubCategoryName.add(subCategory_model_list);


                                    ListOfdataAdapter.add(subCategory_model_list);

                                }

//                                Collections.reverse(ListOfdataAdapter);
//                                Collections.reverse(Topicid);

                                adapter = new SubCategory_List_Adapter(ListOfdataAdapter,getActivity());
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
