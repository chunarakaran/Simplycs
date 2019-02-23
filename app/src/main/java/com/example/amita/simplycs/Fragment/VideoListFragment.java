package com.example.amita.simplycs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.example.amita.simplycs.Adapter.PDFListDataAdapter;
import com.example.amita.simplycs.Adapter.YoutubeVideoAdapter;
import com.example.amita.simplycs.R;
import com.example.amita.simplycs.YoutubePlayerActivity;
import com.example.amita.simplycs.model.YoutubeVideoModel;
import com.example.amita.simplycs.utils.RecyclerViewOnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoListFragment extends Fragment
{

    private RecyclerView recyclerView;

    String Topic_id,SubTopic_id;

    final ArrayList<PDFListDataAdapter> Contentid = new ArrayList<>();

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
        rootview = inflater.inflate(R.layout.fragment_videolist, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");

        Bundle bundle=getArguments();
        Topic_id=String.valueOf(bundle.getString("topic_id"));
        SubTopic_id=String.valueOf(bundle.getString("subTopic_id"));


        requestQueue = Volley.newRequestQueue(getActivity());
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        setUpRecyclerView();
        populateRecyclerView();


        return rootview;
    }

    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = rootview.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateDummyVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getActivity(), youtubeVideoModelArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(getActivity(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //start youtube player activity by passing selected video id via intent
                startActivity(new Intent(getActivity(), YoutubePlayerActivity.class)
                        .putExtra("video_id", youtubeVideoModelArrayList.get(position).getVideoId()));

            }
        }));
    }


    /**
     * method to generate dummy array list of videos
     *
     * @return
     */
    private ArrayList<YoutubeVideoModel> generateDummyVideoList() {
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();

        //get the video id array, title array and duration array from strings.xml
        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
        String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array);
        String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array);




        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/GetTopicContent",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {
//                                Toast.makeText(getActivity(), "success", Toast.LENGTH_LONG).show();

                                ArrayList<String> videoIDArray = new ArrayList<String>();
                                ArrayList<String> videoTitleArray = new ArrayList<String>();
                                ArrayList<String> videoDurationArray = new ArrayList<String>();
                                JSONArray jsonArray=jObj.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++)
                                {

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String video_title=jsonObject1.getString("video_title");
                                    String video_url=jsonObject1.getString("video_url");
                                    String video_lenght=jsonObject1.getString("video_lenght");







                                    if (video_title.equalsIgnoreCase("null")){

                                    }
                                    else
                                    {
                                        videoIDArray.add(video_url);
                                        videoTitleArray.add(video_title);

                                    }


                                }



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
                params.put("TopicId", Topic_id);
                params.put("SubTopicId", SubTopic_id);

                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);







        for (int i = 0; i < videoIDArray.length; i++) {

            YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
            youtubeVideoModel.setVideoId(videoIDArray[i]);
            youtubeVideoModel.setTitle(videoTitleArray[i]);
            youtubeVideoModel.setDuration(videoDurationArray[i]);

            youtubeVideoModelArrayList.add(youtubeVideoModel);

        }

        return youtubeVideoModelArrayList;
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
