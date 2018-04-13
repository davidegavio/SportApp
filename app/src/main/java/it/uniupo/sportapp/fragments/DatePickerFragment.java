package it.uniupo.sportapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.DatePicker;

/**
 * Created by dgavio on 13/11/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    String matchIndex="";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments()!=null)
            matchIndex = getArguments().getString("match");
        // Use the current date as the default date in the picker
        int year = 0, month = 0, day = 0;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("DPF", String.valueOf(month));
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
        // Do something with the date chosen by the user
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        Intent localIntent = new Intent("date_set");
        localIntent.putExtra("date", date);
        localBroadcastManager.sendBroadcast(localIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Bundle b = new Bundle();
        b.putString("match", matchIndex);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"timePicker");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        Intent localIntent = new Intent("error");
        localBroadcastManager.sendBroadcast(localIntent);
    }
}