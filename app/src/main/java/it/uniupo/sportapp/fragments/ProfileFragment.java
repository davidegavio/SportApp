package it.uniupo.sportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;

public class ProfileFragment extends Fragment {

    TextView nameTv, descriptionTv, emailTv;

    public ProfileFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTv = view.findViewById(R.id.name_tv);
        descriptionTv = view.findViewById(R.id.description_tv);
        emailTv = view.findViewById(R.id.email_tv);
        nameTv.setText(getArguments().getString("name"));
        descriptionTv.setText(getArguments().getString("description"));
        emailTv.setText(getArguments().getString("email"));
    }


}
