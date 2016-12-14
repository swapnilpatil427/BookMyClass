package com.example.swapn.bookmyclass.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Constants;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelllingBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SelllingBooksFragment extends Fragment {
    private DatabaseReference mDatabaseSellBooks;
    private DatabaseReference mDatabaseBooks;
    private DatabaseReference mDatabaseBooksALL;
    private DatabaseReference mDatabaseBooksSelling;
    RecyclerView rv;
    Activity activity = this.getActivity();
    private ArrayAdapter<String> booksAdapter;
    private HashMap<String, String> book_ids = new HashMap<String,String>();
    AutoCompleteTextView sellingbooks_autocomplete;
    private String book_type;

    private static OnFragmentInteractionListener mListener;

    public SelllingBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sellling_books, container, false);
        mDatabaseSellBooks = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS).child(FirebaseTables.SELLING_BOOKS);
        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS).child(FirebaseTables.ALL);
        FirebaseRecyclerAdapter<Book, BooksViewHolder> adapter;
        if (getArguments() != null) {
            book_type = getArguments().getString(Constants.BOOKSDETAILTYPE);
        } else {
            book_type = Constants.BOOKS_TYPE_ALL;
        }

        if(book_type == Constants.BOOKS_TYPE_ALL) {
            adapter = initialiseAllBooksAdapter();
        } else {
            adapter = initialiseSellingBooksAdapter();
        }

        sellingbooks_autocomplete = (AutoCompleteTextView) view.findViewById(R.id.sellingbooksautoComplete);
        booksAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        sellingbooks_autocomplete.setAdapter(booksAdapter);

        sellingbooks_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               /* Toast.makeText(getActivity(),
                        booksAdapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show(); */
                mListener.BookDetailsFragment(book_ids.get(booksAdapter.getItem(position).toString()), booksAdapter.getItem(position).toString());
            }
        });
      //  mDatabaseSellBooks.addValueEventListener(postValueListener);
        rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        return view;
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookName;
        TextView bookSummary;
        TextView bookPrice;
        ImageView bookPic;
        String bookID;

        BooksViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            bookName = (TextView)itemView.findViewById(R.id.course_id);
            //bookPrice = (TextView)itemView.findViewById(R.id.book_price);
            bookSummary = (TextView)itemView.findViewById(R.id.book_summary);
            bookPic = (ImageView)itemView.findViewById(R.id.book_pic);

            //imgvws.add

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.BookDetailsFragment(bookID, bookName.getText().toString());
                }
            });
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public FirebaseRecyclerAdapter<Book, BooksViewHolder> initialiseAllBooksAdapter() {
        FirebaseRecyclerAdapter<Book, BooksViewHolder> adapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(
                Book.class,
                R.layout.fragment_selling_books_cardview,
                BooksViewHolder.class,
                mDatabaseBooks
        ) {
            @Override
            protected void populateViewHolder(BooksViewHolder holder, Book book, int i) {
                try {
                    holder.bookName.setText(book.getBook_name());
                    //holder.bookPrice.setText(Double.toString(book.getPrice()));
                    holder.bookID = book.getBook_id();
                    booksAdapter.add(book.getBook_name());
                    book_ids.put(book.getBook_name(), book.getBook_id());
                    String summary = book.getSummary();
                    if (summary.length() < 50) {
                        summary = summary.replace(System.getProperty("line.separator"), " ");
                    } else {
                        summary = summary.substring(0, 50).replace(System.getProperty("line.separator"), " ") + " ... more";
                    }
                    holder.bookSummary.setText(summary);

                    if (!book.getBook_image().equalsIgnoreCase("book_default"))
                        Picasso.with(getActivity()).load(Uri.parse(book.getBook_image())).transform(new CircleTransform()).into(holder.bookPic);
                    else
                        Picasso.with(getActivity()).load(R.drawable.anonymus).transform(new CircleTransform()).into(holder.bookPic);
                } catch(Exception e) {

                }
            }
        };
        return adapter;
    }

    public FirebaseRecyclerAdapter<Book, BooksViewHolder> initialiseSellingBooksAdapter() {
        FirebaseRecyclerAdapter<Book, BooksViewHolder> adapter = new FirebaseRecyclerAdapter<Book, BooksViewHolder>(
                Book.class,
                R.layout.fragment_selling_books_cardview,
                BooksViewHolder.class,
                mDatabaseSellBooks
        ) {
            @Override
            protected void populateViewHolder(BooksViewHolder holder, Book book, int i) {
                try {
                    holder.bookName.setText(book.getBook_name());
                    //holder.bookPrice.setText(Double.toString(book.getPrice()));
                    holder.bookID = book.getBook_id();
                    booksAdapter.add(book.getBook_name());
                    book_ids.put(book.getBook_name(), book.getBook_id());
                    String summary = book.getSummary();
                    if (summary.length() < 50) {
                        summary = summary.replace(System.getProperty("line.separator"), " ");
                    } else {
                        summary = summary.substring(0, 50).replace(System.getProperty("line.separator"), " ") + " ... more";
                    }
                    holder.bookSummary.setText(summary);

                    if (!book.getBook_image().equalsIgnoreCase("book_default"))
                        Picasso.with(getActivity()).load(Uri.parse(book.getBook_image())).transform(new CircleTransform()).into(holder.bookPic);
                    else
                        Picasso.with(getActivity()).load(R.drawable.anonymus).transform(new CircleTransform()).into(holder.bookPic);
                } catch (Exception e) {

                }
            }
        };
        return adapter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        User getUserData();
        void BookDetailsFragment(String book_id, String book_name);
    }
}
