package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by smaikap on 30/7/16.
 */
public class ImageViewerFragment extends DialogFragment {

    private static File crimeImageFle;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_crime_image, null);

        final ImageView crimeImageView = (ImageView) view.findViewById(R.id.crime_image_view);
        if (crimeImageFle.exists()) {
            crimeImageView.setImageBitmap(BitmapFactory.decodeFile(ImageViewerFragment.crimeImageFle.getAbsolutePath()));
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.crime_image_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public static ImageViewerFragment newInstance(final File imageFilePath) {
        crimeImageFle = imageFilePath;
        final ImageViewerFragment datePickerFragment = new ImageViewerFragment();
        return datePickerFragment;
    }
}
