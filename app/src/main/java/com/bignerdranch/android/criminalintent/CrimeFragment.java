package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by smaikap on 6/28/16.
 */
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private CheckBox mSolvedCheckBox;
    private Button mCallSuspect;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private boolean mIsItemBeingDeleted = false;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String EXTRA_CHANGED_INDEX= "com.bignerdranch.android.criminalintent.item_index";
    private static final String EXTRA_IS_ITEM_DELETED = "com.bignerdranch.android.criminalintent.is_item_deleted";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    /**
     * Call this method to initialize {@link CrimeFragment}
     *
     * @param crimeId
     * @return
     */
    public static CrimeFragment newInstance(final UUID crimeId) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        final CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        this.mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        this.mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(this.mCrime);
        if (this.mCrime.getTitle() != null) {
            this.setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
        setupTitleField(view);
        setupDateButton(view);
        setupTimeButton(view);
        setupSolvedCheckBox(view);
        setupReportButton(view);
        setupSuspectButton(view);
        setupCallSuspect(view);
        setupClickPhotoButton(view);
        setupCrimePhotoView(view);
        return view;
    }

    private void setupCrimePhotoView(View view) {
        this.mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);
        this.updatePhotoView();
        this.mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = getFragmentManager();
                final ImageViewerFragment dialog = ImageViewerFragment.newInstance(CrimeFragment.this.mPhotoFile);
                dialog.show(fragmentManager, "");

            }
        });
    }

    private void setupClickPhotoButton(View view) {
        this.mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        final PackageManager packageManager = this.getActivity().getPackageManager();
        if (this.mPhotoFile != null && captureImage.resolveActivity(packageManager) != null) {
            final Uri uri = Uri.fromFile(this.mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        this.mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
    }

    /**
     * Setup call suspect button. This button should be disabled till a suspect is chosen
     * and the suspect has a valid phone number.
     * @param view
     */
    private void setupCallSuspect(View view) {
        this.mCallSuspect = (Button) view.findViewById(R.id.call_suspect);
        if (this.mCrime.getPhoneNumber() != null) {
            this.mCallSuspect.setEnabled(true);
        }
        this.mCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent callContact = new Intent(Intent.ACTION_DIAL);
                callContact.setData(Uri.parse("tel:" + CrimeFragment.this.mCrime.getPhoneNumber()));
                startActivity(callContact);
            }
        });
    }

    /**
     * Suspect displays the contact dispaly name if any contact on the phone book.
     * @param view
     */
    private void setupSuspectButton(View view) {
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        this.mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (this.mCrime.getSuspect() != null) {
            this.mSuspectButton.setText(this.mCrime.getSuspect());
        }

        final PackageManager packageManager = this.getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            this.mSuspectButton.setEnabled(false);
        }
    }

    /**
     * Sends message of the crime committed.
     * @param view
     */
    private void setupReportButton(View view) {
        this.mReportButton = (Button) view.findViewById(R.id.crime_report);
        this.mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = ShareCompat.IntentBuilder.from(CrimeFragment.this.getActivity())
                        .setType("text/plain")
                        .setSubject(CrimeFragment.this.getString(R.string.crime_report_subject))
                        .setText(CrimeFragment.this.getCrimeReport())
                        .getIntent();
                CrimeFragment.this.startActivity(intent);
            }
        });
    }

    /**
     * Checked when the crime is solved.
     * @param view
     */
    private void setupSolvedCheckBox(View view) {
        this.mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        this.mSolvedCheckBox.setChecked(this.mCrime.isSolved());
        this.mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (final CompoundButton buttonView, final boolean isChecked) {
                CrimeFragment.this.mCrime.setSolved(isChecked);
                informCaller();
            }
        });
    }

    /**
     * User input for time of crime
     * @param view
     */
    private void setupTimeButton(View view) {
        this.mTimeButton = (Button) view.findViewById(R.id.crime_time);
        updateTimeField();
        this.mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = CrimeFragment.this.getFragmentManager();
                final TimePickerFragment dialog = TimePickerFragment.newInstance(CrimeFragment.this.mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });
    }

    /**
     * User input for date of crime
     * @param view
     */
    private void setupDateButton(View view) {
        this.mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDateField();
        this.mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = getFragmentManager();
                final DatePickerFragment dialog = DatePickerFragment.newInstance(CrimeFragment.this.mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });
    }

    /**
     * User input for crime title.
     * @param view
     */
    private void setupTitleField(View view) {
        this.mTitleField = (EditText) view.findViewById(R.id.crime_title);
        this.mTitleField.setText(this.mCrime.getTitle());
        this.mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,
                                           int start,
                                           int count,
                                           int after) {

            }

            @Override
            public void onTextChanged (CharSequence s,
                                       int start,
                                       int before,
                                       int count) {
                CrimeFragment.this.mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged (Editable s) {
                informCaller();
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode,
                                 final int resultCode,
                                 final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            final Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            this.mCrime.setDate(date);
            updateDateField();
        }

        if (requestCode == REQUEST_TIME) {
            final Calendar calendar = (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            this.mCrime.setTime(calendar);
            updateTimeField();
        }

        if (requestCode == REQUEST_CONTACT && data != null) {
            final Uri contactUri = data.getData();
            populateContactDisplayName(contactUri);
            populateContactNumber(contactUri);
        }

        if (requestCode == REQUEST_PHOTO) {
            this.updatePhotoView();
        }
    }

    private void populateContactNumber(final Uri contactUri) {
        final Cursor cursorID = this.getActivity().getContentResolver().query(contactUri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        String contactID = null;

        try {
            if (cursorID.getCount() == 0) {
                return;
            }

            cursorID.moveToFirst();
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        } finally {
            cursorID.close();
        }

        final Cursor cursorPhone = this.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);

        try {
            if (cursorPhone.getCount() == 0) {
                return;
            }

            cursorPhone.moveToFirst();
            final String contactNo = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (contactNo != null) {
                this.mCrime.setPhoneNumber(contactNo);
                this.mCallSuspect.setEnabled(true);
            }
        } finally {
            cursorPhone.close();
        }
    }

    private void populateContactDisplayName(final Uri contactUri) {
        final String[] queryFieldContactName = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME
        };

        final Cursor cursor = this.getActivity().getContentResolver().query(contactUri, queryFieldContactName, null, null, null);

        try {
            if (cursor.getCount() == 0) {
                return;
            }

            cursor.moveToFirst();
            final String suspect = cursor.getString(0);
            this.mCrime.setSuspect(suspect);
            this.mSuspectButton.setText(suspect);
        } finally {
            cursor.close();
        }
    }

    private void updateDateField() {
        this.mDateButton.setText(DateFormat.format("EEE, dd, MMM yyyy", this.mCrime.getDate()).toString());
    }

    private void updateTimeField() {
        final String timeString = "Hour " + this.mCrime.getTime().get(Calendar.HOUR_OF_DAY) + ":" + " Min " + this.mCrime.getTime().get(Calendar.MINUTE);
        this.mTimeButton.setText(timeString);
    }

    private void informCaller() {
        Intent data = new Intent();
        data.putExtra(EXTRA_CHANGED_INDEX, this.mCrime.getIndex());
        data.putExtra(EXTRA_IS_ITEM_DELETED, this.mIsItemBeingDeleted);
        this.getActivity().setResult(Activity.RESULT_OK, data);
    }

    public static int getChangedItemIndex(final Intent data) {
        return data.getIntExtra(EXTRA_CHANGED_INDEX, 0);
    }

    public static boolean getIsItemDeleted(final Intent data) {
        return data.getBooleanExtra(EXTRA_IS_ITEM_DELETED, false);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(this.mCrime);
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime :
                if (this.mCrime.getId() != null) {
                    CrimeLab.getInstance(getActivity()).deleteCrime(this.mCrime);
                    this.mIsItemBeingDeleted =true;
                }
                getActivity().finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public String getCrimeReport() {
        String solvedString = null;
        if (this.mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        final String dateFormat = "EEE, MMM, dd";
        final String dateString = DateFormat.format(dateFormat, this.mCrime.getDate()).toString();

        String suspect = this.mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, this.mCrime.getTitle(), dateString, solvedString, suspect);
    }

    private void updatePhotoView() {
        if (this.mPhotoFile == null || !this.mPhotoFile.exists()) {
            this.mPhotoView.setImageDrawable(null);
        } else {
            final Bitmap bitmap = PictureUtils.getScaledBitmap(this.mPhotoFile.getPath(), this.getActivity());
            this.mPhotoView.setImageBitmap(bitmap);
        }
    }
}
