package com.example.whtsappstatussaver.util;


public class Log {

	public static boolean isDebugMode = true;

	// log some informationF
	public static void i(String tag, String message) {
		if (isDebugMode) {
			android.util.Log.i(tag, "" + message);
		}
	}

	// log some error
	public static void e(String tag, String message) {
		if (isDebugMode) {
			android.util.Log.e(tag, "" + message);
		}
	}

	// log some waring
	public static void w(String tag, String message) {
		if (isDebugMode) {
			android.util.Log.w(tag, "" + message);
		}
	}

	// log verbose
	public static void v(String tag, String message) {
		if (isDebugMode) {
			android.util.Log.v(tag, "" + message);
		}
	}

	// log some debug info
	public static void d(String tag, String message) {
		if (!isDebugMode) {
			android.util.Log.d(tag, "" + message);
		}
	}
}
