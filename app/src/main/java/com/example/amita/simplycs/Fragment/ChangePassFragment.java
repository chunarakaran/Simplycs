package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.LoginActivity;
import com.example.amita.simplycs.MainActivity;
import com.example.amita.simplycs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class ChangePassFragment extends Fragment
{

    ImageView picture;
    EditText EoldPass,EnewPass,EconfPass;
    TextView ChangePass;

    String SoldPass,SnewPass,SUser_id;

    //volley
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    PromptDialog promptDialog;

    String URL;

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

        picture=(ImageView)rootview.findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        progressDialog = new ProgressDialog(getActivity());

        promptDialog = new PromptDialog(getActivity());

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
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/ChangePassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {



                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("false_password"))
                            {
//                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                EoldPass.requestFocus();
                                EoldPass.setError(jObj.getString("message"));
                                progressDialog.dismiss();


                            }
                            else if (success.equalsIgnoreCase("true")){

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Success");
                                promptDialog.setContentText(jObj.getString("message"));
                                promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {

                                        ((MainActivity) getActivity()).Logout();

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        dialog.dismiss();
                                    }
                                }).show();


//                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("OldPassword", SoldPass);
                params.put("Password", SnewPass);


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

        else if (VNewpass.length()<6){
            EnewPass.requestFocus();
            EnewPass.setError("Please Enter Maximum 6 Character");
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
