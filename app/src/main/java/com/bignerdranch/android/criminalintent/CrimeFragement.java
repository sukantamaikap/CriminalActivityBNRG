package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by smaikap on 6/28/16.
 */
public class CrimeFragement extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCrime = new Crime();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstancestate) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
        this.mTitleField = (EditText) view.findViewById(R.id.crime_title);
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
                CrimeFragement.this.mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });

        this.mDateButton = (Button) container.findViewById(R.id.crime_date);
        this.mDateButton.setText(mCrime.getDate().toString());
        this.mDateButton.setEnabled(false);

        this.mSolvedCheckBox = (CheckBox) container.findViewById(R.id.crime_solved);
        this.mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                CrimeFragement.this.mCrime.setSolved(isChecked);
            }
        });

        return view;
    }

}
