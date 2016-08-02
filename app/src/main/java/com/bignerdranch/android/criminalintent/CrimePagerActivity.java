package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by smaikap on 13/7/16.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_crime_pager);

        this.mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        this.mCrimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = CrimePagerActivity.this.mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return CrimePagerActivity.this.mCrimes.size();
            }
        });

        final UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for (int i = 0; i < this.mCrimes.size(); i++) {
            if (this.mCrimes.get(i).getId().equals(crimeId)) {
                this.mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(final Context packageContext, final UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
    }
}
