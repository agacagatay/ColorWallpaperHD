package com.taylak.wallpaper.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Gtool {

	private static UncaughtExceptionHandler sDefaultThreadHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
			System.exit(10);
		}
	};

	public static void genelHataYakala() {
		try {
			Thread.currentThread().setDefaultUncaughtExceptionHandler(
					sDefaultThreadHandler);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void mesaj(Context ctx, String mesaj) {
		Toast.makeText(ctx, mesaj, Toast.LENGTH_SHORT).show();
	}

	public static String saatGetir() {
		DateFormat format = SimpleDateFormat.getTimeInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());
		return format.format(new Date());
	}

	private static MediaPlayer mpMusic = null;

	public static void UyariSesCal(Context ctx, int _Rid) {

		if (mpMusic != null && mpMusic.isPlaying()) {
			mpMusic.stop();
			mpMusic = null;
		}

		mpMusic = MediaPlayer.create(ctx, _Rid);
		mpMusic.start();

	}


	public static int BUFFER_SIZE = 1024 * 4;

	public static void zip(String[] files, String zipFile) throws IOException {
		BufferedInputStream origin = null;
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile)));
		try {
			byte data[] = new byte[BUFFER_SIZE];

			for (int i = 0; i < files.length; i++) {
				FileInputStream fi = new FileInputStream(files[i]);
				origin = new BufferedInputStream(fi, BUFFER_SIZE);
				try {
					ZipEntry entry = new ZipEntry(files[i].substring(files[i]
							.lastIndexOf("/") + 1));
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
						out.write(data, 0, count);
					}
				} finally {
					origin.close();
				}
			}
		} finally {
			out.close();
		}
	}

	public static void unzip(String zipFile, String location)
			throws IOException {
		int size;
		byte[] buffer = new byte[BUFFER_SIZE];

		try {
			if (!location.endsWith("/")) {
				location += "/";
			}
			File f = new File(location);
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			ZipInputStream zin = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(zipFile), BUFFER_SIZE));
			try {
				ZipEntry ze = null;
				while ((ze = zin.getNextEntry()) != null) {
					String path = location + ze.getName();
					File unzipFile = new File(path);

					if (ze.isDirectory()) {
						if (!unzipFile.isDirectory()) {
							unzipFile.mkdirs();
						}
					} else {
						// check for and create parent directories if they don't
						// exist
						File parentDir = unzipFile.getParentFile();
						if (null != parentDir) {
							if (!parentDir.isDirectory()) {
								parentDir.mkdirs();
							}
						}

						// unzip the file
						FileOutputStream out = new FileOutputStream(unzipFile,
								false);
						BufferedOutputStream fout = new BufferedOutputStream(
								out, BUFFER_SIZE);
						try {
							while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
								fout.write(buffer, 0, size);
							}

							zin.closeEntry();
						} finally {
							fout.flush();
							fout.close();
						}
					}
				}
			} finally {
				zin.close();
			}
		} catch (Exception e) {
			Log.e("unzip", "Unzip exception", e);
		}
	}

}
