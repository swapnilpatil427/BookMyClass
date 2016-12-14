package com.example.swapn.bookmyclass.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.fragments.ConversationsFragment;
import com.example.swapn.bookmyclass.models.Message;
import com.example.swapn.bookmyclass.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by swapn on 12/9/2016.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private List<User> mDataSet;
    private ConversationsFragment fragment;
    private String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public ImageView user_pic;
        private String user_id;

        public ViewHolder(View v) {
            super(v);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_pic = (ImageView) itemView.findViewById(R.id.user_pic);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onConversationClicked(user_id, user_name.getText().toString());
                }
            });
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    public ConversationAdapter(List<User> dataSet, String id, ConversationsFragment fragment) {
        mDataSet = dataSet;
        mId = id;
        this.fragment = fragment;
    }

    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_conversations_card_view, parent, false);
        return new ConversationAdapter.ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ConversationAdapter.ViewHolder holder, int position) {
        User user = mDataSet.get(position);
        holder.user_name.setText(user.getName());
        holder.user_id = user.getUid();

        if(!user.getProf_pic().equalsIgnoreCase("default")) {
            Bitmap bitmap = Util.loadBitmap(Util.getContext(), user.getUid().trim());
            if(bitmap == null) {
                fragment.loadImage(Util.getContext(), user.getProf_pic(), holder.user_pic, user.getUid());
            } else {
                holder.user_pic.setImageBitmap(bitmap);
            }
        }
        else
            Picasso.with(Util.getContext()).load(R.drawable.anonymus).transform(new CircleTransform()).into(holder.user_pic);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

