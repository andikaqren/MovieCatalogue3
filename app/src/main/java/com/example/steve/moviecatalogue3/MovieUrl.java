package com.example.steve.moviecatalogue3;

import org.json.JSONObject;

public class MovieUrl {
    public MovieUrl(JSONObject urll) {
        try {
            String url = urll.getString("key");
            this.url = url ;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url = null ;
}
