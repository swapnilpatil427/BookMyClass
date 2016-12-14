package com.example.swapn.bookmyclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.common.Constants;
import com.example.swapn.bookmyclass.database.FirebaseTables;
import com.example.swapn.bookmyclass.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private EditText _nameText;
    private EditText _addressText;
    private EditText _emailText;
    private EditText _mobileText;
    private EditText _passwordText;
    private EditText _reEnterPasswordText;
    private Button _signupButton;
    private TextView _loginLink;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference databaseUserRef = databaseRef.child(FirebaseTables.USERS_TABLE);
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        // _mobileText = (EditText) findViewById(R.id.input_mobile);
        // _addressText = (EditText) findViewById(R.id.input_address);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
    }

    public void signup() {
        Log.d(Constants.TAG_Login_MESSAGE, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account and Sending Verification Link...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        //  final String address = _addressText.getText().toString();
        final String email = _emailText.getText().toString();
        // String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        if (validate()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(Constants.TAG_Login_MESSAGE, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            //TODO Change address to Gender


                            if (!task.isSuccessful()) {
                                onSignupFailed();
                            } else {
                                onSignupSuccess(name, email);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }
    }



    public void onSignupSuccess(String name, String email) {
        FirebaseUser userInfo = mAuth.getCurrentUser();
    /*    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        userInfo.updateProfile(profileUpdates); */

        userInfo.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Constants.TAG_SignUp_MESSAGE, "Email sent.");
                            openLoginActivity();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this,R.string.signupfailed, Toast.LENGTH_LONG).show();
                            Log.e(Constants.TAG_SignUp_ERROR,"Error sending verification email.");
                        }
                    }
                });
        progressDialog.setMessage("Sending Verification Email...");
        String user_id = userInfo.getUid();
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUid(user_id);
        user.setAccount_setuped(false);
        if(databaseUserRef != null)
            databaseUserRef.child(user.getUid()).setValue(user);
        else
            databaseRef.child(FirebaseTables.USERS_TABLE).child(user.getUid()).setValue(user);
    }

    public void openLoginActivity () {
        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(loginIntent);
    }

    public void onSignupFailed() {
        Toast.makeText(SignUpActivity.this, R.string.user_exists,
                Toast.LENGTH_SHORT).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        //    String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
