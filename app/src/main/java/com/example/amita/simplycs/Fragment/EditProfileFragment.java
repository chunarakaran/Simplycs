package com.example.amita.simplycs.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.MainActivity;
import com.example.amita.simplycs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class EditProfileFragment extends Fragment
{

    Fragment fragment;
    ImageView picture;

    TextView Update_profile;
    EditText E_Name,E_Email,E_Mobile,E_DOB,E_Education,E_City;
    String S_Name,S_Email,S_Mobile,S_Gender,S_DOB,S_Education,S_City;

    private RadioGroup radioGroup;
    private RadioButton radioButton,Rbmale,Rbfemale;

    private Calendar mcalendar;
    private int day,month,year;
    String aDate;

    private ProgressDialog pDialog;
    PromptDialog promptDialog;

    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;


    String HttpUrl = "https://simply-cs.com/api/edit_profile";

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Edit Profile");


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

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getActivity());

        promptDialog = new PromptDialog(getActivity());


        picture=(ImageView)rootview.findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);

        mcalendar=Calendar.getInstance();
        day=mcalendar.get(Calendar.DAY_OF_MONTH);
        year=mcalendar.get(Calendar.YEAR);
        month=mcalendar.get(Calendar.MONTH);

        Initialization();

        GetProfile();

        E_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });



        Update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();
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

    public void DateDialog(){

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                int Month;
                Month=monthOfYear+1;
                aDate=dayOfMonth+"-"+Month+"-"+year;
                E_DOB.setText(aDate);


            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, year, month, day);
        dpDialog.getDatePicker().setMaxDate(mcalendar.getTimeInMillis());
        dpDialog.show();
    }


    public void Initialization(){
        E_Name=(EditText)rootview.findViewById(R.id.input_name);
        E_Email=(EditText)rootview.findViewById(R.id.input_email);
        E_Mobile=(EditText)rootview.findViewById(R.id.input_mobile);
        E_DOB=(EditText)rootview.findViewById(R.id.input_dob);
        E_Education=(EditText)rootview.findViewById(R.id.input_education);
        E_City=(EditText)rootview.findViewById(R.id.input_city);

        Rbmale=(RadioButton)rootview.findViewById(R.id.radioMale);
        Rbfemale=(RadioButton)rootview.findViewById(R.id.radioFemale);

        Update_profile=(TextView)rootview.findViewById(R.id.update_profile);

    }

    public void GetValueFromEditText()
    {

        radioGroup = (RadioGroup)rootview.findViewById(R.id.radio);
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton)rootview.findViewById(selectedId);

        S_Gender=radioButton.getText().toString();

        S_Name=E_Name.getText().toString();
        S_Email=E_Email.getText().toString();
        S_Mobile=E_Mobile.getText().toString();
        S_DOB=E_DOB.getText().toString();
        S_Education=E_Education.getText().toString();
        S_City=E_City.getText().toString();
    }

    public void Update_Profile(){

        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/EditProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Success");
                                promptDialog.setContentText(jObj.getString("message"));
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                GetValueFromEditText();
                // Adding All values to Params.
                params.put("Name", S_Name);
                params.put("Email", S_Email);
                params.put("Mobile", S_Mobile);
                params.put("Gender", S_Gender);
                params.put("DateOfBirth", S_DOB);
                params.put("Education", S_Education);
                params.put("City", S_City);

                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }

    public void Validation(){
        final String V_Name,V_Email,V_Mobile,V_Gender,V_DOB,V_Education,V_City;

        V_Name=E_Name.getText().toString();
        V_Email=E_Email.getText().toString();
        V_Mobile=E_Mobile.getText().toString();
        V_Education=E_Education.getText().toString();
        V_City=E_City.getText().toString();

        if(V_Name.length()==0)
        {
            E_Name.requestFocus();
            E_Name.setError("Please Enter Name");
        }
        else if (!V_Name.matches("^[a-zA-Z ]+$"))
        {
            E_Name.requestFocus();
            E_Name.setError("Please Enter Valid Name");
        }
        else if (V_Email.length()==0)
        {
            E_Email.requestFocus();
            E_Email.setError("Please Enter Email");
        }
        else if(!V_Email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
        {
            E_Email.requestFocus();
            E_Email.setError("Please Enter Valid Email");
        }
        else if(V_Mobile.length()==0)
        {
            E_Mobile.requestFocus();
            E_Mobile.setError("Please Enter Mobile No");
        }
        else if (!V_Mobile.matches("^[0-9+]{13}$"))
        {
            E_Mobile.requestFocus();
            E_Mobile.setError("Please Enter Valid Mobile No");
        }
        else {

            Update_Profile();
        }

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

                                String name,email,mobile,gender,dob,education,city;
                                JSONObject user = jObj.getJSONObject("data");


                                name=user.getString("name");
                                email=user.getString("email");
                                mobile=user.getString("mobile");
                                gender=user.getString("gender");
                                dob=user.getString("birth_date");
                                education=user.getString("education");
                                city=user.getString("city");

                                E_Name.setText(name);
                                E_Email.setText(email);
                                E_Mobile.setText(mobile);

                                if(gender.equals("male")||gender.equals("Male"))
                                {
                                    Rbmale.setChecked(true);
                                }
                                else {
                                    Rbfemale.setChecked(true);
                                }
                                E_DOB.setText(dob);
                                E_Education.setText(education);
                                E_City.setText(city);


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
