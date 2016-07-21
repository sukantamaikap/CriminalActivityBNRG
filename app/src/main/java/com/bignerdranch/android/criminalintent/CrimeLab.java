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
    }

    public static CrimeLab getInstance(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
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

    public void addCrime(final Crime crime) {
        this.mCrimes.add(crime);
    }
}
