package com.taylak.wallpaper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taylak.wallpaper.R;
import com.taylak.wallpaper.helper.Constants;
import com.taylak.wallpaper.helper.Gtool;
import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;
import com.taylak.wallpaper.net.Jsonislem;
import com.taylak.wallpaper.net.SenkronHttp;
import com.taylak.wallpaper.universalimage.BaseActivity;

public class Wcw_Activity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wcw);

		sabitleriAyarla();
		actionBarAyarla();
	}

	protected void sabitleriAyarla() {

		ImgTool.ctx = this;
		if (Constants.arrbtnMenu == null) {
			Constants.arrbtnMenu = getResources().getStringArray(
					R.array.arrbtnMenu);
		}

	}

	protected void actionBarAyarla() {
		TextView tv = (TextView) findViewById(R.id.txtactionbarbaslik_wcw);
		Typeface face = Typeface.createFromAsset(getAssets(), "wc_font.ttf");
		tv.setTypeface(face);
	}

	public void btnClick(View v) {
		getImageList((String) v.getTag());
	}

	protected void GrupAc(String grup) {

		List<ImageItem> tmpImgItemList;

		try {
			tmpImgItemList = Constants.GRUPLAR.get(grup);
			if ((tmpImgItemList != null) && (tmpImgItemList.size() > 0)) {
				ArrayList<String> arrImageLinks = new ArrayList<String>();

				for (ImageItem imageItem : tmpImgItemList) {
					String picUrl = imageItem.imageURL
							.replace("/hd1/", "/hd2/");

					arrImageLinks.add(picUrl);
				}

				String[] ImageLinks = arrImageLinks
						.toArray(new String[arrImageLinks.size()]);

				Intent intent = new Intent(this, ImageGridActivity.class);
				intent.putExtra(Constants.Extra.IMAGES, ImageLinks);
				startActivity(intent);
				return;

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("wvwAct", e.getMessage());
		}

	}

	protected void getImageList(String grup) {
		List<ImageItem> tmpImgItem;

		try {
			tmpImgItem = Constants.GRUPLAR.get(grup);
			if ((tmpImgItem != null) && (tmpImgItem.size() > 0)) {

				GrupAc(grup);
				return;

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.w("wvwAct", e.getMessage());
		}

		String hostUrl = Constants.HOST + "wallpaper/picturelist.ashx?g="
				+ grup;

		gosterProgressDialog();
		SenkronHttp jsonTask = new SenkronHttp();

		jsonTask.context = this;

		jsonTask.isenkSonuc = new ISenkronSonuc() {

			@Override
			public void isBitti(Object... obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void isBitti(Bitmap bmp) {
				// TODO Auto-generated method stub

			}

			@Override
			public void isBitti(String deger, String mod) {
				progressDialog.dismiss();
				try {
					ArrayList<JSONObject> jsonList = Jsonislem.JsonList(deger);
					if ((jsonList != null) && jsonList.size() > 0) {
						ArrayList<ImageItem> tmpImgItemList = new ArrayList<ImageItem>();
						for (JSONObject jsonimgObj : jsonList) {
							ImageItem tmpImg = new ImageItem();

							tmpImg.setTitle(String.valueOf(jsonimgObj
									.get("title")));

							tmpImg.imageURL = Constants.HOST
									+ (String.valueOf(jsonimgObj.get("url")));

							tmpImg.grup = (String
									.valueOf(jsonimgObj.get("url")));

							tmpImgItemList.add(tmpImg);
						}

						Constants.GRUPLAR.put(mod, tmpImgItemList);
						GrupAc(mod);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Gtool.mesaj(Wcw_Activity.this,
							"Dont get list.Check Your Connection...");
				}
			}
		};

		jsonTask.execute(new String[] { hostUrl, grup });

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		if (null != progressDialog) {
			progressDialog.dismiss();
		}

	}

	private ProgressDialog progressDialog;

	protected void gosterProgressDialog() {
		progressDialog = new ProgressDialog(this);
		// Set the progress dialog to display a horizontal progress bar
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// Set the dialog title to 'Loading...'
		progressDialog.setTitle("Loading...");
		// Set the dialog message to 'Loading application View, please
		// wait...'
		progressDialog.setMessage("Getting Image List, please wait...");
		// This dialog can't be canceled by pressing the back key
		progressDialog.setCancelable(false);
		// This dialog isn't indeterminate
		progressDialog.setIndeterminate(false);
		// The maximum number of items is 100
		// progressDialog.setMax(100);
		// Set the current progress to zero
		// progressDialog.setProgress(0);
		// Display the progress dialog
		progressDialog.show();
	}

}
