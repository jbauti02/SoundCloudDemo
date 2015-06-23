package com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud;

import com.google.gson.annotations.SerializedName;

/**
 * Model class of the contents for each track.
 */

public class Track {

    @SerializedName("title")
    private String title;

    @SerializedName("stream_url")
    private String streamURL;

    @SerializedName("id")
    private int id;

    @SerializedName("artwork_url")
    private String artworkURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        title = t;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public String getArtworkURL() {
        return artworkURL;
    }

    public String getAvatarURL() {
        String avatarURL = getAvatarURL();
        if(avatarURL != null) {
            return artworkURL.replace("large", "tiny");
        }
        return avatarURL;
    }

}
