package com.taylak.wallpaper.helper;

import java.io.InputStream;
import java.net.URL;

import com.taylak.wallpaper.net.SenkronDownload;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageItem {
	private Bitmap imageBmp;
	private String title;
	private int resID;
	private InputStream imageInputStream;

	public String imageURL = null;
	public String grup;
	public Uri uri;

	public ImageItem(InputStream _is, String title) {
		super();
		this.imageInputStream = _is;
		this.title = title;
		this.resID = -1;

	}

	public ImageItem(InputStream _is, String title, int _resID) {
		super();
		this.imageInputStream = _is;
		this.title = title;
		this.resID = _resID;
	}

	public ImageItem() {
		super();
	}

	private int imgW = -1;
	private int imgH = -1;

	public Bitmap getimageBmp(int w, int h) {

		if ((imgW != w) || (imgH != h)) {
			imgW = w;
			imgH = h;
			if (imageInputStream != null) {
				imageBmp = ImgTool.decodeSampledBitmap(imageInputStream, w, h);
			} else {
				return null;
			}

		} else {
			if (imageBmp == null) {
				if (imageInputStream != null) {
					imageBmp = ImgTool.decodeSampledBitmap(imageInputStream, w,
							h);
				} else {
					return null;
				}

			}
		}

		return imageBmp;
	}

	public Bitmap getimageBmp() {

		return imageBmp;
	}

	public void setimageBmp(Bitmap image) {
		this.imageBmp = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageID() {
		return resID;
	}

	public void setImageID(int _resID) {
		resID = _resID;
	}

	public InputStream getImageInputStream() {
		if ((null == imageInputStream) && (null != imageBmp)) {

		}
		return imageInputStream;
	}

	public void setImageInputStream(InputStream imageInputStream) {
		this.imageInputStream = imageInputStream;
	}

	public void dispose() {
		imageBmp = null;
		imageInputStream = null;
	}
}
