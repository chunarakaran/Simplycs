package com.aaddya.amita.simplycs.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.Activity.MainActivity;
import com.aaddya.amita.simplycs.Adapter.BroughtCourse_List_Adapter;
import com.aaddya.amita.simplycs.Adapter.User_Course_List_Adapter;
import com.aaddya.amita.simplycs.Model.BroughtCourse_Model_List;
import com.aaddya.amita.simplycs.Model.UserCourse_Model_List;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aaddya.amita.simplycs.Model.Package_Model_List;
import com.aaddya.amita.simplycs.Adapter.Course_List_Adapter;
import com.aaddya.amita.simplycs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class CourseFragment extends Fragment
{

    TextView Add_More,View_courselist;
    TextView Course_Name;

    List<UserCourse_Model_List> ListOfdataAdapter;
    List<Package_Model_List> ListOfdataAdapter1;
    List<BroughtCourse_Model_List> ListOfdataAdapter2;

    RecyclerView recyclerView,recyclerView1,recyclerView2;
    RecyclerView.Adapter adapter,adapter1,adapter2;

    String Ucourse_id;
    final ArrayList<UserCourse_Model_List> UCourse_id = new ArrayList<>();
    int RecyclerViewItemPosition ;


    String course_id,course_name;
    final ArrayList<Package_Model_List> Course_id = new ArrayList<>();
    final ArrayList<Package_Model_List> Course_name = new ArrayList<>();
    int RecyclerViewItemPosition1 ;

    LinearLayoutManager layoutManagerOfrecyclerView,layoutManagerOfrecyclerView1,layoutManagerOfrecyclerView2;

    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;
    String URL;
    private ProgressDialog pDialog;

    PromptDialog promptDialog;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_course, container, false);


        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        toolbar.setTitle("Course");


        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");



        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promptDialog = new PromptDialog(getActivity());

        Course_Name=(TextView)rootview.findViewById(R.id.current_course);

        ListOfdataAdapter = new ArrayList<>();
        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview);

        layoutManagerOfrecyclerView = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        GetUserCourseList();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });


            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent)
            {


                rootview = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(rootview != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(rootview);

                    Ucourse_id=UCourse_id.get(RecyclerViewItemPosition).getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to Switch Course?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    Toast.makeText(getActivity(), "You clicked " + course_id, Toast.LENGTH_SHORT).show();

                                    pDialog.setMessage("Please Wait...");
                                    showDialog();

                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/SwitchUserCourse",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String ServerResponse) {

                                                    try {

                                                        JSONObject jObj = new JSONObject(ServerResponse);
                                                        String success = jObj.getString("success");

                                                        if(success.equalsIgnoreCase("true"))
                                                        {
                                                            ((MainActivity)getActivity()).Logout();
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
                                                    // Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                                                    hideDialog();
                                                }
                                            }) {

                                        @Override
                                        protected Map<String, String> getParams() {

                                            // Creating Map String Params.
                                            Map<String, String> params = new HashMap<String, String>();

                                            // Adding All values to Params.
                                            params.put("CourseId", Ucourse_id);

                                            return params;
                                        }

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
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();





                }



                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        ListOfdataAdapter1 = new ArrayList<>();
        recyclerView1 = (RecyclerView)rootview.findViewById(R.id.recyclerview1);

        layoutManagerOfrecyclerView1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerOfrecyclerView1.setReverseLayout(true);
        layoutManagerOfrecyclerView1.setStackFromEnd(true);

        recyclerView1.setLayoutManager(layoutManagerOfrecyclerView1);

        GetPackageList();


        recyclerView1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });


            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent)
            {


                rootview = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(rootview != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition1 = Recyclerview.getChildAdapterPosition(rootview);

                    course_id=Course_id.get(RecyclerViewItemPosition1).getId();
                    course_name=Course_name.get(RecyclerViewItemPosition1).getCourseTitle();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();
                    PackageDetailFragment mfragment=new PackageDetailFragment();

                    Bundle bundle=new Bundle();
                    bundle.putString("course_id",course_id);
                    bundle.putString("course_name",course_name);
                    mfragment.setArguments(bundle);

                    transection.replace(R.id.content_frame, mfragment);
                    transection.addToBackStack(null).commit();


//                    Toast.makeText(getActivity(), "You clicked " + course_id, Toast.LENGTH_SHORT).show();


                }



                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        ListOfdataAdapter2 = new ArrayList<>();
        recyclerView2 = (RecyclerView)rootview.findViewById(R.id.recyclerview2);

        layoutManagerOfrecyclerView2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerOfrecyclerView2.setReverseLayout(true);
        layoutManagerOfrecyclerView2.setStackFromEnd(true);
        recyclerView2.setLayoutManager(layoutManagerOfrecyclerView2);

        GetBroughtPackageList();


        View_courselist=(TextView)rootview.findViewById(R.id.view_courselist);

        View_courselist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transection=getFragmentManager().beginTransaction();
                PackageListFragment mfragment=new PackageListFragment();
                transection.replace(R.id.content_frame, mfragment);
                transection.addToBackStack(null).commit();
            }
        });

        Add_More=(TextView)rootview.findViewById(R.id.add_more);

        Add_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();

