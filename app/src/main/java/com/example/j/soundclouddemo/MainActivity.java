package com.example.j.soundclouddemo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud.SoundCloud;
import com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud.SoundCloudService;
import com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This is the list of the songs generated by the user's pick of their
 * mood button, they can also go at the search bar at the top of the app
 * and type for their song or artist and it will generate the song list
 * list for them based on their search.
 */

public class MainActivity extends ActionBarActivity
        implements SearchView.OnQueryTextListener {

    private MediaPlayer mediaPlayer;
    private TracksAdapter tracksAdapter;
    private List<Track> allTracks;
    private List<Track> previousTracks;
    private TextView selectedTitle;
    private ImageView selectedThumbNail;
    private ImageView playerStateButton;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is set so a random song list will generate.
        isFirstRun = true;

        toolbar = (Toolbar)findViewById(R.id.player_toolbar);
        selectedTitle = (TextView)findViewById(R.id.selected_title);
        selectedThumbNail = (ImageView)findViewById(R.id.selected_thumbnail);
        playerStateButton = (ImageView)findViewById(R.id.player_state);
        progressBar = (ProgressBar)findViewById(R.id.player_progress);
        recyclerView = (RecyclerView)findViewById(R.id.songs_list);

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                toggleSongState();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playerStateButton.setImageResource(R.mipmap.ic_play);
            }
        });

        playerStateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleSongState();
            }
        });

        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allTracks = new ArrayList<Track>();
        tracksAdapter = new TracksAdapter(this, allTracks);
        tracksAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = allTracks.get(position);
                selectedTitle.setText(selectedTrack.getTitle());
                Picasso.with(MainActivity.this).load(selectedTrack.getArtworkURL())
                        .into(selectedThumbNail);

                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                toggleProgressBar();
                toolbar.setVisibility(View.VISIBLE);

                try {
                    mediaPlayer.setDataSource(selectedTrack
                            .getStreamURL() + "?client_id=" + SoundCloudService.CLIENT_ID);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(tracksAdapter);

        SoundCloudService service = SoundCloud.getService();
        service.getRecentSongs(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
                new Callback<List<Track>>() {
                    @Override
                    public void success(List<Track> tracks, Response response) {
                        if(!isFirstRun) {
                            // This will prevent a randomly generated song list to override over
                            // the user's updated mood song list based on the mood button they clicked.
                            updateTracks(tracks);
                        } else {
                            // This will generate a random song list over the user's mood song list
                            // immediately when they click on their mood button but the HomeActivity
                            // prevents that with the extra string mood key and the string mood implemented.
                            // This boolean isFirstRun will be set to false.
                            isFirstRun = false;
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

        // This gets the R string value for the extra mood key from the strings values folder
        // and the extra string mood from the HomeActivity class and calls the searchFor method
        // with the extra string mood to search and display the song list associated with the user's
        // mood that they select.
        final String mood = getIntent().getStringExtra(Integer.toString(R.string.mood_key));
        searchFor(mood);
    }

    private void updateTracks(List<Track> tracks) {
        allTracks.clear();
        allTracks.addAll(tracks);
        tracksAdapter.notifyDataSetChanged();
    }

    private void toggleSongState() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playerStateButton.setImageResource(R.mipmap.ic_play);
        } else {
            mediaPlayer.start();
            toggleProgressBar();
            playerStateButton.setImageResource(R.mipmap.ic_pause);
        }
    }

    private void toggleProgressBar() {
        if(mediaPlayer.isPlaying()) {
            progressBar.setVisibility(View.INVISIBLE);
            playerStateButton.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            playerStateButton.setVisibility(View.INVISIBLE);
        }
    }

    /*
        This method is used if the user decides to look for a specific song.
        This searchFor method is used for the button functionality of the HomeActivity
        page which gets the "extra" user mood to display their desired song list from
        the clicked mood button of HomeActivity.
     */

    private void searchFor(final String query) {
        SoundCloud.getService().searchSongs(query, new Callback<List<Track>>() {

            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        searchFor(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /*
        This method is also used if the user decides to look for a specific song
        for the action bar menu.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search_view),
                new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                previousTracks = new ArrayList<Track>(allTracks);
                searchView.setIconified(false);
                searchView.requestFocus();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateTracks(previousTracks);
                searchView.clearFocus();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_view) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
