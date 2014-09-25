package com.taylak.wallpaper.ztmp;

import java.io.InputStream;
import java.util.ArrayList;

import com.taylak.wallpaper.helper.Constants;
import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GridViewAdapter extends BaseAdapter {
	private Context ctx;
	public ArrayList<ImageItem> data;

	public GridViewAdapter(Context ctx, ArrayList<ImageItem> data) {
		super();
		this.ctx = ctx;
		this.data = data;
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
		View row = convertView;
		final ImageItem tmpImg = data.get(position);

		ImageView i = new ImageView(ctx);

		Bitmap tmpBmp = tmpImg.getimageBmp(360, 360);
		if (tmpBmp == null) {
//			ImgTool.LoadBmpFromUrlAsync(Constants.HOST + tmpImg.imageURL, i,
//					new ISenkronSonuc() {
//
//						@Override
//						public void isBitti(Object... obj) {
//							// TODO Auto-generated method stub
//							tmpImg.setImageInputStream((InputStream) obj[0]);
//						}
//
//						@Override
//						public void isBitti(Bitmap bmp) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void isBitti(String deger, String mod) {
//							// TODO Auto-generated method stub
//
//						}
//					});
		} else {
			i.setImageDrawable(new BitmapDrawable(tmpBmp));

			// i.setLayoutParams(new Gallery.LayoutParams(130, 200));
			i.setScaleType(ScaleType.FIT_CENTER);
			// i.setScaleType(ImageView.ScaleType.FIT_XY);

		}
		return i;
	}
}
