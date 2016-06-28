package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by smaikap on 6/28/16.
 */
public class CrimeFragement extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;

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
        return view;
    }

}
