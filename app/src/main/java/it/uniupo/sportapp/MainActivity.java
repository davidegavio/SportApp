package it.uniupo.sportapp;

import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import it.uniupo.sportapp.fragments.AboutFragment;
import it.uniupo.sportapp.fragments.DatePickerFragment;
import it.uniupo.sportapp.fragments.MatchDetailFragment;
import it.uniupo.sportapp.fragments.ProfileFragment;
import it.uniupo.sportapp.fragments.RoomFragment;
import it.uniupo.sportapp.fragments.SeasonDetailFragment;
import it.uniupo.sportapp.fragments.TimePickerFragment;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

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
    private String roomKey, roomIndex, seasonIndex, matchIndex, fragmentSession, userToRestore;
    private boolean firstOpen = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo("it.uniupo.sportapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Singleton singleton = new Singleton();
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
                    RoomFragment fragment = new RoomFragment();
                    Bundle args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    fragment.setArguments(args);
                    addFragment(fragment);
                    break;
                case "seasonList":
                    fragment = new RoomFragment();
                    args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    fragment.setArguments(args);
                    addFragment(fragment);
                    break;
                case "playerList":
                    fragment = new RoomFragment();
                    args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    fragment.setArguments(args);
                    addFragment(fragment);
                    break;
                case "roomList":
                    fragment = new RoomFragment();
                    args = new Bundle();
                    if(Singleton.getCurrentRoom()!=null) {
                        args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                        fragment.setArguments(args);
                        addFragment(fragment);
                    }
                    else addFragment(new ProfileFragment());
                    break;
                case "matchDetail":
                    SeasonDetailFragment sFragment = new SeasonDetailFragment();
                    args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    args.putString("season", String.valueOf(Singleton.getCurrentRoom().getExistingSeasons().size()-1));
                    sFragment.setArguments(args);
                    addFragment(sFragment);
                    break;
                case "chart":
                    sFragment = new SeasonDetailFragment();
                    args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    args.putString("season", String.valueOf(Singleton.getCurrentRoom().getExistingSeasons().size()-1));
                    sFragment.setArguments(args);
                    addFragment(sFragment);
                    break;
                case "about":
                    addFragment(new ProfileFragment());
                    break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK) {
                initViews();
                isAuthenticated = true;
                writeNewUserIfNeeded();
            }
        }
    }

    private void registerNotification() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(loggedPlayer.getPlayerKey()).child("messageToNotify");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!firstOpen)
                    notifyUser(dataSnapshot.getValue(String.class));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().getFragments().clear();
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
            case R.id.nav_about:
                Log.i(TAG, "About pressed");
                addFragment(new AboutFragment());
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
                    loggedPlayer.setPlayerImageUid(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()));
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
                registerNotification();
                initRoom();
                addFragment(new ProfileFragment());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRoom() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        userToRestore = sharedPref.getString("userToRestore", "");
        fragmentSession = sharedPref.getString("fragmentSession", "");
        roomKey = sharedPref.getString("roomKey", "");
        seasonIndex = sharedPref.getString("seasonIndex", "");
        matchIndex = sharedPref.getString("matchIndex", "");
        roomIndex = sharedPref.getString("roomIndex", "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Room room = dataSnapshot.getValue(Room.class);
                Singleton.setCurrentRoom(room);
                if(Singleton.getCurrentPlayer().getPlayerKey().equals(userToRestore))
                    restoreSession();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void addFragment(Fragment fragment) {
        // Fragment Manager
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.your_placeholder, fragment);
        fragmentTransaction.commit();
        firstOpen = false;
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

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void restoreSession(){
        Toast.makeText(getApplicationContext(), "Taking you where you left last time...", Toast.LENGTH_SHORT).show();
        switch (fragmentSession){
            case "room":
                RoomFragment fragment = new RoomFragment();
                Bundle args = new Bundle();
                args.putString("key", roomKey);
                args.putString("index", roomIndex);
                fragment.setArguments(args);
                addFragment(fragment);
                break;
            case "seasonDetailed":
                SeasonDetailFragment sFragment = new SeasonDetailFragment();
                args = new Bundle();
                args.putString("key", roomKey);
                args.putString("season", seasonIndex);
                sFragment.setArguments(args);
                addFragment(sFragment);
                break;
            case "matchDetailed":
                MatchDetailFragment matchDetailFragment = new MatchDetailFragment();
                args = new Bundle();
                args.putString("key", roomKey);
                args.putString("season", seasonIndex);
                args.putString("match", matchIndex);
                matchDetailFragment.setArguments(args);
                addFragment(matchDetailFragment);
                break;
        }
    }
    private void notifyUser(String chatMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.teams48)
                        .setDefaults(DEFAULT_VIBRATE)
                        .setPriority(DEFAULT_ALL)
                        .setContentTitle("SportApp")
                        .setContentText(chatMessage)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(chatMessage));
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(001, mBuilder.build());
        Log.d("c", "C");


    }

}
