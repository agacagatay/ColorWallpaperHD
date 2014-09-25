package com.taylak.wallpaper.helper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Zaman {

 

public static String Now() 	{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh-mm-ss");
		
	    String strTime = sdf.format(now);
	    System.out.println("Time: " + strTime);
	    return strTime;
	}




	public static Date convertToDate(String tarih_zaman) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh-mm-ss");
		ParsePosition pos = new ParsePosition(0);
		Date d = dateFormat.parse(tarih_zaman, pos);
		return d;
	}

	public static long Subtract(String dt1, String dt2)
	{
		long l1 = convertToDate(dt1).getTime();
		long l2 = convertToDate(dt2).getTime();
	
		return (l1-l2) / 1000;
	}
	
	public static  void  islem(){
		
		Calendar cal = Calendar.getInstance();  
		cal.setTime(convertToDate(Now()));  
				 Log.d("tarih",cal.getTime().toString());
		cal.add(Calendar.DATE, 60); // add 10 days  
		
		 Log.d("tarih",cal.getTime().toString());
		 
	}
}
