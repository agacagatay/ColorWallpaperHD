package com.taylak.wallpaper.helper;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

public final class Referanslar {

	static SharedPreferences sp;
	static SharedPreferences.Editor spEdit;
	static SharedPreferences getprefs;

	private static String spSabitler = "spSabitlerXML";

	private static Context ctx;

	public static void ReferanslarConst(Context _ctx) {

		ctx = _ctx;

		sp = ctx.getSharedPreferences(spSabitler,
				android.content.Context.MODE_PRIVATE);
	//	getprefs = PreferenceManager.getDefaultSharedPreferences(ctx);

	}

	private static Hashtable<String, List<ImageItem>> GRUPLAR;

	public static Hashtable<String, List<ImageItem>> getGRUPLAR() {
		
		if (GRUPLAR == null) {
			String grupJson = sp.getString("gruplarXML", "");
			if (grupJson.equalsIgnoreCase("")) {
				GRUPLAR = new Hashtable<String, List<ImageItem>>();
			} else {
				GsonBuilder gsonb = new GsonBuilder();
				Gson gson = gsonb.create();
				Type grupType = new TypeToken<Hashtable<String, List<ImageItem>>>() {
				}.getType();

				GRUPLAR = gson.fromJson(grupJson, grupType);

			}

		}

		return GRUPLAR;

	}

	public static void setGRUPLAR(Hashtable<String, List<ImageItem>> _gruplar) {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();

		Type grupType = new TypeToken<Hashtable<String, List<ImageItem>>>() {
		}.getType();

		String grupJson = gson.toJson(_gruplar, grupType);

		spEdit = sp.edit();
		spEdit.putString("gruplarXML", grupJson);
		spEdit.commit();
	}

}
