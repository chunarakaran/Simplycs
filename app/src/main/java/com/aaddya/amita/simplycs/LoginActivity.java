package com.aaddya.amita.simplycs;

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
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
import com.aaddya.amita.simplycs.Adapter.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    ImageView picture;

    TextView Login, Sign_up, Forgot;

    EditText user_id, user_password;
    RequestQueue requestQueue1;
    String Url;
    String android_id, deviceName, deviceOs, imeiNumber1;

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


        picture = (ImageView) findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);


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

        if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getDeviceImei();
        }



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




               // Toast.makeText(getApplicationContext(), imeiNumber1, Toast.LENGTH_SHORT).show();



                requestQueue1 = Volley.newRequestQueue(getApplicationContext());

                Url = (String) getText(R.string.url);
                if(!user_id.getText().toString().isEmpty() && !user_password.getText().toString().isEmpty()) {
                    if(isNetworkAvailable()) {
                        check_login();
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private void getDeviceImei() {

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber1 = mTelephonyManager.getDeviceId(2);
    }


    private void check_login() {
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest jsonobject = new StringRequest(Request.Method.POST, Url + "api/Login" + "?Email=" + user_id.getText().toString() + "&Password=" + user_password.getText().toString()
                +"&DeviceToken="+android_id + "&OSVersion=" + deviceOs + "&IMEI=" + imeiNumber1 + "&DeviceName=" + deviceName, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
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
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();

                hideDialog();
            }
        });

        jsonobject.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue1.add(jsonobject);
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
