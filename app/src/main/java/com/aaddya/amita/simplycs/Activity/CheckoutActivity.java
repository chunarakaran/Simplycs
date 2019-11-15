package com.aaddya.amita.simplycs.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Fragment.AskusFragment;
import com.aaddya.amita.simplycs.Fragment.CourseFragment;
import com.aaddya.amita.simplycs.R;
import com.aaddya.amita.simplycs.Utils.Constants;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;


public class CheckoutActivity extends AppCompatActivity implements CFClientInterface {

    String User_id,Package_id,cftoken,Order_id,Order_Amount,CustomerName,CustomerPhone,CustomerEmail,course_name,jChapter_id;

    TextView CourseName,Amount,TransactionId;


    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog progressDialog;

    PromptDialog promptDialog;

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        requestQueue = Volley.newRequestQueue(this);
        URL = getString(R.string.url);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        promptDialog = new PromptDialog(this);

        User_id = getIntent().getStringExtra("User_id");
        Package_id = getIntent().getStringExtra("Package_id");
        cftoken = getIntent().getStringExtra("cftoken");
        Order_id = getIntent().getStringExtra("Order_id");
        Order_Amount = getIntent().getStringExtra("Order_Amount");
        CustomerName = getIntent().getStringExtra("CustomerName");
        CustomerPhone = getIntent().getStringExtra("CustomerPhone");
        CustomerEmail = getIntent().getStringExtra("CustomerEmail");
        course_name =getIntent().getStringExtra("course_name");
        jChapter_id=getIntent().getStringExtra("jChapter_id");

//        Toast.makeText(getApplicationContext(),Package_id,Toast.LENGTH_SHORT).show();

        Initialization();


    }

    public void Initialization(){

        CourseName=(TextView)findViewById(R.id.course_name);
        TransactionId=(TextView)findViewById(R.id.transaction_id);
        Amount=(TextView)findViewById(R.id.amount);

        CourseName.setText(course_name);
        TransactionId.setText(Order_id);
        Amount.setText("\u20B9 "+Order_Amount);
    }

    private void triggerPayment(boolean isUpiIntent) throws JSONException, JSONException {
        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        String token = cftoken;


        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "TEST";

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = Constants.APP_ID;
        String orderId = Order_id;
        String orderAmount = Order_Amount;
        String orderNote = "Test Order";
        String customerName = CustomerName;
        String customerPhone = CustomerPhone;
        String customerEmail = CustomerEmail;

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL,customerEmail);


        for(Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);

        if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.gPayPayment(this, params, token, this, stage);
        }
        else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage);
        }

    }


    public void doPayment(View view) throws JSONException {
        this.triggerPayment(false);
    }

    public void upiPayment(View view) throws JSONException {
        this.triggerPayment(true);
    }


    @Override
    public void onSuccess(Map<String, String> map) {
//        Log.d("CFSDKSample", "Payment Success");



        SuccessPayment();

    }

    @Override
    public void onFailure(Map<String, String> map) {
        Log.d("CFSDKSample", "Payment Failure");
    }

    @Override
    public void onNavigateBack() {
        Log.d("CFSDKSample", "Back Pressed");
    }


    public void SuccessPayment()
    {
//        progressDialog.setMessage("Please Wait...");
//        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/SuccessPayment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {

//                                String test;
//                                JSONObject data = jObj.getJSONObject("data");
//
//                                test=data.getString("OrderId");

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Payment Success");
//                                promptDialog.setContentText(test);
                                promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {

                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();

                                        dialog.dismiss();
                                    }
                                }).show();


//                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
//                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
//                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.

                        // Showing error message if something goes wrong.
                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
//                        hideDialog();
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
                params.put("PackageId", Package_id);
                params.put("Amount", Order_Amount);
                params.put("OrderId", Order_id);
                params.put("Signature", cftoken);

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

