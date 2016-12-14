package com.example.swapn.bookmyclass.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;

/**
 * Created by swapn on 12/8/2016.
 */

public class SellersViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    public TextView seller_name;
   public TextView seller_place;
    public TextView selling_date;
    public TextView seller_email;
    public TextView seller_contact;
    public Button message_seller;
    public TextView book_price;
    public String seller_id = "";
    public String book_id = "";
    public ImageView seller_place_image;

    SellersViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.cv);
        seller_name = (TextView)itemView.findViewById(R.id.seller_name);
        selling_date = (TextView)itemView.findViewById(R.id.selling_date);
        seller_email = (TextView)itemView.findViewById(R.id.seller_email);
        seller_place = (TextView)itemView.findViewById(R.id.seller_place);
        book_price = (TextView) itemView.findViewById(R.id.book_price);
        message_seller = (Button) itemView.findViewById(R.id.message_seller);
        seller_contact = (TextView)itemView.findViewById(R.id.seller_contact);
        seller_place_image = (ImageView) itemView.findViewById(R.id.seller_place_image);
    }
}
