package com.example.autolayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageCell extends ImageView {
	private Path path;
	private Paint paint;
	private Bitmap mBitmap;
	
	public ImageCell(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ImageCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageCell(Context context) {
		super(context);
		init();
	}

	public void init() {
		path = new Path();
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save(); 
		canvas.clipRect(0, 0, getWidth(), getHeight());
		path.reset();
		path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, Direction.CCW);
		canvas.clipPath(path, Region.Op.REPLACE);
		canvas.drawBitmap(mBitmap, 0, 0, paint);
		canvas.restore();
		
		paint.setStyle(Style.STROKE);
		canvas.drawPath(path, paint);
	}
	
	public void shuffle() {
	}
	
	public void setImage(Bitmap bitmap) {
		mBitmap = bitmap;
	}
}
