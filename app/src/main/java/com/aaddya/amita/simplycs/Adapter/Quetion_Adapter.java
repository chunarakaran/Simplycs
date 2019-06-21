package com.aaddya.amita.simplycs.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.aaddya.amita.simplycs.Model.Quetion_Model_List;
import com.aaddya.amita.simplycs.R;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Quetion_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Quetion_Model_List> items = new ArrayList<>();
    private Context ctx;
    private Quetion_Adapter.OnItemClickListener mOnItemClickListener;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public interface OnItemClickListener {
        void onItemClick(View view, Quetion_Model_List obj, int position);
    }

    public void setOnItemClickListener(final Quetion_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public Quetion_Adapter(Context context, List<Quetion_Model_List> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public WebView txt_quetion;
        RadioGroup radiogrp;
        RadioButton radio1,radio2,radio3,radio4;
        public View lyt_parent;
        Button btn_Previous,btn_Next;

        public OriginalViewHolder(View v) {
            super(v);
            txt_quetion = (WebView) v.findViewById(R.id.txt_quetion);
            radiogrp = (RadioGroup) v.findViewById(R.id.radiogrp);
            radio1 = (RadioButton) v.findViewById(R.id.radio1);
            radio2 = (RadioButton) v.findViewById(R.id.radio2);
            radio3 = (RadioButton) v.findViewById(R.id.radio3);
            radio4 = (RadioButton) v.findViewById(R.id.radio4);

            btn_Previous = (Button) v.findViewById(R.id.btn_Previous);
            btn_Next = (Button) v.findViewById(R.id.btn_Next);

            lyt_parent = (View) v.findViewById( R.id.lyt_parent);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quetions, parent, false);
        vh = new Quetion_Adapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Quetion_Adapter.OriginalViewHolder) {
            final Quetion_Adapter.OriginalViewHolder view = (Quetion_Adapter.OriginalViewHolder) holder;

            final Quetion_Model_List p = items.get(position);
            int srno = position+1;
//            view.txt_quetion.setText(srno+".  "+p.getQuestion());
            view.txt_quetion.loadData(srno+" "+p.getQuestion(), "text/html", null);
            view.radio1.setText(p.getOp1());
            view.radio2.setText(p.getOp2());
            view.radio3.setText(p.getOp3());
            view.radio4.setText(p.getOp4());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
          /*  if(position == items.size()-1){
                *//*lastItem*//*
            }
            */

            view.radiogrp.check(p.getSelctedId());
            view.radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    p.setSelctedId(checkedId);
                }
            });









            view.btn_Previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  try {

                        sharedPreferences=ctx.getSharedPreferences("Mydata",MODE_PRIVATE);
                        sharedPreferences.edit();
                        String user_id= sharedPreferences.getString("idtag",null);
                        String user_reg_id= sharedPreferences.getString("reg_idtag",null);

                        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
                        nameValuePairs.add(new BasicNameValuePair("user_reg_id", user_reg_id));
                        nameValuePairs.add(new BasicNameValuePair("interest_userid", p.getId()));
                        nameValuePairs.add(new BasicNameValuePair("interest_user_regid", p.getReg_id()));

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost( MyConfig.Update_Interest_Accept);
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        //  is = entity.getContent();
                        String data = EntityUtils.toString(entity);
                        Log.e("Register", data);
                        if(data.matches( "Record Updated Successfully" )) {

                            Log.e( "pass 1", "connection success " );
                            Toast.makeText( ctx, "Request Accepted", Toast.LENGTH_SHORT ).show();
                            ((Activity)ctx).finish();
                            Intent i = new Intent(ctx, MainActivity.class);
                            ctx.startActivity(i);

                        } if(data.matches( "Something went wrong" )) {
                            //  if(data.matches( "phone number already exits" )) {
                            Toast.makeText( ctx, "status not updated,something went wrong", Toast.LENGTH_SHORT ).show();
                            //   }
                        }
                    } catch (ClientProtocolException e) {
                        Log.e("Fail 1", e.toString());
                        Toast.makeText(ctx, "Invalid IP Address", Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                }
            });
            view.btn_Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  try {

                        sharedPreferences=ctx.getSharedPreferences("Mydata",MODE_PRIVATE);
                        sharedPreferences.edit();
                        String user_id= sharedPreferences.getString("idtag",null);
                        String user_reg_id= sharedPreferences.getString("reg_idtag",null);

                        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
                        nameValuePairs.add(new BasicNameValuePair("user_reg_id", user_reg_id));
                        nameValuePairs.add(new BasicNameValuePair("interest_userid", p.getId()));
                        nameValuePairs.add(new BasicNameValuePair("interest_user_regid", p.getReg_id()));

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost( MyConfig.Update_Interest_Decline);
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        HttpEntity entity = response.getEntity();
                        //  is = entity.getContent();
                        String data = EntityUtils.toString(entity);
                        Log.e("Register", data);
                        if(data.matches( "Record Updated Successfully" )) {

                            Toast.makeText( ctx, "Request Declined", Toast.LENGTH_SHORT ).show();
                            ((Activity)ctx).finish();
                            Intent i = new Intent(ctx, MainActivity.class);
                            ctx.startActivity(i);

                        } if(data.matches( "Something went wrong" )) {
                            //  if(data.matches( "phone number already exits" )) {
                            Toast.makeText( ctx, "status not updated,something went wrong", Toast.LENGTH_SHORT ).show();
                            //   }
                        }
                    } catch (ClientProtocolException e) {
                        Log.e("Fail 1", e.toString());
                        Toast.makeText(ctx, "Invalid IP Address", Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/


                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position) {
        return position;
    }



}
