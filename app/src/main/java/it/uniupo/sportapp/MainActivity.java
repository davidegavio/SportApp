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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import it.uniupo.sportapp.fragments.ProfileFragment;
import it.uniupo.sportapp.fragments.RoomFragment;
import it.uniupo.sportapp.fragments.SeasonDetailFragment;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener {

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "Log info";
    private static final String USERS_TABLE = "users";
    //Firebase authenticator
    private FirebaseAuth mAuth;
    //Firebase authenticator listener
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static Player loggedPlayer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(USERS_TABLE);
    private boolean isAuthenticated = false;




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
    public void onBackPressed() {
        // Handle back button clicks here
        Log.d(TAG, "Back pressed");
        Log.d(TAG, Singleton.getCurrentFragment());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            switch (Singleton.getCurrentFragment()) {
                case "room":
                    addFragment(new ProfileFragment());
                    break;
                case "seasonDetailed":
                    break;
                case "seasonList":
                    addFragment(new SeasonDetailFragment());
                    break;
                case "playerList":
                    RoomFragment fragment = new RoomFragment();
                    Bundle args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    fragment.setArguments(args);
                    addFragment(fragment);
                    break;
                case "roomList":
                    fragment = new RoomFragment();
                    args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    fragment.setArguments(args);
                    addFragment(fragment);
                    break;

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                initViews();
                isAuthenticated = true;
                writeNewUserIfNeeded();
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
        Log.d(TAG, getCurrentFirebaseUser().getEmail());
        isAuthenticated = false;
        FirebaseAuth.getInstance().signOut();
        mAuth.removeAuthStateListener(mAuthListener);
        mAuth = FirebaseAuth.getInstance();
        showSignInDialog();
    }

    private FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void writeNewUserIfNeeded() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child(getCurrentFirebaseUser().getUid()).exists() && isAuthenticated) {
                    loggedPlayer = new Player(getCurrentFirebaseUser().getDisplayName(), "", getCurrentFirebaseUser().getEmail(), false);
                    if(loggedPlayer.getPlayerRooms() == null)
                        loggedPlayer.setPlayerRooms(new ArrayList<String>());
                    loggedPlayer.setPlayerKey(getCurrentFirebaseUser().getUid());
                    userRef.child(getCurrentFirebaseUser().getUid()).setValue(loggedPlayer);
                    Log.d(TAG, "Player doesn't exist yet.");
                }
                else if(dataSnapshot.child(getCurrentFirebaseUser().getUid()).exists() && isAuthenticated){
                    loggedPlayer = dataSnapshot.child(getCurrentFirebaseUser().getUid()).getValue(Player.class);
                    if(loggedPlayer.getPlayerRooms() == null)
                        loggedPlayer.setPlayerRooms(new ArrayList<String>());
                    Log.d(TAG, "Name: "+loggedPlayer.getPlayerName()+" "+"Description: "+loggedPlayer.getPlayerDescription()+" "+"Email: "+loggedPlayer.getPlayerMail());
                }
                Singleton.setCurrentPlayer(loggedPlayer);
                addFragment(new ProfileFragment());
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

    public static Player getLoggedPlayer(){
        return loggedPlayer;
    }

    public void showSignInDialog(){
        mAuth.addAuthStateListener(mAuthListener);
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.teams96)
                .setTheme(R.style.GreyTheme)
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                .setTosUrl(GOOGLE_TOS_URL)
                .setIsSmartLockEnabled(false, true)
                .setAllowNewEmailAccounts(true)
                .build(), RC_SIGN_IN);
    }



    @Override
    public void onFragmentInteraction() {
        isAuthenticated = false;
        userRef.child(getCurrentFirebaseUser().getUid()).removeValue();
        getCurrentFirebaseUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                signIn();
            }
        });
    }
}
