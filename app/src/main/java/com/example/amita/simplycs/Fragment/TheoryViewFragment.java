package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.amita.simplycs.R;

public class TheoryViewFragment extends Fragment
{

    String Content_id,content_name,Content_Data;



    WebView webView;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_theory_view, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);



        Bundle bundle=getArguments();
        content_name=String.valueOf(bundle.getString("content_name"));
        Content_Data=String.valueOf(bundle.getString("content_Data"));



        webView = (WebView)rootview.findViewById(R.id.webView);
        webView.loadData(Content_Data, "text/html", null);



        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);


        return rootview;
    }

}
