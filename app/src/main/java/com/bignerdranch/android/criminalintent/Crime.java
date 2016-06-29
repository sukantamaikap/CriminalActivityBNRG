package com.bignerdranch.android.criminalintent;

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

    public Date getDate () {
        return mDate;
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
        this.mDate = new Date();
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
