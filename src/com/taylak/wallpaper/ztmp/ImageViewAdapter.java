package com.taylak.wallpaper.ztmp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taylak.wallpaper.R;
import com.taylak.wallpaper.helper.ImageItem;

public class ImageViewAdapter extends ArrayAdapter<ImageItem> {
	private Context context;
	private int layoutResourceId;
	private View.OnClickListener ViewClickListen;

	public ArrayList<ImageItem> data = new ArrayList<ImageItem>();

	public ImageViewAdapter(Context context, int layoutResourceId,
			ArrayList<ImageItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.text);
			holder.imageView = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		ImageItem item = data.get(position);
		holder.imageTitle.setText(item.getTitle());
		holder.resID = item.getImageID();
		holder.ImagePos = position;
		holder.imageView.setImageBitmap(item.getimageBmp(360, 360));

		// ImgTool.loadBitmapAsync(item.getImageInputStream(), 360, 360,
		// holder.image, new ISenkronSonuc());

		if (ViewClickListen != null) {
			row.setOnClickListener(ViewClickListen);
		}

		return row;
	}

	public class ViewHolder {
		public TextView imageTitle;
		public ImageView imageView;
		public int resID;
		public int ImagePos;
	}

	public void setViewClickListen(View.OnClickListener vcl) {
		this.ViewClickListen = vcl;
	}

}
