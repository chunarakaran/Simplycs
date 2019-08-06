package com.aaddya.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Activity.AudioPlayerActivity;
import com.aaddya.amita.simplycs.Activity.CheckoutActivity;
import com.aaddya.amita.simplycs.Activity.LoginActivity;
import com.aaddya.amita.simplycs.Activity.SignupActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.R;
import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;


public class CourseDetailFragment extends Fragment
{

    ImageView Course_Image;
    TextView Course_Name,Course_Des,Course_Price;
    Button Course_Enroll;

    String Course_id,course_name;
    String cftoken;
    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_course_detail, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        toolbar.setTitle("course");

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        Bundle bundle=getArguments();
        Course_id=String.valueOf(bundle.getString("course_id"));
        course_name=String.valueOf(bundle.getString("course_name"));

        toolbar.setTitle(course_name);

        Initialize();

        GetPackageDetails();



        Course_Enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Submit",Toast.LENGTH_SHORT).show();
                GenerateToken();

                startActivity(new Intent(getActivity(), CheckoutActivity.class)
                        .putExtra("User_id", User_id)
                        .putExtra("cftoken",cftoken));

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

    public void Initialize()
    {

        Course_Enroll=(Button)rootview.findViewById(R.id.Course_enrrol);

        Course_Image=(ImageView)rootview.findViewById(R.id.course_image);

        Course_Name=(TextView)rootview.findViewById(R.id.course_name);
        Course_Des=(TextView)rootview.findViewById(R.id.course_des);
        Course_Price=(TextView)rootview.findViewById(R.id.coursePrice);
    }


    public void GetPackageDetails()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetPackagedetails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                String CourseImage,CourseName,CourseDes,CoursePrice;
                                JSONObject user = jObj.getJSONObject("package");

                                CourseImage=user.getString("file_path");
                                CourseName=user.getString("package_name");
                                CourseDes=user.getString("pakage_details");
                                CoursePrice=user.getString("package_price");



                                Picasso.with(getActivity()).load(CourseImage).placeholder(R.drawable.default_placeholder)
                                        .error(R.drawable.default_placeholder).into(Course_Image);
                                Course_Name.setText(CourseName);
                                Course_Des.setText(Html.fromHtml(CourseDes));
                                Course_Price.setText("\u20B9 "+CoursePrice);


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
                params.put("PackageId", Course_id);

                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }

    public void GenerateToken(){


        pDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        showDialog();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GenerateToken",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        hideDialog();

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {

                                JSONObject data = jObj.getJSONObject("data");
                                cftoken=data.getString("cftoken");

                            }
                            else {

                                JSONObject error = jObj.getJSONObject("error");
                                String msg = String.valueOf(error.getJSONArray("Email"));

                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "The email has already been taken." , Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        hideDialog();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), "The email has already been taken.", Toast.LENGTH_LONG).show();
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
                params.put("OrderId", "Order0001");
                params.put("OrderAmount", "1");
                params.put("OrderCurrency", "INR");


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
