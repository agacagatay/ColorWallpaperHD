package com.taylak.wallpaper.ztmp;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImgTool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask2 extends AsyncTask<InputStream, Void, Bitmap> {

	private final WeakReference<ImageView> imageViewReference;
	private InputStream data;
	Context ctx;

	public int imgW = 360;
	public int imgH = 360;
	public ISenkronSonuc sonuc;

	public BitmapWorkerTask2(Context _ctx, ImageView imageView) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.ctx = _ctx;
	}

	@Override
	protected Bitmap doInBackground(InputStream... params) {
		data = params[0];

		return ImgTool.decodeSampledBitmap(data, imgW, imgH);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask2 bitmapWorkerTask = getBitmapWorkerTask(imageView);
			if (this == bitmapWorkerTask && imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}

		sonuc.isBitti(bitmap);

	}

	private static BitmapWorkerTask2 getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	public static boolean cancelPotentialWork(InputStream data,
			ImageView imageView) {
		final BitmapWorkerTask2 bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final InputStream bitmapData = bitmapWorkerTask.data;
			// If bitmapData is not yet set or it differs from the new data
			if (bitmapData == null || bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	static class AsyncDrawable extends BitmapDrawable {

		private final WeakReference<BitmapWorkerTask2> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, InputStream _is,
				BitmapWorkerTask2 bitmapWorkerTask) {
			super(res,_is);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask2>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask2 getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

}
