package com.example.swapn.bookmyclass.adapters;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.fragments.ConversationsFragment;
import com.example.swapn.bookmyclass.models.Event;
import com.example.swapn.bookmyclass.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by swapn on 12/9/2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> mDataSet;
   // private Eve fragment;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;

    /**
     * Inner Class for a recycler view
     */

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     */
    public EventAdapter(List<Event> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event_card_view, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mDataSet.get(position);
        holder.event_title.setText(event.getEvent_title());
        //holder.bookPrice.setText(Double.toString(book.getPrice()));
        holder.event_description.setText(event.getEvent_description());
        if(!event.getEvent_pic().equalsIgnoreCase("event_default") && event.getEvent_pic() != null)
            Picasso.with(Util.getContext()).load(Uri.parse(event.getEvent_pic())).into(holder.event_pic);
        else
            Picasso.with(Util.getContext()).load(R.drawable.event).into(holder.event_pic);
    }

    //Remove Event
    public void removeEvent(int pos) {
        mDataSet.remove(pos);
        this.notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
