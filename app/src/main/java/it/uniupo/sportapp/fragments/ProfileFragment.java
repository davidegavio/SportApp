package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;
import it.uniupo.sportapp.models.Season;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCR = "descr";
    private static final String ARG_MAIL = "mail";
    private static final String ARG_UID = "uid";

    TextView nameTv, descriptionTv, emailTv;
    String uid;
    ImageView profileIv;
    Button roomsButton, newRoomButton;
    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;

    // TODO: Rename and change types of parameters
    private String mPlayerName;
    private String mPlayerDescription;
    private String mPlayerEmail;
    private String mPlayerUid;

    private OnFragmentInteractionListener mListener;
    private Player currentPlayer;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param playerName
     * @param playerDescription
     * @param playerEmail Parameter 1.
     * @param playerUid Parameter 2.   @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String playerName, String playerDescription, String playerEmail, String playerUid) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, playerName);
        args.putString(ARG_DESCR, playerDescription);
        args.putString(ARG_MAIL, playerEmail);
        args.putString(ARG_UID, playerUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerName = getArguments().getString(ARG_NAME);
            mPlayerDescription = getArguments().getString(ARG_DESCR);
            mPlayerEmail = getArguments().getString(ARG_MAIL);
            mPlayerUid = getArguments().getString(ARG_UID);
            currentPlayer = new Player(mPlayerName, mPlayerDescription, mPlayerEmail, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTv = view.findViewById(R.id.name_tv);
        descriptionTv = view.findViewById(R.id.description_tv);
        emailTv = view.findViewById(R.id.email_tv);
        profileIv = view.findViewById(R.id.profile_image);
        roomsButton = view.findViewById(R.id.rooms_btn);
        roomsButton.setOnClickListener(this);
        newRoomButton = view.findViewById(R.id.new_room_btn);
        newRoomButton.setOnClickListener(this);
        fillFields(currentPlayer);
    }

    public void onDeleteUser() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rooms_btn:

                break;
            case R.id.new_room_btn:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final View editview = inflater.inflate(R.layout.create_room_dialog, null);
                builder.setView(editview)
                        // Add action buttons
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText newRoomName = editview.findViewById(R.id.room_name_dialog);
                                Room newRoom = new Room(String.valueOf(newRoomName.getText()));
                                MainActivity.getLoggedPlayer().getPlayerRooms().add(newRoom);
                                newRoom.getActivePlayers().add(currentPlayer);
                                newRoom.getAdminPlayers().add(currentPlayer);
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                String k = mDatabase.child("rooms").child(mDatabase.push().getKey()).getKey();
                                Log.d("Key", k);
                                mDatabase.child("rooms").child(k).setValue(newRoom);
                                ((MainActivity)getActivity()).addFragment(RoomFragment.newInstance(String.valueOf(newRoomName.getText())));
                            }
                        })
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
        }
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
        void onFragmentInteraction();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_profile:
                showEditDialog();
                return true;

            case R.id.delete_profile:
                onDeleteUser();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private void editLoggedUser(String eName, String eDescription, String eEmail){
        if(!eName.equals(""))
            MainActivity.getLoggedPlayer().setPlayerName(eName);
        if(!eDescription.equals(""))
            MainActivity.getLoggedPlayer().setPlayerDescription(eDescription);
        if(!eEmail.equals(""))
            MainActivity.getLoggedPlayer().setPlayerMail(eEmail);
    }

    private void fillFields(Player player){
        nameTv.setText(player.getPlayerName());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if(player.getPlayerDescription().equals("")){
            descriptionTv.setText("Empty description, fill it with the edit button in the menu");
        }
        else descriptionTv.setText(player.getPlayerDescription());
        emailTv.setText(player.getPlayerMail());
        uid = getArguments().getString("uid");
        Log.d(TAG, "Url "+FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null)
            Picasso.with(getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profileIv);
    }
    
    private void showEditDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View editview = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(editview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editName = editview.findViewById(R.id.name_dialog);
                        EditText editDescription = editview.findViewById(R.id.description_dialog);
                        EditText editEmail = editview.findViewById(R.id.email_dialog);
                        editLoggedUser(editName.getText().toString(), editDescription.getText().toString(), editEmail.getText().toString());
                        fillFields(currentPlayer);
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("users").child(uid).setValue(currentPlayer);
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
