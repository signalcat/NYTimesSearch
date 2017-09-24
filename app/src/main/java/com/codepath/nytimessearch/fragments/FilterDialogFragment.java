package com.codepath.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
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

    private Filters filter = new Filters();
    private EditText etBeginDate;
    private Spinner spinner;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;

    // 1. Defines the listener interface with a method
    //    passing back filters as result to activity.
    public interface OnFilterSearchListener {
        void onUpdateFilters(Filters filter);
    }

    // constructor
    public static FilterDialogFragment newInstance(Parcelable filter) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filters", filter);
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
        // Display the picked date
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

        // Store the filters to a member variable
        filter = (Filters) Parcels.unwrap(getArguments().getParcelable("filters"));

        // Initialize edittext to let user click and show the date.
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        etBeginDate.setOnClickListener(view1 -> showDatePickerDialog(view1));

        // Initialize the spinner to let user select the order
        spinner = (Spinner) view.findViewById(R.id.spinnerSortOrder);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                filter.setOrder(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get the check box view
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashionStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);

        // Get the checked/uncheck value and store in filter object
        cbArts.setOnClickListener(view12 -> {
            if(cbArts.isChecked()) {
                filter.setArt(true);
            } else {
                filter.setArt(false);
            }
        });

        cbFashion.setOnClickListener(view13 -> {
            if(cbFashion.isChecked()) {
                filter.setFashion(true);
            } else {
                filter.setFashion(false);
            }
        });

        cbSports.setOnClickListener(view14 -> {
            if(cbSports.isChecked()) {
                filter.setSport(true);
            } else {
                filter.setSport(false);
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveFilter);
        btnSave.setOnClickListener(view15 -> {
            // Return filters back to activity through the implemented listener
            OnFilterSearchListener listener = (OnFilterSearchListener) getActivity();
            listener.onUpdateFilters(filter);
            // Close the dialog to return back to the parent activity
            dismiss();
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Filters");
        getDialog().setTitle(title);
    }
}
