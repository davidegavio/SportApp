package it.uniupo.sportapp.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by dgavio on 13/11/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return

        return new TimePickerDialog(getContext(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        String time = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
        // Do something with the date chosen by the user
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        Intent localIntent = new Intent("time_set");
        localIntent.putExtra("time", time);
        localBroadcastManager.sendBroadcast(localIntent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        Intent localIntent = new Intent("error");
        //localBroadcastManager.sendBroadcast(localIntent);
    }

}