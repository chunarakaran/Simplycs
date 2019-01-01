package com.example.amita.simplycs.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amita.simplycs.Adapter.TransparentProgressDialog;
import com.example.amita.simplycs.R;

public class AskusFragment extends Fragment
{

    TransparentProgressDialog mProgressDialog;

    TextView Submit;
    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_askus, container, false);

        Submit=(TextView)rootview.findViewById(R.id.submit);


        mProgressDialog = new TransparentProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.show();


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
            }
        });

        return rootview;
    }


}
