package com.aaddya.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aaddya.amita.simplycs.R;

public class StartSubjectiveTestFragment extends Fragment
{

    Button StartTest;

    String CDate,Category_id,SubCategory_id,SubCategory_name,test_id,test_name,test_rules;


    WebView webView;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_startsubjectivetest, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle bundle=getArguments();
        Category_id=String.valueOf(bundle.getString("category_id"));
        SubCategory_id=String.valueOf(bundle.getString("SubCategory_id"));
        SubCategory_name=String.valueOf(bundle.getString("SubCategory_name"));
        CDate=String.valueOf(bundle.getString("CDate"));
        test_id=String.valueOf(bundle.getString("test_id"));
        test_name=String.valueOf(bundle.getString("test_name"));
        test_rules=String.valueOf(bundle.getString("test_rules"));

        toolbar.setTitle(test_name);


        webView = (WebView)rootview.findViewById(R.id.webView);
        String encodedHtml = Base64.encodeToString(test_rules.getBytes(), Base64.NO_PADDING);
        webView.loadData(encodedHtml, "text/html", "base64");

        StartTest=(Button)rootview.findViewById(R.id.start_test);


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);

        StartTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(getActivity(), test_rules, Toast.LENGTH_LONG).show();

                        FragmentTransaction transection = getFragmentManager().beginTransaction();
                        SubjectiveQuestionFragment mfragment = new SubjectiveQuestionFragment();


                        Bundle bundle = new Bundle();
                        bundle.putString("category_id", Category_id);
                        bundle.putString("SubCategory_id", SubCategory_id);
                        bundle.putString("CDate", CDate);
                        bundle.putString("test_id", test_id);
                        bundle.putString("test_name", test_name);

                        mfragment.setArguments(bundle);

                        transection.replace(R.id.content_frame, mfragment);
                        transection.addToBackStack(null).commit();


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
