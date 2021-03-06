package com.example.autolayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.Random;

public class CellGround extends RelativeLayout {
	public static int BASIC_CELL_SIZE = 600;
	public int ERROR_THREDHOLD = 3;
	
	public int[] imageRes = {R.drawable.image_2, R.drawable.image_3, R.drawable.image_1};

	private PointF mUnit;
	private int mUnitTotal;
	private int mCentre;

	private Random mRandom;
	private int[] mRadius;
	private int[] mDistanceX;
	private int[] mDistanceY;
	private int[] mColor;
	private int[] mImage;
	
	private ImageCell[] mCells;
	private Bitmap[] mCellImages;

	public CellGround(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CellGround(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CellGround(Context context) {
		super(context);
		init();
	}

	private void init() {
		mRandom = new Random(System.currentTimeMillis());
		mUnit = new PointF(0f, 0f);
		mUnitTotal = 10;
		mCentre = mUnitTotal / 2;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mUnit.set((right - left) / mUnitTotal, (bottom - top) / mUnitTotal);
	}

	public void setCellCount(int count) {
		mRadius = new int[count];
		mDistanceX = new int[count];
		mDistanceY = new int[count];
		mColor = new int[count];
		
		mCells = new ImageCell[count];
		mCellImages = new Bitmap[count];
		mImage = new int[count];
		for (int i = 0; i < count; i++) {
			mCells[i] = new ImageCell(getContext());
			mCellImages[i] = BitmapFactory.decodeResource(getResources(), imageRes[i % 3]);
			
			Bitmap bitmap = Bitmap.createScaledBitmap(mCellImages[i], BASIC_CELL_SIZE, BASIC_CELL_SIZE, false);
			Mat bitmapMat;
			Utils.bitmapToMat(bitmap, bitmapMat = new Mat());
			Core.inRange(bitmapMat, new Scalar(128, 0, 0, 255), new Scalar(200, 200, 200, 255), bitmapMat);
			mImage[i] = Core.countNonZero(bitmapMat);
			
			mCells[i].setImage(bitmap);
			addView(mCells[i], BASIC_CELL_SIZE / 2, BASIC_CELL_SIZE / 2);

			// Set alpha will make canvas to shift down
			// mCells[i].setAlpha(0.8f);
		}
		
		for (int j = 0; j < count; j++) {
			Logger.d(getClass(), "None Zero: " + mImage[j]);
		}
		int[] tmp = bubSort(mImage, 3);
		for (int j = 0; j < count; j++) {
			Logger.d(getClass(), "None Zero: " + tmp[j] + ", " + mImage[j]);
		}
		for (int j = 0; j < count; j++) {
			
			for (int k = 0; k < count; k++) {
				if (tmp[j] == mImage[k]) {
					mImage[k] = j + 1;
				}
			}
		}
		
		for (int j = 0; j < count; j++) {
			Logger.d(getClass(), "final:" + tmp[j] + ", " + mImage[j]);

		}
	}
	
	public int[] bubSort(int A[], int n) // 氣泡排序法之副程式
	{
		int Temp, sp;
		int[] tmp = new int[A.length];
		
		for (int i = 0; i < A.length; i++) {
			tmp[i] = A[i];
		}
		
		for (int i = n - 1; i > 0; i--) {
			sp = 1;
			for (int j = 0; j < i; j++)
				if (tmp[j] > tmp[j + 1]) { // 兩數交換位置
					Temp = tmp[j];
					tmp[j] = tmp[j + 1];
					tmp[j + 1] = Temp;
					sp = 0;
				}
			if (sp == 1)
				break;
		}
		
		return tmp;
	}
	
	public void shuffle() {
		float leftTotal;
		float rightTotal;
		boolean dup;

		do {
			leftTotal = 0;
			rightTotal = 0;
			dup = false;

			for (int i = 0; i < mCells.length; i++) {
				// Size
				do {
					mRadius[i] = mRandom.nextInt(mUnitTotal);
					for (int j = 0; j < i; j++) {
						if (mRadius[j] == mRadius[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (mRadius[i] <= 1 || mRadius[i] > 3);

				// Distance in x axis from centroid
				do {
					mDistanceX[i] = mRandom.nextInt(mUnitTotal);
					for (int j = 0; j < i; j++) {
						if (mDistanceX[j] == mDistanceX[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (mDistanceX[i] <= 1 || mDistanceX[i] > 8 || dup);

				// Distance in y axis from centroid
				do {
					mDistanceY[i] = mRandom.nextInt(mUnitTotal);
					for (int j = 0; j < i; j++) {
						if (mDistanceY[j] == mDistanceY[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (mDistanceY[i] <= 1 || mDistanceY[i] > 8 || dup);

				int upper = Color.rgb(255, 255, 255);
				int lower = Color.rgb(100, 100, 100);
//				do {
//					mColor[i] = Color.rgb(mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
//				} while (mColor[i] <= lower || mColor[i] > upper);
//				// Color
//				float colorWeight = mColor[i] / Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
				
				// Weight of tall, separated by centroid
				float weight = mRadius[i] + mDistanceY[i] + mImage[i];
				if (mDistanceX[i] < mCentre) {
					leftTotal += weight * (mCentre - mDistanceX[i]);
				} else {
					rightTotal += weight * (mDistanceX[i] - mCentre);
				}
			}
			
			// Check for cover
			for (int i = 0; i <  mCells.length - 1; i++) {
				int newLeft = (int) (mDistanceX[i] * mUnit.x - mRadius[i] * mUnit.y);
				int newRight = (int) (mDistanceX[i] * mUnit.x + mRadius[i] * mUnit.y);
				int newTop = (int) (mDistanceY[i] * mUnit.y - mRadius[i] * mUnit.y);
				int newBottom = (int) (mDistanceY[i] * mUnit.y + mRadius[i] * mUnit.y);
				for (int j = i + 1; j < mCells.length; j++) {
					int tmpLeft = (int) (mDistanceX[j] * mUnit.x - mRadius[j] * mUnit.y);
					int tmpRight = (int) (mDistanceX[j] * mUnit.x + mRadius[j] * mUnit.y);
					int tmpTop = (int) (mDistanceY[j] * mUnit.y - mRadius[j] * mUnit.y);
					int tmpBottom = (int) (mDistanceY[j] * mUnit.y + mRadius[j] * mUnit.y);
				}
			}
		} while (Math.abs(leftTotal - rightTotal) > ERROR_THREDHOLD);

		Logger.d(getClass(), "Left: " + leftTotal + ", Right: " + rightTotal);
		applySetting();
	}

	// Use for changing the layout of cells 
	public void applySetting() {
		for (int i = 0; i < mCells.length; i++) {
			int newLeft = (int) (mDistanceX[i] * mUnit.x - mRadius[i] * mUnit.y);
			int newRight = (int) (mDistanceX[i] * mUnit.x + mRadius[i] * mUnit.y);
			int newTop = (int) (mDistanceY[i] * mUnit.y - mRadius[i] * mUnit.y);
			int newBottom = (int) (mDistanceY[i] * mUnit.y + mRadius[i] * mUnit.y);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCells[i].getLayoutParams();
			params.leftMargin = newLeft;
			params.topMargin = newTop;
			params.width = newRight - newLeft;
			params.height = newBottom - newTop;
			mCells[i].setImage(Bitmap.createScaledBitmap(mCellImages[i], params.width, params.height, false));
			mCells[i].setLayoutParams(params);
			invalidate();
		}
	}
}
