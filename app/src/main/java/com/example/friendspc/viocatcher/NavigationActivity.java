package com.example.friendspc.viocatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String emailnav2= "";
    public static String unamenav2= "";
    public static String TAG= "";
    TextView emailnav, unamenav;
    Button welcome;
    ListView simpleList;
    ArrayAdapter arrayAdapter;
    String countryList[] = {"ESPRESSO","ESPRESSO DOPPIO","ESPRESSO MACCHIATO","ESPRESSO CONPANNA","BREWED COFFEE", "HOT CAFE AMERICANO", "HOT CAFE LATTE", "HOT LATTE MACCHIATO", "HOT CAPPUCCINO", "HOT CAFE MOCHA"};

    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    Firebase fName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        MainFragment fragment=new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Administrator");
        setSupportActionBar(toolbar);

        /*arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_listviewtext, R.id.listviewtext, countryList);
        simpleList=(ListView)findViewById(R.id.simpleListView);
        simpleList.setAdapter(arrayAdapter);*/

        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        //SAVE DATA ON TOAST
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        emailnav2 = prefs.getString("email", "UNKNOWN");
        /*SharedPreferences prefs2 = getSharedPreferences("MyApp", MODE_PRIVATE);
        unamenav2 = prefs2.getString("uname", "UNKNOWN");*/
        //sAVE DATA ON TOAST



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        emailnav = (TextView) header.findViewById(R.id.emailnav);
        unamenav = (TextView) header.findViewById(R.id.unamenav);
        emailnav.setText(emailnav2);

        //for searching via UID
        FirebaseUser user =  mAuth.getCurrentUser();
        String uid = user.getUid();
        fName = new Firebase("https://viocatcher.firebaseio.com/AdminFile/" + uid + "/Username");

        fName.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usern = dataSnapshot.getValue(String.class);
                unamenav.setText(usern);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //for searching via UID
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            //LoginManager.getInstance().logOut();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //displaySelectedScreen(item.getItemId());
        int id = item.getItemId();

        if (id == R.id.nav_addstud) {
            toolbar.setTitle("Add Student");
            AddFragment fragment=new AddFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_viewstud) {
            toolbar.setTitle("View Student");
            Toast.makeText(this, "View Student is under construction!", Toast.LENGTH_SHORT).show();
            ViewFragment fragment=new ViewFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_updatestud) {
            toolbar.setTitle("Update Student");
            Toast.makeText(this, "Update Student is under construction!", Toast.LENGTH_SHORT).show();
            UpdateFragment fragment=new UpdateFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_deletestud) {
            toolbar.setTitle("Delete Student");
            Toast.makeText(this, "Delete Student is under construction!", Toast.LENGTH_SHORT).show();
            DeleteFragment fragment=new DeleteFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
