package com.bignerdranch.android.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by smaikap on 24/7/16.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        final String uuidString = this.getString(this.getColumnIndex(CrimeDBSchema.Columns.UUID));
        final String title = this.getString(this.getColumnIndex(CrimeDBSchema.Columns.TITLE));
        final long date = this.getLong(this.getColumnIndex(CrimeDBSchema.Columns.DATE));
        final int isSolved = this.getInt(this.getColumnIndex(CrimeDBSchema.Columns.SOLVED));

        final Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }


}
