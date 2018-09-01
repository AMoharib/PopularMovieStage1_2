package com.amoharib.popularmoviestage1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoharib.popularmoviestage1.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MoviesViewHolder extends RecyclerView.ViewHolder {
    ImageView poster;
    TextView movieTitle;

    public MoviesViewHolder(View itemView) {
        super(itemView);
        poster = (ImageView) itemView.findViewById(R.id.movie_poster);
        movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
    }

    public void populateView(String imageUrl, String title) {
        System.out.println(imageUrl);
        Picasso.with(itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.addphoto)
                .into(poster);
        movieTitle.setText(title);
    }

}
