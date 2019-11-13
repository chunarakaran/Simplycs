package com.aaddya.amita.simplycs.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Activity.TestResultActivity;
import com.aaddya.amita.simplycs.Adapter.Quetion_List_Adapter;
import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestionFragment extends Fragment {

    // private RecyclerView mRecyclerView;
    private Quetion_List_Adapter mAdapter;
    public static List<Quetion_Model_List> rowListItem;
    private JSONArray result;
    public static final String JSON_ARRAY = "data";

    final ArrayList<Quetion_Model_List> Scale_list = new ArrayList<>();

    TextView txt_timer;
    LinearLayout empty_view;


    Button btn_Previous, btn_Next, btn_submit;

    String CDate,Category_id,SubCategory_id,Test_id, test_name, test_duration, test_marks;
    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";

    View rootview;
    public int correctAns = 0, wrongAns = 0, skippAns = 0;
    public String strCorrectAns = "", strWrongAns = "", strSkippAns = "";
    public ArrayList<Quetion_Model_List> correctAnsArr = new ArrayList<Quetion_Model_List>();
    public ArrayList<Quetion_Model_List> wrongAnsArr = new ArrayList<Quetion_Model_List>();
    public ArrayList<Quetion_Model_List> skipAnsArr = new ArrayList<Quetion_Model_List>();
    public static int currentPos = 0;
    public int performanceIndex;
    public static int minutes = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_question, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        URL = getString(R.string.url);


        Bundle bundle = getArguments();
        Category_id=String.valueOf(bundle.getString("category_id"));
        SubCategory_id=String.valueOf(bundle.getString("SubCategory_id"));
        CDate=String.valueOf(bundle.getString("CDate"));
        Test_id = String.valueOf(bundle.getString("test_id"));
        test_name = String.valueOf(bundle.getString("test_name"));
        test_duration = String.valueOf(bundle.getString("test_duration"));
        test_marks = String.valueOf(bundle.getString("test_marks"));

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getFragmentManager().popBackStack();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              /*  Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();*/

                                Submit();
                                //Log.i("Questions", "Questions Selected Answer Correct is : " + correctAns + " & Wrong Answer : " + wrongAns + " & Skipp Answer : " + skippAns);
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


        toolbar.setTitle(test_name);

        empty_view = (LinearLayout) rootview.findViewById(R.id.empty_view);
        //mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        // mRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //snapHelper.attachToRecyclerView(mRecyclerView);

        btn_submit = (Button) rootview.findViewById(R.id.btn_submit);
        btn_Previous = (Button) rootview.findViewById(R.id.btn_Previous);
        btn_Next = (Button) rootview.findViewById(R.id.btn_Next);
        // btn_Previous.setVisibility(View.GONE);
        txt_timer = (TextView) rootview.findViewById(R.id.txt_timer);
        //txt_timer.setText(exam_duration +"Minutes");
        minutes = Integer.parseInt(test_duration);


        new CountDownTimer(60 * minutes * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_timer.setText(hms);//set text
            }

            public void onFinish() {
                txt_timer.setText("TIME'S UP!!"); //On finish change timer text
                Submit();
            }
        }.start();
        rowListItem = new ArrayList<Quetion_Model_List>();
        get_Questions();
        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPos > 0) {
                    rowListItem.get(currentPos).setIsStopTimer(0);
                    //  rowListItem.get(currentPos - 1).setIsStopTimer(1);
                    // mRecyclerView.getLayoutManager().scrollToPosition(currentPos - 1);
                    currentPos--;

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.question_frame, new TestQuestionsFragment());
                    transaction.commit();

                    if (currentPos == 0) {
                        btn_Previous.setVisibility(View.GONE);

                    } else {
                        btn_Next.setVisibility(View.VISIBLE);
                    }

                }


//                  btn_Next.setVisibility(View.VISIBLE);
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalPos = rowListItem.size() - 1;
                if (currentPos < totalPos) {
                    rowListItem.get(currentPos).setIsStopTimer(0);
                    //  rowListItem.get(currentPos + 1).setIsStopTimer(1);
                    //mRecyclerView.getLayoutManager().scrollToPosition(currentPos + 1);
                    currentPos++;
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.question_frame, new TestQuestionsFragment());
                    transaction.commit();
                    if (currentPos == totalPos) {
                        btn_Next.setVisibility(View.GONE);

                    } else {
                        btn_Previous.setVisibility(View.VISIBLE);
                    }
                }

