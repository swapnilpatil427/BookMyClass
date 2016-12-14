package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.ChatAdapter;
import com.example.swapn.bookmyclass.adapters.ConversationAdapter;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.Message;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Conversation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConversationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConversationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User userData;
    private DatabaseReference mDatabaseconversations;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseconversationsmessages;
    private DatabaseReference mDatabaseconversationsDetails;

    private List<User> mChats;
    private RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;
    private String mId;

    private OnFragmentInteractionListener mListener;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversationsFragment newInstance(String param1, String param2) {
        ConversationsFragment fragment = new ConversationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Target mTarget;

    public void loadImage(Context context, String url, final ImageView prof_pic,final String user_id) {

     //   final ImageView imageView = (ImageView) findViewById(R.id.image);

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Do something
                System.out.println("Success");
                Util.saveToInternalStorage(bitmap, user_id);
                prof_pic.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                System.out.println("Failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url).transform(new CircleTransform())
                .into(mTarget);
    }

    ValueEventListener userValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    // Util.saveToInternalStorage(null, user.getUid());
                    mChats.add(user);
                    mRecyclerView.scrollToPosition(mChats.size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
            }catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void onConversationClicked(String user_id, String user_name) {
        mListener.openSellerMessageFragment(user_id, "", user_name);
    }

    ChildEventListener UserConversationValueListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            User_Conversation user_conversations = dataSnapshot.getValue(User_Conversation.class);
            if(user_conversations != null) {
                mDatabaseUsers.child(user_conversations.getUser_id()).addListenerForSingleValueEvent(userValueListener);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        userData = mListener.getUserData();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mId = userData.getUid();
        Util.setContext(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ConversationAdapter(mChats, mId,this);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseconversations = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.CONVERSATIONS)
                .child(FirebaseTables.USER_CONVERSATIONS).child(userData.getUid());

        mDatabaseconversations.addChildEventListener(UserConversationValueListener);
        mDatabaseconversationsDetails = mDatabaseconversations.child(FirebaseTables.CONVERSATIONS_DETAILS);
        mDatabaseconversationsmessages = mDatabaseconversations.child(FirebaseTables.CONVERSATIONS_MESSAGES);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.USERS_TABLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseconversations.removeEventListener(UserConversationValueListener);
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
        void openSellerMessageFragment(String seller_id, String book_name, String seller_name);
    }
}
