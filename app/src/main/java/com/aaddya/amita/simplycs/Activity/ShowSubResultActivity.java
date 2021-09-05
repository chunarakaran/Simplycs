package com.aaddya.amita.simplycs.Activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Adapter.MarksDetails_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.SubCategory_List_Adapter;
import com.aaddya.amita.simplycs.Model.SubCategory_Model_List;
import com.aaddya.amita.simplycs.Model.SubjectiveMarksDetail_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.keenfin.audioview.AudioView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowSubResultActivity extends AppCompatActivity  {

    List<SubjectiveMarksDetail_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String mark_id,mark_title,given_mark;

    final ArrayList<SubjectiveMarksDetail_Model_List> Mark_id = new ArrayList<>();
    final ArrayList<SubjectiveMarksDetail_Model_List> Mark_title = new ArrayList<>();
    final ArrayList<SubjectiveMarksDetail_Model_List> Given_mark = new ArrayList<>();
    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id,Course_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    TextView Done_Button;

    String result_question,result_audio,result_comment,result_mark,result_question_id;

    AudioView audioView;

    TextView Result_comment,Result_mark,AudioSuggestion;

    public AudioViewListener mAudioViewListener;



    public interface AudioViewListener {
        void onPrepared();
        void onCompletion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsubresult);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        result_question = getIntent().getStringExtra("result_question");
        result_audio = getIntent().getStringExtra("result_audio");
        result_comment = getIntent().getStringExtra("result_comment");
        result_question_id = getIntent().getStringExtra("result_question_id");
        result_mark = getIntent().getStringExtra("result_mark");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Initialize();

        GetSubjectiveTestResultMarks();

        if (result_comment.equals("null")){
            Result_comment.setText("No Comment");
        }
        else {
            Result_comment.setText(result_comment);
        }

        Result_mark.setText(result_mark);

        if(result_audio.equals("null")){

            AudioSuggestion.setVisibility(View.VISIBLE);
        }
        else {

            try {
                audioView.setDataSource(result_audio);
                audioView.start();
            } catch (IOException p) {
                p.printStackTrace();
            }

            audioView.setOnAudioViewListener(new com.keenfin.audioview.AudioViewListener() {
                @Override
                public void onPrepared() {
//                pDialog.setMessage("Please Wait...");
//                showDialog();
                }

                @Override
                public void onCompletion() {
//                hideDialog();
                }
            });
        }

        Done_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void Initialize()
    {
        audioView=(AudioView)findViewById(R.id.audioview);
        Result_comment=(TextView)findViewById(R.id.result_comment);
        Result_mark=(TextView)findViewById(R.id.result_mark);
        AudioSuggestion=(TextView)findViewById(R.id.AudioSuggestion);
        Done_Button=(TextView)findViewById(R.id.done_button);

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getApplicationContext());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onStop() {
        super.onPause();
        audioView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioView.pause();
    }

    public void setOnAudioViewListener(AudioViewListener audioViewListener) {
        mAudioViewListener = audioViewListener;
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed()
    {
        onStop();
        finish();
    }


    public void GetSubjectiveTestResultMarks()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetSubjectiveTestResultMarks",
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
                                    SubjectiveMarksDetail_Model_List subjectiveMarksDetail_model_list=new SubjectiveMarksDetail_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    subjectiveMarksDetail_model_list.setId(jsonObject1.getString("id"));
                                    subjectiveMarksDetail_model_list.setMark_title(jsonObject1.getString("mark_title"));
                                    subjectiveMarksDetail_model_list.setGiven_mark(jsonObject1.getString("given_mark"));




                                    Mark_id.add(subjectiveMarksDetail_model_list);
                                    Mark_title.add(subjectiveMarksDetail_model_list);
                                    Given_mark.add(subjectiveMarksDetail_model_list);

                                    ListOfdataAdapter.add(subjectiveMarksDetail_model_list);

                                }

//                                Collections.reverse(ListOfdataAdapter);
//                                Collections.reverse(Topicid);

                                adapter = new MarksDetails_List_Adapter(ListOfdataAdapter,getApplicationContext());
                                recyclerView.setAdapter(adapter);



                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
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
                params.put("result_question_id", result_question_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }


}

