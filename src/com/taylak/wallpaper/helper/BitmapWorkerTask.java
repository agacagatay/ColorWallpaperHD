package com.taylak.wallpaper.helper;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class BitmapWorkerTask extends AsyncTask<InputStream, Void, Bitmap> {

	Context ctx;

	public int imgW = 360;
	public int imgH = 360;
	public ISenkronSonuc sonuc;

	public BitmapWorkerTask(Context _ctx) {

		this.ctx = _ctx;
	}

	@Override
	protected Bitmap doInBackground(InputStream... params) {
		InputStream _is = params[0];
		return ImgTool.decodeSampledBitmap(_is, imgW, imgH);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
	    if (isCancelled()) {
            bitmap = null;
        }

		sonuc.isBitti(bitmap);

	}
	
	
}
