package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.SellersViewHolder;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.Course;
import com.example.swapn.bookmyclass.models.Message;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Book;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewSellersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewSellersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSellersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BOOK_ID = "book_id";

    // TODO: Rename and change types of parameters
    private String book_id;
    private Query mDatabaseUser_Book;
    private DatabaseReference mDatabaseBook;
    private DatabaseReference mDatabaseUser;
    private User user;
    private Book book;
    User userData;

    private OnFragmentInteractionListener mListener;
    FirebaseRecyclerAdapter<User_Book, SellersViewHolder> adapter;
    RecyclerView rv;
    HashMap<String,TextView> user_text_view = new HashMap<String,TextView>();
    HashMap<String,TextView> book_price_text_view = new HashMap<String,TextView>();

    public ViewSellersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewSellersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewSellersFragment newInstance(String param1) {
        ViewSellersFragment fragment = new ViewSellersFragment();
        Bundle args = new Bundle();
        args.putString(BOOK_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book_id = getArguments().getString(BOOK_ID);
        }
    }

    FirebaseRecyclerAdapter<User_Book, SellersViewHolder> initialiseApp () {
        FirebaseRecyclerAdapter<User_Book, SellersViewHolder> adapter = new FirebaseRecyclerAdapter<User_Book, SellersViewHolder>(
                User_Book.class,
                R.layout.fragments_view_sellers_card_view,
                SellersViewHolder.class,
                mDatabaseUser_Book.orderByChild("book_id").startAt(book_id.trim()).endAt(book_id.trim())
        ) {

            @Override
            protected void populateViewHolder(final SellersViewHolder holder, User_Book model, int position) {
                holder.seller_place.setText(model.getPlace());
                holder.seller_email.setText(model.getEmail());
                holder.selling_date.setText(Util.getStringDate(model.getSelling_date()));
                holder.seller_contact.setText(model.getContact());
                if(!holder.seller_place_image.hasOnClickListeners()) {
                    holder.seller_place_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!holder.seller_place.getText().equals("")) {
                                Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + holder.seller_place.getText()));
                                startActivity(searchAddress);
                                //  mListener.openMapFragment(dept_address);
                            }
                        }
                    });
                }
                mDatabaseUser.child(model.getUser_id().trim()).addListenerForSingleValueEvent(userValueListener);
                user_text_view.put(model.getUser_id(),holder.seller_name);
                mDatabaseBook.child(model.getBook_id().trim()).addListenerForSingleValueEvent(bookValueListener);
                book_price_text_view.put(model.getBook_id(),holder.book_price);

                if(!holder.message_seller.hasOnClickListeners()) {
                        holder.message_seller.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                if (user != null && book != null) {
                                    if(!userData.getUid().equals(user.getUid())) {
                                        mListener.openSellerMessageFragment(user.getUid(), book.getBook_name(), user.getName());
                                    } else {
                                        Toast.makeText(getContext(), "YOu are the seller of this book.", Toast.LENGTH_LONG).show();
                                    }
                                }
                        }
                    });
                }
            }
        };

        return  adapter;
    }

    private void createUserGroup() {

    }

    // Returns Group ID
    public String createNewGroup() {
        return "";
    }

    public void pushMessage(String group_id, Message message) {

    }

    public boolean pushGroupUser(String user_id, String group_id) {
        return false;
    }

    public boolean pushUserGroup(String user_id, String group_id) {
        return false;
    }


    ValueEventListener bookValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() !=0) {
                book = dataSnapshot.getValue(Book.class);
                TextView bookprice = book_price_text_view.get(book.getBook_id());
                bookprice.setText(Double.toString(book.getPrice()));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    ValueEventListener userValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                if (dataSnapshot.getChildrenCount() != 0) {
                    user = dataSnapshot.getValue(User.class);
                    TextView username = user_text_view.get(user.getUid());
                    username.setText(user.getName());
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view_sellers, container, false);
        userData = mListener.getUserData();
        mDatabaseUser_Book = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.USER_BOOK);
        mDatabaseBook = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS)
                .child(FirebaseTables.ALL);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.USERS_TABLE);
      //  mDatabaseUser_Book.orderByChild("book_id").startAt(book_id.trim()).endAt(book_id.trim())
       //         .addValueEventListener(userValueListener);
        adapter = initialiseApp();
        rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        return view;
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
        void openSellerMessageFragment(String seller_id, String book_name, String group_name);
        User getUserData();
    }
}
