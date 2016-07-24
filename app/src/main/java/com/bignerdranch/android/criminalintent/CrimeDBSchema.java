package com.bignerdranch.android.criminalintent;

/**
 * Created by smaikap on 24/7/16.
 */
public class CrimeDBSchema {
    public static final class CrimeTable {
        public static final String NAME = "crime";
    }

    public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "DATE";
            public static final String SOLVED = "SOLVED";
    }
}
