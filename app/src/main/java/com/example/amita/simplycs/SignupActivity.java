package com.example.amita.simplycs;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class SignupActivity extends AppCompatActivity {

    ImageView picture;

    TextView Sign_up;

    EditText EName,Email,EPassword,EConfPass;
    String SName,SEmail,SMobile,SPassword,SdeviceName,SdeviceOs;


    String Imobile,deviceName, deviceOs;

    String URL;

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

        picture=(ImageView)findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);


        Initialize();

        Intent intent = getIntent();
        Imobile = intent.getStringExtra("mobile");


        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

        deviceOs = Build.VERSION.RELEASE;

        promptDialog = new PromptDialog(this);

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),Imobile,Toast.LENGTH_SHORT).show();

               // Validation();

                Register();

            }
        });


    }


    public void Initialize()
    {
        Sign_up=(TextView)findViewById(R.id.sign_up);


        EName=(EditText)findViewById(R.id.input_name);
        Email=(EditText)findViewById(R.id.input_email);
        EPassword=(EditText)findViewById(R.id.input_password);
        EConfPass=(EditText)findViewById(R.id.input_ConfPass);

    }

    public void GetEditTextValue()
    {

        SName=EName.getText().toString();
        SEmail=Email.getText().toString();
        SPassword=EPassword.getText().toString();
        SMobile=Imobile;

        SdeviceName=deviceName;
        SdeviceOs=deviceOs;

    }


    public void Validation()
    {
        GetEditTextValue();

        final String Vname,Vemail,Vpassword,VConfPass;

        Vname=EName.getText().toString();
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
                                String msg = error.getString("Email");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        hideDialog();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.

                //Company
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




    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
