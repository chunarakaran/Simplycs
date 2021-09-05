package com.aaddya.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import com.aaddya.amita.simplycs.Adapter.SubjectiveTest_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.Test_List_Adapter;
import com.aaddya.amita.simplycs.Model.SubjectiveTest_Model_List;
import com.aaddya.amita.simplycs.Model.Test_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class SubjectiveTestListFragment extends Fragment
{

    List<SubjectiveTest_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String CDate,Category_id,Category_name,SubCategory_id,SubCategory_name,test_id,test_name,test_rules,is_Completed,total_question;

    final ArrayList<SubjectiveTest_Model_List> Testid = new ArrayList<>();
    final ArrayList<SubjectiveTest_Model_List> Testname = new ArrayList<>();
    final ArrayList<SubjectiveTest_Model_List> Testrules = new ArrayList<>();
    final ArrayList<SubjectiveTest_Model_List> IsCompleted = new ArrayList<>();
    final ArrayList<SubjectiveTest_Model_List> Total_question = new ArrayList<>();

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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_subjectivetestlist, container, false);

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
        Category_name=String.valueOf(bundle.getString("Category_name"));
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

        GetSubjectiveTestList();


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

                    test_id=Testid.get(RecyclerViewItemPosition).getId();
                    test_name=Testname.get(RecyclerViewItemPosition).getTitle();
                    test_rules=Testrules.get(RecyclerViewItemPosition).getRules();
                    is_Completed=IsCompleted.get(RecyclerViewItemPosition).getIsComplete();



                    if (is_Completed.equalsIgnoreCase("1")) {
                            FragmentTransaction transection = getFragmentManager().beginTransaction();
                            ShowSubResultFragment mfragment = new ShowSubResultFragment();

                            Bundle bundle = new Bundle();

                            bundle.putString("test_id", test_id);
                            bundle.putString("test_name", test_name);


                            mfragment.setArguments(bundle);

                            transection.replace(R.id.content_frame, mfragment);
                            transection.addToBackStack(null).commit();

                    } else {

                            FragmentTransaction transection = getFragmentManager().beginTransaction();
                            StartSubjectiveTestFragment mfragment = new StartSubjectiveTestFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("category_id", Category_id);
                            bundle.putString("SubCategory_id", SubCategory_id);
                            bundle.putString("SubCategory_name", SubCategory_name);
                            bundle.putString("CDate", CDate);

                            bundle.putString("test_id", test_id);
                            bundle.putString("test_name", test_name);
                            bundle.putString("test_rules", test_rules);

                            mfragment.setArguments(bundle);

                            transection.replace(R.id.content_frame, mfragment);
                            transection.addToBackStack(null).commit();

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



    public void GetSubjectiveTestList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetSubjectiveTestList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("test_list");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    SubjectiveTest_Model_List subjectiveTest_model_list=new SubjectiveTest_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    subjectiveTest_model_list.setId(jsonObject1.getString("id"));
                                    subjectiveTest_model_list.setTitle(jsonObject1.getString("test_name"));
                                    subjectiveTest_model_list.setRules(jsonObject1.getString("rules"));
                                    subjectiveTest_model_list.setIsComplete(jsonObject1.getString("is_complete"));
                                    subjectiveTest_model_list.setTotal_question(jsonObject1.getString("total_question"));




                                    Testid.add(subjectiveTest_model_list);
                                    Testname.add(subjectiveTest_model_list);
                                    Testrules.add(subjectiveTest_model_list);
                                    IsCompleted.add(subjectiveTest_model_list);
                                    Total_question.add(subjectiveTest_model_list);

                                    ListOfdataAdapter.add(subjectiveTest_model_list);

                                }


                                adapter = new SubjectiveTest_List_Adapter(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);



                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
//                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                adapter = new SubjectiveTest_List_Adapter(ListOfdataAdapter,getActivity());
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




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}