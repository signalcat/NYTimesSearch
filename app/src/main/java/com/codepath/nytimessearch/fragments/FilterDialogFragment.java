package com.codepath.nytimessearch.fragments;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.nytimessearch.R;

/**
 * Created by hezhang on 9/19/17.
 */

public class FilterDialogFragment extends DialogFragment
        implements View.OnClickListener  {
    public FilterDialogFragment() {
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        Button btnPickDate = (Button) view.findViewById(R.id.btnDatePicker);
        btnPickDate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Filters");
        getDialog().setTitle(title);

    }
}
