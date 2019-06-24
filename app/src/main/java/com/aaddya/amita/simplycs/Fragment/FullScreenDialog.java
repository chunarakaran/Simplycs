package com.aaddya.amita.simplycs.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Model.Notification_Model_List;
import com.aaddya.amita.simplycs.Adapter.Notification_List_Adapter;
import com.aaddya.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class FullScreenDialog extends DialogFragment  {

    public static String TAG = "FullScreenDialog";

    List<Notification_Model_List> ListOfdataAdapter;

    LinearLayout emptyView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String CDate,Category_id,SubCategory_id,SubCategory_name,content_id,content_name,content_Data;

    final ArrayList<Notification_Model_List> Contentid = new ArrayList<>();

    final ArrayList<Notification_Model_List> ContentData = new ArrayList<>();

    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id,Course_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    PromptDialog promptDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_full_screen_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(view1 -> cancelUpload());
        toolbar.setTitle("Notification");


        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        Course_id = sp.getString("Courseid", "");


        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(getActivity());

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview1);
        emptyView=(LinearLayout)view.findViewById(R.id.empty_view);
        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        GetNotificationList();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }



    private void cancelUpload() {

        android.app.Fragment prev = getFragmentManager().findFragmentByTag(TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }


    public void show(FragmentTransaction ft, String tag) {
    }


    public void GetNotificationList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetNotification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("false"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("content");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    Notification_Model_List GetDataAdapter2=new Notification_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("content_name"));
                                    GetDataAdapter2.setContentData(jsonObject1.getString("content_data"));




                                    Contentid.add(GetDataAdapter2);


                                    ContentData.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }

                                JSONArray jsonArray1=jObj.getJSONArray("exist_content");
                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    Notification_Model_List GetDataAdapter2=new Notification_Model_List();
                                    JSONObject jsonObject1=jsonArray1.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("content_name"));
                                    GetDataAdapter2.setContentData(jsonObject1.getString("content_data"));




                                    Contentid.add(GetDataAdapter2);


                                    ContentData.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }

                                JSONArray jsonArray2=jObj.getJSONArray("test");
                                for(int i=0;i<jsonArray2.length();i++)
                                {
                                    Notification_Model_List GetDataAdapter2=new Notification_Model_List();
                                    JSONObject jsonObject1=jsonArray2.getJSONObject(i);


                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("test_name"));





                                    Contentid.add(GetDataAdapter2);

                                    ListOfdataAdapter.add(GetDataAdapter2);

                                }



                                adapter = new Notification_List_Adapter(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);

                                if(adapter.getItemCount()==0)
                                {
                                    recyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                }
                                else{
                                    recyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                }

                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("true")){
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

            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("CourseId", Course_id);

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
}
