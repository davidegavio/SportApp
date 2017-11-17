package it.uniupo.sportapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.Utility;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MatchInfoTabFragment extends Fragment implements Button.OnClickListener{

    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "season";

    private String matchIndex, seasonIndex;
    private Button editTeamsButton, editResultButton, editGoalsButton;
    private TextView homeResultTextView, awayResultTextView;


    public MatchInfoTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString(ARG_PARAM1);
            seasonIndex = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_info_tab, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        homeResultTextView = view.findViewById(R.id.result_home);
        awayResultTextView = view.findViewById(R.id.result_away);
        editTeamsButton = view.findViewById(R.id.edit_teams_btn);
        editResultButton = view.findViewById(R.id.edit_result_btn);
        editGoalsButton = view.findViewById(R.id.edit_goals_btn);
        if (!Utility.checkIfAdmin()) {
            editTeamsButton.setVisibility(View.INVISIBLE);
            editResultButton.setVisibility(View.INVISIBLE);
            editGoalsButton.setVisibility(View.INVISIBLE);
        }else {
            editTeamsButton.setOnClickListener(this);
            editResultButton.setOnClickListener(this);
            editGoalsButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_teams_btn:
                Toast.makeText(getContext(), "Edit teams!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_result_btn:

                Toast.makeText(getContext(), "Edit result!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_goals_btn:
                Toast.makeText(getContext(), "Edit goals!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void editResult() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View itemview = inflater.inflate(R.layout.edit_result_dialog, null);
        builder.setView(itemview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final EditText homeEt = itemview.findViewById(R.id.home_result_dialog);
                        final EditText awayEt = itemview.findViewById(R.id.away_result_dialog);

                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
