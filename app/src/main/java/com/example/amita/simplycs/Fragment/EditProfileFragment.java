package com.example.amita.simplycs.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.MainActivity;
import com.example.amita.simplycs.R;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment
{

    ImageView picture;

    TextView Update_profile;
    EditText E_Name,E_Email,E_Mobile,E_DOB,E_Education,E_City;
    String S_Name,S_Email,S_Mobile,S_Gender,S_DOB,S_Education,S_City;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Calendar mcalendar;
    private int day,month,year;
    String aDate;

    private ProgressDialog pDialog;

    //volley
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    String HttpUrl = "https://simply-cs.com/api/edit_profile";

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        picture=(ImageView)rootview.findViewById(R.id.logo);
        int imageid = R.drawable.p2;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);

        mcalendar=Calendar.getInstance();
        day=mcalendar.get(Calendar.DAY_OF_MONTH);
        year=mcalendar.get(Calendar.YEAR);
        month=mcalendar.get(Calendar.MONTH);

        Initialization();


        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());



        E_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });



        Update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetValueFromEditText();

                Toast.makeText(getActivity(),S_Gender,Toast.LENGTH_SHORT).show();
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

        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        progressDialog.show();

        GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing response message coming from server.
//                        Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                        new FancyGifDialog.Builder(getActivity())
                                .setTitle("Success")
                                .setMessage("Your Leave Request Send Successfully")
                                .setNegativeBtnText("Cancel")
                                .setPositiveBtnBackground("#FF4081")
                                .setPositiveBtnText("Ok")
                                .setNegativeBtnBackground("#FFA9A7A8")
                                .setGifResource(R.drawable.check)   //Pass your Gif here
                                .isCancellable(false)
                                .OnPositiveClicked(new FancyGifDialogListener() {
                                    @Override
                                    public void OnClick() {
//                                        Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                })
                                .OnNegativeClicked(new FancyGifDialogListener() {
                                    @Override
                                    public void OnClick() {
//
//                                        fromDate.setText("");
//                                        toDate.setText("");
//                                        remark.setText("");
                                    }
                                })
                                .build();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.

                //Company
//                params.put("user_id", hUid);
//                params.put("leave_type", hLeaveid);
//                params.put("from_date", hfromDate);
//                params.put("to_date", htoDate);
//                params.put("remark", hRemark);

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
        else if (!V_Mobile.matches("^[0-9]{10}$"))
        {
            E_Mobile.requestFocus();
            E_Mobile.setError("Please Enter Valid Mobile No");
        }
        else {

        }

    }



}
