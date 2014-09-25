package com.taylak.wallpaper.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.taylak.wallpaper.helper.ISenkronSonuc;
import com.taylak.wallpaper.helper.ImgTool;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
//import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class SenkronDownload extends AsyncTask<Object, Integer, Object[]> {

	public Context context;

	public int w = 360, h = 360;
	public ISenkronSonuc isenkSonuc;
	public int timeoutConnection = 5000;
	// public Boolean DiaGoster = false;
	public List<NameValuePair> postVerileri = null;
	public String imageURL;

	public SenkronDownload(ISenkronSonuc sonuc) {
		// TODO Auto-generated constructor stub
		super();
		this.isenkSonuc = sonuc;

	}

	@Override
	protected Object[] doInBackground(Object... params) {
		Object[] obj = new Object[2];
		try {
			
			imageURL = (String) params[0];
			InputStream is;
			is = fDefaultCon(imageURL);
			obj[0] = is;
			obj[1] = imageURL;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return obj;

	}

	@Override
	protected void onPostExecute(Object[] result) {
		super.onPostExecute(result);
		// if (DiaGoster) {dialog.dismiss(); }

		isenkSonuc.isBitti(result[0], result[1]);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		// if (DiaGoster) {dialog.incrementProgressBy(values[0]);}

	}

	private InputStream fDefaultCon(String strUrl) {
		InputStream is = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost;
		HttpGet httpGet;
		HttpResponse httpResponse;
		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		httpClient.setParams(httpParameters);
		try {
			// AndroidHttpClient httpClient=AndroidHttpClient.newInstance("1");

			if (postVerileri != null) {

				httpPost = new HttpPost(strUrl);
				httpPost.setEntity(new UrlEncodedFormEntity(postVerileri));
				httpResponse = httpClient.execute(httpPost);
				publishProgress(60);
			} else {
				httpGet = new HttpGet(strUrl);
				httpResponse = httpClient.execute(httpGet);
				publishProgress(60);
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			publishProgress(100);

			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			Log.e("sennkreondownload", e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("sennkreondownload", e.getMessage());
		} catch (IOException e) {
			Log.e("sennkreondownload", e.getMessage());
		}

		return is;
	}

	private InputStream fUrlCon(String strUrl) {
		InputStream retvalue = null;
		try {
			// URL url = new URL("http", strUrl, 5167, "");
			URL url = new URL(strUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			retvalue = con.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("fUrlCon", e.getMessage());
			retvalue = null;
		}
		return retvalue;
	}

	private String okuStream(InputStream in) {
		BufferedReader reader = null;
		String line = "Hata";
		try {
			reader = new BufferedReader(new InputStreamReader(in));

			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			line = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("readStream", e.getMessage());
					e.printStackTrace();
				}
			}

		}
		return line;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		Log.w("SenkronHttp", "onCancelled HttpDownload");

	}

	public static BasicNameValuePair BitmapNameValueGetir(String bmAd, Bitmap bm) {

		BasicNameValuePair retValue = null;
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
			byte[] ba = bao.toByteArray();
			String ba1 = Base64.encodeToString(ba, 0);
			retValue = new BasicNameValuePair(bmAd, ba1);
		} finally {
		}

		return retValue;
	}
}
