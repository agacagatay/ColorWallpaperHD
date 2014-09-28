package com.taylak.wallpaper.ztmp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.taylak.wallpaper.R;
import com.taylak.wallpaper.helper.Constants;
import com.taylak.wallpaper.helper.Gtool;
import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;
import com.taylak.wallpaper.helper.Referanslar;

public class FullScreenImageActivity extends Activity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory,
		android.view.View.OnTouchListener {

	private ImageSwitcher mSwitcher;
	private Button btnMenu;

	int pos = 0;
	Gallery g;
	GalleryImageAdapter galleryImageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.full_screen_image_activity);

		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(1);
			}
		});

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		mSwitcher.setOnTouchListener(this);

		g = (Gallery) findViewById(R.id.gallery);

		// ImageViewAdapter imgVAdapter = new ImageViewAdapter(this,
		// R.layout.row_grid, AppConstant.ListYerelResimler(this));
		// imgVAdapter.setViewClickListen(new
		// android.view.View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ViewHolder ViewTiklanan = ((ViewHolder) v.getTag());
		//
		// mSwitcher.setImageDrawable(ViewTiklanan.image.getDrawable());
		//
		// }
		// });

		galleryImageAdapter = new GalleryImageAdapter(this,
				(ArrayList<ImageItem>) Referanslar.getGRUPLAR().get(0));

		g.setAdapter(galleryImageAdapter);

		g.setOnItemSelectedListener(this);
		g.setSelected(true);

		if (getIntent().getExtras() != null) {
			Bundle veri = getIntent().getExtras();
			int imgID = veri.getInt("ImageID");
			pos = veri.getInt("ImagePos");

			g.setSelection(pos, true);
		}

	}

	protected void sonraki() {
		if (!(pos >= g.getAdapter().getCount() - 1)) {
			pos += 1;
			g.setSelection(pos, true);
		}

	}

	protected void onceki() {
		if (!(pos <= 0)) {
			pos -= 1;
			g.setSelection(pos, true);

		}

	}

	protected void SaveWallpaper() {

		ImageItem tmpImg = null;
		try {
			tmpImg = (ImageItem) galleryImageAdapter.data.get(pos);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("SaveWallpaper() Error", e.getMessage());
			return;
		}

		SaveWallpaper(tmpImg);
	}

	protected void SaveWallpaper(ImageItem img) {
		String imgName = img.getTitle() + ".jpg";
		// ImgTool.BitmapSaveTask bmpSave = new ImgTool.BitmapSaveTask(
		// new ISenkronSonuc() {
		//
		// @Override
		// public void isBitti(Object... obj) {
		// File tmpFile = (File) obj[0];
		// Gtool.mesaj(FullScreenImageActivity.this,
		// tmpFile.getName() + " SAVED");
		//
		// }
		//
		// @Override
		// public void isBitti(Bitmap bmp) {
		//
		// }
		//
		// @Override
		// public void isBitti(String deger, String mod) {
		//
		// }
		// });

		// bmpSave.execute(img.getImageInputStream(), imgName);

	}

	protected void SetAsWallpaper() {
		ImageItem tmpImg = null;
		try {
			tmpImg = (ImageItem) galleryImageAdapter.data.get(pos);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("SetAsWallpaper Error", e.getMessage());
			return;
		}
		SetAsWallpaper(tmpImg);
	}

	protected void SetAsWallpaper(ImageItem img) {
		ImageItem tmpImg = img;

		if (tmpImg != null) {

			ImgTool.loadBitmapAsync(tmpImg.getImageInputStream(), 2000, 2000,
					new ISenkronSonuc() {

						@Override
						public void isBitti(Bitmap bmp) {

							// ImgTool.SetAsWallpaper(bmp);
							// Intent intent = new Intent(
							// Intent.ACTION_SET_WALLPAPER);
							// intent.putExtra("mimeType", "image/jpeg");
							// intent.putExtra(Intent.EXTRA_STREAM, bmp);
							// intent.setAction(Intent.ACTION_SEND);
							// intent.setType("image/jpeg");
							// startActivity(Intent.createChooser(intent,
							// "Share From"));
							// startActivityForResult(intent, 12345);

							// List localList =
							// getPackageManager().queryIntentActivities(intent,
							// 65536);

							Gtool.mesaj(FullScreenImageActivity.this, "OK");
						}

						@Override
						public void isBitti(String deger, String mod) {
							// TODO Auto-generated method stub

						}

						@Override
						public void isBitti(Object... obj) {
							// TODO Auto-generated method stub

						}
					});

		}
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long arg3) {
		// TODO Auto-generated method stub

		ImageItem Tiklanan = (ImageItem) ((GalleryImageAdapter) arg0
				.getAdapter()).data.get(position);

		mSwitcher.setImageDrawable(new BitmapDrawable(Tiklanan.getimageBmp(640,
				640)));
		pos = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	float downX, upX;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.switcher) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				// Log.i("event.getX()", " downX " + downX);

				return true;

			case MotionEvent.ACTION_UP:
				upX = event.getX();
				// Log.i("event.getX()", " upX " + upX);
				if (upX - downX > 100) {
					onceki();

				} else if (downX - upX > 100) {
					sonraki();

				}
				return true;
			}

		}

		return false;
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			return new AlertDialog.Builder(this)
					.setTitle(R.string.app_name)
					.setItems(R.array.arrbtnMenu,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									try {
										switch (which) {
										case 0: // set wallpaper
											SetAsWallpaper();
											break;
										case 1: // save
											SaveWallpaper();
											break;
										default:
											break;
										}
									} catch (Exception e) {
										// TODO: handle exception
										Log.e("Dialog secildi 1 hata",
												e.getMessage());
									}

								}

							}).create();

		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
