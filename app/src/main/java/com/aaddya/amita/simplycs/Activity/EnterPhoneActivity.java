package com.aaddya.amita.simplycs.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EnterPhoneActivity extends AppCompatActivity {

    ImageView picture;

    private EditText editTextMobile;
    Button buttonContinue;

    String S_mobileno;

    TextView backLogin;

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);

//        picture=(ImageView)findViewById(R.id.logo);
//        int imageid = R.drawable.login_logo;
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
//        picture.setImageBitmap(bitmap);

        requestQueue = Volley.newRequestQueue(this);
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        editTextMobile = findViewById(R.id.editTextMobile);
        buttonContinue=(Button)findViewById(R.id.buttonContinue);
        backLogin=(TextView)findViewById(R.id.backLogin);

        editTextMobile.setText("+91");
        Selection.setSelection(editTextMobile.getText(), editTextMobile.getText().length());

        editTextMobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+91")){
                    editTextMobile.setText("+91");
                    Selection.setSelection(editTextMobile.getText(), editTextMobile.getText().length());

                }

            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEditTextValue();

                if(S_mobileno.isEmpty() || S_mobileno.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                else {
                    CheckUserMobile();
                }

            }
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterPhoneActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void GetEditTextValue()
    {
        S_mobileno=editTextMobile.getText().toString();
    }


    public void CheckUserMobile()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();
        GetEditTextValue();
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/CheckUserMobile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("is_register");

//                            Toast.makeText(getApplicationContext(), S_mobileno, Toast.LENGTH_LONG).show();

                            if(success.equalsIgnoreCase("false"))
                            {

                                Intent intent = new Intent(EnterPhoneActivity.this, VerifyPhoneActivity.class);
                                intent.putExtra("mobile", S_mobileno);
                                startActivity(intent);
                                hideDialog();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Your Mobile Number is Registered", Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();


                // Adding All values to Params.
                params.put("mobile_no", S_mobileno);

                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
