package com.android.anime2;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class UlaAdapter extends RecyclerView.Adapter<UlaAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Ula> mData ;


    public UlaAdapter(Context mContext, List<Ula> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public UlaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_ulas,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_nama.setText(mData.get(position).getNama());
        holder.tv_ulasan.setText(mData.get(position).getIsi());
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        holder.cardView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nama,tv_ulasan;
        RatingBar rating;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_nama = (TextView) itemView.findViewById(R.id.tv_nama);
            tv_ulasan = (TextView) itemView.findViewById(R.id.tv_ulasan);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
