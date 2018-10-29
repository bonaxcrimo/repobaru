package com.android.anime2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.MyViewHolder> {
    private Context mContext ;
    private List<Anime> mData ;


    public AnimeAdapter(Context mContext, List<Anime> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @Override
    public AnimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_anime,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.anime_title.setText(mData.get(position).getTitle());
        holder.anime_member.setText("Total Member : "+String.valueOf(mData.get(position).getMember()));
        String URL= mData.get(position).getGambar();
//        holder.rating.setRating(Integer.valueOf(mData.get(position).getRating()));
        Glide.with(holder.itemView.getContext()).load(URL).placeholder(R.drawable.ic_menu_gallery).crossFade().into(holder.img_anime);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,animedetail.class);

                // passing data to the book activity
                intent.putExtra("id",mData.get(position).getId());
                intent.putExtra("title",mData.get(position).getTitle());
                intent.putExtra("gambar",mData.get(position).getGambar());
                intent.putExtra("tipe",mData.get(position).getTipe());
                intent.putExtra("genre",mData.get(position).getGenre());
                intent.putExtra("sinopsis",mData.get(position).getSinopsis());
                intent.putExtra("rating",mData.get(position).getRating());
                // start the activity
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView anime_title,anime_member;
        ImageView img_anime;
        CardView cardView;
        RatingBar rating;
        public MyViewHolder(View itemView) {
            super(itemView);
            anime_title = (TextView) itemView.findViewById(R.id.anime_title);
            img_anime = (ImageView) itemView.findViewById(R.id.gambar_anime);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            anime_member = (TextView) itemView.findViewById(R.id.anime_member);
//            rating = (RatingBar)itemView.findViewById(R.id.ratingBar);
        }
    }
}
