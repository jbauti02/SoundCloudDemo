package com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * The class uses the client ID from the registered app for implementation.
 */

public interface SoundCloudService {
    static final String CLIENT_ID = "24fc3b9d5d4c7b21ad6dc3a9123aee0a";

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void searchSongs(@Query("q") String query, Callback<List<Track>> cb);

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void getRecentSongs(@Query("created_at[from]") String date, Callback<List<Track>> cb);

    public void songsAfter(@Query("created_at[to]") String date, Callback<List<Track>> cb);
    public void bpmFrom(@Query("bpm[from]") String date, Callback<List<Track>> cb);
}