//                 btn_Previous.setVisibility(View.VISIBLE);
                //  Toast.makeText(getActivity(), "Radio button clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              /*  Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();*/

                                Submit();
                                //Log.i("Questions", "Questions Selected Answer Correct is : " + correctAns + " & Wrong Answer : " + wrongAns + " & Skipp Answer : " + skippAns);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//                                FragmentTransaction transection=getFragmentManager().beginTransaction();
//                                TestListFragment mfragment=new TestListFragment();
//
//                                Bundle bundle=new Bundle();
//                                bundle.putString("category_id",Category_id);
//                                bundle.putString("SubCategory_id",SubCategory_id);
//                                bundle.putString("CDate",CDate);
////                                bundle.putString("SubCategory_name",SubCategory_name);
//                                mfragment.setArguments(bundle);
//
//                                transection.replace(R.id.content_frame, mfragment);
//                                transection.commit();
//
//                                getActivity().finish();
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
//                    getFragmentManager().popBackStack();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Submit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Submit();
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

    public void Submit() {
        for (int i = 0; i < rowListItem.size(); i++) {
            if (rowListItem.get(i).getSelectedAnswer() != null) {
                if (rowListItem.get(i).getSelectedAnswer().equalsIgnoreCase(rowListItem.get(i).getAnswer())) {
                    correctAns++;
                    correctAnsArr.add(rowListItem.get(i));
                } else {
                    wrongAns++;
                    wrongAnsArr.add(rowListItem.get(i));
                }
            } else {
                skippAns++;
                skipAnsArr.add(rowListItem.get(i));
            }
        }

        submitExam();
    }

    private void submitExam() {
        JSONArray jCorrectArr = new JSONArray();
        JSONArray jWrongArr = new JSONArray();
        JSONArray jSkippArr = new JSONArray();
        JSONArray jTimeTakeArr = new JSONArray();
        JSONArray jPerformanceMaxArr = new JSONArray();
        JSONArray jPerformanceScoreArr = new JSONArray();
        int totalPMax = 0;
        int totalPScore = 0;

        //Make Correct Answer Json Array
        if (correctAnsArr.size() > 0) {
            for (int i = 0; i < correctAnsArr.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("Ques_id", Integer.parseInt(correctAnsArr.get(i).getQuetion_id()));
                    jGroup.put("correct_answer", correctAnsArr.get(i).getAnswer());
                    jCorrectArr.put(jGroup);
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //Make Wrong Answer Json Array

        if (wrongAnsArr.size() > 0) {
            for (int i = 0; i < wrongAnsArr.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("Ques_id", Integer.parseInt(wrongAnsArr.get(i).getQuetion_id()));
                    jGroup.put("Incorrect_answer", wrongAnsArr.get(i).getAnswer());
                    jWrongArr.put(jGroup);
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //Make Skip Answer Json Array

        if (skipAnsArr.size() > 0) {
            for (int i = 0; i < skipAnsArr.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("Ques_id", Integer.parseInt(skipAnsArr.get(i).getQuetion_id()));
                    jGroup.put("Incorrect_answer", skipAnsArr.get(i).getAnswer());
                    jSkippArr.put(jGroup);
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //Make TimeTake Json Array
        if (rowListItem.size() > 0) {
            for (int i = 0; i < rowListItem.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("Ques_id", Integer.parseInt(rowListItem.get(i).getQuetion_id()));
                    jGroup.put("TimeTaken", rowListItem.get(i).getTimeTakeSec() + "");
                    jTimeTakeArr.put(jGroup);
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //Make PerformanceMax Json Array
        if (rowListItem.size() > 0) {
            for (int i = 0; i < rowListItem.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {

                    jGroup.put("Ques_id", Integer.parseInt(rowListItem.get(i).getQuetion_id()));
                    int Performance = Integer.parseInt(rowListItem.get(i).getScale());
                    int Second = rowListItem.get(i).getTimeTakeSec();
                    int totalPerformance = Performance * Second;
                    jGroup.put("Performance", totalPerformance + "");
                    jPerformanceMaxArr.put(jGroup);
                    totalPMax = totalPMax + totalPerformance;
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //Make PerformanceScore Json Array
        if (rowListItem.size() > 0) {
            for (int i = 0; i < rowListItem.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    jGroup.put("Ques_id", Integer.parseInt(rowListItem.get(i).getQuetion_id()));
                    int Performance = Integer.parseInt(rowListItem.get(i).getScale());
                    int Second = rowListItem.get(i).getTimeTakeSec();
                    int totalPerformance = Performance * Second;
                    if (rowListItem.get(i).getSelectedAnswer() != null) {
                        if (!rowListItem.get(i).getSelectedAnswer().equalsIgnoreCase(rowListItem.get(i).getAnswer())) {
                            totalPerformance = 0;
                        } else {
                            totalPerformance = totalPerformance;
                        }
                    } else {
                        totalPerformance = 0;
                    }
                    totalPScore = totalPScore + totalPerformance;
                    jGroup.put("Performance", totalPerformance + "");
                    jPerformanceScoreArr.put(jGroup);
                    // /itemDetail Name is JsonArray Name
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        submitTestToServer(jCorrectArr, jWrongArr, jSkippArr, jTimeTakeArr, jPerformanceMaxArr, jPerformanceScoreArr, totalPMax, totalPScore);
      /*  Intent i = new Intent(getActivity(), TestResultActivity.class);
        i.putExtra("rightans", correctAns + "");
        i.putExtra("skipans", skippAns + "");
        i.putExtra("wrongans", wrongAns + "");
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST", (Serializable) rowListItem);
        i.putExtra("BUNDLE", args);
        startActivity(i);*/

        Log.i("Answer", "Answer json Array : " + jCorrectArr.toString() + "\nWrong json Array : " + jWrongArr.toString() + "\nSkipp json Array : " + jSkippArr.toString() + "\nTimeTaken json Array : " + jTimeTakeArr.toString() + "\nPerformanceMax json Array : " + jPerformanceMaxArr.toString() + "\nPerformanceScore json Array : " + jPerformanceScoreArr.toString() + "\n total pMax:" + totalPMax + "\n total pscore:" + totalPScore);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    public void get_Questions() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "api/TestQuestionList",
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

                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TestId", Test_id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    private void getCategory(JSONArray j) {
        Log.i("Questions", "Question Array : " + j);
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                rowListItem.add(new Quetion_Model_List(json.getString("id"),
                        json.getString("question"),
                        json.getString("scale"),
                        json.getString("option1"),
                        json.getString("option2"),
                        json.getString("option3"),
                        json.getString("option4"),
                        json.getString("correct_answer")


                ));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (rowListItem.size() > 0) {

            mAdapter = new Quetion_List_Adapter(getActivity(), rowListItem);
            //mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            currentPos = 0;
            btn_Previous.setVisibility(View.GONE);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.question_frame, new TestQuestionsFragment());
            transaction.commit();


        } else {
            empty_view.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.GONE);
        }
    }

    //Submit Test Answer To Server
    public void submitTestToServer(JSONArray jCorrectArr, JSONArray jWrongArr, JSONArray jSkippArr, JSONArray jTimeTakeArr, JSONArray jPerformanceMaxArr, JSONArray jPerformanceScoreArr, int totalPMax, int totalPScore) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "api/TestResult",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("ServerResponse", "ServerResponse of Submit Test : " + response.toString());
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);

                            if (j.optString("success").equalsIgnoreCase("true")) {
                                Intent i = new Intent(getActivity(), TestResultActivity.class);
                                i.putExtra("rightans", correctAns + "");
                                i.putExtra("skipans", skippAns + "");
                                i.putExtra("wrongans", wrongAns + "");
                                i.putExtra("minutes", minutes);
                                i.putExtra("pindex", performanceIndex);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLIST", (Serializable) rowListItem);
                                i.putExtra("BUNDLE", args);
                                startActivity(i);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                Map<String, String> params = new HashMap<String, String>();
                performanceIndex = totalPScore * 100 / totalPMax;
                params.put("TestId", Test_id);
                params.put("CurrectAns", jCorrectArr + "");
                params.put("IncurrectAns", jWrongArr + "");
                params.put("UnattamptAns", jSkippArr + "");
                params.put("TimeTaken", jTimeTakeArr + "");

                params.put("PerformanceMax", jPerformanceMaxArr + "");
                params.put("PerformanceScore", jPerformanceScoreArr + "");
                params.put("TotalPerformanceMax", totalPMax + "");
                params.put("TotalPerformanceScore", totalPScore + "");
                params.put("PerformanceIndex", performanceIndex + "");


                params.put("TotalCurrectAns", correctAns + "");
                params.put("TotalIncurrectAns", wrongAns + "");
                params.put("TotalUnAttamptAns", skippAns + "");
                Log.d("Answer", params + "");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }
}
