package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by smaikap on 17/7/16.
 */
public class TimePickerFragment extends DialogFragment {

    private TimePicker mTimePicker;

    private final static String ARG_TIME = "time";

    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Calendar calender = (Calendar) this.getArguments().getSerializable(ARG_TIME);
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);

        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        this.mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);
        this.mTimePicker.setCurrentHour(hour);
        this.mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final int hour = TimePickerFragment.this.mTimePicker.getCurrentHour();
                        final int minute = TimePickerFragment.this.mTimePicker.getCurrentMinute();
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        TimePickerFragment.this.sendResult(Activity.RESULT_OK, calendar);
                    }
                })
                .create();
    }

    public static TimePickerFragment newInstance(final Calendar calendar) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, calendar);

        final TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    private void sendResult(final int resultCode, final Calendar time) {
        if (getTargetFragment() == null) {
            return;
        }

        final Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
