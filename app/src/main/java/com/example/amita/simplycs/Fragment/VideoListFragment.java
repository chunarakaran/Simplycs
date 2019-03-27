package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.Adapter.VideoListDataAdapter;
import com.example.amita.simplycs.Adapter.VideoListRecyclerViewAdapter;
import com.example.amita.simplycs.R;
import com.example.amita.simplycs.YoutubePlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoListFragment extends Fragment
{

    List<VideoListDataAdapter> ListOfdataAdapter;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    String CDate,Category_id,SubCategory_id,content_id,video_url;

    final ArrayList<VideoListDataAdapter> Contentid = new ArrayList<>();
    final ArrayList<VideoListDataAdapter> Video_URL = new ArrayList<>();

    int RecyclerViewItemPosition ;

    LinearLayoutManager layoutManagerOfrecyclerView;

    String User_id,Course_id;
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
        rootview = inflater.inflate(R.layout.fragment_videolist, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        Course_id = sp.getString("Courseid", "");

        Bundle bundle=getArguments();
        Category_id=String.valueOf(bundle.getString("Category_id"));
        SubCategory_id=String.valueOf(bundle.getString("SubCategory_id"));
        CDate=String.valueOf(bundle.getString("CDate"));

        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);




        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManagerOfrecyclerView=new LinearLayoutManager(getActivity());
        layoutManagerOfrecyclerView.setReverseLayout(true);
        layoutManagerOfrecyclerView.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);


        GetVideoList();


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

                    content_id=Contentid.get(RecyclerViewItemPosition).getId();

                    video_url=Video_URL.get(RecyclerViewItemPosition).getVideoURL();


                    startActivity(new Intent(getActivity(),YoutubePlayerActivity.class)
                            .putExtra("video_url", video_url));


//                    Toast.makeText(getActivity(), "You clicked " + video_url, Toast.LENGTH_SHORT).show();


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


        return rootview;
    }



    public void GetVideoList()
    {
        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetContent",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                JSONArray jsonArray=jObj.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    VideoListDataAdapter GetDataAdapter2=new VideoListDataAdapter();
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String video_title=jsonObject1.getString("video_title");
                                    String video_url=jsonObject1.getString("video_url");

                                    GetDataAdapter2.setId(jsonObject1.getString("id"));
                                    GetDataAdapter2.setTitle(jsonObject1.getString("video_title"));
                                    GetDataAdapter2.setVideoURL(jsonObject1.getString("video_url"));
                                    GetDataAdapter2.setDuration(jsonObject1.getString("video_lenght"));

                                    if (video_title.equalsIgnoreCase("null")){

                                    }
                                    else
                                    {

                                        Contentid.add(GetDataAdapter2);

                                        Video_URL.add(GetDataAdapter2);

                                        ListOfdataAdapter.add(GetDataAdapter2);
                                    }


                                }


                                adapter = new VideoListRecyclerViewAdapter(ListOfdataAdapter,getActivity());
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
                params.put("TopicId", Category_id);
                params.put("SubTopicId", SubCategory_id);
                params.put("Date", CDate);

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
