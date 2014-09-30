package com.taylak.wallpaper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.OpenRequest;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.taylak.wallpaper.helper.Constants;
import com.taylak.wallpaper.helper.Gtool;
import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImageItem;
import com.taylak.wallpaper.helper.ImgTool;
import com.taylak.wallpaper.helper.MainFragment;
import com.taylak.wallpaper.helper.Referanslar;
import com.taylak.wallpaper.net.Jsonislem;
import com.taylak.wallpaper.net.SenkronHttp;
import com.taylak.wallpaperhd.R;

public class Wcw_Activity extends FragmentActivity {

	private MainFragment mainFragment;
	

	//private LoginButton loginButton;
	private GraphUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wcw);

		sabitleriAyarla();
		actionBarAyarla();
	//	FbOnyukleme(savedInstanceState);

	}
	
	
//private UiLifecycleHelper uiHelper;
//	private PendingAction pendingAction = PendingAction.NONE;
//
//	private enum PendingAction {
//		NONE, POST_PHOTO, POST_STATUS_UPDATE
//	}
//
//	private final String PENDING_ACTION_BUNDLE_KEY = "com.taylak.wallpaperhd:PendingAction";

	// protected void FbOnyukleme(Bundle savedInstanceState) {
	// if (savedInstanceState == null) {
	// // Add the fragment on initial activity setup
	// mainFragment = new MainFragment();
	// getSupportFragmentManager().beginTransaction()
	// .add(android.R.id.content, mainFragment).commit();
	// } else {
	// // Or set the fragment from restored state info
	// mainFragment = (MainFragment) getSupportFragmentManager()
	// .findFragmentById(android.R.id.content);
	// }
	//
	// uiHelper = new UiLifecycleHelper(this, callback);
	// uiHelper.onCreate(savedInstanceState);
	//
	// if (savedInstanceState != null) {
	// String name = savedInstanceState
	// .getString(PENDING_ACTION_BUNDLE_KEY);
	// pendingAction = PendingAction.valueOf(name);
	// }
	//
	// loginButton = (LoginButton)findViewById(R.id.authButton);
	// loginButton.setReadPermissions(Arrays.asList("user_likes",
	// "user_status"));
	// //loginButton.setPublishPermissions("user_friends","email");
	//
	// loginButton.setUserInfoChangedCallback(new
	// LoginButton.UserInfoChangedCallback() {
	//
	// @Override
	// public void onUserInfoFetched(GraphUser user) {
	// // TODO Auto-generated method stub
	// Wcw_Activity.this.user = user;
	//
	// handlePendingAction();
	// }
	// });
	//
	// }

	// private Session.StatusCallback callback = new Session.StatusCallback() {
	// @Override
	// public void call(Session session, SessionState state,
	// Exception exception) {
	// onSessionStateChange(session, state, exception);
	// }
	// };

	// private FacebookDialog.Callback dialogCallback = new
	// FacebookDialog.Callback() {
	// @Override
	// public void onError(FacebookDialog.PendingCall pendingCall,
	// Exception error, Bundle data) {
	// Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
	// }
	//
	// @Override
	// public void onComplete(FacebookDialog.PendingCall pendingCall,
	// Bundle data) {
	// Log.d("HelloFacebook", "Success!");
	// }
	// };

	// private void onSessionStateChange(Session session, SessionState state,
	// Exception exception) {
	// if (pendingAction != PendingAction.NONE
	// && (exception instanceof FacebookOperationCanceledException || exception
	// instanceof FacebookAuthorizationException)) {
	// new AlertDialog.Builder(Wcw_Activity.this)
	// .setTitle(R.string.cancelled)
	// .setMessage(R.string.permission_not_granted)
	// .setPositiveButton(R.string.ok, null).show();
	// pendingAction = PendingAction.NONE;
	// } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
	// handlePendingAction();
	// }
	//
	// }

	private static Session openActiveSession(Activity activity,
			boolean allowLoginUI, List permissions, StatusCallback callback) {
		OpenRequest openRequest = new OpenRequest(activity).setPermissions(
				permissions).setCallback(callback);
		Session session = new Session.Builder(activity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}
		return null;
	}

	private void openFacebookSession() {

		Session.openActiveSession(this, true,
				Arrays.asList("email", "user_birthday","user_friends"),
				new Session.StatusCallback() {
			
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						if (exception != null) {
							Log.d("__Facebook__hata", exception.getMessage());
						}
						Log.d("__Facebook__state",
								"Session State: " + session.getState());
						
						
						
						// you can make request to the /me API or do other stuff
						// like
						// post, etc. here
					}
				});
	}

	protected void sabitleriAyarla() {

		ImgTool.ctx = this;
		if (Constants.arrbtnMenu == null) {
			Constants.arrbtnMenu = getResources().getStringArray(
					R.array.arrbtnMenu);
		}

		Referanslar.ReferanslarConst(this);

	}

	protected void actionBarAyarla() {
		TextView tv = (TextView) findViewById(R.id.txtactionbarbaslik_wcw);
		Typeface face = Typeface.createFromAsset(getAssets(), "wc_font.ttf");
		tv.setTypeface(face);
	}

	public void btnClick(View v) {
		openFacebookSession();
		// getImageList((String) v.getTag());
	}

	protected void GrupAc(String grup) {

		List<ImageItem> tmpImgItemList;

		try {
			tmpImgItemList = Referanslar.getGRUPLAR().get(grup);
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
			if (!Gtool.isNetworkConnected(this)) {
				Gtool.mesaj(this, "Check Connection for new Wallpaper");
				tmpImgItem = Referanslar.getGRUPLAR().get(grup);
				if ((tmpImgItem != null) && (tmpImgItem.size() > 0)) {

					GrupAc(grup);
					return;

				}
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

						if (!Referanslar.getGRUPLAR().containsKey(mod)
								|| Referanslar.getGRUPLAR().get(mod).size() != tmpImgItemList
										.size()) {
							Referanslar.getGRUPLAR().put(mod, tmpImgItemList);
							Referanslar.setGRUPLAR(Referanslar.getGRUPLAR());

						}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// uiHelper.onActivityResult(requestCode, resultCode, data,
		// dialogCallback);
		Session.getActiveSession().onActivityResult(Wcw_Activity.this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//uiHelper.onResume();


		//AppEventsLogger.activateApp(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		//uiHelper.onPause();

		// Call the 'deactivateApp' method to log an app event for use in
		// analytics and advertising
		// reporting. Do so in the onPause methods of the primary Activities
		// that an app may be launched into.
		//AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//uiHelper.onSaveInstanceState(outState);

		// outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//uiHelper.onDestroy();
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
