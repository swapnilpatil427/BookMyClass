package com.example.swapn.bookmyclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.swapn.bookmyclass.common.Constants;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AccountSetupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int CAMERA_REQUEST_IMAGE = 1;
    private Uri selectedPic;
    private String gender = "";
    ImageView prof_pic;
    EditText address;
    FirebaseUser user;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    User userData;
    ProgressDialog progressDialog;
    ValueEventListener userValueListener;
    Util u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u = new Util();
        setContentView(R.layout.activity_account_setup);
        prof_pic = (ImageView) findViewById(R.id.profile_picture);
        address = (EditText) findViewById(R.id.input_address);

        prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, CAMERA_REQUEST_IMAGE);
            }
        });
        Button submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        mStorage = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        if (user != null) {
            mDatabaseUser.addValueEventListener(postListener);
        } else {
            openLoginActivity();
        }
        progressDialog = new ProgressDialog(AccountSetupActivity.this,
                R.style.AppTheme_Dark_Dialog);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseUser.removeEventListener(postListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_IMAGE){
            selectedPic = data.getData();
            Picasso.with(AccountSetupActivity.this).load(selectedPic).fit().centerCrop().into(prof_pic);
        }
    }

    protected boolean validate() {
        //TODO add address validator and Autocomplete for address

        boolean valid = false;
    /*    if(selectedPic == null) {
            Toast.makeText(AccountSetupActivity.this,"Uplaod Profile Picture...", Toast.LENGTH_LONG).show();
            return false;
        } */

        if(gender.isEmpty()) {
            Toast.makeText(this,"Select Gender...", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender = "male";
                break;
            case R.id.radio_female:
                if (checked)
                    gender = "female";
                break;
        }
    }

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI

            userData = dataSnapshot.getValue(User.class);
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("AccountSetup", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    private void saveUserData() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Data...");
        progressDialog.show();
        if(validate()) {
            if(selectedPic != null) {
                StorageReference filePath = mStorage.child("photos").child(selectedPic.getLastPathSegment());
                filePath.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        StoreUserData(taskSnapshot.getDownloadUrl());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountSetupActivity.this, "Failed to Uplaod Data...", Toast.LENGTH_LONG);
                        progressDialog.hide();
                    }
                });
            } else {
                StoreUserData(null);
            }
        } else {
            Log.d("AccountSetup", "Failed to Validated");
            progressDialog.hide();
        }
    }

    public void StoreUserData(Uri uri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userData.getName().toString())
                .build();
        user.updateProfile(profileUpdates);
        Uri downloadUri = uri;
        userData.setAccount_setuped(true);
        if(uri == null) {
            userData.setProf_pic(Constants.DEFAULT_IMAGE);
        } else
            userData.setProf_pic(downloadUri.toString());
        userData.setAddress(address.getText().toString());
        userData.setGender(gender);
        mDatabaseUser.child(user.getUid()).setValue(userData);
        u.setSharedPreferences(getApplicationContext(), userData);
        progressDialog.hide();
        openMainActivity();
    }

    public void openMainActivity() {
        Intent loginIntent = new Intent(AccountSetupActivity.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    public void openLoginActivity () {
        Intent loginIntent = new Intent(AccountSetupActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(loginIntent);
    }
}
