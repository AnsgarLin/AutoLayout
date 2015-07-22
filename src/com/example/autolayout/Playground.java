package com.example.autolayout;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.example.autolayout.TouchListenerUtil.ScaledImageViewTouchListener;

public class Playground extends RelativeLayout implements CustomView {
	/**
	 * The minimum number of cells will be shown on playground after shuffle procedure
	 */
	public static final int IMAGE_SHOW_LIMIT = 5;
	private boolean CLASSIC = true;
	private boolean SCALABLE = false;

	private Random mRandom;
	private Paint mPaint;

	private PointF mUnit;
	private Point mUnitTotal;
	/**
	 * All count start from 0
	 */
	private int mItemMaxCount;
	/**
	 * The final number of cells will be shown on playground after shuffle procedure
	 */
	private int mItemFinalCount;

	/**
	 * ASUS Logo
	 */
	private Bitmap mLogo;
	/**
	 * Bitmap of each cell
	 */
	private Bitmap[] mImages;
	/**
	 * ImageView of each cell
	 */
	private ImageView[] mCells;

	/**
	 * Rect of each cell
	 */
	private RectF[] mRect;
	/**
	 * Background of each cell
	 */
	private int[] mColor;

	public Playground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initConfigure();
		initView();
	}

	public Playground(Context context, AttributeSet attrs) {
		super(context, attrs);
		initConfigure();
		initView();
	}

	public Playground(Context context) {
		super(context);
		initConfigure();
		initView();
	}

	@Override
	public void initAttr(AttributeSet attrs) {
	}

	@Override
	public void initStickConfigure() {
	}

	/**
	 * Initial all variables that might be used in whole process
	 */
	@Override
	public void initConfigure() {
		mRandom = new Random(System.currentTimeMillis());
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#ffcccc"));
		mPaint.setStyle(Style.FILL);

		mUnit = new PointF();
		mUnitTotal = new Point(3, 3);
		mItemMaxCount = mUnitTotal.x * mUnitTotal.y;
		mItemFinalCount = 0;

		mRect = new RectF[mItemMaxCount];
		for (int i = 0; i < mRect.length; i++) {
			mRect[i] = new RectF(0f, 0f, 0f, 0f);
		}
		mColor = new int[mItemMaxCount];

		for (int i = 0; i < mItemMaxCount; i++) {
			mColor[i] = Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
		}

		mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.asus);
	}

	/**
	 * Initial all ImageViews for grid cell, basic size is 200px * 200px
	 */
	@Override
	public void initView() {
		mCells = new ImageView[mItemMaxCount];
		for (int i = 0; i < mItemMaxCount; i++) {
			mCells[i] = new ImageView(getContext());
			mCells[i].setScaleType(ScaleType.CENTER_CROP);
			if (SCALABLE) {
				mCells[i].setScaleType(ScaleType.MATRIX);
				mCells[i].setOnTouchListener(new ScaledImageViewTouchListener(mCells[i]));
			}
			addView(mCells[i], 200, 200);
		}
	}

	/**
	 * Get basic unit of grid based on playground size
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mUnit.set((right - left) / mUnitTotal.x, (bottom - top) / mUnitTotal.y);
	}

	/**
	 * Rewrite dispatchDraw to draw background before onDraw()
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// for (int i = 0; i < itemCount; i++) {
		// paint.setColor(color[i]);
		// paint.setAlpha(128);
		// float left = distanceX[i] * unit.x - radius[i] * unit.y;
		// float right = distanceX[i] * unit.x + radius[i] * unit.y;
		// float top = distanceY[i] * unit.y - radius[i] * unit.y;
		// float bottom = distanceY[i] * unit.y + radius[i] * unit.y;
		// canvas.drawRect(left, top, right, bottom, paint);
		// canvas.drawCircle(distanceX[i] * unit.x, distanceY[i] * unit.y, radius[i] * unit.y, paint);
		// }
		for (int i = 0; i < mItemFinalCount; i++) {
			mPaint.setColor(mColor[i]);
			canvas.drawRect(mRect[i].left * mUnit.x, mRect[i].top * mUnit.y, mRect[i].right * mUnit.x, mRect[i].bottom * mUnit.y, mPaint);
			mPaint.setColor(Color.BLACK);
		}
		super.dispatchDraw(canvas);
	}

	/**
	 * Shuffle each cell to cover the whole playground, the
	 */
	public void shuffleeGrid() {
		if (mImages == null) {
			return;
		}

		boolean dup = false;
		// Set the default count of final cells
		mItemFinalCount = mItemMaxCount;
		// Generate cells that will not cross each other
		for (int k = 0; k < mItemMaxCount; k++) {
			do {
				float left;
				float top;
				float right;
				float bottom;
				if (CLASSIC) {
					left = mRandom.nextInt(mUnitTotal.x);
					top = mRandom.nextInt(mUnitTotal.y);
					right = left + mRandom.nextInt(mUnitTotal.x - (int) left) + 1;
					bottom = top + mRandom.nextInt(mUnitTotal.y - (int) top) + 1;
				} else {
					left = mRandom.nextFloat() * mUnitTotal.x;
					top = mRandom.nextFloat() * mUnitTotal.y;
					right = left + ((1 - mRandom.nextFloat()) * (mUnitTotal.x - left));
					bottom = top + ((1 - mRandom.nextFloat()) * (mUnitTotal.y - top));
				}
				// Logger.d(getClass(), "O: " + mLeftTop[k].x + "," + mLeftTop[k].y + "," + mbottomRight[k].x + "," +mbottomRight[k].y);
				mRect[k].set(left, top, right, bottom);

				for (int i = 0; i < k; i++) {
					if (dup = containIn(mRect[k], mRect[i]) || containIn(mRect[i], mRect[k])) {
						break;
					}
				}
			} while (dup);

			if (checkCoverArea(k)) {
				// If cells cover the whole playground, but not enough cell count, do the shuffle procedure again
				if (k < (IMAGE_SHOW_LIMIT - 1)) {
					Logger.d(getClass(), "Not enough cells");
					shuffleeGrid();
				} else {
					setViewWithShffle();
					invalidate();
				}
				return;
			}
		}

		// For float
		if (CLASSIC) {
			mItemFinalCount = mItemMaxCount;
			setViewWithShffle();
			invalidate();
		}
	}

	/**
	 * Apply shuffle result to all cell
	 */
	public void setViewWithShffle() {
		for (int i = 0; i < mItemFinalCount; i++) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCells[i].getLayoutParams();
			params.leftMargin = (int) (mRect[i].left * mUnit.x);
			params.topMargin = (int) (mRect[i].top * mUnit.y);
			params.width = (int) (mRect[i].width() * mUnit.x);
			params.height = (int) (mRect[i].height() * mUnit.y);
			Logger.d(getClass(), "Set view [" + i + "]: " + params.width + "," + params.height);
			mCells[i].setLayoutParams(params);
			if (SCALABLE) {
				if ((i >= 0) && (i < IMAGE_SHOW_LIMIT)) {
					Bitmap bitmap = ((BitmapDrawable) mCells[i].getDrawable()).getBitmap();
					mCells[i]
							.setImageBitmap(BitmapUtil.getScaleBitmap(bitmap, params.width, params.height * (params.width / bitmap.getWidth()), true));
				}
			} else {
				Bitmap bitmap;
				// if (mCells[i].getDrawable() != null) {
				// bitmap = ((BitmapDrawable) mCells[i].getDrawable()).getBitmap();
				// bitmap.recycle();
				// }

				if ((i >= 0) && (i < IMAGE_SHOW_LIMIT)) {
					bitmap = mImages[mRandom.nextInt(mImages.length)];
					mCells[i].setImageBitmap(bitmap);
					mCells[i].setColorFilter(null);
				} else {
					// bitmap = BitmapUtil.createMutableBitmap(mLogo);
					mCells[i].setImageBitmap(mLogo);
					mCells[i].setColorFilter(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)), PorterDuff.Mode.MULTIPLY);
				}
				bitmap = null;
			}
			mCells[i].setVisibility(VISIBLE);
		}
		for (int i = mItemFinalCount; i < mItemMaxCount; i++) {
			mCells[i].setVisibility(GONE);
		}
	}

	// =============================================================================
	/**
	 * Check whether there is no room for new rect
	 */
	public boolean checkCoverArea(int k) {
		float currentCoverArea = 0;

		for (int i = 0; i <= k; i++) {
			currentCoverArea += mRect[i].width() * mUnit.x * mRect[i].height() * mUnit.y;
			if (currentCoverArea == (mUnitTotal.x * mUnit.x * mUnitTotal.y * mUnit.y)) {
				mItemFinalCount = k + 1;
				Logger.d(getClass(), "Area full: " + mItemFinalCount + "," + currentCoverArea);
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the target rect is contained in others
	 */
	public boolean containIn(RectF newRect, RectF oldRect) {
		return
		// Check the top-right point
		(((newRect.right > oldRect.left) && (newRect.right <= oldRect.right)) && ((newRect.top >= oldRect.top) && (newRect.top < oldRect.bottom)))
		// Check the top-left
				|| (((newRect.left >= oldRect.left) && (newRect.left < oldRect.right)) && ((newRect.top >= oldRect.top) && (newRect.top < oldRect.bottom)))
				// Check the bottom-right
				|| (((newRect.right > oldRect.left) && (newRect.right <= oldRect.right)) && ((newRect.bottom > oldRect.top) && (newRect.bottom <= oldRect.bottom)))
				// Check the bottom-left
				|| (((newRect.left >= oldRect.left) && (newRect.left < oldRect.right)) && ((newRect.bottom > oldRect.top) && (newRect.bottom <= oldRect.bottom)))
				// Check crossing
				|| (((newRect.left < oldRect.left) && (newRect.top > oldRect.top)) && ((newRect.right > oldRect.right) && (newRect.bottom < oldRect.bottom)));
	}

	/**
	 * Get images from file paths
	 * 
	 * @param paths
	 *            File path of image
	 */
	public void loadImageWithPath(String[] paths) {
		if ((mImages != null) && (mImages.length > 0)) {
			for (Bitmap mImage : mImages) {
				if (mImage != null) {
					mImage.recycle();
				}
			}
		}

		mImages = new Bitmap[paths.length];
		for (int i = 0; i < paths.length; i++) {
			if ((mImages[i] = BitmapFactory.decodeFile(paths[i])) == null) {
				Logger.d(getClass(), "Result is null: " + paths[i]);
			}
		}
	}
}
