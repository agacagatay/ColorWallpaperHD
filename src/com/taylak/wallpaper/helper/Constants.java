package com.taylak.wallpaper.helper;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class Constants {
	public static final String[] IMAGESs = new String[] {
		
	};

	public static String[] arrbtnMenu;

	public static final String HOST = "http://wallpaper.taylak.com/";
	
	
	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	
	
	
	
}
