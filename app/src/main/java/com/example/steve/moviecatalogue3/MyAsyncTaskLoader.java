package com.example.steve.moviecatalogue3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private ArrayList<Movie> mData;
    private boolean mHasResult = false;

    public MyAsyncTaskLoader(@NonNull Context context) {
        super(context);
        onContentChanged();
    }
    @Override
    protected void onStartLoading(){
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<Movie> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }
    private static final String API_KEY = "6cec76c7551f93b96434affae74479ba";



    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<Movie> movies = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1";
        Log.d("url",url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < 20; i++) {
                        JSONObject moviee = list.getJSONObject(i);
                        Movie movie = new Movie(moviee);
                        movies.add(movie);
                        Log.d("movies",movie.toString());
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Jika response gagal maka , do nothing
            }
        });
        return movies;
    }
}
