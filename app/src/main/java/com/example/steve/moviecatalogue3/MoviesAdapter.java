package com.example.steve.moviecatalogue3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CategoryViewHolder> {
    private Context context;

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getListMovies() {
        return listMovies;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
    }

    private ArrayList<Movie> listMovies;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movies_row, viewGroup, false);
        return new CategoryViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.rate.setText(String.format("%s %.2f",context.getResources().getString(R.string.rate), getListMovies().get(i).getVote_average()));
        categoryViewHolder.namaFilm.setText(getListMovies().get(i).getTitle());
        categoryViewHolder.descFilm.setText(getListMovies().get(i).getOverview());
        String linkPoster = "https://image.tmdb.org/t/p/w500" + getListMovies().get(i).getPath();
        Glide.with(context)
                .load(linkPoster)
                .into(categoryViewHolder.fotoFilm);
    }

    @Override
    public int getItemCount() {
        return getListMovies().size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView namaFilm;
        TextView descFilm;
        ImageView fotoFilm;
        TextView rate;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            namaFilm = itemView.findViewById(R.id.movies_title);
            descFilm = itemView.findViewById(R.id.movies_desc);
            fotoFilm = itemView.findViewById(R.id.movies_mini_poster);
            rate = itemView.findViewById(R.id.movie_rate);
        }
    }

}
