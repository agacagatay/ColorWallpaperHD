package com.taylak.wallpaper.universalimage;

import android.app.Activity;
import android.view.Menu;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taylak.wallpaper.R;


public abstract class BaseActivity extends Activity {
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	
	
}
