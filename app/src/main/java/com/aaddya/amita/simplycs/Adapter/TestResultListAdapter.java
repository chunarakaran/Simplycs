package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.ArrayList;

public class TestResultListAdapter extends BaseAdapter {
    Context ct;
    LayoutInflater inflater;
    ArrayList<Quetion_Model_List> mlist = new ArrayList<Quetion_Model_List>();

    public TestResultListAdapter(Context ct, ArrayList<Quetion_Model_List> list) {
        this.ct = ct;
        this.mlist = list;
        this.inflater = LayoutInflater.from(ct);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_test_result_list_row, null);
            holder.txt_quetion = (WebView) convertView.findViewById(R.id.txt_quetion);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.img3);
            holder.img4 = (ImageView) convertView.findViewById(R.id.img4);
            holder.tvOption1 = (TextView) convertView.findViewById(R.id.tvOption1);
            holder.tvOption2 = (TextView) convertView.findViewById(R.id.tvOption2);
            holder.tvOption3 = (TextView) convertView.findViewById(R.id.tvOption3);
            holder.tvOption4 = (TextView) convertView.findViewById(R.id.tvOption4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Quetion_Model_List p = mlist.get(position);
        int srno = position + 1;
        holder.txt_quetion.loadData(srno + " " + p.getQuestion(), "text/html", null);
        holder.tvOption1.setText(p.getOp1());
        holder.tvOption2.setText(p.getOp2());
        holder.tvOption3.setText(p.getOp3());
        holder.tvOption4.setText(p.getOp4());
        if (p.getSelectedAnswer() != null) {
            holder.img1.setImageResource(R.drawable.dark_circle_grey);
            holder.img2.setImageResource(R.drawable.dark_circle_grey);
            holder.img3.setImageResource(R.drawable.dark_circle_grey);
            holder.img4.setImageResource(R.drawable.dark_circle_grey);
            if (p.getOp1().equalsIgnoreCase(p.getAnswer())) {
                holder.img1.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp2().equalsIgnoreCase(p.getAnswer())) {
                holder.img2.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp3().equalsIgnoreCase(p.getAnswer())) {
                holder.img3.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp4().equalsIgnoreCase(p.getAnswer())) {
                holder.img4.setImageResource(R.drawable.ic_right_green);
            }
            if (p.getOp1().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img1.setImageResource(R.drawable.ic_wrong_red);
                }
            } else if (p.getOp2().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img2.setImageResource(R.drawable.ic_wrong_red);
                }
            } else if (p.getOp3().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img3.setImageResource(R.drawable.ic_wrong_red);
                }
            } else if (p.getOp4().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img4.setImageResource(R.drawable.ic_wrong_red);
                }
            }

        } else {
            holder.img1.setImageResource(R.drawable.dark_circle_grey);
            holder.img2.setImageResource(R.drawable.dark_circle_grey);
            holder.img3.setImageResource(R.drawable.dark_circle_grey);
            holder.img4.setImageResource(R.drawable.dark_circle_grey);

            if (p.getOp1().equalsIgnoreCase(p.getAnswer())) {
                holder.img1.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp2().equalsIgnoreCase(p.getAnswer())) {
                holder.img2.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp3().equalsIgnoreCase(p.getAnswer())) {
                holder.img3.setImageResource(R.drawable.ic_right_green);
            } else if (p.getOp4().equalsIgnoreCase(p.getAnswer())) {
                holder.img4.setImageResource(R.drawable.ic_right_green);
            }
        }
        return convertView;
    }

    class ViewHolder {
        WebView txt_quetion;
        ImageView img1, img2, img3, img4;
        TextView tvOption1, tvOption2, tvOption3, tvOption4;
    }
}
