package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by smaikap on 30/7/16.
 */
public class PictureUtils {

    public static Bitmap getScaledBitmap(final String path, final int destWidth, final int destHeigh) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final float srcWidth = options.outWidth;
        final float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeigh || srcWidth >destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeigh);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options =  new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(final String path, final Activity activity) {
        final Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return PictureUtils.getScaledBitmap(path, size.x, size.y);
    }
}
