package com.example.steve.moviecatalogue3.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class TvSeries implements Parcelable {
    private int id;
    private int vote_count;
    private Double vote_average;
    private String popularity;
    private String title;
    private String path;
    private String overview;
    private String release_date;

    public TvSeries(JSONObject object) {
        try {
            int id = object.getInt("id");
            int vote_count = object.getInt("vote_count");
            double vote_average = object.getDouble("vote_average");
            String popularity = object.getString("popularity");
            String overview = object.getString("overview");
            String title = object.getString("name");
            String release_date = object.getString("first_air_date");
            String path = object.getString("poster_path");
            Log.d("pathsss",path);
            this.id = id;
            this.title = title;
            this.vote_count = vote_count;
            this.overview = overview;
            this.release_date = release_date;
            this.path = path;
            this.popularity = popularity;
            this.vote_average = vote_average;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error",e.toString());

        }
    }

    public TvSeries() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.vote_count);
        dest.writeValue(this.vote_average);
        dest.writeString(this.popularity);
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    protected TvSeries(Parcel in) {
        this.id = in.readInt();
        this.vote_count = in.readInt();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.popularity = in.readString();
        this.title = in.readString();
        this.path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<TvSeries> CREATOR = new Parcelable.Creator<TvSeries>() {
        @Override
        public TvSeries createFromParcel(Parcel source) {
            return new TvSeries(source);
        }

        @Override
        public TvSeries[] newArray(int size) {
            return new TvSeries[size];
        }
    };
}
