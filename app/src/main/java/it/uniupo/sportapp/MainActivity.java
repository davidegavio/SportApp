package it.uniupo.sportapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import it.uniupo.sportapp.fragments.ProfileFragment;
import it.uniupo.sportapp.models.Player;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "Log info";
    private static final String USERS_TABLE = "users" ;
    //Firebase authenticator
    private FirebaseAuth mAuth;
    //Firebase authenticator listener
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Player loggedPlayer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //private ProfileFragment profileFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        //Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (getCurrentFirebaseUser() != null) {
                    // User is signed in
                    loggedPlayer = new Player(getCurrentFirebaseUser().getDisplayName(), "Player3", getCurrentFirebaseUser().getEmail() ,false);
                    writeNewUserIfNeeded();
                    Log.d(TAG, "Name: "+loggedPlayer.getPlayerName()+" "+"Description: "+loggedPlayer.getPlayerDescription()+" "+"Email: "+loggedPlayer.getPlayerMail());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + getCurrentFirebaseUser().getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        signIn();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                initViews();
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", loggedPlayer.getPlayerName());
                bundle.putString("description", loggedPlayer.getPlayerDescription());
                bundle.putString("email", loggedPlayer.getPlayerMail());
                bundle.putString("uid", getCurrentFirebaseUser().getUid());
                profileFragment.setArguments(bundle);
                addFragment(profileFragment);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            drawer.openDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case R.id.nav_football:
                Log.i(TAG, "Football pressed");
                break;
            case R.id.nav_settings:
                Log.i(TAG, "Settings pressed");
                break;
            case R.id.nav_about:
                Log.i(TAG, "About pressed");
                break;
            case R.id.nav_logout:
                signOut();
                break;
        }
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signIn() {
        showSignInDialog();
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        showSignInDialog();
    }

    private FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void writeNewUserIfNeeded() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_TABLE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(getCurrentFirebaseUser().getUid()).exists()) {
                    ref.child(getCurrentFirebaseUser().getUid()).setValue(loggedPlayer);
                    Log.d(TAG, "Player doesn't exist yet.");
                }
                else Log.d(TAG, "Player already exists.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFragment(Fragment fragment) {
        // Fragment Manager
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.your_placeholder, fragment);
        fragmentTransaction.commit();
    }

    public void showSignInDialog(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.teams96)
                .setTheme(R.style.GreyTheme)
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .setTosUrl(GOOGLE_TOS_URL)
                .setIsSmartLockEnabled(false, true)
                .setAllowNewEmailAccounts(true)
                .build(), RC_SIGN_IN);
    }

    private void initViews(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
