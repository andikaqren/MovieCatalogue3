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

public class AsyncTaskUrlMovie extends AsyncTaskLoader<ArrayList<MovieUrl>> {
    private ArrayList<MovieUrl> mData;
    private boolean mHasResult = false;
    private int id ;
    private boolean isMovie;
    private String url;
    public AsyncTaskUrlMovie(@NonNull Context context ,int id,boolean isMovie) {
        super(context);
        this.id = id;
        this.isMovie=isMovie;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<MovieUrl> data) {
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

    @Nullable

    private static final String API_KEY = "6cec76c7551f93b96434affae74479ba";

    @Override
    public ArrayList<MovieUrl> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MovieUrl> urls = new ArrayList<>();
        if(isMovie) {
            url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + API_KEY + "&language=en-US&page=1";
        }else {
            url = "https://api.themoviedb.org/3/tv/" + id + "/videos?api_key=" + API_KEY + "&language=en-US&page=1";
        }
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
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject urll = list.getJSONObject(i);
                        MovieUrl url = new MovieUrl(urll);
                        urls.add(url);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return urls;
    }

}
