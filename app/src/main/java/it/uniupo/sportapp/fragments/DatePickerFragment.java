package it.uniupo.sportapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.DatePicker;

/**
 * Created by dgavio on 13/11/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = 0, month = 0, day = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            c = Calendar.getInstance();
        }


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
        // Do something with the date chosen by the user
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        Intent localIntent = new Intent("date_set");
        localIntent.putExtra("date", date);
        localBroadcastManager.sendBroadcast(localIntent);

    }
}