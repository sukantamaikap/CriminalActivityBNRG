package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by smaikap on 24/7/16.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CrimeDBSchema.CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeDBSchema.Columns.UUID + ", " +
                CrimeDBSchema.Columns.TITLE + ", " +
                CrimeDBSchema.Columns.DATE + ", " +
                CrimeDBSchema.Columns.SOLVED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
