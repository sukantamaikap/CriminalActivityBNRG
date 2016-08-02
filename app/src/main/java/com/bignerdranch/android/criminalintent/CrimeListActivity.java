package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.ativity_masterdetail;
    }

    @Override
    public void onCrimeSelected(final Crime crime) {
        if (this.findViewById(R.id.details_fragment_container) == null) {
            final Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            this.startActivity(intent);
        } else {
            final Fragment newDetails = CrimeFragment.newInstance(crime.getId());
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.details_fragment_container, newDetails)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        final CrimeListFragment listFragment = (CrimeListFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
