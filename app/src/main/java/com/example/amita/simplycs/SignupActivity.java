package com.example.amita.simplycs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amita.simplycs.Mail.Mail;

public class SignupActivity extends AppCompatActivity {

    ImageView picture;

    TextView Sign_up,backLogin;

    EditText EName,Email,EMobile,EPassword,EConfPass;
    String SName,SEmail,SMobile,SPassword,Sandroid_id,SdeviceName,SdeviceOs,SimeiNumber1;

    private ProgressDialog progressDialog;

    String android_id, deviceName, deviceOs, imeiNumber1;

    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private TelephonyManager mTelephonyManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        picture=(ImageView)findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);


        Initialize();

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


        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),imeiNumber1,Toast.LENGTH_SHORT).show();
               // new SendMail().execute("");

                GetEditTextValue();

                Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile",SMobile);
                startActivity(intent);
                finish();



               // Validation();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
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

    public void Initialize()
    {
        Sign_up=(TextView)findViewById(R.id.sign_up);
        backLogin=(TextView)findViewById(R.id.backLogin);

        EName=(EditText)findViewById(R.id.input_name);
        Email=(EditText)findViewById(R.id.input_email);
        EMobile=(EditText)findViewById(R.id.input_mobile);
        EPassword=(EditText)findViewById(R.id.input_password);
        EConfPass=(EditText)findViewById(R.id.input_ConfPass);

    }

    public void GetEditTextValue()
    {

        SName=EName.getText().toString();
        SEmail=Email.getText().toString();
        SMobile=EMobile.getText().toString();
        SPassword=EPassword.getText().toString();
        Sandroid_id=android_id;
        SdeviceName=deviceName;
        SdeviceOs=deviceOs;
        SimeiNumber1=imeiNumber1;
    }


    public void Validation()
    {
        GetEditTextValue();

        final String Vname,Vemail,Vmobile,Vpassword,VConfPass;

        Vname=EName.getText().toString();
        Vemail=Email.getText().toString();
        Vmobile=EMobile.getText().toString();
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

        else if (Vmobile.length()==0){
            EMobile.requestFocus();
            EMobile.setError("Please Enter Mobile No");
        }
        else if (!Vmobile.matches("^[0-9]{10}$")||Vmobile.contains(" ")){
            EMobile.requestFocus();
            EMobile.setError("Invalid Mobile No");
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
            Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("name",SName);
            intent.putExtra("email",SEmail);
            intent.putExtra("mobile",SMobile);
            intent.putExtra("password",SPassword);
            intent.putExtra("android_id",Sandroid_id);
            intent.putExtra("deviceName",SdeviceName);
            intent.putExtra("deviceOs",SdeviceOs);
            intent.putExtra("imeiNumber1",SimeiNumber1);
            startActivity(intent);
            finish();
        }

        else{
            EConfPass.requestFocus();
            EConfPass.setError("Password does not match");
        }


    }


    private class SendMail extends AsyncTask<String, Integer, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(getApplicationContext(), "Please wait", "Sending mail", true, false);

            progressDialog.setMessage("Sending mail");
            showDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressDialog.dismiss();
            hideDialog();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail("chunarakaran@gmail.com", "Sheetal.");

            int otp=001230;

            String body;
            body=getResources().getString(R.string.Semail);
            Html.fromHtml("<h2>Title</h2><br><p>Description here</p>");
            SEmail=Email.getText().toString();
            String[] toArr = {SEmail};
            m.setTo(toArr);
            m.setFrom("chunarakaran@gmail.com");
            m.setSubject("Simply-cs OTP Verification");
            m.setBody("Dear Customer, "+otp+" is your one time password (OTP),please enter OTP to proceed\n Thank you\nTeam Simply");

            try {
                if(m.send()) {
                    Toast.makeText(SignupActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
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
