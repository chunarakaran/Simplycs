package com.aaddya.amita.simplycs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotpwdActivity extends AppCompatActivity {

    ImageView picture;

    EditText Email;

    TextView SendOTP,BacktoLogin;

    RequestQueue requestQueue1;
    String Url;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);



        picture=(ImageView)findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        Initialize();



        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Send Email...",Toast.LENGTH_SHORT).show();


                requestQueue1 = Volley.newRequestQueue(getApplicationContext());

                Url = (String) getText(R.string.url);
                if(!Email.getText().toString().isEmpty()) {
                    if(isNetworkAvailable()) {
                        check_email();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please check your internet connecion or try again.", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Email.setError("You must enter Email Id");
                }

            }
        });



        BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotpwdActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void Initialize(){
        Email=(EditText)findViewById(R.id.input_email);
        SendOTP=(TextView)findViewById(R.id.sendotp);
        BacktoLogin=(TextView)findViewById(R.id.backLogin);
    }


    private void check_email()
    {
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest jsonobject = new StringRequest(Request.Method.POST, Url+"api/ForgotPassword" + "?Email="+Email.getText().toString(), new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    if (success.equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                        hideDialog();
                    }
                    else if (success.equalsIgnoreCase("false")){
                        Email.setError(jObj.getString("message"));
                        hideDialog();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Server Error", Toast.LENGTH_LONG).show();

                hideDialog();
            }
        });

        jsonobject.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue1.add(jsonobject);
    }




    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();

        return isConnected;
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
