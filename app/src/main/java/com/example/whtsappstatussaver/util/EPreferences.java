package com.example.whtsappstatussaver.util;

import android.content.Context;
import android.content.SharedPreferences;

public class EPreferences {
    private static final String PREF_NAME = "slideshow_pref";
	private SharedPreferences m_csPref;
	private static final int MODE_PRIVATE = 0;

	public static  final String PREF_KEY_WHATSAPP_STORAGE_PERMISSION = "pref_key_whatsapp_storage_permission";


	public static EPreferences getInstance(Context context) {
		try {
			return new EPreferences(context, PREF_NAME, MODE_PRIVATE);
		} catch (Exception e){

		}

		return null;
	}

	private EPreferences(final Context context, final String pref_name, final int mode) {
		super();
		this.m_csPref = context.getSharedPreferences(pref_name, mode);
	}

	public String getString(final String key, final String defaultValue) {
		return this.m_csPref.getString(key, defaultValue);
	}

	public void setString(String key, String value) {
		final SharedPreferences.Editor edit = this.m_csPref.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public int getInt(final String key, final int defaultValue) {
		return this.m_csPref.getInt(key, defaultValue);
	}

	public void clear(String key) {
		m_csPref.edit().remove(key).commit();
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		return this.m_csPref.getBoolean(key, defaultValue);
	}

	public int putBoolean(final String key, final boolean defaultValue) {
		final SharedPreferences.Editor edit = this.m_csPref.edit();
		edit.putBoolean(key, defaultValue);
		edit.commit();
		return 0;
	}

	public int putInt(final String key, final int defaultValue) {
		final SharedPreferences.Editor edit = this.m_csPref.edit();
		edit.putInt(key, defaultValue);
		edit.commit();
		return 0;
	}

	public int putString(final String key, final String defaultValue) {
		final SharedPreferences.Editor edit = this.m_csPref.edit();
		edit.putString(key, defaultValue);
		edit.commit();
		return 0;
	}
}
