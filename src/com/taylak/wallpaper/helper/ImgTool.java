package com.taylak.wallpaper.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taylak.wallpaper.net.SenkronDownload;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImgTool {
	public static Context ctx;

	public static ImageItem ImageItemFromUrl(String url) {

		for (List<ImageItem> lstImg : Referanslar.getGRUPLAR().values()) {
			for (ImageItem item : lstImg) {
				if (item.imageURL.equalsIgnoreCase(url)) {
					return item;
				}
			}
		}
		return null;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		while (((height / inSampleSize) > reqHeight)
				|| ((width / inSampleSize) > reqWidth)) {
			inSampleSize *= 2;
		}

		return inSampleSize;
	}

	public static InputStream getImageFromRes(Resources res, int resId) {

		InputStream _is;
		try {
			_is = ctx.getResources().openRawResource(resId);
		} catch (NotFoundException e) {
			// TODO: handle exception
			return null;
		}

		return _is;
	}

	public static Bitmap decodeSampledBitmap(Resources res, int resId,
			int reqWidth, int reqHeight) {
		Bitmap retVal;
		retVal = decodeSampledBitmap(getImageFromRes(res, resId), reqWidth,
				reqHeight);
		return retVal;

	}

	public static Bitmap decodeSampledBitmap(InputStream _is, int reqWidth,
			int reqHeight) {

		if (_is == null)
			return null;

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeStream(_is, null, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		return BitmapFactory.decodeStream(_is, null, options);
	}

	public static void loadBitmapAsync(InputStream _is, int _w, int _h,
			ISenkronSonuc _sonuc) {

		final BitmapWorkerTask task = new BitmapWorkerTask(ctx);
		task.imgW = _w;
		task.imgH = _h;
		task.sonuc = _sonuc;
		task.execute(_is);

	}

	// public static void loadBitmapAsync2(InputStream _is, int _w, int _h,
	// ImageView imageView, ISenkronSonuc _sonuc) {
	//
	// if (BitmapWorkerTask2.cancelPotentialWork(_is, imageView)) {
	// final BitmapWorkerTask2 task = new BitmapWorkerTask2(ctx, imageView);
	// task.imgW = _w;
	// task.imgH = _h;
	// task.sonuc = _sonuc;
	// final BitmapWorkerTask2.AsyncDrawable asyncDrawable = new
	// BitmapWorkerTask2.AsyncDrawable(
	// ctx.getResources(), _is, task);
	//
	// imageView.setImageDrawable(asyncDrawable);
	// task.execute(_is);
	// }
	// }

	public static void SetAsWallpaper(Uri _uri) {

		Intent localIntent = new Intent("android.intent.action.ATTACH_DATA");
		try {

			localIntent.setDataAndType(_uri, "image/jpeg");
			localIntent.putExtra("mimeType", "image/jpeg");
			ctx.startActivity(Intent.createChooser(localIntent,
					"Set Image..World Cup.."));

			return;
		} catch (Exception localException) {

		}
	}

	public static void SetAsWallpaper(InputStream _data) {
		WallpaperManager myWallpaperManager = WallpaperManager.getInstance(ctx);
		try {
			int w = getScreenMetrics().widthPixels;
			int h = getScreenMetrics().heightPixels;

			myWallpaperManager.setStream(_data);
			myWallpaperManager.suggestDesiredDimensions(2, 3);

			Gtool.mesaj(ctx, "WallPaper Ok..");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void SetAsWallpaper(Bitmap _data) {
		WallpaperManager myWallpaperManager = WallpaperManager.getInstance(ctx);
		try {
			int w = getScreenMetrics().widthPixels;
			int h = getScreenMetrics().heightPixels;

			// myWallpaperManager.suggestDesiredDimensions(w, h);
			myWallpaperManager.setBitmap(_data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public static Point getScreenSize() {

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}

		return point;
	}

	public static DisplayMetrics getScreenMetrics() {

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);

		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);

		return metrics;
	}

	public static class BitmapSaveTaskInputStream extends
			AsyncTask<Object, Void, Object[]> {

		public ISenkronSonuc sonuc;

		public BitmapSaveTaskInputStream(ISenkronSonuc sonuc) {
			super();
			this.sonuc = sonuc;
		}

		@Override
		protected Object[] doInBackground(Object... params) {
			Object[] retVal = new Object[2];

			InputStream _is = (InputStream) params[0];
			String _imageName = (String) params[1];

			File pictureFile = getOutputMediaFile(_imageName);
			if (pictureFile == null) {
				Log.d("BitmapSaveTask", "storeimage picturefile==null");// e.getMessage());
				return null;
			}
			if (pictureFile.exists()) {
				retVal[0] = pictureFile;

				return retVal;
			}
			try {

				FileOutputStream fos = new FileOutputStream(pictureFile);

				try {

					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					int len = 0;

					while ((len = _is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					fos.close();
					_is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				Log.d("storeImage", "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d("storeImage", "Error accessing file: " + e.getMessage());
			}
			retVal[0] = pictureFile;
			return retVal;
		}

		@Override
		protected void onPostExecute(Object[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			sonuc.isBitti(result[0], result[1]);
		}

	}

	// public static class BitmapSaveTask extends AsyncTask<Object, Void, File>
	// {
	//
	// public ISenkronSonuc sonuc;
	//
	// public BitmapSaveTask(ISenkronSonuc sonuc) {
	// super();
	// this.sonuc = sonuc;
	// }
	//
	// @Override
	// protected File doInBackground(Object... params) {
	// Bitmap bmp = (Bitmap) params[0];
	// String _imageName = (String) params[1];
	//
	// File pictureFile = getOutputMediaFile(_imageName);
	// if (pictureFile == null) {
	// Log.d("BitmapSaveTask", "storeimage picturefile==null");//
	// e.getMessage());
	// return null;
	// } else if (pictureFile.exists()) {
	// pictureFile.delete();
	// }
	//
	// try {
	// FileOutputStream out = new FileOutputStream(pictureFile);
	// bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
	// out.flush();
	// out.close();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// bmp = null;
	// return pictureFile;
	// }
	//
	// @Override
	// protected void onPostExecute(File result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// sonuc.isBitti(result);
	// }
	//
	// }

	public static File getOutputMediaFile(String _imageName) {

		File mediaStorageDir;

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {

			mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
							+ "/ColorWallpaper");
		} else {

			ContextWrapper cw = new ContextWrapper(ctx);
			// path to /data/data/yourapp/app_data/imageDir
			mediaStorageDir = cw.getDir("ColorWallpaper", Context.MODE_PRIVATE);

		}

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		File mediaFile;
		String mImageName;
		// mImageName = "MI_" + timeStamp + ".jpg";
		mImageName = _imageName;

		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;

	}
}
