package com.aaddya.amita.simplycs.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.MainActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Adapter.Quetion_Adapter;
import com.aaddya.amita.simplycs.Adapter.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExamFragment extends Fragment
{

    private RecyclerView mRecyclerView;
    private Quetion_Adapter mAdapter;
    List<Quetion_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "test_questions";


    TextView txt_timer;
    LinearLayout empty_view;
    String exam_id,exam_title,exam_duration;

    Button btn_Previous,btn_Next,btn_submit;

    String Test_id,test_name,test_duration,test_marks;
    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_start_exam, container, false);

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
        URL = getString(R.string.url);

        Bundle bundle=getArguments();
        Test_id=String.valueOf(bundle.getString("test_id"));
        test_name=String.valueOf(bundle.getString("test_name"));
        test_duration=String.valueOf(bundle.getString("test_duration"));
        test_marks=String.valueOf(bundle.getString("test_marks"));

        toolbar.setTitle(test_name);

        empty_view  = (LinearLayout)rootview.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.HORIZONTAL,false);
        SnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        snapHelper.attachToRecyclerView(mRecyclerView);

        btn_submit = (Button)rootview.findViewById(R.id.btn_submit);
        btn_Previous = (Button)rootview.findViewById(R.id.btn_Previous);
        btn_Next = (Button)rootview.findViewById(R.id.btn_Next);
        // btn_Previous.setVisibility(View.GONE);
        txt_timer=(TextView)rootview.findViewById( R.id.txt_timer);
        //txt_timer.setText(exam_duration +"Minutes");
        int minutes = Integer.parseInt(test_duration);


        new CountDownTimer(60*minutes*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_timer.setText(hms);//set text
            }
            public void onFinish() {
                txt_timer.setText("TIME'S UP!!"); //On finish change timer text
            }
        }.start();


        rowListItem=new ArrayList<Quetion_Model_List>();
        get_Questions();


        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.getLayoutManager().scrollToPosition(gridLayoutManager.findFirstVisibleItemPosition() - 1);
//                  btn_Next.setVisibility(View.VISIBLE);
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.getLayoutManager().scrollToPosition(gridLayoutManager.findLastVisibleItemPosition() + 1);
//                 btn_Previous.setVisibility(View.VISIBLE);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            }
        });


        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // DO WHAT YOU WANT ON BACK PRESSED
//                    getFragmentManager().popBackStack();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Submit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

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

    public void get_Questions(){
        StringRequest stringRequest = new StringRequest( Request.Method.POST, URL+"api/TestQuestionList",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            getCategory(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("TestId",Test_id);


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private void getCategory(JSONArray j){
        for(int i=0;i<j.length();i++){
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Quetion_Model_List(json.getString("id"),
                        json.getString("question"),
//                        json.getString("type"),
                        json.getString("option1"),
                        json.getString("option2"),
                        json.getString("option3"),
                        json.getString("option4"),
                        json.getString("correct_answer")
//                        json.getString("question_image"),
//                        json.getString("op1_image"),
//                        json.getString("op2_image"),
//                        json.getString("op3_image")
//                        json.getString("op4_image")

                ) );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0){

            mAdapter =new Quetion_Adapter(getActivity(),rowListItem);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else{
            empty_view.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }



    }


}
