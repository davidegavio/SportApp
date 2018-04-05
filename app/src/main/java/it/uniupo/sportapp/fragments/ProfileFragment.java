package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;
import it.uniupo.sportapp.models.Season;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Singleton.getCurrentPlayer() != null) {
            mPlayerName = Singleton.getCurrentPlayer().getPlayerName();
            mPlayerDescription = Singleton.getCurrentPlayer().getPlayerDescription();
            mPlayerEmail = Singleton.getCurrentPlayer().getPlayerMail();
            mPlayerUid = FirebaseAuth.getInstance().getUid();
            currentPlayer = new Player(mPlayerName, mPlayerDescription, mPlayerEmail, false);
            currentPlayer.setPlayerKey(Singleton.getCurrentPlayer().getPlayerKey());
        }
        Singleton.setCurrentFragment("profile");
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
                ((MainActivity)getActivity()).addFragment(new RoomListFragment());
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
                                newRoom.getActivePlayers().add(currentPlayer.getPlayerKey());
                                newRoom.getAdminPlayers().add(currentPlayer.getPlayerKey());
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String k = mDatabase.child("rooms").child(mDatabase.push().getKey()).getKey();
                                Log.d("Key", k);
                                newRoom.setRoomKey(k);
                                Singleton.getCurrentPlayer().getPlayerRooms().add(newRoom.getRoomKey());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Singleton.getCurrentPlayer());
                                int n = Singleton.getCurrentPlayer().getPlayerRooms().size()-1;
                                RoomFragment fragment = new RoomFragment();
                                Bundle args = new Bundle();
                                args.putString("key", newRoom.getRoomKey());
                                //args.putInt("index", n);
                                fragment.setArguments(args);
                                Singleton.setCurrentRoom(newRoom);
                                ((MainActivity)getActivity()).addFragment(fragment);
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

    private void editLoggedUser(String eName, String eDescription, String eEmail){
        if(!eName.equals(""))
            Singleton.getCurrentPlayer().setPlayerName(eName);
        if(!eDescription.equals(""))
            Singleton.getCurrentPlayer().setPlayerDescription(eDescription);
        if(!eEmail.equals(""))
            Singleton.getCurrentPlayer().setPlayerMail(eEmail);
    }

    private void fillFields(Player player){
        nameTv.setText(player.getPlayerName());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if(player.getPlayerDescription().equals("")){
            descriptionTv.setText("Empty description, fill it with the edit button in the menu");
        }
        else descriptionTv.setText(player.getPlayerDescription());
        emailTv.setText(player.getPlayerMail());
        uid = FirebaseAuth.getInstance().getUid();
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
        final EditText editName = editview.findViewById(R.id.name_dialog);
        editName.setText(Singleton.getCurrentPlayer().getPlayerName());
        final EditText editDescription = editview.findViewById(R.id.description_dialog);
        editDescription.setText(Singleton.getCurrentPlayer().getPlayerDescription());
        final EditText editEmail = editview.findViewById(R.id.email_dialog);
        editEmail.setText(Singleton.getCurrentPlayer().getPlayerMail());
        builder.setView(editview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        editLoggedUser(editName.getText().toString(), editDescription.getText().toString(), editEmail.getText().toString());
                        fillFields(Singleton.getCurrentPlayer());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("users").child(uid).setValue(Singleton.getCurrentPlayer());
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
