package com.aaddya.amita.simplycs.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Model.GetCourse_Model_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class SignupActivity extends AppCompatActivity {

    ImageView picture,passwordEyeImg,confirm_password_eye_img;

    TextView Sign_up,backLogin;

    Spinner CourseSpinner;
    final ArrayList<GetCourse_Model_List> Coursedatalist = new ArrayList<>();


    EditText EName,Email,EPassword,EConfPass,Emobile;
    String SCourseid,SName,SEmail,SMobile,SPassword,SdeviceName,SdeviceOs;


    String Imobile,deviceName, deviceOs;

    String URL;

    String Courseid;

    //volley
    RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    PromptDialog promptDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        URL = getString(R.string.url);

        Intent intent = getIntent();
        Imobile = intent.getStringExtra("mobile");

        Initialize();

        passwordEyeImg.setTag(1);
        confirm_password_eye_img.setTag(1);

//        Emobile.setText("+91");
//        Selection.setSelection(Emobile.getText(), Emobile.getText().length());

//        Emobile.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!s.toString().startsWith("+91")){
//                    Emobile.setText("+91");
//                    Selection.setSelection(Emobile.getText(), Emobile.getText().length());
//
//                }
//
//            }
//        });


        GetCourseName();

        CourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id)
            {
//                com_name=   statespinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Courseid = Coursedatalist.get(position).getId();
//                Toast.makeText(getApplicationContext(),"Id   " +Courseid , Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });





        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

        deviceOs = Build.VERSION.RELEASE;

        promptDialog = new PromptDialog(this);

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),Imobile,Toast.LENGTH_SHORT).show();
                Validation();
            }
        });


        passwordEyeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordEyeImg.getTag().equals(1)) {
                    EPassword.setTransformationMethod(null);
                    passwordEyeImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_close));
                    passwordEyeImg.setTag(0);
                } else {
                    passwordEyeImg.setTag(1);
                    EPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordEyeImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_open));
                }
            }
        });

        confirm_password_eye_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm_password_eye_img.getTag().equals(1)) {
                    EConfPass.setTransformationMethod(null);
                    confirm_password_eye_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_close));
                    confirm_password_eye_img.setTag(0);
                } else {
                    confirm_password_eye_img.setTag(1);
                    EConfPass.setTransformationMethod(new PasswordTransformationMethod());
                    confirm_password_eye_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_open));
                }
            }
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }


    public void Initialize()
    {
        Sign_up=(TextView)findViewById(R.id.sign_up);
        backLogin=(TextView)findViewById(R.id.backLogin);

        passwordEyeImg=(ImageView)findViewById(R.id.password_eye_img);
        confirm_password_eye_img=(ImageView)findViewById(R.id.confirm_password_eye_img);

        CourseSpinner=(Spinner)findViewById(R.id.course_spinner);
        EName=(EditText)findViewById(R.id.input_name);
        Emobile=(EditText)findViewById(R.id.input_mobile);
        Email=(EditText)findViewById(R.id.input_email);
        EPassword=(EditText)findViewById(R.id.input_password);
        EConfPass=(EditText)findViewById(R.id.input_ConfPass);


    }

    public void GetEditTextValue()
    {

        SCourseid=Courseid;
        SName=EName.getText().toString();
        SEmail=Email.getText().toString();
        SPassword=EPassword.getText().toString();
        SMobile=Emobile.getText().toString();

        SdeviceName=deviceName;
        SdeviceOs=deviceOs;

    }


    public void Validation()
    {
        GetEditTextValue();

        final String Vname,Vemail,Vpassword,VConfPass,Vmobile;

        Vname=EName.getText().toString();
        Vmobile=Emobile.getText().toString();
        Vemail=Email.getText().toString();
        Vpassword=EPassword.getText().toString();
        VConfPass=EConfPass.getText().toString();

        if (Vname.isEmpty())
        {
            EName.requestFocus();
            EName.setError("Please Enter Name");
        }
        else if(!Vname.matches("^[a-zA-Z ]+$"))
        {
            EName.requestFocus();
            EName.setError("Enter Valid Name");
        }

        else if (Vmobile.isEmpty() || Vmobile.length() < 10){
            Emobile.setError("Enter a valid mobile");
            Emobile.requestFocus();
        }

        else if (Vemail.length()==0){
            Email.requestFocus();
            Email.setError("Please Enter Email Address");
        }
        else if (!Vemail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")||Vemail.contains(" ")){
            Email.requestFocus();
            Email.setError("Invalid Email Address");
        }


        else if (Vpassword.length()==0){
            EPassword.requestFocus();
            EPassword.setError("Please Enter Password");
        }

        else if (Vpassword.length()<6){
            EPassword.requestFocus();
            EPassword.setError("Please Enter 6 OR More Character");
        }

        else if(VConfPass.length()==0){
            EConfPass.requestFocus();
            EConfPass.setError("Please Enter Password");
        }

        else if (Vpassword.equals(VConfPass)){

            Register();
        }

        else{
            EConfPass.requestFocus();
            EConfPass.setError("Password does not match");
        }


    }


    public void Register()
    {
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        showDialog();

        GetEditTextValue();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/SignUp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        hideDialog();

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
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
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                }).show();



                            }
                            else {

                                JSONObject error = jObj.getJSONObject("error");
                                String msg = String.valueOf(error.getJSONArray("Email"));

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "The email has already been taken." , Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        hideDialog();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), "The email has already been taken.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.

                //Company
                params.put("CourceId", SCourseid);
                params.put("Email", SEmail);
                params.put("Name", SName);
                params.put("Mobileno", SMobile);
                params.put("Password", SPassword);
                params.put("OSVersion", SdeviceOs);
                params.put("DeviceName", SdeviceName);

                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);

    }

    public void GetCourseName()
    {
        progressDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetAllCource",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        final ArrayList<String> list = new ArrayList<>();

                        list.clear();

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("false"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    GetCourse_Model_List GetDataAdapter2=new GetCourse_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setCourseName(jsonObject1.getString("course_name"));



                                    Coursedatalist.add(GetDataAdapter2);

                                    list.add(jsonObject1.getString("course_name"));

                                }


                                CourseSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));

                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("true")){
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
