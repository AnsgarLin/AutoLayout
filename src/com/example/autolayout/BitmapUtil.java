package com.example.autolayout;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class BitmapUtil {
	/**
	 * Recycle a Bitmap resource, but need to set null outside
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		bitmap.recycle();
		System.gc();
	}

	/**
	 * Get a bitmap with target size
	 * 
	 * @param bitmap
	 *            The original bitmap
	 * @param dstW
	 *            The target width
	 * @param dstH
	 *            The target height
	 * @param recycle
	 *            true if recycle the origin, false otherwise
	 * @return The small bitmap with target size
	 */
	public static Bitmap getScaleBitmap(Bitmap bitmap, float dstW, float dstH, boolean recycle) {
		Bitmap cloneBitmap = createMutableBitmap(bitmap);

		android.graphics.Point smallSize = measureSmallSize(dstW, dstH, cloneBitmap.getWidth(), cloneBitmap.getHeight());
		Bitmap smallBitmap = Bitmap.createScaledBitmap(cloneBitmap, smallSize.x, smallSize.y, false);
		if (recycle && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		cloneBitmap.recycle();

		return smallBitmap;
	}

	public static android.graphics.Point measureSmallSize(float dstW, float dstH, float srcW, float srcH) {
		float scaleHeight = srcH / dstH;
		float scaleWidth = srcW / dstW;
		float finalW = srcW;
		float finalH = srcH;

		// To get a smaller size, either scaleHeight or scaleWidth need to be greater than 0 as int
		if (((srcW / scaleHeight) > 0f) && (scaleHeight > 0f) && (scaleHeight > scaleWidth)) {
			Log.d("Ansgar", "Scale by height");
			finalW = srcW / scaleHeight;
			finalH = srcH / scaleHeight;
		} else if (((srcH / scaleWidth) > 0f) && (scaleWidth > 0f) && (scaleWidth > scaleHeight)) {
			Log.d("Ansgar", "Scale by width");
			finalW = srcW / scaleWidth;
			finalH = srcH / scaleWidth;
		}

		// Too make the small size is absolutely smaller than the target size, resize again if necessary.
		if (dstW < finalW) {
			float scale = finalW / dstW;
			finalW = dstW;
			finalH = finalH / scale;
		} else if (dstH < finalH) {
			float scale = finalH / dstH;
			finalH = dstH;
			finalW = finalW / scale;
		}
		return new android.graphics.Point((int) finalW, (int) finalH);
	}

	public static Bitmap createMutableBitmap(Bitmap bitmap) {
		Bitmap mutableBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(mutableBitmap);
		canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG));

		return mutableBitmap;
	}

}
