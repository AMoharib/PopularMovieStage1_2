package com.amoharib.popularmoviestage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.popularmoviestage1.activities.DetailsActivity;
import com.amoharib.popularmoviestage1.models.Movie;
import com.amoharib.popularmoviestage1.R;
import com.amoharib.popularmoviestage1.viewholders.MoviesViewHolder;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {

    ArrayList<Movie> movies;
    Context context;
    Cursor cursor;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies (ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public MoviesAdapter(ArrayList<Movie> movies, Context context, Cursor cursor) {
        this.movies = movies;
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new MoviesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.populateView(movie.getPoster(), movie.getOriginalTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
