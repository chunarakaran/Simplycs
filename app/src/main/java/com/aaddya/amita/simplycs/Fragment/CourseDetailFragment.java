package com.aaddya.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.aaddya.amita.simplycs.Activity.CheckoutActivity;
import com.aaddya.amita.simplycs.Adapter.Chepter_List_Adapter;
import com.aaddya.amita.simplycs.Model.Chepter_Model_List;
import com.aaddya.amita.simplycs.Model.Course_Model_List;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDetailFragment extends Fragment
{

    List<Chepter_Model_List> ListOfdataAdapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;


    String chepter_id,chapter_name;
    final ArrayList<Chepter_Model_List> Chapter_id = new ArrayList<>();
    final ArrayList<Chepter_Model_List> Ch_Course_id = new ArrayList<>();
    final ArrayList<Chepter_Model_List> Ch_Category_id = new ArrayList<>();
    final ArrayList<Chepter_Model_List> Subcategory_id = new ArrayList<>();
    final ArrayList<Chepter_Model_List> Chapter_name = new ArrayList<>();
    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    ImageView Course_Image;
    TextView Course_Name,Course_Des,Course_Price;
    Button Course_Enroll;

    String Course_id,course_name;
    String Package_id,Order_id,cftoken,Order_Amount,CustomerName,CustomerPhone,CustomerEmail;
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


        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);


        GetPackageDetails();

        GetProfile();



        Course_Enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray jChapter_id = new JSONArray();

                //Make Subcategory id Json Array
                if (Subcategory_id.size() > 0) {
                    for (int i = 0; i < Subcategory_id.size(); i++) {
                        JSONObject jGroup = new JSONObject();// /sub Object
                        try {
                            jGroup.put("id", Integer.parseInt(Chapter_id.get(i).getId()));
                            jGroup.put("course_id", Integer.parseInt(Ch_Course_id.get(i).getCh_Course_id()));
                            jGroup.put("topic_id", Integer.parseInt(Ch_Category_id.get(i).getCh_Category_id()));
                            jGroup.put("sub_topic_id", Integer.parseInt(Subcategory_id.get(i).getCh_Subcategory_id()));
                            jGroup.put("subtopic_name", Chapter_name.get(i).getChapterName());
                            jChapter_id.put(jGroup);
                            // /itemDetail Name is JsonArray Name
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                Toast.makeText(getActivity(),jChapter_id.toString(),Toast.LENGTH_SHORT).show();




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
                                        Order_id=data.getString("OrderId");

                                        startActivity(new Intent(getActivity(), CheckoutActivity.class)
                                                .putExtra("User_id", User_id)
                                                .putExtra("Package_id",Package_id)
                                                .putExtra("Order_id",Order_id)
                                                .putExtra("cftoken",cftoken)
                                                .putExtra("Order_Amount",Order_Amount)
                                                .putExtra("CustomerName",CustomerName)
                                                .putExtra("CustomerPhone",CustomerPhone)
                                                .putExtra("CustomerEmail",CustomerEmail)
                                                .putExtra("course_name",course_name)
                                                .putExtra("jChapter_id", jChapter_id.toString())
                                        );



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
                                    Toast.makeText(getActivity(), "server error" , Toast.LENGTH_LONG).show();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                hideDialog();

                                // Showing error message if something goes wrong.
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
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

                        params.put("OrderAmount",Order_Amount);
                        params.put("OrderCurrency", "INR");


                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest1);

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

                                String courseId,CourseImage,CourseName,CourseDes,CoursePrice;
                                JSONObject user = jObj.getJSONObject("package");


                                courseId=user.getString("id");
                                CourseImage=user.getString("file_path");
                                CourseName=user.getString("package_name");
                                CourseDes=user.getString("pakage_details");
                                CoursePrice=user.getString("package_price");



                                Picasso.with(getActivity()).load(CourseImage).placeholder(R.drawable.default_placeholder)
                                        .error(R.drawable.default_placeholder).into(Course_Image);
                                Course_Name.setText(CourseName);
                                Course_Des.setText(Html.fromHtml(CourseDes));
                                Course_Price.setText("\u20B9 "+CoursePrice);

                                Package_id=courseId;
                                Order_Amount=CoursePrice;

                                JSONArray jsonArray=jObj.getJSONArray("multiple_package_content");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    Chepter_Model_List chepter_model_list=new Chepter_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    chepter_model_list.setId(jsonObject1.getString("id"));
//                                    chepter_model_list.setCh_Course_id(jsonObject1.getString("course_id"));
//                                    chepter_model_list.setCh_Category_id(jsonObject1.getString("topic_id"));
//                                    chepter_model_list.setCh_Subcategory_id(jsonObject1.getString("sub_topic_id"));
                                    chepter_model_list.setChapterName(jsonObject1.getString("subtopic_name"));


                                    Chapter_id.add(chepter_model_list);
//                                    Ch_Course_id.add(chepter_model_list);
//                                    Ch_Category_id.add(chepter_model_list);
//                                    Subcategory_id.add(chepter_model_list);
                                    Chapter_name.add(chepter_model_list);

                                    ListOfdataAdapter.add(chepter_model_list);

                                }


                                adapter = new Chepter_List_Adapter(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);


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

    public void GetProfile()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                String Pic,name,email,mobile,gender,dob,education,city;
                                JSONObject user = jObj.getJSONObject("data");

                                Pic=user.getString("profile_pic");
                                name=user.getString("name");
                                email=user.getString("email");
                                mobile=user.getString("mobile");
                                gender=user.getString("gender");
                                dob=user.getString("birth_date");
                                education=user.getString("education");
                                city=user.getString("city");

                                CustomerName=name;
                                CustomerPhone=mobile;
                                CustomerEmail=email;

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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
