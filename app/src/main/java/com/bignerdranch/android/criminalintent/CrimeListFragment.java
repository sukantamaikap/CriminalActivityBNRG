package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mCrimeAdapter;
    private int mChangedItemIndex;
    private static final int REQUEST_CRIME = 1;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        this.mCrimeRecycleView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        this.mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        final CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (this.mCrimeAdapter == null) {
            this.mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(this.mCrimeAdapter);
        } else {
//            this.mCrimeAdapter.notifyDataSetChanged();
            this.mCrimeAdapter.notifyItemChanged(this.mChangedItemIndex);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (data == null) {
            return;
        }

        this.mChangedItemIndex = CrimeFragment.getChangedItemIndex(data);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            this.mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            this.mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(final Crime crime) {
            this.mCrime = crime;
            this.mTitleTextView.setText(this.mCrime.getTitle());
            this.mDateTextView.setText(DateFormat.format("EEE, dd, MMM yyyy", this.mCrime.getDate()).toString());
            this.mSolvedCheckBox.setChecked(this.mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            final Intent intent = CrimePagerActivity.newIntent(getActivity(), this.mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(final List<Crime> crimes) {
            this.mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            final View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(final CrimeHolder holder, final int position) {
            final Crime crime = this.mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return this.mCrimes.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    private void updateSubtitle() {
        final CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        final int crimeCount = crimeLab.getCrimes().size();
        final String subTitle = getString(R.string.subtitle_format, crimeCount);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime :
                final Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                final Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            case R.id.menu_item_show_subtitle :
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
