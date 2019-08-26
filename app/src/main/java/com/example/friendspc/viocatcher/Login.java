package com.example.friendspc.viocatcher;

import android.app.ProgressDialog;
//import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendspc.viocatcher.NavigationActivity;
import com.example.friendspc.viocatcher.R;
import com.example.friendspc.viocatcher.Registration;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Alyssa on 3/5/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    //private Firebase fElement, fNames;
    //int increement = 0;
    private Context mContext;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginPage";
    TextView tvSighUpS;
    Button log;
    /**Button sbsignup;*/
    EditText email,pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //setTitle("Login");
        //Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();

        rFIDs();
        tvSighUpS.setOnClickListener(this);
        log.setOnClickListener(this);
        mContext = this;
       /* fElement = new Firebase("https://account-login-4dc0a.firebaseio.com/" + increement + "/name");

        fElement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                String fname = dataSnapshot.getValue(String.class);
                Toast.makeText(Login.this, "First Element is: "+fname, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

            if (currentUser != null) {
                if (mContext != null) {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Please wait...", "\n(...PoweredByLexDark...)", true);

                    Intent profile = new Intent(Login.this, NavigationActivity.class);
                    startActivity(profile);
                    mProgressDialog.dismiss();
            }
        }
        /*String userid=mAuth.getCurrentUser().toString();
        Toast.makeText(Login.this, "User: "+ userid , Toast.LENGTH_LONG).show();*/
    }

    public void rFIDs(){
        tvSighUpS = (TextView) findViewById(R.id.tvSignup);
        log = (Button) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.edtEmail);
        pword = (EditText) findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvSignup:

                Intent signup = new Intent(Login.this, Registration.class);
                startActivity(signup);
                //finish();
                break;

            case R.id.btnLogin:
                String email2= email.getText().toString();
                String password= pword.getText().toString();
                if(email2.equals("")||password.equals(""))
                {
                    Toast.makeText(Login.this, "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                /*
                String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
                // check if the Stored password matches with Password entered by user
                if(password.equals(storedPassword))
                {
                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                    prefs.edit().putString("username", userName).apply();

                    finish();

                    Intent login = new Intent(this, MainActivity.class);
                    startActivity(login);

                    Toast.makeText(Login.this, "Login Successfull!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }*/


                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("email", email.getText().toString()).apply();

                if (mContext != null) {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(mContext,"Please wait...","Connecting...\n(Powered by LexDark)", true);


                //final ProgressDialog prog=new ProgressDialog.show(Login.this,"Please wait...","Connecting...", true);
                (mAuth.signInWithEmailAndPassword(email2, password))
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");
                                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                    prefs.edit().putString("email", email.getText().toString()).apply();
                                    Intent login = new Intent(Login.this, NavigationActivity.class);
                                    startActivity(login);
                                    finish();
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w("signInWithEmail:failure", task.getException().toString());
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    //updateUI(null);
                                }
                            }
                        });
                }
                break;
        }
    }

}
