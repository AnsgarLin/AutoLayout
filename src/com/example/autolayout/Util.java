package com.example.autolayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class Util {
	public final static boolean LOG = true;

	public static List<Integer> getFactors(int val) {

		List<Integer> factors = new ArrayList<Integer>();
		for (int i = 1; i <= (val / 2); i++) {
			if ((val % i) == 0) {
				factors.add(i);
			}
		}
		factors.add(val);
		return factors;
	}

	/**
	 * Convert content uri(file://) to file uri(content://)
	 */
	public static String toFileUri(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
