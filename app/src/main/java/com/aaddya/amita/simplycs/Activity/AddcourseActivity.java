package com.aaddya.amita.simplycs.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Model.GetCourse_Model_List;
import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyhope.materialtagview.TagView;
import com.skyhope.materialtagview.model.TagModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddcourseActivity extends AppCompatActivity  {

    TagView tagView;
    Button AddCourse;

    final ArrayList<GetCourse_Model_List> Coursedatalist = new ArrayList<>();

    final ArrayList<String> list = new ArrayList<>();

    private ProgressDialog pDialog;

    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";


    //volley
    RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        SharedPreferences sp = getApplicationContext().getSharedPreferences(PREFS_NAME, getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        URL = getString(R.string.url);

        AddCourse=(Button)findViewById(R.id.add_course);
        tagView = findViewById(R.id.tag_view_test);

        tagView.setHint("Add Courses");

        //tagView.addTagSeparator(TagSeparator.AT_SEPARATOR);

        tagView.addTagLimit(5);

        tagView.setTagBackgroundColor(Color.BLUE);


        tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hello", Toast.LENGTH_LONG).show();
            }
        });

        GetCourseName();

        AddCourse.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                TagModel x= (TagModel) tagView.getSelectedTags().get(0);

                Toast.makeText(getApplicationContext(),x.getTagText(), Toast.LENGTH_LONG).show();

//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                finish();

            }
        });



    }


    public void GetCourseName()
    {
        progressDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetCourseNotUser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {


                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("data");

//                                TagModel tagModel=new TagModel();



                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    GetCourse_Model_List GetDataAdapter2=new GetCourse_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setCourseName(jsonObject1.getString("course_name"));


                                    Coursedatalist.add(GetDataAdapter2);

                                    list.add(jsonObject1.getString("course_name"));


                                }

                                String[] id = new String[Coursedatalist.size()];
                                for (int i = 0; i < Coursedatalist.size(); i++) {
                                    id[i] = Coursedatalist.get(i).getId();
                                }

                                String[] name = new String[Coursedatalist.size()];
                                for (int i = 0; i < Coursedatalist.size(); i++) {
                                    name[i] = Coursedatalist.get(i).getCourseName();
                                }


                                tagView.setTagList(name);
//                                tagModel.setTagText(String.valueOf(id));



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



        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
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

