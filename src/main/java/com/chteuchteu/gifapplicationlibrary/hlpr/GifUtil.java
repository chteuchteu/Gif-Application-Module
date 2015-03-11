package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GifUtil {
	public static String getHtml(String gifPath) {
		String css = "html, body, #wrapper {height:100%;width: 100%;margin: 0;padding: 0;border: 0;} #wrapper td {vertical-align: middle;text-align: center;} .container{width:100%;height:100%;background-image:url('" + gifPath +"'); background-size:contain; background-repeat:no-repeat;background-position:center;}";
		//String js = "function resizeToMax(id){myImage = new Image();var img = document.getElementById(id);myImage.src = img.src;if(myImage.width / document.body.clientWidth > myImage.height / document.body.clientHeight){img.style.width = \"100%\"; } else {img.style.height = \"100%\";}}";
		//String html = "<html><head><script>" + js + "</script><style>" + css + "</style></head><body><table id=\"wrapper\"><tr><td><img id=\"gif\" src=\""+ imagePath + "\" onload=\"resizeToMax(this.id)\" /></td></tr></table></body></html>";
		return "<html><head><style>" + css + "</style></head><body><div class=\"container\"></div></body></html>";
	}

	@SuppressLint("SimpleDateFormat")
	public static Date stringToDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(date);
	}

	public static long getSecsDiff(Date first, Date latest) {
		return (latest.getTime() - first.getTime()) / 1000;
	}
}
