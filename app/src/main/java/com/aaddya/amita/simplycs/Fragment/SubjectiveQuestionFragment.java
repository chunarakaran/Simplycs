package com.aaddya.amita.simplycs.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Activity.LoginActivity;
import com.aaddya.amita.simplycs.Activity.MainActivity;
import com.aaddya.amita.simplycs.Activity.PDFViewActivity;
import com.aaddya.amita.simplycs.Activity.QuestionUploaderActivity;
import com.aaddya.amita.simplycs.Activity.SignupActivity;
import com.aaddya.amita.simplycs.Adapter.PDF_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.SubCategory_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.SubjectiveQuestion_List_Adapter;
import com.aaddya.amita.simplycs.Model.GetCourse_Model_List;
import com.aaddya.amita.simplycs.Model.GetLanguage_Model_List;
import com.aaddya.amita.simplycs.Model.PDF_Model_List;
import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Model.SubjectiveQues_Model_List;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.refactor.lib.colordialog.PromptDialog;


public class SubjectiveQuestionFragment extends Fragment
{

    TextView Submit;
    List<SubjectiveQues_Model_List> ListOfdataAdapter;
    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String CDate,Category_id,SubCategory_id,Test_id, test_name, test_duration, test_marks,Sub_Question_url;
    String User_id;
    public static final String PREFS_NAME = "login";

    Spinner LanguageSpinner;
    final ArrayList<GetLanguage_Model_List> Languagedatalist = new ArrayList<>();
    String Languageid;

    final ArrayList<SubjectiveQues_Model_List> Sub_Question_id = new ArrayList<>();
    final ArrayList<SubjectiveQues_Model_List> Sub_Question = new ArrayList<>();
    final ArrayList<SubjectiveQues_Model_List> Sub_Question_scale = new ArrayList<>();
    final ArrayList<SubjectiveQues_Model_List> Sub_Question_explanation = new ArrayList<>();
    final ArrayList<SubjectiveQues_Model_List> Sub_Question_dropzone = new ArrayList<>();
    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    TextView txt_timer;
    public static int minutes = 0;

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
        rootview = inflater.inflate(R.layout.fragment_subjectivequestion, container, false);

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

        //volley
        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(getActivity());

        LanguageSpinner=rootview.findViewById(R.id.course_spinner);
        Submit=rootview.findViewById(R.id.btn_submit);

        Bundle bundle = getArguments();
        Category_id=String.valueOf(bundle.getString("category_id"));
        SubCategory_id=String.valueOf(bundle.getString("SubCategory_id"));
        CDate=String.valueOf(bundle.getString("CDate"));
        Test_id = String.valueOf(bundle.getString("test_id"));
        test_name = String.valueOf(bundle.getString("test_name"));
        test_duration = String.valueOf(bundle.getString("test_duration"));
        test_marks = String.valueOf(bundle.getString("test_marks"));
        txt_timer = (TextView) rootview.findViewById(R.id.txt_timer);
        //txt_timer.setText(exam_duration +"Minutes");
        minutes = Integer.parseInt(test_duration);

        toolbar.setTitle(test_name);

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview1);
        emptyView=(LinearLayout)rootview.findViewById(R.id.empty_view);
        recyclerView.setHasFixedSize(true);


        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);


        GetSubjectiveTestQuestionList();

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

                    Sub_Question_url=Sub_Question_dropzone.get(RecyclerViewItemPosition).getDropzone();

                    startActivity(new Intent(getActivity(), QuestionUploaderActivity.class)
                            .putExtra("Sub_Question_url", Sub_Question_url));

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

        LanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id)
            {
//                com_name=   statespinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Languageid = Languagedatalist.get(position).getId();
//                Toast.makeText(getActivity(),"Id   " +Languageid , Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Submit Test Because The Test is Only One Attempt ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SubmitSubjectiveTest();
//
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


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


    public void GetSubjectiveTestQuestionList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/SubjectiveTestQuestionList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        final ArrayList<String> list = new ArrayList<>();

                        list.clear();

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    SubjectiveQues_Model_List subjectiveQues_model_list=new SubjectiveQues_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    int srno = i+1;
                                    subjectiveQues_model_list.setId(jsonObject1.getString("id"));
                                    subjectiveQues_model_list.setQuestion("Q-" + srno + " "+ jsonObject1.getString("question"));
                                    subjectiveQues_model_list.setScale(jsonObject1.getString("scale"));
                                    subjectiveQues_model_list.setExplanation(jsonObject1.getString("explanation"));
                                    subjectiveQues_model_list.setDropzone(jsonObject1.getString("dropzone"));

                                    Sub_Question_id.add(subjectiveQues_model_list);
                                    Sub_Question.add(subjectiveQues_model_list);
                                    Sub_Question_scale.add(subjectiveQues_model_list);
                                    Sub_Question_explanation.add(subjectiveQues_model_list);
                                    Sub_Question_dropzone.add(subjectiveQues_model_list);

                                    ListOfdataAdapter.add(subjectiveQues_model_list);
                                }

                                JSONArray jsonArray1=jObj.getJSONArray("languages");
                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    GetLanguage_Model_List getLanguage_model_list=new GetLanguage_Model_List();
                                    JSONObject jsonObject2=jsonArray1.getJSONObject(i);


                                    getLanguage_model_list.setId(jsonObject2.getString("id"));
                                    getLanguage_model_list.setLanguageName(jsonObject2.getString("language"));



                                    Languagedatalist.add(getLanguage_model_list);

                                    list.add(jsonObject2.getString("language"));

                                }


                                LanguageSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list));

                                Collections.reverse(ListOfdataAdapter);
                                Collections.reverse(Sub_Question_id);

                                adapter = new SubjectiveQuestion_List_Adapter(ListOfdataAdapter,getActivity());
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
                params.put("TestId", Test_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }

    public void SubmitSubjectiveTest()
    {
        pDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        showDialog();



        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/SubmitSubjectiveTest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        hideDialog();

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String status = jObj.getString("status");

                            if(status.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Success");
                                promptDialog.setContentText(jObj.getString("message"));
                                promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        dialog.dismiss();
                                    }
                                }).show();



                            }
                            else {

                                JSONObject error = jObj.getJSONObject("error");
                                String msg = String.valueOf(error.getJSONArray("message"));

                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Server Error" , Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        hideDialog();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
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

                //Company
                params.put("TestId", Test_id);
                params.put("LanguageId", Languageid);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);

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
