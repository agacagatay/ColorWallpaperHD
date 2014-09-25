package com.taylak.wallpaper.ztmp;


public class tmpCode {
/*
	
	protected void GridHostImage() {

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
				try {
					ArrayList<JSONObject> jsonList = Jsonislem.JsonList(deger);
					if ((jsonList != null) && jsonList.size() > 0) {
						ArrayList<ImageItem> tmpImgItemList = new ArrayList<ImageItem>();
						for (JSONObject jsonimgObj : jsonList) {
							ImageItem tmpImg = new ImageItem();

							tmpImg.setTitle(String.valueOf(jsonimgObj
									.get("title")));

							tmpImg.imageURL = (String.valueOf(jsonimgObj
									.get("url")));

							tmpImgItemList.add(tmpImg);
						}

						gvAdapter = new GridViewAdapter(Wcw_Activity.this,
								tmpImgItemList);
						AppConstant.GRUPLAR.put(mod, tmpImgItemList);
						gridView.setAdapter(gvAdapter);
						gridView.setOnItemClickListener(Wcw_Activity.this);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		jsonTask.execute(new String[] {
				AppConstant.HOST + "wallpaper/picturelist.ashx?g=" + "0", "0" });

	}

	protected void GridYerelImage() {
		customGridAdapter = new ImageViewAdapter(this, R.layout.row_grid,
				AppConstant.ListYerelResimler(this));

		customGridAdapter
				.setViewClickListen(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {

						ImageViewAdapter.ViewHolder ViewTiklanan = ((ViewHolder) v
								.getTag());

						Intent fullScreenIntent = new Intent(Wcw_Activity.this,
								FullScreenImageActivity.class);

						// passing array index
						fullScreenIntent
								.putExtra("ImageID", ViewTiklanan.resID);
						fullScreenIntent.putExtra("ImagePos",
								ViewTiklanan.ImagePos);

						// fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(fullScreenIntent);
					}
				});

		gridView.setAdapter(customGridAdapter);
	}




*/
}
