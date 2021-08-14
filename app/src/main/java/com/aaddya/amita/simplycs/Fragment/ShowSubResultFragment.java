package com.aaddya.amita.simplycs.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.aaddya.amita.simplycs.Activity.QuestionUploaderActivity;
import com.aaddya.amita.simplycs.Activity.ShowSubResultActivity;
import com.aaddya.amita.simplycs.Adapter.ShowResult_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.Test_List_Adapter;
import com.aaddya.amita.simplycs.Model.SubjectiveResult_Model_List;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;


public class ShowSubResultFragment extends Fragment
{

    List<SubjectiveResult_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String User_id;
    public static final String PREFS_NAME = "login";

    String test_id,test_name,question_id,result_question,result_audio,result_comment,result_mark,is_check,result_question_id;

    final ArrayList<SubjectiveResult_Model_List> Question_id = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Result_question = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Result_audio = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Result_comment = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Result_mark = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Result_question_id = new ArrayList<>();
    final ArrayList<SubjectiveResult_Model_List> Is_check = new ArrayList<>();

    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

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
        rootview = inflater.inflate(R.layout.fragment_showsubressult, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle=getArguments();
        test_id=String.valueOf(bundle.getString("test_id"));
        test_name=String.valueOf(bundle.getString("test_name"));

        toolbar.setTitle(test_name);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

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

        GetSubjectiveTestResult();

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

                    question_id=Question_id.get(RecyclerViewItemPosition).getId();
                    result_question=Result_question.get(RecyclerViewItemPosition).getQuestion();
                    result_audio=Result_audio.get(RecyclerViewItemPosition).getResult_audio();
                    result_comment=Result_comment.get(RecyclerViewItemPosition).getResult_comment();
                    result_mark=Result_mark.get(RecyclerViewItemPosition).getResult_mark();
                    result_question_id=Result_question_id.get(RecyclerViewItemPosition).getResult_question_id();
                    is_check=Is_check.get(RecyclerViewItemPosition).getIs_check();

                    if (is_check.equalsIgnoreCase("1")) {

                        startActivity(new Intent(getActivity(), ShowSubResultActivity.class)
                                .putExtra("result_question", result_question)
                                .putExtra("result_audio", result_audio)
                                .putExtra("result_comment", result_comment)
                                .putExtra("result_mark", result_mark)
                                .putExtra("result_question_id", result_question_id));

                    } else {

                        promptDialog.setCancelable(false);
                        promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_INFO);
                        promptDialog.setAnimationEnable(true);
                        promptDialog.setTitleText("Information");
                        promptDialog.setContentText("Result Pending");
                        promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();

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


    public void GetSubjectiveTestResult()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetSubjectiveTestResult",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("subjective_test_result");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    SubjectiveResult_Model_List subjectiveResult_model_list=new SubjectiveResult_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    int srno = i+1;
                                    subjectiveResult_model_list.setId(jsonObject1.getString("id"));
                                    subjectiveResult_model_list.setQuestion("Q-" + srno + " "+ jsonObject1.getString("result_question"));
                                    subjectiveResult_model_list.setResult_audio(jsonObject1.getString("result_audio"));
                                    subjectiveResult_model_list.setResult_comment(jsonObject1.getString("result_comment"));
                                    subjectiveResult_model_list.setResult_mark(jsonObject1.getString("result_mark"));
                                    subjectiveResult_model_list.setResult_question_id(jsonObject1.getString("result_question_id"));
                                    subjectiveResult_model_list.setIs_check(jsonObject1.getString("is_check"));




                                    Question_id.add(subjectiveResult_model_list);
                                    Result_question.add(subjectiveResult_model_list);
                                    Result_audio.add(subjectiveResult_model_list);
                                    Result_comment.add(subjectiveResult_model_list);
                                    Result_mark.add(subjectiveResult_model_list);
                                    Result_question_id.add(subjectiveResult_model_list);
                                    Is_check.add(subjectiveResult_model_list);

                                    ListOfdataAdapter.add(subjectiveResult_model_list);

                                }

//                                Collections.reverse(ListOfdataAdapter);
//                                Collections.reverse(Question_id);

                                adapter = new ShowResult_List_Adapter(ListOfdataAdapter,getActivity());
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
                params.put("TestId", test_id);

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
