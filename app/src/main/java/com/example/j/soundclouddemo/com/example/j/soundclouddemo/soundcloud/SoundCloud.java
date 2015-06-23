package com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud;

import retrofit.RestAdapter;

/**
 * This class implements the SoundCloud API from the SoundCloud website.
 */

public class SoundCloud {
    private static final String API_URL = "http://api.soundcloud.com";
    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();
    private static final SoundCloudService SERVICE = REST_ADAPTER.create(SoundCloudService.class);
    public static SoundCloudService getService() {
        return SERVICE;
    }
}
