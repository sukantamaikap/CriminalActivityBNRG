package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by smaikap on 6/28/16.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private boolean mIsItemBeingDeleted = false;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String EXTRA_CHANGED_INDEX= "com.bignerdranch.android.criminalintent.item_index";
    private static final String EXTRA_IS_ITEM_DELETED = "com.bignerdranch.android.criminalintent.is_item_deleted";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    /**
     * Call this method to initialize {@link CrimeFragment}
     *
     * @param crimeId
     * @return
     */
    public static CrimeFragment newInstance(final UUID crimeId) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        final CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        this.mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        if (this.mCrime.getTitle() != null) {
            this.setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
        this.mTitleField = (EditText) view.findViewById(R.id.crime_title);
        this.mTitleField.setText(this.mCrime.getTitle());
        this.mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,
                                           int start,
                                           int count,
                                           int after) {

            }

            @Override
            public void onTextChanged (CharSequence s,
                                       int start,
                                       int before,
                                       int count) {
                CrimeFragment.this.mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged (Editable s) {
                informCaller();
            }
        });

        this.mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDateField();
        this.mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = getFragmentManager();
                final DatePickerFragment dialog = DatePickerFragment.newInstance(CrimeFragment.this.mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        this.mTimeButton = (Button) view.findViewById(R.id.crime_time);
        updateTimeField();
        this.mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = CrimeFragment.this.getFragmentManager();
                final TimePickerFragment dialog = TimePickerFragment.newInstance(CrimeFragment.this.mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        this.mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        this.mSolvedCheckBox.setChecked(this.mCrime.isSolved());
        this.mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (final CompoundButton buttonView, final boolean isChecked) {
                CrimeFragment.this.mCrime.setSolved(isChecked);
                informCaller();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode,
                                 final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            final Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            this.mCrime.setDate(date);
            updateDateField();
        }

        if (requestCode == REQUEST_TIME) {
            final Calendar calendar = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            this.mCrime.setTime(calendar);
            updateTimeField();
        }
    }

    private void updateDateField() {
        this.mDateButton.setText(DateFormat.format("EEE, dd, MMM yyyy", this.mCrime.getDate()).toString());
    }

    private void updateTimeField() {
        final String timeString = "Hour " + this.mCrime.getTime().get(Calendar.HOUR_OF_DAY) + ":" + " Min " + this.mCrime.getTime().get(Calendar.MINUTE);
        this.mTimeButton.setText(timeString);
    }

    private void informCaller() {
        Intent data = new Intent();
        data.putExtra(EXTRA_CHANGED_INDEX, this.mCrime.getIndex());
        data.putExtra(EXTRA_IS_ITEM_DELETED, this.mIsItemBeingDeleted);
        this.getActivity().setResult(Activity.RESULT_OK, data);
    }

    public static int getChangedItemIndex(final Intent data) {
        return data.getIntExtra(EXTRA_CHANGED_INDEX, 0);
    }

    public static boolean getIsItemDeleted(final Intent data) {
        return data.getBooleanExtra(EXTRA_IS_ITEM_DELETED, false);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime :
                if (this.mCrime.getId() != null) {
                    CrimeLab.getInstance(getActivity()).deleteCrime(this.mCrime);
                    this.mIsItemBeingDeleted =true;
                }
                getActivity().finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
