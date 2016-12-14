package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.ConversationAdapter;
import com.example.swapn.bookmyclass.adapters.EventAdapter;
import com.example.swapn.bookmyclass.adapters.EventViewHolder;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.mSwiper.EventSwiper;
import com.example.swapn.bookmyclass.models.Course;
import com.example.swapn.bookmyclass.models.Event;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Conversation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Query mDatabaseEvents;
    FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TableRow text_books;
    TableRow courses;
    TableRow professors_row;
    TableRow events_row;
    RecyclerView rv;
    private List<Event> mEvents;
    private EventAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        text_books = (TableRow) view.findViewById(R.id.text_books);
        text_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openAllBookFragment();
            }
        });
        courses = (TableRow) view.findViewById(R.id.courses);
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openAllCoursesFragment();
            }
        });
        professors_row = (TableRow) view.findViewById(R.id.professors_row);
        professors_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openInstructorFragment();
            }
        });
        events_row = (TableRow) view.findViewById(R.id.events_row);
        events_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openEventsFragment();
            }
        });
        mDatabaseEvents = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.EVENTS).orderByChild("event_id").limitToFirst(2);
        mDatabaseEvents.addChildEventListener(EventValueListener);
        mEvents = new ArrayList<>();
        mAdapter = new EventAdapter(mEvents);
        rv = (RecyclerView) view.findViewById(R.id.rvEvents);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new EventSwiper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDatabaseEvents !=null)
            mDatabaseEvents.removeEventListener(EventValueListener);
    }

    ChildEventListener EventValueListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            try {
                Event event = dataSnapshot.getValue(Event.class);
                if (event != null) {
                    mEvents.add(event);
                    mAdapter.notifyDataSetChanged();
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
        void openAllBookFragment ();
        void openAllCoursesFragment ();
        void openInstructorFragment ();
        void openEventsFragment();
    }
}
