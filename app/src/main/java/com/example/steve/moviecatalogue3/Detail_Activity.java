package com.example.steve.moviecatalogue3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Detail_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieUrl>> {
    Movie extraMovie;
    TvSeries extraTv;
    public static final String extraMovies = "extra_movies";
    public static final String extraId = "extra_id";
    public static boolean isMovie = false;
    private int id;
    private String linkPoster;
    TextView titleMovie, overview, voteAverage, voteCount, popularity, dateRelease, keterangan;
    ImageView posterMovie;
    ProgressBar progressBar;
    WebView trailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        if(savedInstanceState!=null){
            prepareView();
            titleMovie.setText(savedInstanceState.getString("my_title"));
            overview.setText(savedInstanceState.getString("my_overview"));
            voteAverage.setText(savedInstanceState.getString("my_vote"));
            Log.d("recovery2",overview.getText().toString());
            voteCount.setText(savedInstanceState.getString("my_vote_count"));
            popularity.setText(savedInstanceState.getString("my_popularity"));
        }

        prepareView();
        setView();
    }


    @NonNull
    @Override
    public Loader<ArrayList<MovieUrl>> onCreateLoader(int i, @Nullable Bundle bundle) {
        int id = 0;
        if (bundle != null) {
            id = bundle.getInt(extraId);
        }
        return new AsyncTaskUrlMovie(this, id, isMovie);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieUrl>> loader, ArrayList<MovieUrl> movieUrls) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movieUrls.isEmpty()) {
            keterangan.setVisibility(View.VISIBLE);
        } else {
            String baseurl = "<iframe width=\"330\" height=\"215\" src=\"https://www.youtube.com/embed/";
            String idUrl = movieUrls.get(0).getUrl();
            String realUrl = baseurl + idUrl + "\"  ></iframe>";
            trailer = findViewById(R.id.trailers1);
            WebSettings webSettings = trailer.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            trailer.loadData(realUrl, "text/html", null);
            Log.d("realUrl", realUrl);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieUrl>> loader) {

    }

    private void prepareView() {
        titleMovie = findViewById(R.id.title_detail);
        overview = findViewById(R.id.overview_detail);
        voteAverage = findViewById(R.id.rate_detail);
        voteCount = findViewById(R.id.vote_count);
        popularity = findViewById(R.id.popularity);
        posterMovie = findViewById(R.id.poster_detail);
        dateRelease = findViewById(R.id.release_date);
        progressBar = findViewById(R.id.progress_detail);
        keterangan = findViewById(R.id.keterangan);
    }

    private void setView() {
        if (isMovie == true) {
            extraMovie = getIntent().getParcelableExtra(extraMovies);
            getSupportActionBar().setTitle(extraMovie.getTitle());
            titleMovie.setText(extraMovie.getTitle());
            dateRelease.setText(String.format("%s : %s", getResources().getString(R.string.release_date), extraMovie.getRelease_date()));
            overview.setText(extraMovie.getOverview());
            voteAverage.setText(String.format("%s : %.2f", getResources().getString(R.string.rate), extraMovie.getVote_average()));
            voteCount.setText(String.format("%s : %03d", getResources().getString(R.string.vote_count), extraMovie.getVote_count()));
            popularity.setText(String.format("%s : %s", getResources().getString(R.string.popularity), extraMovie.getPopularity()));
            id = extraMovie.getId();
            linkPoster = "https://image.tmdb.org/t/p/w500" + extraMovie.getPath();

        } else {
            extraTv = getIntent().getParcelableExtra(extraMovies);
            getSupportActionBar().setTitle(extraTv.getTitle());
            titleMovie.setText(extraTv.getTitle());
            dateRelease.setText(String.format("%s : %s", getResources().getString(R.string.release_date), extraTv.getRelease_date()));
            overview.setText(extraTv.getOverview());
            voteAverage.setText(String.format("%s : %.2f", getResources().getString(R.string.rate), extraTv.getVote_average()));
            voteCount.setText(String.format("%s : %03d", getResources().getString(R.string.vote_count), extraTv.getVote_count()));
            popularity.setText(String.format("%s : %s", getResources().getString(R.string.popularity), extraTv.getPopularity()));
            id = extraTv.getId();
            linkPoster = "https://image.tmdb.org/t/p/w500" + extraTv.getPath();
        }
        progressBar.setVisibility(View.VISIBLE);
        keterangan.setVisibility(View.INVISIBLE);
        Glide.with(this)
                .load(linkPoster)
                .into(posterMovie);

        Bundle bundle = new Bundle();
        bundle.putInt(extraId, id);
        LoaderManager.getInstance(this).initLoader(0, bundle, this);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("my_vote", voteAverage.getText().toString());
        outState.putString("my_vote_count", voteCount.getText().toString());
        outState.putString("my_popularity", popularity.getText().toString());
        outState.putString("my_overview", overview.getText().toString());
        outState.putString("my_title", titleMovie.getText().toString());
        Log.d("outstate",outState.toString());
    }

}
