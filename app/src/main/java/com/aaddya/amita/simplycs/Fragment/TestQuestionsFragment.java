package com.aaddya.amita.simplycs.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;

public class TestQuestionsFragment extends Fragment {
    TextView tvTimer;
    WebView txt_quetion;
    RadioGroup radiogrp;
    RadioButton radio1, radio2, radio3, radio4;
    Quetion_Model_List p;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test_questions, null);
        tvTimer = (TextView) v.findViewById(R.id.tvTimer);
        txt_quetion = (WebView) v.findViewById(R.id.txt_quetion);
        radiogrp = (RadioGroup) v.findViewById(R.id.radiogrp);
        radio1 = (RadioButton) v.findViewById(R.id.radio1);
        radio2 = (RadioButton) v.findViewById(R.id.radio2);
        radio3 = (RadioButton) v.findViewById(R.id.radio3);
        radio4 = (RadioButton) v.findViewById(R.id.radio4);
        int srno = QuestionFragment.currentPos + 1;
        p = (Quetion_Model_List) QuestionFragment.rowListItem.get(QuestionFragment.currentPos);
        txt_quetion.loadData(srno + " " + p.getQuestion(), "text/html", null);
        radio1.setText(p.getOp1());
        radio2.setText(p.getOp2());
        radio3.setText(p.getOp3());
        radio4.setText(p.getOp4());
        radiogrp.check(p.getSelctedId());
        radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();

                // find the radio button by returned id
                RadioButton radioButton = (RadioButton) group.findViewById(selectedId);


                p.setSelectedAnswer(radioButton.getText() + "");
                p.setSelctedId(checkedId);
            }
        });
        p.setIsStopTimer(1);
        runTimer(p);

        return v;
    }
    private void runTimer(Quetion_Model_List p) {
        if (p.getIsStopTimer() == 1) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int availSec = p.getTimeTakeSec();
                    availSec++;
                    p.setTimeTakeSec(availSec);
                    String output = getDurationString(availSec);
                    tvTimer.setText(output);
                    runTimer(p);
                }
            }, 1000);
        }

    }
    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
