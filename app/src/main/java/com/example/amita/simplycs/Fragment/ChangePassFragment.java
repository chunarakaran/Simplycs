package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePassFragment extends Fragment
{

    EditText EoldPass,EnewPass,EconfPass;
    TextView ChangePass;

    String SoldPass,SnewPass,SUser_id;

    //volley
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    String HttpUrl = "http://biotechautomfg.com/api/change_pass";

    String User_id;
    public static final String PREFS_NAME = "login";

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_changepass, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        //getActivity().setTitle("Change Password");

        requestQueue = Volley.newRequestQueue(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        Initialize();


        ChangePass.setOnClickListener(new View.OnClickListener() {
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

    public void Initialize()
    {
        EoldPass=(EditText)rootview.findViewById(R.id.input_oldpass);
        EnewPass=(EditText)rootview.findViewById(R.id.input_newpass);
        EconfPass=(EditText)rootview.findViewById(R.id.input_confpass);
        ChangePass=(TextView)rootview.findViewById(R.id.change_pass);
    }

    public void GetValueFromEditText(){
        SoldPass=EoldPass.getText().toString();
        SnewPass=EnewPass.getText().toString();
        SUser_id=User_id.toString();
    }

    public void ChangePassword()
    {
        progressDialog.setMessage("Please Wait, We are Inserting Your Data on Server");
        progressDialog.show();

        GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {


                        if(ServerResponse.equalsIgnoreCase("false_oldpassword"))
                        {
                            EoldPass.setError("Old Password does not Match");
                            progressDialog.dismiss();
                        }
                        else
                        {

                            new FancyGifDialog.Builder(getActivity())
                                    .setTitle("Success")
                                    .setMessage("Password Changed Successfully")
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

                                        }
                                    })
                                    .build();



                            Toast.makeText(getActivity(), ServerResponse, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

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
                params.put("user_id", SUser_id);
                params.put("oldpassword", SoldPass);
                params.put("password", SnewPass);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }


    public void Validation()
    {

        String VOldpass,VNewpass,VConPass;
        VOldpass=EoldPass.getText().toString();
        VNewpass=EnewPass.getText().toString();
        VConPass=EconfPass.getText().toString();

        if (VOldpass.length()==0)
        {
            EoldPass.requestFocus();
            EoldPass.setError("Please Enter Current Password");

        }
        else if (VNewpass.length()==0)
        {
            EnewPass.requestFocus();
            EnewPass.setError("Please Enter New Password");
        }

        else if (VConPass.length()==0)
        {
            EconfPass.requestFocus();
            EconfPass.setError("Please Enter confirm Password");
        }

        else if (VNewpass.equals(VConPass))
        {
            ChangePassword();
        }
        else {
            EconfPass.requestFocus();
            EconfPass.setError("Password does not match");
        }

    }



}
