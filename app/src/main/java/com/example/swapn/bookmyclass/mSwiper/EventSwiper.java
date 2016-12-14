package com.example.swapn.bookmyclass.mSwiper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.swapn.bookmyclass.adapters.EventAdapter;
import com.example.swapn.bookmyclass.adapters.EventViewHolder;
import com.example.swapn.bookmyclass.models.Event;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by swapn on 12/9/2016.
 */

public class EventSwiper extends ItemTouchHelper.SimpleCallback {

    EventAdapter adapter;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
    public EventSwiper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public EventSwiper(EventAdapter adapter) {
        super(ItemTouchHelper.UP, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.removeEvent(viewHolder.getAdapterPosition());
    }
}
