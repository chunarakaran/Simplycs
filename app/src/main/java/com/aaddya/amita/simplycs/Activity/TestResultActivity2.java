package com.aaddya.amita.simplycs.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Adapter.Category_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.TestResult_RecycleAdapter;
import com.aaddya.amita.simplycs.CustomUtils.MyBarDataSet;
import com.aaddya.amita.simplycs.CustomUtils.MyValueFormatter;
import com.aaddya.amita.simplycs.Model.Category_Model_List;
import com.aaddya.amita.simplycs.Model.Performance_index;
import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class TestResultActivity2 extends AppCompatActivity {
    public TextView txt_score, txt_wrong, txt_skip, tvPIndex;
    public String wrong, right, skip,pindex,test_id;
    public static int minutes = 0;
    public static ArrayList<Quetion_Model_List> object = new ArrayList<Quetion_Model_List>();
    public RelativeLayout relPindex;
    public LinearLayout liChartPindex;
    public ImageView imgpIndex;
    private LineChart mChart;
    private RecyclerView mRecyclerView;
    TestResult_RecycleAdapter sAdapter;
    private LinearLayoutManager mLayoutManager;
    String User_id, Course_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;
    List<Performance_index> IndexList = new ArrayList<Performance_index>();
    PromptDialog promptDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test_result);
        test_id = getIntent().getStringExtra("test_id");
//        right = getIntent().getStringExtra("rightans");
//        skip = getIntent().getStringExtra("skipans");
//        pindex = getIntent().getIntExtra("pindex", 0);
//        minutes = getIntent().getIntExtra("minutes", 0);
//        Bundle args = getIntent().getBundleExtra("BUNDLE");
//        object = (ArrayList<Quetion_Model_List>) args.getSerializable("ARRAYLIST");
        txt_score = (TextView) findViewById(R.id.txt_score);
        txt_wrong = (TextView) findViewById(R.id.txt_wrong);
        txt_skip = (TextView) findViewById(R.id.txt_skip);
        tvPIndex = (TextView) findViewById(R.id.tvPIndex);
        relPindex = (RelativeLayout) findViewById(R.id.relPindex);
        liChartPindex = (LinearLayout) findViewById(R.id.liChartPindex);
        imgpIndex = (ImageView) findViewById(R.id.imgpIndex);
        mChart = (LineChart) findViewById(R.id.linechart);


        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        //recycleiew
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        sAdapter = new TestResult_RecycleAdapter(object, this);
//        mRecyclerView.setAdapter(sAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        SharedPreferences sp = this.getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        Course_id = sp.getString("Courseid", "");
        requestQueue = Volley.newRequestQueue(TestResultActivity2.this);
        URL = getString(R.string.url);
        pDialog = new ProgressDialog(TestResultActivity2.this);
        pDialog.setCancelable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GetData();
        GetTestResult();

        ArrayList<BarEntry> entriesR = new ArrayList<>();

//        for (int i = 0; i < object.size(); i++) {
//            int seconds = object.get(i).getTimeTakeSec();
//            float minutes;
//            minutes = seconds / 60f;
//
//            DecimalFormat df = new DecimalFormat("#.##");
//            float formatted = Float.parseFloat(df.format(minutes));
//            Log.i("TagF", "TagF floating value :" + formatted);
//            entriesR.add(new BarEntry(formatted, i));
//
//
//        }

        MyBarDataSet set = new MyBarDataSet(entriesR, "Right");
        set.setColors(new int[]{ContextCompat.getColor(TestResultActivity2.this, R.color.green),
                ContextCompat.getColor(TestResultActivity2.this, R.color.red),
                ContextCompat.getColor(TestResultActivity2.this, R.color.yellow)});


        //set.setValueTextColor(Color.RED);

        //  BarDataSet rightbardataset = new BarDataSet(entriesR, "Right Answer");
//        ArrayList<String> labelsR = new ArrayList<String>();
//        for (int j = 0; j < object.size(); j++) {
//            int Que = j + 1;
//            labelsR.add("Q." + Que);
//
//        }


