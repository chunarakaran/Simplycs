package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aaddya.amita.simplycs.R;

import java.util.List;

public class ExamListRecyclerViewAdapter extends RecyclerView.Adapter<ExamListRecyclerViewAdapter.ViewHolder> {


    Context context;

    List<ExamListDataAdapter> dataAdapters;

    ImageLoader imageLoader;

    public ExamListRecyclerViewAdapter(List<ExamListDataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_exams, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        ExamListDataAdapter dataAdapterOBJ =  dataAdapters.get(position);

        imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getImageUrl(),
                ImageLoader.getImageListener(
                        Viewholder.VollyImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.VollyImageView.setImageUrl(dataAdapterOBJ.getImageUrl(), imageLoader);

        Viewholder.ExamTitle.setText(dataAdapterOBJ.getExamTitle());
        Viewholder.ExamDesc.setText(dataAdapterOBJ.getexamDesc());
        Viewholder.ExamDate.setText("Starts From:"+dataAdapterOBJ.getExamDate());
        Viewholder.Exam_start_Time.setText(dataAdapterOBJ.getExam_start_Time());
        Viewholder.Exam_end_Time.setText("To "+dataAdapterOBJ.getExam_end_Time());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ExamTitle,ExamDesc,ExamDate,Exam_start_Time,Exam_end_Time;
        public NetworkImageView VollyImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            VollyImageView = (NetworkImageView) itemView.findViewById(R.id.VolleyImageView) ;

            ExamTitle = (TextView) itemView.findViewById(R.id.examTitle) ;
            ExamDesc = (TextView) itemView.findViewById(R.id.examDesc) ;
            ExamDate = (TextView) itemView.findViewById(R.id.examDate) ;
            Exam_start_Time = (TextView) itemView.findViewById(R.id.exam_start_Time) ;
            Exam_end_Time = (TextView) itemView.findViewById(R.id.exam_end_Time) ;

        }
    }

}
