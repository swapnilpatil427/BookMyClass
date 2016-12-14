package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.net.Uri;
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

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.ChatAdapter;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Message;
import com.example.swapn.bookmyclass.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudyGroupMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudyGroupMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyGroupMessagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUP_ID = "group_id";

    User userData;


    // TODO: Rename and change types of parameters
    private String group_id;
    private EditText metText;
    private Button mbtSent;

    private List<Message> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabaseCourseStudyGroup;

    public StudyGroupMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StudyGroupMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyGroupMessagesFragment newInstance(String param1) {
        StudyGroupMessagesFragment fragment = new StudyGroupMessagesFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group_id = getArguments().getString(GROUP_ID);
        }
    }

    ChildEventListener groupconversationChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                try{
                    Message model = dataSnapshot.getValue(Message.class);
                    mChats.add(model);
                    mRecyclerView.scrollToPosition(mChats.size() - 1);
                    mAdapter.notifyItemInserted(mChats.size() - 1);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
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
        View view = inflater.inflate(R.layout.fragment_study_group_messages, container, false);
        mDatabaseCourseStudyGroup = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.STUDYGROUP)
                                .child(group_id).child(FirebaseTables.MESSAGES);
        mDatabaseCourseStudyGroup.addChildEventListener(groupconversationChildListener);
        userData = mListener.getUserData();
        metText = (EditText) view.findViewById(R.id.etText);
        mbtSent = (Button) view.findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mId = userData.getUid();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();
                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send message
                     */
                    if(!group_id.equals("")) {
                        mDatabaseCourseStudyGroup.push().setValue(new Message(mId, new Date(), message));
                    } else {
                        Toast.makeText(getContext(), "Error getting the database Reference", Toast.LENGTH_LONG).show();
                    }
                }

                metText.setText("");
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
        mId = userData.getUid();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);
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
        User getUserData();
    }
}