//              startActivity(new Intent(getActivity(),AddcourseActivity.class));
//              getActivity().finish();

                FragmentTransaction transection=getFragmentManager().beginTransaction();
                AddCourseFragment mfragment=new AddCourseFragment();
                transection.replace(R.id.content_frame, mfragment);
                transection.addToBackStack(null).commit();



            }
        });

        return rootview;
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

    public void GetUserCourseList()
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
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONObject user = jObj.getJSONObject("data");
                                JSONArray jsonArray=jObj.getJSONArray("user_course");
                                String Current_course=user.getString("current_course");

                                Course_Name.setText(Current_course);

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    UserCourse_Model_List userCourse_model_list=new UserCourse_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    userCourse_model_list.setId(jsonObject1.getString("course_id"));
                                    userCourse_model_list.setUser_courseName(jsonObject1.getString("course_name"));




                                    UCourse_id.add(userCourse_model_list);

                                    ListOfdataAdapter.add(userCourse_model_list);

                                }



                                adapter = new User_Course_List_Adapter(ListOfdataAdapter,getActivity());
                                recyclerView.setAdapter(adapter);


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
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
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

    public void GetPackageList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/GetPackagelist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("package");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    Package_Model_List package_model_list=new Package_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    package_model_list.setId(jsonObject1.getString("id"));
                                    package_model_list.setImageUrl(jsonObject1.getString("file_path"));
                                    package_model_list.setCourseTitle(jsonObject1.getString("package_name"));
                                    package_model_list.setCourseDesc(String.valueOf(Html.fromHtml(jsonObject1.getString("pakage_details"))));
                                    package_model_list.setCoursePrice(jsonObject1.getString("package_price"));
                                    package_model_list.setCourseFinalPrice(jsonObject1.getString("final_price"));
                                    package_model_list.setCourseDiscount(jsonObject1.getString("discount_percentage"));
                                    package_model_list.setCourseStartDate(jsonObject1.getString("date"));
                                    package_model_list.setDaysLeft(jsonObject1.getString("left_days"));
                                    package_model_list.setIsBUy(jsonObject1.getString("is_buy"));



                                    Course_id.add(package_model_list);
                                    Course_name.add(package_model_list);

                                    ListOfdataAdapter1.add(package_model_list);

                                }



                                adapter1 = new Course_List_Adapter(ListOfdataAdapter1,getActivity());
                                recyclerView1.setAdapter(adapter1);


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
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
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

    public void GetBroughtPackageList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL+"api/BroughtPackage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
                                JSONArray jsonArray=jObj.getJSONArray("package");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    BroughtCourse_Model_List broughtCourse_model_list=new BroughtCourse_Model_List();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);


                                    broughtCourse_model_list.setId(jsonObject1.getString("id"));
                                    broughtCourse_model_list.setTitle(jsonObject1.getString("package_name"));
                                    broughtCourse_model_list.setFromDate(jsonObject1.getString("date"));
                                    broughtCourse_model_list.setToDate(String.valueOf(Html.fromHtml(jsonObject1.getString("to_date"))));
                                    broughtCourse_model_list.setDaysLeft(jsonObject1.getString("left_days"));


                                    ListOfdataAdapter2.add(broughtCourse_model_list);

                                }

                                adapter2 = new BroughtCourse_List_Adapter(ListOfdataAdapter2,getActivity());
                                recyclerView2.setAdapter(adapter2);

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
//                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
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

    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
