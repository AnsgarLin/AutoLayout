package com.example.autolayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageCell extends ImageView {
	private Paint paint;
	private Bitmap bitmap;
	
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
//		setImageResource(R.drawable.ic_launcher);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		
		canvas.save(); 
		canvas.clipRect(0, 0, getWidth(), getHeight());
		Path path = new Path();
		path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, Direction.CCW);
		canvas.clipPath(path, Region.Op.REPLACE);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		canvas.restore(); 
	}
	
	public void shuffle() {
	}
}
