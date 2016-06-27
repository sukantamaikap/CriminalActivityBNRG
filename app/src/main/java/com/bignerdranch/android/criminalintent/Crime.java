package com.bignerdranch.android.criminalintent;

import java.util.UUID;

/**
 * Crime model object.
 */
public class Crime {
    private UUID mId;
    private String mTitle;

    public Crime setTitle (String title) {
        this.mTitle = title;
        return this;
    }

    public Crime setId (UUID id) {
        this.mId = id;
        return this;
    }

    public String getTitle () {
        return this.mTitle;
    }

    public UUID getId () {
        return this.mId;
    }
}
