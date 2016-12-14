package com.example.swapn.bookmyclass.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swapn.bookmyclass.R;
import com.example.swapn.bookmyclass.models.User_Book;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookSellingInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookSellingInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookSellingInformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    final int PLACE_PICKER_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText selling_place;
    private EditText seller_email;
    private EditText sellect_contact;
    private Button btn_sell;

    private String address = "";

    public BookSellingInformationFragment() {
        // Required empty public constructor
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                address = place.getName().toString() + place.getAddress().toString();
                selling_place.setText(place.getAddress().toString());
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookSellingInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookSellingInformationFragment newInstance(String param1, String param2) {
        BookSellingInformationFragment fragment = new BookSellingInformationFragment();
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
        View view = inflater.inflate(R.layout.fragment_book_selling_information, container, false);
        selling_place = (EditText) view.findViewById(R.id.input_place);
        seller_email = (EditText) view.findViewById(R.id.input_email);
        sellect_contact = (EditText) view.findViewById(R.id.input_contact);
        btn_sell = (Button) view.findViewById(R.id.btn_sell);
        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookSellingInformation();
            }
        });
        selling_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    protected boolean validate() {
        boolean valid = false;

        if (selling_place.getText().toString().isEmpty()) {
            selling_place.setError("Please select a place..");
            return false;
        }

        if (seller_email.getText().toString().isEmpty()) {
            seller_email.setError("Please select a email...");
            return false;
        }

        if(sellect_contact.getText().toString().isEmpty()) {
            sellect_contact.setError("Please Enter Contact...");
            return false;
        }

     /*   if(courses_autocomplete.getText().toString().isEmpty()) {
            courses_autocomplete.setError("Please Select Courses...");
            return false;
        } */



        return true;
    }

    public void saveBookSellingInformation() {
        if(validate()) {
            User_Book user_book = new User_Book();
            user_book.setPlace(selling_place.getText().toString());
            user_book.setEmail(seller_email.getText().toString());
            user_book.setContact(sellect_contact.getText().toString());
            user_book.setSelling_date(new Date());
            user_book.setSold(false);
            user_book.setSold_date(null);
            ((SellBookMasterFragment)getParentFragment()).saveBookInformation(user_book);

        } else {
            Toast.makeText(getActivity(), "Please Check the Information Entered.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
