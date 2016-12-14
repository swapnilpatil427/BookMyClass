package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Course;
import com.example.swapn.bookmyclass.models.Instructor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstructorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InstructorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabaseProfessor;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rv;

    private OnFragmentInteractionListener mListener;

    public InstructorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstructorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstructorsFragment newInstance(String param1, String param2) {
        InstructorsFragment fragment = new InstructorsFragment();
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

    public static class InstructorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView professor_name;
        TextView professor_email;


        InstructorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            professor_name = (TextView)itemView.findViewById(R.id.instructor_name);
            professor_email = (TextView)itemView.findViewById(R.id.instructor_email);
        }
    }

    FirebaseRecyclerAdapter<Instructor, InstructorViewHolder> initialiseApp () {
        FirebaseRecyclerAdapter<Instructor, InstructorViewHolder> adapter = new FirebaseRecyclerAdapter<Instructor, InstructorViewHolder>(
                Instructor.class,
                R.layout.fragment_instructors_card_views,
                InstructorViewHolder.class,
                mDatabaseProfessor
        ) {
            @Override
            protected void populateViewHolder(InstructorViewHolder holder, Instructor instructor, int position) {
                try {
                    holder.professor_name.setText(instructor.getName());
                    //holder.bookPrice.setText(Double.toString(book.getPrice()));
                    holder.professor_email.setText(instructor.getEmail());
                } catch (Exception e) {

                }

            }
        };

        return  adapter;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instructors, container, false);
        mDatabaseProfessor = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.PROFESSOR);
        FirebaseRecyclerAdapter<Instructor, InstructorViewHolder> adapter = initialiseApp();
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
        void openInstructorFragment();
    }
}
