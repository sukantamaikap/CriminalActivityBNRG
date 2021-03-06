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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sm on 6/7/16.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mCrimeAdapter;
    private TextView mNoCrimeTextView;
    private ImageButton mAddImageButton;
    private int mChangedItemIndex;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    private static final int REQUEST_CRIME = 1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private boolean mItemDeleted;

    public interface Callbacks {
        void onCrimeSelected(final Crime crime);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mCallbacks = (Callbacks) activity;

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        this.mCrimeRecycleView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        this.mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.mNoCrimeTextView = (TextView) view.findViewById(R.id.no_item_message);

        this.mAddImageButton = (ImageButton) view.findViewById(R.id.add_crime);
        this.mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimeListFragment.this.createNewCrime();
            }
        });

        if (savedInstanceState != null) {
            this.mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    /**
     * call this to to update crime list fragment ui
     */
    public void updateUI() {
        final CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (this.mCrimeAdapter == null) {
            this.mCrimeAdapter = new CrimeAdapter(crimes);
            this.mCrimeRecycleView.setAdapter(this.mCrimeAdapter);
        } else {
            this.mCrimeAdapter.setCrimes(crimes);
            this.mCrimeAdapter.notifyDataSetChanged();
        }

        if (crimes.size() == 0) {
            this.mCrimeRecycleView.setVisibility(View.GONE);
            this.mNoCrimeTextView.setVisibility(View.VISIBLE);
            this.mAddImageButton.setVisibility(View.VISIBLE);
        }

        updateSubtitle();
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
        this.mItemDeleted = CrimeFragment.getIsItemDeleted(data);
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
            CrimeListFragment.this.mCallbacks.onCrimeSelected(mCrime);
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

        public void setCrimes(final List<Crime> crimes) {
            this.mCrimes = crimes;
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

        final MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (this.mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateSubtitle() {
        final CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        final int crimeCount = crimeLab.getCrimes().size();
//        String subTitle = getString(R.string.subtitle_format, crimeCount);
        String subTitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!this.mSubtitleVisible) {
            subTitle = null;
        }

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime :
                this.createNewCrime();
                return true;

            case R.id.menu_item_show_subtitle :
                this.mSubtitleVisible = !this.mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewCrime() {
        final Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        this.updateUI();
        this.mCallbacks.onCrimeSelected(crime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }
}
