package com.example.swapn.bookmyclass.adapters;

import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;

/**
 * Created by swapn on 12/8/2016.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public ImageView event_pic;
        public TextView event_title;
        public TextView event_description;
        public String event_id = "";

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            event_pic = (ImageView) itemView.findViewById(R.id.event_pic);
            event_title = (TextView)itemView.findViewById(R.id.event_title);
            event_description = (TextView)itemView.findViewById(R.id.event_description);
        }
}
