package com.example.steve.moviecatalogue3;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class Movie implements Parcelable {
    private int id;
    private int vote_count;
    private double vote_average;

    public Movie(JSONObject object) {
        try {
            int id = object.getInt("id");
            int vote_count = object.getInt("vote_count");
            double vote_average = object.getDouble("vote_average");
            String popularity = object.getString("popularity");
            String overview = object.getString("overview");
            String title = object.getString("title");
            String release_date = object.getString("release_date");
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
        }
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

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
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

    private String popularity;
    private String title;
    private String path;
    private String overview;
    private String release_date;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.vote_count);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.popularity);
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.vote_count = in.readInt();
        this.vote_average = in.readDouble();
        this.popularity = in.readString();
        this.title = in.readString();
        this.path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
