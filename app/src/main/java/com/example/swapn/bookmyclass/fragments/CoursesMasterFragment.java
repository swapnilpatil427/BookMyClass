package com.example.swapn.bookmyclass.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.Course;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoursesMasterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoursesMasterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesMasterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabaseCourses;
    Activity activity = this.getActivity();
    private ArrayAdapter<String> coursesAdapter;
    private HashMap<String, String> courses_ids = new HashMap<String,String>();
    AutoCompleteTextView courses_autocomplete;
    RecyclerView rv;

    private static OnFragmentInteractionListener mListener;

    public CoursesMasterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesMasterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesMasterFragment newInstance(String param1, String param2) {
        CoursesMasterFragment fragment = new CoursesMasterFragment();
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
        View view = inflater.inflate(R.layout.fragment_courses_master, container, false);
        mDatabaseCourses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.COURSES);
        courses_autocomplete = (AutoCompleteTextView) view.findViewById(R.id.coursesautoComplete);
        coursesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        courses_autocomplete.setAdapter(coursesAdapter);

        courses_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mListener.openCourseDetailsFragment(coursesAdapter.getItem(position).toString());
            }
        });
        FirebaseRecyclerAdapter<Course, CoursesMasterFragment.CoursesViewHolder> adapter = initialiseApp();
        rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        return view;
    }

    public static class CoursesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView course_id;
        TextView course_name;
        TextView green_sheet;
        String green_sheet_url = "";
        String courseID;

        CoursesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            course_id = (TextView)itemView.findViewById(R.id.course_id);
            course_name = (TextView)itemView.findViewById(R.id.course_name);
            green_sheet = (TextView)itemView.findViewById(R.id.green_sheet);
            green_sheet.setPaintFlags(green_sheet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            green_sheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.openUrl(green_sheet_url);
                }
            });

            //imgvws.add

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  mListener.openCourseDetailsFragment(course_id.getText().toString().trim());
                }
            });
        }
    }

    FirebaseRecyclerAdapter<Course, CoursesMasterFragment.CoursesViewHolder> initialiseApp () {
        FirebaseRecyclerAdapter<Course, CoursesMasterFragment.CoursesViewHolder> adapter = new FirebaseRecyclerAdapter<Course, CoursesMasterFragment.CoursesViewHolder>(
                Course.class,
                R.layout.fragment_courses_master_card_view,
                CoursesMasterFragment.CoursesViewHolder.class,
                mDatabaseCourses
        ) {
            @Override
            protected void populateViewHolder(CoursesViewHolder holder, Course course, int position) {
                try {
                    holder.course_name.setText(course.getCoursename());
                    //holder.bookPrice.setText(Double.toString(book.getPrice()));
                    holder.course_id.setText(course.getCourseid());
                    coursesAdapter.add(course.getCourseid());
                    holder.green_sheet_url = course.getGreensheetUrl();
                } catch (Exception e) {

                }
                //courses_ids.put(book.getBook_name(),book.getBook_id());

            }
        };

        return  adapter;
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
        void openAllCoursesFragment ();
        void openUrl(String url);
        void openCourseDetailsFragment (String course_id);
    }
}
