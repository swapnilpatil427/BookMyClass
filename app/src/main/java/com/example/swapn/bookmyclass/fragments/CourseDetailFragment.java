package com.example.swapn.bookmyclass.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.Course;
import com.example.swapn.bookmyclass.models.Department;
import com.example.swapn.bookmyclass.models.StudyGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COURSE_ID = "course_id";

    // TODO: Rename and change types of parameters
    private String mcourse_id;
    private DatabaseReference mDatabaseCourses;
    private DatabaseReference mDatabaseBooks;
    private DatabaseReference mDatabaseCourseStudyGroup;
    private DatabaseReference mDatabaseDept;
    private DatabaseReference mDatabaseCoursesStudyRoom;
    TextView course_name;
    TextView course_id;
    TextView course_summary;
    TextView course_schedule;
    TextView course_dept;
    TextView green_sheet;
    Button btn_study_group;
    ImageView course_location;
    LinearLayout booksLayout;
    LinearLayout groupLayout;
    Button btn_course_greensheet;
    String green_sheet_url = "";
    HashMap<String, String> bookname_id = new HashMap<String, String>();
    HashMap<String, TextView> bookName_Textview = new HashMap<String, TextView>();
    HashMap<String, String> groupname_id = new HashMap<String, String>();
    HashMap<String, TextView> group_Textview = new HashMap<String, TextView>();
    Long dept_id;
    String dept_address;
    AlertDialog.Builder builder;
    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CourseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseDetailFragment newInstance(String param1) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcourse_id = getArguments().getString(COURSE_ID);
        }
    }

    ValueEventListener CourseValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                Course course = dataSnapshot.getValue(Course.class);
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                course_name.setText(course.getCoursename().toString());
                course_id.setText(course.getCourseid().toString());
                course_summary.setText(course.getSummary().toString());
                course_schedule.setText(course.getSchedule().toString());
                green_sheet_url = course.getGreensheetUrl();
                if (!green_sheet.hasOnClickListeners()) {
                    green_sheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!green_sheet_url.equals("")) {
                                mListener.openUrl(green_sheet_url);
                            }
                        }
                    });
                }
                HashMap<String, String> groups = course.getStudygroups();
                if (groups != null) {
                    for (Map.Entry<String, String> entry : groups.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        mDatabaseCourseStudyGroup.child(value).child(FirebaseTables.DETAILS).addValueEventListener(GroupValueListener);
                        // do what you have to do here
                        // In your case, an other loop.
                    }
                }
                List<String> depts = course.getDept();
                if (depts != null)
                    mDatabaseDept.child(depts.get(0)).addValueEventListener(DepartmentValueListener);
                List<String> textbooks = course.getTextbooks();
                booksLayout.removeAllViews();
                if (textbooks != null) {
                    for (String bookid : textbooks) {
                        mDatabaseBooks.child(bookid.trim()).addValueEventListener(bookValueListener);
                    }
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void addBookTextView(Book book) {
        try {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(0, 10, 0, 0);
            tv.setLayoutParams(lparams);
            tv.setText(book.getBook_name());
            tv.setTextColor(getResources().getColor(R.color.primary));
            booksLayout.addView(tv);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            bookName_Textview.put(book.getBook_id(), tv);
            bookname_id.put(book.getBook_name(), book.getBook_id());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Test", "Here");
                    TextView tv1 = (TextView) v;
                    ((MainActivity) getActivity()).BookDetailsFragment(bookname_id.get(tv1.getText().toString().trim()), tv1.getText().toString());
                }
            });
        } catch (Exception e) {

        }
    }

    private void addGroupTextView(StudyGroup group) {
        try {
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(0, 10, 0, 0);
            tv.setLayoutParams(lparams);
            tv.setText(group.getName());
            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            groupLayout.addView(tv);
            tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            group_Textview.put(group.getId(), tv);
            groupname_id.put(group.getName(), group.getId());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Test", "Here");
                    TextView tv1 = (TextView) v;
                    mListener.openStudyGroupMessageFragment(groupname_id.get(tv1.getText().toString().trim()), tv1.getText().toString().trim());
                    //  ((MainActivity) getActivity()).BookDetailsFragment(bookname_id.get(tv1.getText().toString().trim()));
                }
            });
        }catch (Exception e) {

        }
    }


    ValueEventListener DepartmentValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                Department dept = dataSnapshot.getValue(Department.class);
                course_dept.setText(dept.getDeptname());
                dept_id = dept.getDeptid();
                dept_address = dept.getDeptaddress();
                if (!course_location.hasOnClickListeners()) {
                    course_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!dept_address.equals("")) {
                                Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + dept_address));
                                startActivity(searchAddress);
                                //  mListener.openMapFragment(dept_address);
                            }
                        }
                    });
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener GroupValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                StudyGroup group = dataSnapshot.getValue(StudyGroup.class);
                groupLayout.removeView(group_Textview.get(group.getId()));
                addGroupTextView(group);
            } catch (Exception e) {

            }
            //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
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
                booksLayout.removeView(bookName_Textview.get(book.getBook_id()));
                addBookTextView(book);
            } catch (Exception e) {

            }
            //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private String m_Text = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);
        mDatabaseCourses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.COURSES).child(mcourse_id);

        mDatabaseCourseStudyGroup = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.STUDYGROUP);
        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS).child(FirebaseTables.ALL);
        mDatabaseCoursesStudyRoom = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.COURSES_STUDY_ROOMS);
        mDatabaseDept = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.DEPARTMENTS);
        course_name = (TextView) view.findViewById(R.id.course_name);
        course_id = (TextView) view.findViewById(R.id.course_id);
        course_summary = (TextView) view.findViewById(R.id.course_summary);
        course_schedule = (TextView) view.findViewById(R.id.courses_schedule);
        course_location = (ImageView) view.findViewById(R.id.course_location);
        course_dept = (TextView) view.findViewById(R.id.course_dept);
        green_sheet = (TextView) view.findViewById(R.id.green_sheet);
        green_sheet.setPaintFlags(green_sheet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        booksLayout = (LinearLayout) view.findViewById(R.id.books_layout);
        groupLayout = (LinearLayout) view.findViewById(R.id.group_layout);
        btn_study_group = (Button) view.findViewById(R.id.btn_study_group);
        btn_study_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_course_study_group_dialog, (ViewGroup) getView(), false);
                // Set up the input
                final EditText group_name = (EditText) viewInflated.findViewById(R.id.input_group_name);
                final EditText group_desc = (EditText) viewInflated.findViewById(R.id.input_group_description);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(group_name.getText().toString().trim().equals(""))  {
                            group_name.setError("Enter Group Name");
                        } else if(group_desc.getText().toString().trim().equals(""))  {
                            group_desc.setError("Enter Group Description");
                        } else {
                            createStudyGroup(group_name.getText().toString(), group_desc.getText().toString());
                        }
                        dialog.dismiss();
                        m_Text = group_name.getText().toString();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        mDatabaseCourses.addValueEventListener(CourseValueListener);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseCourses.removeEventListener(CourseValueListener);
    }

    private void createStudyGroup(String group_name, String group_desc) {
        StudyGroup study_group = new StudyGroup();
        study_group.setName(group_name);
        study_group.setDescription(group_desc);
        DatabaseReference ref = mDatabaseCourseStudyGroup.push();
        study_group.setId(ref.getKey());
        mDatabaseCoursesStudyRoom = ref.child(FirebaseTables.DETAILS);
        mDatabaseCoursesStudyRoom.setValue(study_group);
        mDatabaseCourses.child(FirebaseTables.STUDYGROUP).push().setValue(study_group.getId());
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
        void openUrl(String url);
        void openMapFragment (String location);
        void openStudyGroupMessageFragment(String group_id, String group_name);
    }
}