//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set);
//
//        BarData dataR = new BarData(labelsR, dataSets);
//        barChart.setData(dataR);
//        dataR.setValueFormatter(new MyValueFormatter());
//        //   rightbardataset.setColor(Color.rgb(0, 155, 0));
//        barChart.setDescription("");
//        barChart.animateY(5000);
//        barChart.getLegend().setEnabled(false);
//        //barChart.setScaleMinima(4f, 1f);
//        //barChart.moveViewTo(dataR.getXValCount() - 7, 0.6f, YAxis.AxisDependency.LEFT);




        relPindex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liChartPindex.getVisibility() == View.GONE) {
                    liChartPindex.setVisibility(View.VISIBLE);
                    imgpIndex.setImageResource(R.drawable.ic_expand_less_gray);
                } else {
                    liChartPindex.setVisibility(View.GONE);
                    imgpIndex.setImageResource(R.drawable.ic_expand_more_gray);
                }
            }
        });

    }

    public void GetTestResult()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetTestResult",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String status = jObj.getString("status");

                            if(status.equalsIgnoreCase("true"))
                            {
                                JSONObject data = jObj.getJSONObject("data");

                                right=data.getString("total_correct");
                                wrong=data.getString("total_incorrect");
                                skip=data.getString("total_unattampt");
                                pindex=data.getString("perfomance_index");

                                tvPIndex.setText("Your Performance Index: " + pindex);
                                txt_score.setText(right + "");
                                txt_wrong.setText(wrong + "");
                                txt_skip.setText(skip + "");

                                hideDialog();

                            }
                            else if (status.equalsIgnoreCase("false")){
//                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();



                                hideDialog();
                            }
                            else
                            {
//                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
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

                        // Hiding the progress dialog after all task complete.0


                        // Showing error message if something goes wrong.
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();


                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
//                            Toast.makeText(getActivity(), "Time Out",Toast.LENGTH_LONG).show();

                            promptDialog.setCancelable(false);
                            promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_WARNING);
                            promptDialog.setAnimationEnable(true);
                            promptDialog.setTitleText("Connection Time Out");
                            promptDialog.setContentText("Please Check Your Internet Connection");
                            promptDialog.setPositiveListener("Retry", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
//                                    GetCategoryList();
                                    dialog.dismiss();
                                }
                            }).show();

                        } else if (volleyError instanceof AuthFailureError) {
                            //TODO
                        } else if (volleyError instanceof ServerError) {
                            //TODO
                        } else if (volleyError instanceof NetworkError) {
                            //TODO
                        } else if (volleyError instanceof ParseError) {
                            //TODO
                        }

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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }


    public void GetData() {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL + "api/TestPerformanceIndex",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        Log.d("IndexRes", "Index Response is" + ServerResponse);
                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if (success.equalsIgnoreCase("true")) {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray = jObj.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    Performance_index p = new Performance_index();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    p.setId(jsonObject1.getString("id"));
                                    p.setTest_id(jsonObject1.getString("test_id"));
                                    p.setUser_id(jsonObject1.getString("user_id"));
                                    p.setP_index(jsonObject1.getString("perfomance_index"));
                                    p.setTest_name(jsonObject1.getString("test_name"));
                                    IndexList.add(p);

                                }

                                Collections.reverse(IndexList);

                                setData();

                                hideDialog();

                            } else if (success.equalsIgnoreCase("false")) {
                                Toast.makeText(TestResultActivity2.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            } else {
                                Toast.makeText(TestResultActivity2.this, "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        } catch (JSONException e) {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(TestResultActivity2.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(TestResultActivity2.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }

    private ArrayList<String> setXAxisValues() {
        Log.d("myval", "list size" + IndexList.size());
        ArrayList<String> xVals = new ArrayList<String>();
        for (int j = 0; j < IndexList.size(); j++) {

            xVals.add(IndexList.get(j).getTest_name() + "");
            Log.d("myval", "x value is" + IndexList.get(j).getTest_name());

        }
       /* xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("40");
        xVals.add("50");*/

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < IndexList.size(); i++) {
            String index = IndexList.get(i).getP_index();
            yVals.add(new Entry(Integer.parseInt(index), i));
            Log.d("myval", "Y value is" + IndexList.get(i).getP_index());
        }


       /* yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));*/

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();
        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Performance Index");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent i = new Intent(TestResultActivity2.this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        finish();
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(TestResultActivity2.this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//        finish();
//
//    }
}
