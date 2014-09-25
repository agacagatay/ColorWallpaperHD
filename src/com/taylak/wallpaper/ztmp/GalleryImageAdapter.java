package com.taylak.wallpaper.ztmp;

import java.util.ArrayList;

import com.taylak.wallpaper.helper.ImageItem;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GalleryImageAdapter extends BaseAdapter  {
	private Context ctx;
	public ArrayList<ImageItem> data;

	public GalleryImageAdapter(Context mContext,ArrayList<ImageItem> _data) {
		super();
		this.ctx = mContext;
		this.data = _data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		ImageItem tmpImg = data.get(position);

		ImageView i = new ImageView(ctx);
	

		i.setImageDrawable(new BitmapDrawable(tmpImg.getimageBmp(200, 200)));

		i.setLayoutParams(new Gallery.LayoutParams(130, 200));
		i.setScaleType(ScaleType.FIT_CENTER);
		// i.setScaleType(ImageView.ScaleType.FIT_XY);

		return i;
	}

}
