package com.taylak.wallpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.taylak.wallpaper.helper.Constants.Extra;
import com.taylak.wallpaper.helper.Gtool;
import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;
import com.taylak.wallpaper.net.SenkronDownload;
import com.taylak.wallpaper.universalimage.BaseActivity;

public class ImagePagerActivity extends BaseActivity {
	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;

	ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		ActionBarAyarla();

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(pagerPosition);
		
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	protected void ActionBarAyarla() {
		TextView tv = (TextView) findViewById(R.id.txtactionbarbaslik_pager);
		Typeface face = Typeface.createFromAsset(getAssets(), "wc_font.ttf");
		tv.setTypeface(face);
	}

	protected void ResimAyarla(final Boolean _setWallpaper,
			final Boolean _share, final Boolean _save) {

		try {
			String[] imageUrls = ((ImagePagerAdapter) pager.getAdapter()).images;
			int pos = pager.getCurrentItem();

			if ((imageUrls.length > 0) && (imageUrls[pos] != null)) {
				String imgUrl = imageUrls[pos].replace("/hd2/", "/hd1/");
				ResimAyarla(imgUrl, _setWallpaper, _share, _save);
			}

		} catch (Exception e) {

			Log.e("SaveWallpaper() Error", e.getMessage());
			return;
		}

	}

	protected void ResimAyarla(ImageItem img, final Boolean _setWallpaper,
			final Boolean _share, final Boolean _save) {

		String imgName = img.getTitle();

		ImgTool.BitmapSaveTaskInputStream bmpSave = new ImgTool.BitmapSaveTaskInputStream(
				new ISenkronSonuc() {

					@Override
					public void isBitti(Object... obj) {
						File tmpFile = (File) obj[0];
						Uri imgUri = Uri.fromFile(tmpFile);
						//Gtool.mesaj(ImagePagerActivity.this, "Processing image");
						if (_save) {
							Gtool.mesaj(ImagePagerActivity.this,
									tmpFile.getName() + " SAVED");
						}

						if (_share) {
							startActivity(createShareIntent(imgUri));
						}
						if (_setWallpaper) {
							ImgTool.SetAsWallpaper(imgUri);
							Gtool.mesaj(ImagePagerActivity.this,
									"HD WallPaper Setting..");
						}

					}

					@Override
					public void isBitti(Bitmap bmp) {

					}

					@Override
					public void isBitti(String deger, String mod) {

					}
				});

		bmpSave.execute(img.getImageInputStream(), imgName);

	}

	protected void ResimAyarla(String url, final Boolean _setWallpaper,
			final Boolean _share, final Boolean _save) {

		File pictureFile = null;
		ImageItem tmpImageItem;
		tmpImageItem = ImgTool.ImageItemFromUrl(url);
		if (tmpImageItem != null) {
			pictureFile = ImgTool.getOutputMediaFile(tmpImageItem.getTitle());
			if ((pictureFile != null) && (pictureFile.exists())) {
				try {
					FileInputStream fis = new FileInputStream(pictureFile);
					tmpImageItem.setImageInputStream(fis);
					if (_save) {
						Gtool.mesaj(
								this,
								"Already Saved:   "
										+ pictureFile.getAbsolutePath());
					} else {
						ResimAyarla(tmpImageItem, _setWallpaper, _share, false);
					}

					return;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		pictureFile = imageLoader.getDiscCache().get(url);
		if (null != pictureFile) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(pictureFile);

				if (tmpImageItem != null) {
					tmpImageItem.setImageInputStream(fis);
					ResimAyarla(tmpImageItem, _setWallpaper, _share, _save);

					return;

				}

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
		}

		// ****************************************************************

		Gtool.mesaj(this, "HD Photo is downloading...");
		SenkronDownload imgDownTask = new SenkronDownload(new ISenkronSonuc() {

			@Override
			public void isBitti(Object... obj) {
				// TODO Auto-generated method stub
				try {
					String url = (String) obj[1];
					InputStream is = (InputStream) obj[0];
					if (null == is) {
						Log.w("imgDownTask", "is=null geldi");
						return;
					}
					ImageItem tmpImageItem;
					tmpImageItem = ImgTool.ImageItemFromUrl(url);
					if (tmpImageItem != null) {
						tmpImageItem.setImageInputStream(is);
						ResimAyarla(tmpImageItem, _setWallpaper, _share, _save);
						return;

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void isBitti(Bitmap bmp) {
				// TODO Auto-generated method stub

			}

			@Override
			public void isBitti(String deger, String mod) {
				// TODO Auto-generated method stub

			}
		});

		imgDownTask.execute(url);

	}

	private Intent createShareIntent(Uri uri) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");

		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		return shareIntent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if (resultCode == RESULT_OK) {
		// Bundle extras = data.getExtras();
		// Bitmap bmp = extras.getParcelable("data");
		// ImgTool.SetAsWallpaper(bmp);
		// }

	};

	public void btnActionBarMenu(View view) {
		showDialog(1);

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
											ResimAyarla(true, false, false);
											break;
										case 1: // save
											ResimAyarla(false, false, true);
											break;

										case 2: // share
											ResimAyarla(false, true, false);
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

	private class ImagePagerAdapter extends PagerAdapter {

		public String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {

			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			imageLoader.displayImage(images[position], imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								imageLoader.clearMemoryCache();
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}
							Toast.makeText(ImagePagerActivity.this, message,
									Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							spinner.setVisibility(View.GONE);
							ImageView tmpImageview = (ImageView)view;
							tmpImageview.setOnClickListener(null);
							tmpImageview.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showDialog(1);
									
								}
							});

						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

	
}
