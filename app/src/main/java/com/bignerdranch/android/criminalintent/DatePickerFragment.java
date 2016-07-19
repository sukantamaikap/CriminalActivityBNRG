package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by smaikap on 16/7/16.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;
    private static final String ARG_DATE = "date";

    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_date, container, false);

        final Date date = (Date) this.getArguments().getSerializable(ARG_DATE);
        final Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        final DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.dialog_date_date_picker);
        return rootView;
    }

//    @Override
//    public Dialog onCreateDialog(final Bundle savedInstanceState) {
//        final Date date = (Date) this.getArguments().getSerializable(ARG_DATE);
//        final Calendar calender = Calendar.getInstance();
//        calender.setTime(date);
//        final int year = calender.get(Calendar.YEAR);
//        final int month = calender.get(Calendar.MONTH);
//        final int day = calender.get(Calendar.DAY_OF_MONTH);
//
//        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
//
//        this.mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
//        this.mDatePicker.init(year, month, day, null);
//
//        return new AlertDialog.Builder(getActivity())
//                .setView(view)
//                .setTitle(R.string.date_picker_title)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(final DialogInterface dialogInterface, final int i) {
//                        final int year = DatePickerFragment.this.mDatePicker.getYear();
//                        final int month = DatePickerFragment.this.mDatePicker.getMonth();
//                        final int day = DatePickerFragment.this.mDatePicker.getDayOfMonth();
//                        final Date date = new GregorianCalendar(year, month, day).getTime();
//                        DatePickerFragment.this.sendResult(Activity.RESULT_OK, date);
//                    }
//                })
//                .create();
//    }

    public static DatePickerFragment newInstance(final Date date) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        final DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    private void sendResult(final int resultCode, final Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        final Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
