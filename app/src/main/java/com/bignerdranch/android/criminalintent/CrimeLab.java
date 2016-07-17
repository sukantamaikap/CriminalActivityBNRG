package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    private CrimeLab() {
        this.mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime no #" + (i+1));
            crime.setSolved(i%2 == 0);
            crime.setIndex(i);
            this.mCrimes.add(crime);
        }
    }

    public static CrimeLab getInstance(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }
        return sCrimeLab;
    }

    public List<Crime> getmCrimes() {
        return this.mCrimes;
    }

    public Crime getCrime(final UUID id){
        for (final Crime crime : this.mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
