package com.bignerdranch.android.criminalintent;

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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeListFragement extends Fragment {
    private RecyclerView mCrimeRecycleView;
    private CrimeAdaper mCrimeAdaper;

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

        this.mCrimeAdaper = new CrimeAdaper(crimes);
        mCrimeRecycleView.setAdapter(mCrimeAdaper);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedChecBox;

        public CrimeHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            this.mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            this.mSolvedChecBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(final Crime crime) {
            this.mCrime = crime;
            this.mTitleTextView.setText(this.mCrime.getTitle());
            this.mDateTextView.setText(this.mCrime.getDate());
            this.mSolvedChecBox.setChecked(this.mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), this.mCrime.getTitle() + " clicked !!", Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdaper extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdaper(final List<Crime> crimes) {
            this.mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // something is wrong with the list
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
}
