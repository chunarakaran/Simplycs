package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;

import java.util.ArrayList;

public class TestResult_RecycleAdapter extends RecyclerView.Adapter<TestResult_RecycleAdapter.ViewHolder> {
    ArrayList<Quetion_Model_List> mlist = new ArrayList<Quetion_Model_List>();
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        WebView txt_quetion;
        ImageView img1, img2, img3, img4;
        TextView tvOption1, tvOption2, tvOption3, tvOption4;

        public ViewHolder(View v) {
            super(v);
            txt_quetion = (WebView) v.findViewById(R.id.txt_quetion);
            img1 = (ImageView) v.findViewById(R.id.img1);
            img2 = (ImageView) v.findViewById(R.id.img2);
            img3 = (ImageView) v.findViewById(R.id.img3);
            img4 = (ImageView) v.findViewById(R.id.img4);
            tvOption1 = (TextView) v.findViewById(R.id.tvOption1);
            tvOption2 = (TextView) v.findViewById(R.id.tvOption2);
            tvOption3 = (TextView) v.findViewById(R.id.tvOption3);
            tvOption4 = (TextView) v.findViewById(R.id.tvOption4);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TestResult_RecycleAdapter(ArrayList<Quetion_Model_List> list, Context ct) {
        super();
        this.mlist = list;
        this.context = ct;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TestResult_RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_test_result_list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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
                holder.img1.setImageResource(R.drawable.right_green);
            } else if (p.getOp2().equalsIgnoreCase(p.getAnswer())) {
                holder.img2.setImageResource(R.drawable.right_green);
            } else if (p.getOp3().equalsIgnoreCase(p.getAnswer())) {
                holder.img3.setImageResource(R.drawable.right_green);
            } else if (p.getOp4().equalsIgnoreCase(p.getAnswer())) {
                holder.img4.setImageResource(R.drawable.right_green);
            }
            if (p.getOp1().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img1.setImageResource(R.drawable.wrong_red);
                }
            } else if (p.getOp2().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img2.setImageResource(R.drawable.wrong_red);
                }
            } else if (p.getOp3().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img3.setImageResource(R.drawable.wrong_red);
                }
            } else if (p.getOp4().equalsIgnoreCase(p.getSelectedAnswer())) {
                if (!p.getSelectedAnswer().equalsIgnoreCase(p.getAnswer())) {
                    holder.img4.setImageResource(R.drawable.wrong_red);
                }
            }

        } else {
            holder.img1.setImageResource(R.drawable.dark_circle_grey);
            holder.img2.setImageResource(R.drawable.dark_circle_grey);
            holder.img3.setImageResource(R.drawable.dark_circle_grey);
            holder.img4.setImageResource(R.drawable.dark_circle_grey);

            if (p.getOp1().equalsIgnoreCase(p.getAnswer())) {
                holder.img1.setImageResource(R.drawable.right_green);
            } else if (p.getOp2().equalsIgnoreCase(p.getAnswer())) {
                holder.img2.setImageResource(R.drawable.right_green);
            } else if (p.getOp3().equalsIgnoreCase(p.getAnswer())) {
                holder.img3.setImageResource(R.drawable.right_green);
            } else if (p.getOp4().equalsIgnoreCase(p.getAnswer())) {
                holder.img4.setImageResource(R.drawable.right_green);
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
