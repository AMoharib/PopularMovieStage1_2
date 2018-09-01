package com.amoharib.popularmoviestage1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.popularmoviestage1.R;

public class TrailerViewHolder extends RecyclerView.ViewHolder {
    private TextView trailerName;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
    }

    public void updateUI(String name) {
        trailerName.setText(name);
    }

}
