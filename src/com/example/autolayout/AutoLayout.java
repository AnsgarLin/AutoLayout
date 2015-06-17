package com.example.autolayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AutoLayout extends ActionBarActivity {
	private int mCellCount = 3;

	private Playground playground;
	private CellGround mCellGround;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_layout);
		mCellGround = (CellGround) findViewById(R.id.cell_ground);

		final TextView init = (TextView) findViewById(R.id.init);
		final TextView shuffle = (TextView) findViewById(R.id.shuffle);

		init.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCellGround.setCellCount(3);
				shuffle.setClickable(true);
			}
		});
		
		shuffle.setClickable(false);
		shuffle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCellGround.shuffle();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auto_layout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}