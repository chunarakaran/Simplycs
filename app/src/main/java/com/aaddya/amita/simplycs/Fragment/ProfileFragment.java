package com.aaddya.amita.simplycs.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.aaddya.amita.simplycs.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment
{
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    FloatingActionButton EditButton;

    TextView User_name,User_email,User_mobile,User_gender,User_dob,User_education,User_city;
    TextView ChangePassword;
    Fragment fragment;
    ImageView Profile_pic;

    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;


    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Profile");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog=new ProgressDialog(getActivity());
        pDialog.setMessage("Uploading Image. Please Wait");
        pDialog.setCancelable(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        Initialize();

        GetProfile();

        Profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Requesting storage permission
                requestStoragePermission();
            }
        });

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection=getFragmentManager().beginTransaction();
                fragment=new EditProfileFragment();
                transection.replace(R.id.content_frame, fragment);
                transection.addToBackStack(null).commit();
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transection=getFragmentManager().beginTransaction();
                fragment=new ChangePassFragment();
                transection.replace(R.id.content_frame, fragment);
                transection.addToBackStack(null).commit();

//                Toast.makeText(getActivity(),"Change Password",Toast.LENGTH_SHORT).show();


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

    //Requesting permission
    private void requestStoragePermission() {

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(getContext(), ProfileFragment.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setTitle("Permission Required")
                                    .setMessage("Permission to Access your device storage is required to pick Profile Image. Please go to setting to enable permission to access storage ")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getActivity().getPackageName(),null));
                                            startActivityForResult(intent,51);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Profile_pic.setImageURI(resultUri);

                File imageFile = new File(resultUri.getPath());
                pDialog.show();
                AndroidNetworking.upload(URL+"api/UpdateProfilePicture")
                        .addMultipartFile("Image",imageFile)
                        .addHeaders("Auth",User_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {

                                float progress = (float) bytesUploaded / totalBytes * 100;
                                pDialog.setProgress((int)progress);
                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    pDialog.dismiss();
                                    JSONObject jsonObject = new JSONObject (response);
                                    String status =jsonObject.getString("success");
                                    String message =jsonObject.getString("message");
                                    if (status.equals("false"))
                                    {
                                        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e)
                                {
                                    pDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),"Parsing Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                anError.printStackTrace();
                                Toast.makeText(getActivity(),"Error Uploading Image",Toast.LENGTH_SHORT).show();
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void Initialize()
    {
        ChangePassword=(TextView)rootview.findViewById(R.id.pass1);
        EditButton=(FloatingActionButton) rootview.findViewById(R.id.fab);
        Profile_pic=(ImageView)rootview.findViewById(R.id.Profile_pic);

        User_name=(TextView) rootview.findViewById(R.id.user_name);
        User_email=(TextView) rootview.findViewById(R.id.user_email);
        User_mobile=(TextView) rootview.findViewById(R.id.user_mobile);
        User_gender=(TextView) rootview.findViewById(R.id.user_gender);
        User_dob=(TextView) rootview.findViewById(R.id.user_dob);
        User_education=(TextView) rootview.findViewById(R.id.user_education);
        User_city=(TextView) rootview.findViewById(R.id.user_city);

    }

    public void GetProfile()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();

                                String Pic,name,email,mobile,gender,dob,education,city;
                                JSONObject user = jObj.getJSONObject("data");

                                Pic=user.getString("profile_pic");
                                name=user.getString("name");
                                email=user.getString("email");
                                mobile=user.getString("mobile");
                                gender=user.getString("gender");
                                dob=user.getString("birth_date");
                                education=user.getString("education");
                                city=user.getString("city");

                                Picasso.with(getActivity()).load(Pic).into(Profile_pic);
                                User_name.setText(name);
                                User_email.setText(email);
                                User_mobile.setText(mobile);
                                User_gender.setText(gender);
                                User_dob.setText(dob);
                                User_education.setText(education);
                                User_city.setText(city);


                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
