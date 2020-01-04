package com.aaddya.amita.simplycs.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Adapter.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class LoginActivity extends AppCompatActivity {

    ImageView picture;

    TextView Login, Sign_up, Forgot;

    EditText user_id, user_password;
    RequestQueue requestQueue1;
    String Url;
    String android_id, deviceName, deviceOs;
    String imeiNumber1="122412412414";

    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private TelephonyManager mTelephonyManager;


    private SessionManager session;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "login";

    boolean mRecentlyBackPressed;
    Handler mExitHandler;
    Runnable mExitRunnable;
    long delay;
    private Context context;

    private ProgressDialog pDialog;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        // DO WHAT YOU WANT ON BACK PRESSED
        if (mRecentlyBackPressed) {
            //super.onBackPressed();
            MainActivity a = new MainActivity();
            a.finish();
            finish();
//                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();

        } else {
            mRecentlyBackPressed = true;
            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mRecentlyBackPressed = false;
                }
            }, 2000);

        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        delay = 2000L;
        mRecentlyBackPressed = false;
        mExitHandler = new Handler();
        mExitRunnable = new Runnable() {

            @Override
            public void run() {
                mRecentlyBackPressed = false;
            }
        };


//        picture = (ImageView) findViewById(R.id.logo);
//        int imageid = R.drawable.login_logo;
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
//        picture.setImageBitmap(bitmap);


        Login = (TextView) findViewById(R.id.login);
        Sign_up = (TextView) findViewById(R.id.sign_up);
        Forgot = (TextView) findViewById(R.id.forgot);
        user_id = (EditText) findViewById(R.id.userEmail);
        user_password = (EditText) findViewById(R.id.userpassword);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        sharedpreferences = getSharedPreferences(PREFS_NAME, this.MODE_PRIVATE);

        editor = sharedpreferences.edit();

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        deviceName = myDevice.getName();

        deviceOs = Build.VERSION.RELEASE;

//        if (Build.VERSION.SDK_INT >= 23) {
//
//            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
//                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
//            } else {
//                getDeviceImei();
//            }
//        }
//        else{
//            getDeviceImei();
//        }





        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), imeiNumber1, Toast.LENGTH_SHORT).show();

                requestQueue1 = Volley.newRequestQueue(getApplicationContext());

                Url = (String) getText(R.string.url);
                if(!user_id.getText().toString().isEmpty() && !user_password.getText().toString().isEmpty()) {
                    if(isNetworkAvailable()) {
                        Login();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please check your internet connecion or try again.", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    user_id.setError("You must enter user name");
                    user_password.setError("You must enter password");
                }
            }


        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotpwdActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getDeviceImei();
//        }
//    }

//
//    @SuppressLint({"MissingPermission", "NewApi"})
//    private void getDeviceImei() {
//        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        imeiNumber1 = mTelephonyManager.getDeviceId(2);
//    }

    public void Login()
    {
        pDialog.setMessage("Logging in ...");
        showDialog();

        // Creating string request with post method.
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Url+"api/Login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {
                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if (success.equalsIgnoreCase("false_email")) {
                                user_id.setError("Email-id does not exist");
                                user_password.setText("");
                                hideDialog();
                            } else if (success.equalsIgnoreCase("false_password")) {
                                user_password.setError("Incorrect password");
                                user_password.setText("");
                                hideDialog();
                            } else {

                                JSONObject user = jObj.getJSONObject("data");
                                String Uid = user.getString("Auth");
                                String Cid = user.getString("cource_id");

                                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                                editor.putString("User", Uid);
                                editor.putString("Courseid", Cid);
                                editor.commit();

                                // Create login session
                                session.setLogin(true);

                                hideDialog();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } catch (JSONException e) {

                            hideDialog();
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Server Error Please Try After Sometime", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        hideDialog();

                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.

                //Company
                params.put("Email",user_id.getText().toString());
                params.put("Password",user_password.getText().toString());
                params.put("DeviceToken",android_id);
                params.put("OSVersion",deviceOs);
                params.put("IMEI",imeiNumber1);
                params.put("DeviceName",deviceName);


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //10000 is the time in milliseconds adn is equal to 10 sec
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
