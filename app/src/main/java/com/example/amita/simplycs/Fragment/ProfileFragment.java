package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
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
import com.example.amita.simplycs.Adapter.SingleUploadBroadcastReceiver;
import com.example.amita.simplycs.R;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements SingleUploadBroadcastReceiver.Delegate
{

    private int PICK_IMAGE_REQUEST = 100;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    TextView User_name,User_email,User_mobile,User_gender,User_dob,User_education,User_city;
    TextView EditButton,ChangePassword;
    Fragment fragment;
    ImageView Profile_pic;

    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;


    private static final String TAG = "AndroidUploadService";
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getActivity());
    }




    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);




        Initialize();

        GetProfile();



        Profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Requesting storage permission
                requestStoragePermission();

                showFileChooser();
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



    //method to show file chooser
    private void showFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        i.setType("image/*");
//        i.putExtra("crop", "true");
//        i.putExtra("outputX", 200);
//        i.putExtra("outputY", 200);
//        i.putExtra("aspectX", 1);
//        i.putExtra("aspectY", 1);
//        i.putExtra("scale", true);
//        i.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
//        i.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                Profile_pic.setImageBitmap(bitmap);
                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        cursor.close();

        return path;
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadMultipart() {


        //getting the actual path of the image
        String path = getPath(filePath);


//        Bitmap bitmap = params[0];
//        String uploadImage = getStringImage(bitmap);


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, URL+"api/UpdateProfilePicture")
                    .addFileToUpload(path, "Image") //Adding file
                    .addHeader("Auth", User_id) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
//            Toast.makeText(getActivity(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();

        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    public void Initialize()
    {
        ChangePassword=(TextView)rootview.findViewById(R.id.pass1);
        EditButton=(TextView) rootview.findViewById(R.id.edit_profile);
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

                                Picasso.with(getActivity()).load(Pic).into(Profile_pic);
                                User_name.setText(name);
                                User_email.setText(email);
                                User_mobile.setText(mobile);
                                User_gender.setText(gender);

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
    public void onProgress(int progress) {
        //your implementation

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        //your implementation
        pDialog.setMessage("Please Wait...");
        showDialog();
    }

    @Override
    public void onError(Exception exception) {
        //your implementation
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //your implementation
        hideDialog();

    }

    @Override
    public void onCancelled() {
        //your implementation
    }
}
