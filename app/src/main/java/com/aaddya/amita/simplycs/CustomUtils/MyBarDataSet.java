package com.aaddya.amita.simplycs.CustomUtils;

import android.util.Log;

import com.aaddya.amita.simplycs.Activity.TestResultActivity;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {


    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        Log.i("Index", "Index of chart" + index);
        if (TestResultActivity.object.get(index).getSelectedAnswer() != null) {
            if (TestResultActivity.object.get(index).getAnswer().equalsIgnoreCase(TestResultActivity.object.get(index).getSelectedAnswer())) {
                return mColors.get(0);
            } else {
                return mColors.get(1);
            }
        } else {
            return mColors.get(2);
        }
    }

}