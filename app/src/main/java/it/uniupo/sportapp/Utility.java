package it.uniupo.sportapp;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import it.uniupo.sportapp.fragments.TimePickerFragment;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by 20010562 on 10/24/17.
 */

public class Utility extends AppCompatActivity{


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


}
