package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(final Context context) {
        this.mContext = context.getApplicationContext();
        this.mDatabase = new CrimeBaseHelper(this.mContext).getWritableDatabase();
    }

    public static CrimeLab getInstance(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        final List<Crime> crimes = new ArrayList<Crime>();
        final CrimeCursorWrapper cursor = this.queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(final UUID id) {
        final CrimeCursorWrapper cursor = this.queryCrimes(CrimeDBSchema.Columns.UUID + " = ?", new String[] {id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void addCrime(final Crime crime) {
        final ContentValues contentValues = getContentValues(crime);
        this.mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, contentValues);
    }

    public void deleteCrime(final Crime crime) {
        final String uuIdString = crime.getId().toString();
        final ContentValues values = getContentValues(crime);

        this.mDatabase.delete(CrimeDBSchema.CrimeTable.NAME,
                CrimeDBSchema.Columns.UUID + " = ?",
                new String[] {uuIdString});
    }

    public void updateCrime(final Crime crime) {
        final String uuIdString = crime.getId().toString();
        final ContentValues values = getContentValues(crime);

        this.mDatabase.update(CrimeDBSchema.CrimeTable.NAME,
                values,
                CrimeDBSchema.Columns.UUID + " = ?",
                new String[] {uuIdString});
    }

    private static ContentValues getContentValues(final Crime crime) {
        final ContentValues values = new ContentValues();
        values.put(CrimeDBSchema.Columns.UUID, crime.getId().toString());
        values.put(CrimeDBSchema.Columns.TITLE, crime.getTitle());
        values.put(CrimeDBSchema.Columns.DATE, crime.getDate().toString());
        values.put(CrimeDBSchema.Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDBSchema.Columns.SUSPECT, crime.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(final String whereClause, final String[] whereArgs) {
        final Cursor cursor = this.mDatabase.query(CrimeDBSchema.CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
}
