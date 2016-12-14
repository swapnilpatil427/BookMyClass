package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.ChatAdapter;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Message;
import com.example.swapn.bookmyclass.models.User_Conversation;
import com.example.swapn.bookmyclass.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SELLER_ID = "param1";
    private static final String BOOK_NAME = "param2";
    private static final String TAG = MainActivity.class.getName();

    // TODO: Rename and change types of parameters
    private String seller_id;
    private String book_name;
    private User userData;
    private DatabaseReference user_group;
    private DatabaseReference group_user;
    private DatabaseReference mDatabaseconversations;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseconversationsmessages;
    private DatabaseReference mDatabaseconversationsDetails;
    private DatabaseReference mconversation;
    String conversation_id = "";
    private EditText metText;
    private Button mbtSent;

    private List<Message> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;

    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(SELLER_ID, param1);
        args.putString(BOOK_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String getConversationID(String user_id1, String user_id2) {
        if(user_id1.compareToIgnoreCase(user_id2) > 0) {
            return Integer.toString(Math.abs((user_id2 + user_id1).hashCode()));
        }
        else {
            return Integer.toString(Math.abs((user_id1 + user_id2).hashCode()));
        }
    }

    public void checkAndCreateNewConversation(final String conversationid) {
        if(!SELLER_ID.equals(""))
            mDatabaseconversationsDetails.child(conversationid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null) {
                        mDatabaseconversationsDetails.child(conversationid).setValue(conversationid);
                        mDatabaseconversations.child("user_conversations").child(seller_id.trim()).push().setValue(new User_Conversation(userData.getUid(),conversation_id));
                        mDatabaseconversations.child("user_conversations").child(userData.getUid().trim()).push().setValue(new User_Conversation(seller_id,conversation_id));
                    }
                    conversation_id = conversationid;
                    mDatabaseconversationsmessages.child(conversation_id).addChildEventListener(conversationChildListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    // Tests to see if /users/<userId> has any data.
    private void checkIfConversationExists() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seller_id = getArguments().getString(SELLER_ID);
            book_name = getArguments().getString(BOOK_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        userData = mListener.getUserData();
        metText = (EditText) view.findViewById(R.id.etText);
        mbtSent = (Button) view.findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mId = userData.getUid();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseconversations = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.CONVERSATIONS);
        mDatabaseconversationsDetails = mDatabaseconversations.child(FirebaseTables.CONVERSATIONS_DETAILS);
        mDatabaseconversationsmessages = mDatabaseconversations.child(FirebaseTables.CONVERSATIONS_MESSAGES);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.USERS_TABLE);

        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();
                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send message
                     */
                    if(!conversation_id.equals("")) {
                        mDatabaseconversationsmessages.child(conversation_id).push().setValue(new Message(mId, new Date(), message));
                    } else {
                        Toast.makeText(getContext(), "Error getting the database Reference", Toast.LENGTH_LONG).show();
                    }
                }

                metText.setText("");
            }
        });

        String conversation = getConversationID(seller_id.trim(), userData.getUid().trim());
        checkAndCreateNewConversation(conversation);
        return view;
    }


    ChildEventListener conversationChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        Message model = dataSnapshot.getValue(Message.class);
                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            } catch (Exception e) {

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
    }
}
