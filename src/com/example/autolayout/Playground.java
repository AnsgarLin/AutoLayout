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
	
	private int itemCount;
	private int[] direction;
	private int[] radius;
	private int[] distanceX;
	private int[] distanceY;
	private int[] color;

	private Random random;

	private int unitTotal;
	private int centre;
	private PointF unit;
	private Paint paint;
	private float strokeWidth;

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
		itemCount = 3;
		direction = new int[itemCount];
		radius = new int[itemCount];
		distanceX = new int[itemCount];
		distanceY = new int[itemCount];
		color = new int[itemCount];

		random = new Random(System.currentTimeMillis());

		unit = new PointF();
		unitTotal = 10;
		centre = unitTotal / 2;

		paint = new Paint();
		paint.setColor(Color.parseColor("#ffcccc"));
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(strokeWidth = 20f);
		
		Logger.d(getClass(), "Start");
		
		cells = new ImageView[itemCount];
		for (int i = 0; i < itemCount; i++) {
			cells[i] = new ImageView(context);
			cells[i].setImageResource(R.drawable.ic_launcher);
			cells[i].setScaleType(ScaleType.CENTER_CROP);
			addView(cells[i], 200, 200);
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

			for (int i = 0; i < itemCount; i++) {
				do {
					radius[i] = random.nextInt(unitTotal);
					for (int j = 0; j < i; j++) {
						if (radius[j] == radius[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (radius[i] <= 1 || radius[i] > 3);

				do {
					distanceX[i] = random.nextInt(unitTotal);
					for (int j = 0; j < i; j++) {
						if (distanceX[j] == distanceX[i]) {
							dup = true;
							break;
						}
						dup = false;
					}
				} while (distanceX[i] <= 1 || distanceX[i] > 8 || dup);

				do {
					distanceY[i] = random.nextInt(unitTotal);
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

		for (int i = 0; i < itemCount; i++) {
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
		unit.set((right - left) / unitTotal, (bottom - top) / unitTotal);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		for (int i = 0; i < itemCount; i++) {
			paint.setColor(color[i]);
			paint.setAlpha(128);
			float left = distanceX[i] * unit.x - radius[i] * unit.y;
			float right = distanceX[i] * unit.x + radius[i] * unit.y;
			float top = distanceY[i] * unit.y - radius[i] * unit.y;
			float bottom = distanceY[i] * unit.y + radius[i] * unit.y;
			canvas.drawRect(left, top, right, bottom, paint);
//			canvas.drawCircle(distanceX[i] * unit.x, distanceY[i] * unit.y, radius[i] * unit.y, paint);
		}
		super.dispatchDraw(canvas);
		
	}
}
