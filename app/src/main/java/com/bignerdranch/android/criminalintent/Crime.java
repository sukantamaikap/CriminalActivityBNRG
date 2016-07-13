package com.bignerdranch.android.criminalintent;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Crime model object.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private int mIndex;

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public Crime() {
        this.mId = UUID.randomUUID();
        this.mDate = new Date();
    }

    public String getDate () {
        return DateFormat.format("EEE, dd, MMM yyyy", this.mDate).toString();
    }

    public void setDate (Date date) {
        mDate = date;
    }

    public boolean isSolved () {
        return mSolved;
    }

    public void setSolved (boolean solved) {
        mSolved = solved;
    }

    public void setTitle (String title) {
        this.mTitle = title;
    }

    public void setId (UUID id) {
        this.mId = id;
    }

    public String getTitle () {
        return this.mTitle;
    }

    public UUID getId () {
        return this.mId;
    }
}
