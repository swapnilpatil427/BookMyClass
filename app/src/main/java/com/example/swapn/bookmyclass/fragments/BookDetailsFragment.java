package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Constants;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.Course;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;
    TextView bookName;
    TextView bookSummary;
    TextView bookAuthor;
    TextView bookPrice;
    Button btn_buy;
    LinearLayout coursesLayout;
    ImageView bookPic;
    User userData;
    private DatabaseReference mDatabaseBookSell;
    private DatabaseReference mDatabaseUser_Book;
    private DatabaseReference mDatabaseBook_COurses;
    private DatabaseReference mDatabaseCourses;
    private String bookID;
    User_Book user_book;
    HashMap<String, String> coursename_id = new HashMap<String, String>();
    HashMap<String, TextView> courseName_Textview = new HashMap<String, TextView>();

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailsFragment newInstance(String param1, String param2) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        bookName = (TextView)view.findViewById(R.id.course_id);
        bookAuthor = (TextView)view.findViewById(R.id.book_author);
        bookSummary = (TextView)view.findViewById(R.id.book_summary);
        bookPic = (ImageView)view.findViewById(R.id.book_pic);
        btn_buy = (Button) view.findViewById(R.id.btn_buy);
        coursesLayout = (LinearLayout) view.findViewById(R.id.courses_layout);
        userData = mListener.getUserData();
        mDatabaseBookSell = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS)
                .child(FirebaseTables.SELLING_BOOKS);

        mDatabaseUser_Book = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.USER_BOOK);
        mDatabaseBook_COurses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.BOOK_COURSE);

        mDatabaseCourses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.COURSES);

        if (getArguments() != null) {
            bookID = getArguments().getString(Constants.BOOKID);
            mDatabaseBookSell.child(bookID).addValueEventListener(bookValueListener);
            mDatabaseUser_Book.orderByChild("book_id").startAt(bookID.trim()).endAt(bookID.trim())
                    .addValueEventListener(userValueListener);
        }

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

    private void addCourseTextView(Course course) {
        try {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(0, 10, 0, 0);
            tv.setLayoutParams(lparams);
            tv.setText(course.getCourseid() + " : " + course.getCoursename());
            tv.setTextColor(getResources().getColor(R.color.primary));
            coursesLayout.addView(tv);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            coursename_id.put(course.getCoursename().trim(), course.getCourseid());
            courseName_Textview.put(course.getCourseid(), tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Test", "Here");
                    TextView tv1 = (TextView) v;
                    String[] courses = tv1.getText().toString().trim().split(":");
                    ((MainActivity) getActivity()).openCourseDetailsFragment(coursename_id.get(courses[1].trim()));
                }
            });
        } catch (Exception e) {

        }

    }

    ValueEventListener coursesValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                Course course = dataSnapshot.getValue(Course.class);
                coursesLayout.removeView(courseName_Textview.get(course.getCourseid()));
                addCourseTextView(course);
            } catch (Exception e) {

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
                    user_book = dataSnapshot.getChildren().iterator().next().getValue(User_Book.class);
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    ValueEventListener bookValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                Book book = dataSnapshot.getValue(Book.class);
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                bookName.setText(book.getBook_name().toString());
                bookSummary.setText(book.getSummary().toString());
                bookAuthor.setText(book.getAuthor().toString());
                if (!book.getBook_image().equalsIgnoreCase("book_default"))
                    Picasso.with(getActivity()).load(Uri.parse(book.getBook_image())).transform(new CircleTransform()).into(bookPic);
                else
                    Picasso.with(getActivity()).load(R.drawable.anonymus).transform(new CircleTransform()).into(bookPic);
                if (!btn_buy.hasOnClickListeners()) {
                    btn_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).openViewSellerFragment(bookID);
                        }
                    });
                }
                List<String> courses = book.getCourse_ids();
                coursesLayout.removeAllViews();
                if (courses != null) {
                    for (String course : courses) {
                        mDatabaseCourses.child(course).addValueEventListener(coursesValueListener);
                    }
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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
        void openViewSellerFragment(String book_id);
    }
}
