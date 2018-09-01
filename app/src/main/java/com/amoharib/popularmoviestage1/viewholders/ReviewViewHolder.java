package com.amoharib.popularmoviestage1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amoharib.popularmoviestage1.models.Review;
import com.amoharib.popularmoviestage1.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    TextView author, content;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        author = (TextView) itemView.findViewById(R.id.author);
        content = (TextView) itemView.findViewById(R.id.content);
    }

    public void updateUI(Review review) {
        author.setText(review.getAuthor());
        content.setText(review.getContent());
    }
}
