package com.example.swapn.bookmyclass.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.MainActivity;
import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Book;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SellBookFragment extends Fragment {
    private EditText input_name;
    private EditText input_author;
    private EditText input_price;
    private EditText input_summary;
    private MultiAutoCompleteTextView courses_autocomplete;
    private EditText input_location;
    private Button btn_sell;
    private ImageView input_pic;
    private static final int CAMERA_REQUEST_IMAGE = 1;
    private Uri selectedPic;
    private User userData;
    private DatabaseReference mDatabaseBookSell;
    private DatabaseReference mDatabaseUser_Book;
    private DatabaseReference mDatabaseBook_COurses;
    private DatabaseReference mDatabaseCourses;
    private StorageReference mStorage;

    private ArrayAdapter<String> coursesAdapter;
    private HashMap<String, String> courses_ids = new HashMap<String,String>();

    private Double inputPrice;

    public SellBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_book, container, false);
        mDatabaseBookSell = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS)
                .child(FirebaseTables.SELLING_BOOKS);
        mDatabaseUser_Book = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.USER_BOOK).child(FirebaseTables.ALL);
        mDatabaseBook_COurses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.BOOK_COURSE);
        mDatabaseCourses = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.COURSES);
        mDatabaseCourses.addValueEventListener(coursesValueListener);

        mStorage = FirebaseStorage.getInstance().getReference();
        userData = ((MainActivity) getActivity()).getUserData();
        input_name = (EditText) view.findViewById(R.id.input_bookname);
        input_author = (EditText) view.findViewById(R.id.input_bookauthor);
        input_price = (EditText) view.findViewById(R.id.input_bookprice);
        input_summary = (EditText) view.findViewById(R.id.input_booksummary);
        //input_location = (EditText) view.findViewById(R.id.input_booklocation);

        //Courses
      //  String[] MultipleTextStringValue = { "Android","Android-MultiAutoCompleteTextView","Android Top Tutorials" };
        courses_autocomplete = (MultiAutoCompleteTextView) view.findViewById(R.id.courses_autocomplete);
        coursesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        courses_autocomplete.setAdapter(coursesAdapter);
        courses_autocomplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        input_pic = (ImageView) view.findViewById(R.id.input_bookpic);
        btn_sell = (Button) view.findViewById(R.id.btn_next);
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellBook();
            }
        });
        input_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, CAMERA_REQUEST_IMAGE);
            }
        });
        return view;
    }

    protected boolean validate() {
        boolean valid = false;
        if(selectedPic == null) {
            Toast.makeText(getActivity(),"Uplaod Profile Picture...", Toast.LENGTH_LONG).show();
            return false;
        }

        if (input_name.getText().toString().isEmpty()) {
            input_name.setError("Enter Book Name...");
            return false;
        }

        if (input_author.getText().toString().isEmpty()) {
            input_author.setError("Enter Author...");
            return false;
        }

        if(input_summary.getText().toString().isEmpty()) {
            input_summary.setError("Enter Summary...");
            return false;
        }

     /*   if(courses_autocomplete.getText().toString().isEmpty()) {
            courses_autocomplete.setError("Please Select Courses...");
            return false;
        } */

        try {
            if(input_price.getText().toString().isEmpty()) {
                input_price.setError("Enter Price...");
                return false;
            }
            inputPrice = Double.parseDouble(input_price.getText().toString());
        } catch(Exception e) {
            input_price.setError("Please Enter Price in Number...");
            return false;
        }

        return true;
    }

    private void sellBook () {

        if(validate()) {
            Book book = new Book();
            book.setBook_name(input_name.getText().toString());
            book.setAuthor(input_author.getText().toString());
            book.setPrice(inputPrice);
            book.setSummary(input_summary.getText().toString());
            book.setCourse_ids(getCourseIdsFromNames(courses_autocomplete.getText().toString(), courses_ids));
            book.setBook_image(selectedPic.toString());
            ((SellBookMasterFragment)getParentFragment()).saveBookInformation(book);
        } else {
            Toast.makeText(getActivity(), "Please Check the Information Entered.", Toast.LENGTH_LONG).show();
        }
    }

    protected List<String> getCourseIdsFromNames(String selectedCourses, HashMap<String,String> courses_ids) {
        String[] courses = selectedCourses.split(",");
        ArrayList<String> courseIds = new ArrayList<String>();
        for(String course : courses) {
            courseIds.add(courses_ids.get(course.trim()));
        }
        return courseIds;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_IMAGE){
            selectedPic = data.getData();
            Picasso.with(getActivity()).load(selectedPic).fit().centerCrop().into(input_pic);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    ValueEventListener coursesValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                coursesAdapter.clear();
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot coursesSnapshot : dataSnapshot.getChildren()) {
                    String course_id = coursesSnapshot.child("courseid").getValue().toString();
                    courses_ids.put(course_id, coursesSnapshot.getKey());
                    //Get the suggestion by childing the key of the string you want to get.
                    // String suggestion = suggestionSnapshot.child("suggestion").getValue(String.class);
                    //Add the retrieved string to the list
                    coursesAdapter.add(course_id);
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
    }

}
