package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.UUID;

public class CriminalActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    @Override
    protected Fragment createFragment() {
        return new CrimeFragement();
    }

    public static Intent newIntent(final Context packageContex, final UUID crimeId) {
        Intent intent = new Intent(packageContex, CriminalActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
