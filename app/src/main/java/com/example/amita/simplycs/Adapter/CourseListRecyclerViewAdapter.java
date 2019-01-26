package com.example.amita.simplycs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amita.simplycs.R;

import java.util.List;

public class CourseListRecyclerViewAdapter extends RecyclerView.Adapter<CourseListRecyclerViewAdapter.ViewHolder> {


    Context context;

    List<CourseListDataAdapter> dataAdapters;

    ImageLoader imageLoader;

    public CourseListRecyclerViewAdapter(List<CourseListDataAdapter> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_courses, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        CourseListDataAdapter dataAdapterOBJ =  dataAdapters.get(position);

        imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getImageUrl(),
                ImageLoader.getImageListener(
                        Viewholder.VollyImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        Viewholder.VollyImageView.setImageUrl(dataAdapterOBJ.getImageUrl(), imageLoader);

        Viewholder.CourseTitle.setText(dataAdapterOBJ.getCourseTitle());
        Viewholder.CourseDesc.setText(dataAdapterOBJ.getCourseDesc());
        Viewholder.CoursePrice.setText("\u20B9"+dataAdapterOBJ.getCoursePrice());
        Viewholder.CourseDiscount.setText(dataAdapterOBJ.getCourseDiscount()+"% off");
//        Viewholder.CourseHours.setText(dataAdapterOBJ.getCourseHours());

    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CourseTitle,CourseDesc,CoursePrice,CourseDiscount,CourseHours;
        public NetworkImageView VollyImageView ;

        public ViewHolder(View itemView) {

            super(itemView);

            VollyImageView = (NetworkImageView) itemView.findViewById(R.id.VolleyImageView) ;

            CourseTitle = (TextView) itemView.findViewById(R.id.courseTitle) ;
            CourseDesc = (TextView) itemView.findViewById(R.id.courseDesc) ;
            CoursePrice = (TextView) itemView.findViewById(R.id.coursePrice) ;
            CourseDiscount = (TextView) itemView.findViewById(R.id.courseDiscount) ;
//            CourseHours = (TextView) itemView.findViewById(R.id.courseHours) ;

        }
    }

}
