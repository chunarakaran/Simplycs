package com.example.amita.simplycs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPhoneActivity extends AppCompatActivity {

    ImageView picture;

    private EditText editTextMobile;
    Button buttonContinue;

    TextView backLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);

        picture=(ImageView)findViewById(R.id.logo);
        int imageid = R.drawable.login_logo;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageid, opts);
        picture.setImageBitmap(bitmap);

        editTextMobile = findViewById(R.id.editTextMobile);
        buttonContinue=(Button)findViewById(R.id.buttonContinue);
        backLogin=(TextView)findViewById(R.id.backLogin);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }


                Intent intent = new Intent(EnterPhoneActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);


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

}
