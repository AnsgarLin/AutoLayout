package com.example.autolayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import java.util.Random;

public class Playground extends RelativeLayout {
	private ImageView[] cells;
	
	private int itemCountX;
	private int itemCountY;
	private int mFinalCount;
	
	private int[] direction;
	private int[] radius;
	private int[] distanceX;
	private int[] distanceY;
	private int[] color;

	private Random random;

	private int unitTotalX;
	private int unitTotalY;

	private int centre;
	private PointF unit;
	private Paint paint;
	private float strokeWidth;

	private float[] startX;
	private float[] startY;
	private float[] endX;
	private float[] endY;
	private float[] width;
	private float[] height;
	
	public Playground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public Playground(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Playground(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		itemCountX = 16;
		itemCountY = itemCountX;
		mFinalCount = 0;
		direction = new int[itemCountX];
		radius = new int[itemCountX];
		distanceX = new int[itemCountX];
		distanceY = new int[itemCountX];
		color = new int[itemCountX];
		random = new Random(System.currentTimeMillis());	
		for (int i = 0; i < itemCountX; i++) {
			color[i] = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
		}
		
		startX = new float[itemCountX];
		startY = new float[itemCountX];
		endX = new float[itemCountX];
		endY = new float[itemCountX];
		width = new float[itemCountX];
		height = new float[itemCountX];
	
		unit = new PointF();
		unitTotalX = 4;
		unitTotalY = 4;
		centre = unitTotalX / 2;

		paint = new Paint();
		paint.setColor(Color.parseColor("#ffcccc"));
		paint.setStyle(Style.FILL_AND_STROKE);
//		paint.setStrokeWidth(strokeWidth = 20f);
		
		Logger.d(getClass(), "Start");
		
		cells = new ImageView[itemCountX];
		for (int i = 0; i < itemCountX; i++) {
			cells[i] = new ImageView(context);
//			cells[i].setImageResource(R.drawable.ic_launcher);
			cells[i].setScaleType(ScaleType.CENTER_CROP);
			addView(cells[i], 200, 200);
		}
	}

	public void shuffleeGrid() {
		boolean dup = false;
		// Set the default count of final cells
		mFinalCount = itemCountX - 1;
		for (int k = 0; k < itemCountX; k++) {
			do {
				startX[k] = random.nextInt(unitTotalX);
				startY[k] = random.nextInt(unitTotalY);
				width[k] = random.nextInt(unitTotalX - (int) startX[k]) + 1;
				height[k] = random.nextInt(unitTotalY - (int) startY[k]) + 1;
				endX[k] = startX[k] + width[k];
				endY[k] = startY[k] + height[k];

				for (int i = 0; i < k; i++) {
					if (dup = containIn(startX[k], startY[k], endX[k], endY[k], startX[i], startY[i], endX[i], endY[i]) 
							|| containIn(startX[i], startY[i], endX[i], endY[i], startX[k], startY[k], endX[k], endY[k])
							) {
						break;
					}
				}
				
//				for (int i = 0; i < k; i++) {
//					Logger.d(getClass(), "[" + i +"] " + startX[i] + "," + startY[i] + "," +  endX[i] + "," + endY[i]);
//				}
//				Logger.d(getClass(), "/" + k +"/ " + startX[k] + "," + startY[k] + "," +  endX[k] + "," + endY[k]);
			} while (dup);

			int area = 0;
			for (int i = 0; i <= k; i++) {
				area += (width[i] * height[i]);
				if (area == unitTotalX * unitTotalY) {
					mFinalCount = k;
//					printAll(k);
					k = itemCountX;
					Logger.d(getClass(), "Area full: " + mFinalCount + 1 + "," + area);

					break;
				}
			}
		}
		invalidate();
	}
	
	public boolean containIn(float startX2, float startY2, float endX2, float endY2, float startX1, float startY1, float endX1, float endY1) {
		return containInX(startX2, startY2, startX1, startY1, endX1, endY1) ||
				containInXY(startX2, endY2, startX1, startY1, endX1, endY1) ||
				containInYX(endX2, startY2, startX1, startY1, endX1, endY1) ||
				containInY(endX2, endY2, startX1, startY1, endX1, endY1) ||
				(((startX2 < startX1) && (startY2 > startY1)) && ((endX2 > endX1) && (endY2 < endY1)));
	}
	
	public boolean containInX(float startX2, float startY2, float startX1, float startY1, float endX1, float endY1) {
		return((startX2 >= startX1) && (startX2 < endX1)) && 
				((startY2 >= startY1) && (startY2 < endY1));
	}
	
	public boolean containInY(float startX2, float startY2, float startX1, float startY1, float endX1, float endY1) {
		return((startX2 > startX1) && (startX2 <= endX1)) && 
				((startY2 > startY1) && (startY2 <= endY1));
	}
	
	public boolean containInXY(float startX2, float startY2, float startX1, float startY1, float endX1, float endY1) {
		return((startX2 >= startX1) && (startX2 < endX1)) && 
				((startY2 > startY1) && (startY2 <= endY1));
	}
	
	public boolean containInYX(float startX2, float startY2, float startX1, float startY1, float endX1, float endY1) {
		return((startX2 > startX1) && (startX2 <= endX1)) && 
				((startY2 >= startY1) && (startY2 < endY1));
	}
	
	public void printAll(int n) {
		for (int i = 0; i < n; i++) {
			Logger.d(getClass(), "Area full [" + i +"] " + startX[i] + "," + startY[i] + "," +  endX[i] + "," + endY[i]);
		}
	}
	
	public void shuffle() {
		int left;
		int right;
		float leftTotal;
		float rightTotal;
		boolean dup;

		do {
			left = 0;
			right = 0;
			leftTotal = 0;
			rightTotal = 0;
			dup = false;

			for (int i = 0; i < itemCountX; i++) {
				do {
					radius[i] = random.nextInt(unitTotalX);
					for (int j = 0; j < i; j++) {
						if (radius[j] == radius[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (radius[i] <= 1 || radius[i] > 3);

				do {
					distanceX[i] = random.nextInt(unitTotalX);
					for (int j = 0; j < i; j++) {
						if (distanceX[j] == distanceX[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (distanceX[i] <= 1 || distanceX[i] > 8 || dup);

				do {
					distanceY[i] = random.nextInt(unitTotalY);
				} while (distanceY[i] <= 1 || distanceY[i] > 8);

				int upper = Color.rgb(255, 255, 255);
				int lower = Color.rgb(100, 100, 100);
				do {
					color[i] = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
				} while (color[i] <= lower || color[i] > upper);
				
				float colorWeight = color[i] / Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
				Logger.d(getClass(), "Color: " + colorWeight);
				
				float weight = radius[i] + distanceY[i] + colorWeight;
				if (distanceX[i] < centre) {
					left++;
					leftTotal += weight * (centre - distanceX[i]);
				} else {
					right++;
					rightTotal += weight * (distanceX[i] - centre);
				}
			}
		} while (Math.abs(leftTotal - rightTotal) > 3);
		
		Logger.d(getClass(), "Left: " + leftTotal + ", Right: " + rightTotal);

		for (int i = 0; i < itemCountX; i++) {
			int newLeft = (int) (distanceX[i] * unit.x - radius[i] * unit.y);
			int newRight = (int) (distanceX[i] * unit.x + radius[i] * unit.y);
			int newTop = (int) (distanceY[i] * unit.y - radius[i] * unit.y);
			int newBottom = (int) (distanceY[i] * unit.y + radius[i] * unit.y);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cells[i].getLayoutParams();
			params.leftMargin = newLeft;
			params.topMargin = newTop;
			params.width = newRight - newLeft;
			params.height = newBottom - newTop;
			cells[i].setLayoutParams(params);
		}
//		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		unit.set((right - left) / unitTotalX, (bottom - top) / unitTotalY);
		Logger.d(getClass(), "Unit: " + unit);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
//		for (int i = 0; i < itemCount; i++) {
//			paint.setColor(color[i]);
//			paint.setAlpha(128);
//			float left = distanceX[i] * unit.x - radius[i] * unit.y;
//			float right = distanceX[i] * unit.x + radius[i] * unit.y;
//			float top = distanceY[i] * unit.y - radius[i] * unit.y;
//			float bottom = distanceY[i] * unit.y + radius[i] * unit.y;
//			canvas.drawRect(left, top, right, bottom, paint);
//			canvas.drawCircle(distanceX[i] * unit.x, distanceY[i] * unit.y, radius[i] * unit.y, paint);
//		}
		for (int i = 0; i <= mFinalCount; i++) {
			paint.setColor(color[i]);
			canvas.drawRect(startX[i] * unit.x, startY[i] * unit.y, endX[i] * unit.x, endY[i] * unit.y, paint);
			paint.setColor(Color.BLACK);
			canvas.drawLine(startX[i] * unit.x, startY[i] * unit.y, endX[i] * unit.x, endY[i] * unit.y, paint);
			Logger.d(getClass(), "/" + i + "/[" + paint.getColor() + "]-" + (int) startX[i] + "," + (int) startY[i] + "," + (int) endX[i]+ "," + (int) endY[i]);
		}
		super.dispatchDraw(canvas);
		
	}
}
