package com.codepath.nytimessearch.fragments;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.myclass.Filters;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hezhang on 9/19/17.
 */

public class DatePickerFragment extends DialogFragment {
    // Defines the listener interface
    public interface DatePickerDialogListener {
        void onFinishDatePick(String inputText);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Parent filters fragment needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
