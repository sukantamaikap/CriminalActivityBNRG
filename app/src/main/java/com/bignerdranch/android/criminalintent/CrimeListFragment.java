package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private CrimeAdapter mCrimeAdaper;
    private int mChahngedItemIndex;
    private static final int REQUEST_CRIME = 1;

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
        List<Crime> crimes = crimeLab.getmCrimes();

        if (this.mCrimeAdaper == null) {
            this.mCrimeAdaper = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(this.mCrimeAdaper);
        } else {
//            this.mCrimeAdaper.notifyDataSetChanged();
            this.mCrimeAdaper.notifyItemChanged(this.mChahngedItemIndex);
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

        this.mChahngedItemIndex = CrimeFragment.getChangedItemIndex(data);
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
            this.mDateTextView.setText(this.mCrime.getDate().toString());
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
}
