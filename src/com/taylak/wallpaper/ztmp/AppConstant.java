package com.taylak.wallpaper.ztmp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.taylak.wallpaperhd.R;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;
import com.taylak.wallpaper.net.SenkronHttp;

public class AppConstant {
	public static Context ctx;

	public static final Integer[] YerelResimIDs = {  

	};

 

 



	private static ArrayList<ImageItem> pListYerelResimler = null;

	public static ArrayList<ImageItem> ListYerelResimler(Context _ctx) {

		if (pListYerelResimler != null) {
			return pListYerelResimler;
		}

		ArrayList<ImageItem> retVal = new ArrayList<ImageItem>();

		ImageItem imgItem;

		int sira = 0;
		for (int imgID : YerelResimIDs) {

			// Bitmap bitmap = ImgTool.decodeSampledBitmap(_ctx.getResources(),
			// imgID, 640, 640);
			InputStream imgInpStr = ImgTool.getImageFromRes(
					_ctx.getResources(), imgID);

			imgItem = new ImageItem(imgInpStr, "Image#" + sira, imgID);

			retVal.add(imgItem);
			sira++;
		}

		pListYerelResimler = retVal;

		return pListYerelResimler;

	}

 

}
