package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.models.Player;

public class ProfileFragment extends Fragment {

    TextView nameTv, descriptionTv, emailTv;
    String uid;

    public ProfileFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_profile:
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
                                nameTv.setText(editName.getText());
                                descriptionTv.setText(editDescription.getText());
                                emailTv.setText(editEmail.getText());
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(uid).child("name").setValue(nameTv.getText());
                                mDatabase.child("users").child(uid).child("description").setValue(descriptionTv.getText());
                                //mDatabase.child("users").child(uid).child());
                            }
                        })
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return true;

            case R.id.delete_profile:
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTv = view.findViewById(R.id.name_tv);
        descriptionTv = view.findViewById(R.id.description_tv);
        emailTv = view.findViewById(R.id.email_tv);
        nameTv.setText(getArguments().getString("name"));
        descriptionTv.setText(getArguments().getString("description"));
        emailTv.setText(getArguments().getString("email"));
        uid = getArguments().getString("uid");
    }


}
