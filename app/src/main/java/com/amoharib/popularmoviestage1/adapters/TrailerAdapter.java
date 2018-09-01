package com.amoharib.popularmoviestage1.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.popularmoviestage1.models.Trailer;
import com.amoharib.popularmoviestage1.R;
import com.amoharib.popularmoviestage1.viewholders.TrailerViewHolder;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    ArrayList<Trailer> trailers;
    Context context;

    public TrailerAdapter(ArrayList<Trailer> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);
        holder.updateUI(trailer.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}
