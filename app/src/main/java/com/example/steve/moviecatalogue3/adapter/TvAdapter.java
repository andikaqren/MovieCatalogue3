package com.example.steve.moviecatalogue3.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.entity.TvSeries;

import java.util.ArrayList;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.CategoryViewHolder> {
    private Context context;

    public TvAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<TvSeries> getTvSeriesList() {
        return tvSeriesList;
    }

    public void setTvSeriesList(ArrayList<TvSeries> tvSeriesList) {
        this.tvSeriesList = tvSeriesList;
    }

    private ArrayList<TvSeries> tvSeriesList;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tv_series_row, viewGroup, false
        );
        return new CategoryViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {

        categoryViewHolder.namaFilm.setText(getTvSeriesList().get(i).getTitle());

        categoryViewHolder.rateFilm.setText(String.format("%s %.2f",context.getResources().getString(R.string.rate) , getTvSeriesList().get(i).getVote_average()));
        categoryViewHolder.descFilm.setText(getTvSeriesList().get(i).getOverview());
        String linkPoster = "https://image.tmdb.org/t/p/w500" + getTvSeriesList().get(i).getPath();
        Glide.with(context)
                .load(linkPoster)
                .into(categoryViewHolder.fotoFilm);

    }

    @Override
    public int getItemCount() {
        return getTvSeriesList().size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView namaFilm;
        TextView descFilm;
        TextView rateFilm;
        ImageView fotoFilm;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            namaFilm = itemView.findViewById(R.id.tv_series_title);
            descFilm = itemView.findViewById(R.id.tv_series_desc);
            fotoFilm = itemView.findViewById(R.id.tv_series_mini_poster);
            rateFilm = itemView.findViewById(R.id.tv_series_rate);
        }
    }
}
