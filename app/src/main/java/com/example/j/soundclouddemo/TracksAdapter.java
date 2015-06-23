package com.example.j.soundclouddemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.j.soundclouddemo.com.example.j.soundclouddemo.soundcloud.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This adapter is used for display of all the songs in a list in a Recycler view with their
 * click listeners for the user to press. This grabs all the tracks with their titles, and picture
 * thumbnails for the user to see. The Picasso class is used for the image loading for the songs in the list.
 */

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView titleTextView;
        private final ImageView thumbImageView;

        ViewHolder(View view) {
            super(view);
            titleTextView = (TextView)view.findViewById(R.id.track_title);
            thumbImageView = (ImageView)view.findViewById(R.id.track_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(null, v, getPosition(), 0);
            }
        }
    }

    private List<Track> allTracks;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    TracksAdapter(Context c, List<Track> tracks) {
        allTracks = tracks;
        context = c;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClick) {
        onItemClickListener = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.track_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Track track = allTracks.get(i);
        viewHolder.titleTextView.setText(track.getTitle());
        Picasso.with(context).load(track.getArtworkURL()).into(viewHolder.thumbImageView);
    }

    @Override
    public int getItemCount() {
        return allTracks.size();
    }
}
