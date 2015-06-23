package com.example.autolayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import java.util.Random;

public class Playground extends RelativeLayout implements CustomView {
	private ImageView[] mCells;
	private Random mRandom;
	private Paint mPaint;

	private PointF mUnit;
	private Point mUnitTotal;
	// All count start from 0
	private int mItemMaxCount;
	private int mItemFinalCount;

	private RectF[] mRect;
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
	public void initAttr(AttributeSet attrs) {}

	@Override
	public void initStickConfigure() {}

	@Override
	public void initConfigure() {
		mRandom = new Random(System.currentTimeMillis());
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#ffcccc"));
		mPaint.setStyle(Style.FILL);

		mUnit = new PointF();
		mUnitTotal = new Point(4, 4);
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
	}

	@Override
	public void initView() {
		mCells = new ImageView[mItemMaxCount];
		for (int i = 0; i < mItemMaxCount; i++) {
			mCells[i] = new ImageView(getContext());
			mCells[i].setImageResource(R.drawable.ic_launcher);
			mCells[i].setScaleType(ScaleType.CENTER_CROP);
			addView(mCells[i], 200, 200);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mUnit.set((right - left) / mUnitTotal.x, (bottom - top) / mUnitTotal.y);
	}

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
			canvas.drawLine(mRect[i].left * mUnit.x, mRect[i].top * mUnit.y, mRect[i].right * mUnit.x, mRect[i].bottom * mUnit.y, mPaint);

			// Logger.d(getClass(), "/" + i + "/->" + mRect[i]);
		}
		super.dispatchDraw(canvas);

	}

	public void shuffleeGrid() {
		boolean dup = false;
		// Set the default count of final cells
		mItemFinalCount = mItemMaxCount;
		for (int k = 0; k < mItemMaxCount; k++) {
			do {
				int left = mRandom.nextInt(mUnitTotal.x);
				int top = mRandom.nextInt(mUnitTotal.y);
				int right = left + mRandom.nextInt(mUnitTotal.x - (int) left) + 1;
				int bottom = top + mRandom.nextInt(mUnitTotal.y - (int) top) + 1;

				// Logger.d(getClass(), "O: " + mLeftTop[k].x + "," + mLeftTop[k].y + "," + mbottomRight[k].x + "," +mbottomRight[k].y);
				mRect[k].set(left, top, right, bottom);

				for (int i = 0; i < k; i++) {
					if (dup = containIn(mRect[k], mRect[i]) || containIn(mRect[i], mRect[k])) {
						break;
					}
				}
			} while (dup);

			if (checkCoverArea(k)) {
				setViewWithShffle();
				invalidate();
				return;
			}
		}
	}
	
	public void setViewWithShffle() {
		for (int i = 0; i < mItemFinalCount; i++) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCells[i].getLayoutParams();
			params.leftMargin = (int) (mRect[i].left * mUnit.x);
			params.topMargin = (int) (mRect[i].top * mUnit.y);
			params.width = (int) (mRect[i].width() * mUnit.x);
			params.height = (int) (mRect[i].height() * mUnit.y);
			Logger.d(getClass(), "Set view [" + i + "]: " + params.width + "," + params.height);
			mCells[i].setLayoutParams(params);
			mCells[i].setVisibility(VISIBLE);
		}
		for (int i = mItemFinalCount; i < mItemMaxCount; i++) {
			mCells[i].setVisibility(GONE);
		}
	}
	
	/** 
	 * Check whether there is no room for new rect
	 */
	public boolean checkCoverArea(int k) {
		int currentCoverArea = 0;

		for (int i = 0; i <= k; i++) {
			currentCoverArea += mRect[i].width() * mRect[i].height();
			if (currentCoverArea == mItemMaxCount) {
				mItemFinalCount = k + 1;
				// Logger.d(getClass(), "Area full: " + mItemFinalCount + "," + currentCoverArea);
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  Check whether the target rect is contained in others
	 */
	public boolean containIn(RectF newRect, RectF oldRect) {
		return
		// Check the top-right point
		((newRect.right > oldRect.left) && (newRect.right <= oldRect.right)) && ((newRect.top >= oldRect.top) && (newRect.top < oldRect.bottom))
		// Check the top-left
				|| ((newRect.left >= oldRect.left) && (newRect.left < oldRect.right)) && ((newRect.top >= oldRect.top) && (newRect.top < oldRect.bottom))
				// Check the bottom-right
				|| ((newRect.right > oldRect.left) && (newRect.right <= oldRect.right)) && ((newRect.bottom > oldRect.top) && (newRect.bottom <= oldRect.bottom))
				// Check the bottom-left
				|| ((newRect.left >= oldRect.left) && (newRect.left < oldRect.right)) && ((newRect.bottom > oldRect.top) && (newRect.bottom <= oldRect.bottom))
				// Check crossing
				|| (((newRect.left < oldRect.left) && (newRect.top > oldRect.top)) && ((newRect.right > oldRect.right) && (newRect.bottom < oldRect.bottom)));
	}
}
