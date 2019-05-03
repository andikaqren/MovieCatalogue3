package com.example.steve.moviecatalogue3.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.db.FavHelper;
import com.example.steve.moviecatalogue3.entity.Favourite;
import com.example.steve.moviecatalogue3.entity.Movie;
import com.example.steve.moviecatalogue3.entity.MovieUrl;
import com.example.steve.moviecatalogue3.entity.TvSeries;
import com.example.steve.moviecatalogue3.loader.AsyncTaskUrlMovie;
import com.example.steve.moviecatalogue3.loader.LoadFavCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.steve.moviecatalogue3.db.DatabaseContract.FavouriteColumn.CONTENT_URI;

public class Detail_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieUrl>>, LoadFavCallback {
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
    Favourite favourite;
    ArrayList<Favourite> favouriteArrayList;
    Menu menu;
    WebView trailer;
    private FavHelper favHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        if (savedInstanceState != null) {
            prepareView();
            titleMovie.setText(savedInstanceState.getString("my_title"));
            overview.setText(savedInstanceState.getString("my_overview"));
            voteAverage.setText(savedInstanceState.getString("my_vote"));
            voteCount.setText(savedInstanceState.getString("my_vote_count"));
            popularity.setText(savedInstanceState.getString("my_popularity"));
        }
        favHelper = FavHelper.getInstance(getApplicationContext());
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
            String realUrl = baseurl + idUrl + "\" frameborder=\"0\"\"allowfullscreen\">  </iframe>";
            trailer = findViewById(R.id.trailers1);
            WebSettings webSettings = trailer.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            trailer.loadData(realUrl, "text/html", null);
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
        new LoadFavAsync(favHelper, this).execute();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("my_vote", voteAverage.getText().toString());
        outState.putString("my_vote_count", voteCount.getText().toString());
        outState.putString("my_popularity", popularity.getText().toString());
        outState.putString("my_overview", overview.getText().toString());
        outState.putString("my_title", titleMovie.getText().toString());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_fav_item:
                if (menuItem.isChecked()) {
                    String id2 = Integer.toString(id);
                    Log.d("testing5", String.valueOf(CONTENT_URI));
                    getContentResolver().delete(CONTENT_URI, id2, null);
                    showSnackbarMessage(getResources().getString(R.string.success_delete));
                    menuItem.setChecked(false);
                    menuItem.setIcon(R.drawable.favicon);
                    break;

                } else {
                    long result = 0;
                    if (isMovie) {
                        favourite = new Favourite();
                        favourite.setVote_average(extraMovie.getVote_average());
                        favourite.setTitle(extraMovie.getTitle());
                        favourite.setOverview(extraMovie.getOverview());
                        favourite.setVote_count(extraMovie.getVote_count());
                        favourite.setId(extraMovie.getId());
                        favourite.setPath(extraMovie.getPath());
                        favourite.setPopularity(extraMovie.getPopularity());
                        favourite.setRelease_date(extraMovie.getRelease_date());
                        favourite.setIsMovie(1);
                        result = favHelper.insertNote(favourite);
                    } else {
                        favourite = new Favourite();
                        favourite.setVote_average(extraTv.getVote_average());
                        favourite.setTitle(extraTv.getTitle());
                        favourite.setOverview(extraTv.getOverview());
                        favourite.setVote_count(extraTv.getVote_count());
                        favourite.setId(extraTv.getId());
                        favourite.setPath(extraTv.getPath());
                        favourite.setPopularity(extraTv.getPopularity());
                        favourite.setRelease_date(extraTv.getRelease_date());
                        favourite.setIsMovie(0);
                        result = favHelper.insertNote(favourite);

                    }
                    if (result > 0) {
                        showSnackbarMessage(getResources().getString(R.string.success_insert_favorit));
                        menuItem.setChecked(true);
                        menuItem.setIcon(R.drawable.ic_baseline_favorite_24px);
                    } else {
                        showSnackbarMessage(getResources().getString(R.string.failed_insert_favorit));
                    }

                    break;
                }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(posterMovie, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void PostExecute(Cursor favourites) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        menu.getItem(0).setChecked(false);

        menu.getItem(0).setIcon(R.drawable.favicon);

        for (Favourite fav : favouriteArrayList) {
            if (fav.getId() == id) {
                menu.getItem(0).setChecked(true);
                menu.getItem(0).setIcon(R.drawable.ic_baseline_favorite_24px);
                return super.onPrepareOptionsMenu(menu);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void postExecute(ArrayList<Favourite> favourites) {
        favouriteArrayList = new ArrayList<>();
        favouriteArrayList.addAll(favourites);

    }

    private static class LoadFavAsync extends AsyncTask<Void, Void, ArrayList<Favourite>> {
        private final WeakReference<FavHelper> weakNoteHelper;
        private final WeakReference<LoadFavCallback> weakCallback;

        private LoadFavAsync(FavHelper favHelper, LoadFavCallback callback) {
            weakNoteHelper = new WeakReference<>(favHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }


        @Override
        protected ArrayList<Favourite> doInBackground(Void... voids) {
            return weakNoteHelper.get().getAllFav();
        }

        @Override
        protected void onPostExecute(ArrayList<Favourite> favourites) {
            super.onPostExecute(favourites);
            weakCallback.get().postExecute(favourites);

        }
    }


}




