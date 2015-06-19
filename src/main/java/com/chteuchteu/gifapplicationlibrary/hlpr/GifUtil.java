package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GifUtil {
	public static String getHtml(String gifPath) {
		String mainCss = "html, body {height:100%;width: 100%;margin: 0;padding: 0;border: 0;}";
		String html = "";

		if (gifPath.endsWith(".mp4") || gifPath.endsWith(".webm") || gifPath.endsWith(".ogg")) {
			// This is a video
			mainCss += "video { width: 100%; height: 100%; }";
			String fileType = MainUtil.getExtension(gifPath);
			html = "<video autoplay loop muted><source src=\"" + gifPath + "\" type=\"video/" + fileType + "\"></video>";
		} else {
			// This is a gif
			mainCss += ".container{width:100%;height:100%;background-image:url('" + gifPath + "'); background-size:contain; background-repeat:no-repeat;background-position:center;}";
		}

		return "<html><head><style>" + mainCss + "</style></head><body><div class=\"container\">" + html + "</div></body></html>";
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
