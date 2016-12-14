package com.example.swapn.bookmyclass.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.adapters.PagerAdapter;
import com.example.swapn.bookmyclass.common.LockableViewPager;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.User;
import com.example.swapn.bookmyclass.models.User_Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellBookMasterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SellBookMasterFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private PagerAdapter mPagerAdapter;
    private Book book;
    private User_Book user_book;
    LockableViewPager pager;
    private DatabaseReference mDatabaseBookSell;
    private DatabaseReference mDatabaseBooks;
    private DatabaseReference mDatabaseUser_Book;
    private DatabaseReference mDatabaseBook_COurses;
    private DatabaseReference mDatabaseCourses;
    private User userData;
    private StorageReference mStorage;

    public SellBookMasterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_book_master, container, false);
        mStorage = FirebaseStorage.getInstance().getReference();
        initialisePaging(view);
        mDatabaseBookSell = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS)
                .child(FirebaseTables.SELLING_BOOKS);
        mDatabaseBooks = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.BOOKS).child(FirebaseTables.ALL);
        mDatabaseUser_Book = FirebaseDatabase.getInstance().getReference().child(FirebaseTables.MAPPING)
                .child(FirebaseTables.USER_BOOK);
        return view;
    }

    private void initialisePaging(View view) {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(getContext(),SellBookFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(),BookSellingInformationFragment.class.getName()));
        mPagerAdapter = new PagerAdapter(this.getChildFragmentManager(), fragments);
        userData = mListener.getUserData();

        pager = (LockableViewPager) view.findViewById(R.id.pager);
        pager.setSwipeLocked(false);
        pager.setAdapter(mPagerAdapter);
    }

    public void MoveNext() {
        //it doesn't matter if you're already in the last item
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    public void MovePrevious() {
        //it doesn't matter if you're already in the first item
        pager.setCurrentItem(pager.getCurrentItem() - 1);
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


    public void saveBookInformation(Book book) {
        this.book = book;
        MoveNext();
    }

    public void saveBookInformation(User_Book user_book) {
        this.user_book = user_book;
        if(book != null) {
            saveToFirebaseDatabase();
            mListener.openSellingBookFragment();
        } else {
            Toast.makeText(getActivity(),"Please comple the information on previous page", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToFirebaseDatabase () {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Data...");
        progressDialog.show();
        final Uri selectedPic = Uri.parse(book.getBook_image());
        StorageReference filePath = mStorage.child("photos").child(selectedPic.getLastPathSegment());
        filePath.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Store Data under selling/book
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                if(selectedPic != null) {
                    book.setBook_image(downloadUri.toString());
                } else {
                    book.setBook_image("book_default");
                }
                DatabaseReference bookRef = mDatabaseBookSell.push();
                book.setBook_id(bookRef.getKey());
                mDatabaseBooks.child(book.getBook_id()).setValue(book);
                bookRef.setValue(book);
                //bookRef.child("book_id").setValue(bookRef.getKey());

                // Storing the User to book mapping user user_book
                user_book.setBook_id(bookRef.getKey());
                user_book.setUser_id(userData.getUid());
                mDatabaseUser_Book.push().setValue(user_book);

                // #TODO Add courses
                // Add book to course mapping

                Log.d("Key",bookRef.getKey());
                progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed to Uplaod Data...", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });
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
        void openSellingBookFragment();
    }
}
