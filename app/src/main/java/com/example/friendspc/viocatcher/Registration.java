package com.example.friendspc.viocatcher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import com.firebase.client.core.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    Button bsignup;
    EditText uname,pword,pword2,email;

    private static final String TAG = "Registration";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //setTitle("Registration");

        mAuth = FirebaseAuth.getInstance();
        mContext=this;
        RFIDs();

        bsignup.setOnClickListener(this);
    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }*/

    public void RFIDs()
    {
        bsignup = (Button) findViewById(R.id.btnSigUp);
        email = (EditText) findViewById(R.id.etEmail);
        uname = (EditText) findViewById(R.id.etUsername);
        pword = (EditText) findViewById(R.id.etPassword);
        pword2 = (EditText) findViewById(R.id.etPassword2);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSigUp:

                String usern=uname.getText().toString();
                String email2=email.getText().toString();
                String password=pword.getText().toString();
                String confirmPassword=pword2.getText().toString();

                // check if any of the fields are vaccant
                if(email2.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(Registration.this, "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(Registration.this, "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    if (mContext != null) {
                        final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Please wait...", "Connecting...\n(Powered by LexDark)", true);

                        //final ProgressDialog prog=new ProgressDialog.show(Registration.this,"Please wait...","Connecting...", true);
                        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pword.getText().toString().trim())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        mProgressDialog.dismiss();
                               /* if(!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        password.setError(getString(R.string.error_weak_password));
                                        mTxtPassword.requestFocus();
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        mTxtEmail.setError(getString(R.string.error_invalid_email));
                                        mTxtEmail.requestFocus();
                                    } catch(FirebaseAuthUserCollisionException e) {
                                        mTxtEmail.setError(getString(R.string.error_user_exists));
                                        mTxtEmail.requestFocus();
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }*/

                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //Log.d(TAG, "createUserWithEmail:success");
                                            Toast.makeText(Registration.this, "Registration successful!",
                                                    Toast.LENGTH_SHORT).show();
                                   /* FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);*/
                                            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                            prefs.edit().putString("email", email.getText().toString()).apply();

                                           /* SharedPreferences prefs2 = getSharedPreferences("MyApp", MODE_PRIVATE);
                                            prefs2.edit().putString("uname", uname.getText().toString()).apply();*/

                                            //realtimedatabase
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            String uid = user.getUid();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                                            DatabaseReference myRef = database.getReference().child("AdminFile/"+uid);
                                            myRef.child("Username").setValue(uname.getText().toString());
                                            myRef.child("Email").setValue(email.getText().toString());
                                            Toast.makeText(Registration.this, "Realtime Database Updated!", Toast.LENGTH_LONG).show();
                                            //realtimedatabase

                                            Intent i = new Intent(Registration.this, NavigationActivity.class);
                                            //i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                            startActivity(i);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("signInWithEmail:failure", task.getException().toString());
                                            Toast.makeText(Registration.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }
                                    }
                                });
                    }
                    /*
                    String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
                    // check if the Stored password matches with Password entered by user
                    if(password.equals(storedPassword))
                    {
                        Toast.makeText(Registration.this, "Account already exist!\nPlease try again...", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Save personal name
                        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                        prefs.edit().putString("username", userName).apply();

                        // Save the Data in Database
                        loginDataBaseAdapter.insertEntry(userName, password);
                        Toast.makeText(Registration.this, "Account Successfully Created ", Toast.LENGTH_LONG).show();
                        finish();

                        //intent
                        Intent i=new Intent(this, MainActivity.class);
                        startActivity(i);
                    }*/
                }
                break;
        }
    }
}
