package com.superdan.app.coolweather.modules.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superdan.app.coolweather.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/23.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String>dataList;
    private  OnRecyclerViewItemClickListener mOntemClickListener;


    public CityAdapter(Context context,ArrayList<String>dataList){
        mContext=context;
        mLayoutInflater=LayoutInflater.from(mContext);
        this.dataList=dataList;
    }

    public void setOntemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOntemClickListener=listener;
    }
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(mLayoutInflater.inflate(R.layout.item_city,parent,false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOntemClickListener.onItemClick(v,position);
            }
        });
        holder.itemCity.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int pos);
    }


    class CityViewHolder extends RecyclerView.ViewHolder{
        private TextView itemCity;
        private CardView cardView;


        public CityViewHolder(View itemView) {
            super(itemView);
            itemCity=(TextView)itemView.findViewById(R.id.item_city);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
        }
    }
}
