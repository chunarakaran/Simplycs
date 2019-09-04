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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Activity.LoginActivity;
import com.aaddya.amita.simplycs.Activity.MainActivity;
import com.aaddya.amita.simplycs.Activity.SignupActivity;
import com.aaddya.amita.simplycs.Model.GetCourse_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.libaml.android.view.chip.ChipLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;


public class AddCourseFragment extends Fragment
{

    ChipLayout chip;
    Spinner CourseSpinner;
    final ArrayList<GetCourse_Model_List> Course_Id = new ArrayList<>();
    final ArrayList<GetCourse_Model_List> CourseName = new ArrayList<>();

   final ArrayList<String> PostCourseid = new ArrayList<>();

    String User_id;
    public static final String PREFS_NAME = "login";

    String URL;

    String Courseid;

    Button Add_Course;

    //volley
    RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    PromptDialog promptDialog;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_add_course, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        toolbar.setTitle("Add Course");

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        // Progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getActivity());

        URL = getString(R.string.url);

        promptDialog = new PromptDialog(getActivity());


        CourseSpinner=(Spinner)rootview.findViewById(R.id.course_spinner);

        GetCourseName();

        CourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id)
            {
//                com_name=   statespinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Courseid = Course_Id.get(position).getId();
//                Toast.makeText(getActivity(),"Id   " +Courseid , Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });



//        ChipLayout.MAX_CHARACTER_COUNT = 20;
//        chip = (ChipLayout)rootview.findViewById(R.id.chipText);



//        chip.setOnChipItemChangeListener(new ChipLayout.ChipItemChangeListener() {
//            @Override
//            public void onChipAdded(int pos, String txt) {
////                Log.d(txt,String.valueOf(pos));
//
////                Courseid = Course_Id.get(pos).getCourseName();
////
////                PostCourseid.add(Courseid);
//
//                Toast.makeText(getActivity(),txt+pos , Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onChipRemoved(int pos, String txt) {
////                Log.d(txt,String.valueOf(pos));
//
//                Courseid = Course_Id.get(pos).getCourseName();
//
//                PostCourseid.remove(Courseid);
//            }
//        });

//        chip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Courseid = Course_Id.get(i).getId();
//
//                PostCourseid.add(Courseid);
//
//                Toast.makeText(getActivity(),"Id   "+Courseid , Toast.LENGTH_LONG).show();
//            }
//        });


        Add_Course=(Button)rootview.findViewById(R.id.add_course);

        Add_Course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String result = TextUtils.join(",", PostCourseid);
//
//                Toast.makeText(getActivity(),Courseid , Toast.LENGTH_LONG).show();
                AddCourse();

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

    public void GetCourseName()
    {
        progressDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetCourseNotUser",
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
                                    GetCourse_Model_List GetDataAdapter2=new GetCourse_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setCourseName(jsonObject1.getString("course_name"));


                                    Course_Id.add(GetDataAdapter2);
                                    CourseName.add(GetDataAdapter2);

                                    list.add(jsonObject1.getString("course_name"));


                                }

                                CourseSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list));



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



        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }


    public void AddCourse()
    {
        progressDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/AddCourse",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {

//                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Success");
                                promptDialog.setContentText("Course Added Successfully");
                                promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        dialog.dismiss();
                                    }
                                }).show();

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
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.

                //Company
                params.put("CourseId", Courseid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
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
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
