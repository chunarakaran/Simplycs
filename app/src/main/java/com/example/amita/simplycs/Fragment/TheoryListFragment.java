package com.example.amita.simplycs.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amita.simplycs.R;

public class TheoryListFragment extends Fragment
{

    String Topic_id,SubTopic_id;

    String User_id;
    public static final String PREFS_NAME = "login";

    TextView Test;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_theorylist, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");


        Bundle bundle=getArguments();
        Topic_id=String.valueOf(bundle.getString("topic_id"));
        SubTopic_id=String.valueOf(bundle.getString("subTopic_id"));

        Test=(TextView)rootview.findViewById(R.id.test);

        Test.setText(SubTopic_id);

        return rootview;
    }

}
