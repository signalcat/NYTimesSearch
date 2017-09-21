package com.codepath.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.activities.SearchActivity;
import com.codepath.nytimessearch.myclass.Filters;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by hezhang on 9/19/17.
 */

public class FilterDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public FilterDialogFragment() {
    }
    private Filters filter = new Filters("default", "default", "default");
    private EditText etBeginDate;

    // constructor
    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        filterDialogFragment.setArguments(args);
        return filterDialogFragment;
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        // Get the beginDate here from the calendar parsed to correct format
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String urlDate = format.format(c.getTime());
        // => "20160405"
        // Store this date into the filers object
        filter.setDate(urlDate);
        // send through bundle back to
        etBeginDate.setText(filter.getDate(), TextView.BufferType.NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        return view;
    }

    // After the view has been created
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize button view and attach a onclick listener
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveFilter);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Filters");
        getDialog().setTitle(title);
    }
}
