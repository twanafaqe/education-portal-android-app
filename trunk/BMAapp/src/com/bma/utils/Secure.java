package com.bma.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

@SuppressLint("NewApi")
public class Secure {

	private static final String TAG = Secure.class.getSimpleName();

	public Secure(PackageManager pm, String packageName, Context context) {

		try {
			PackageInfo packageInfo = null;
			try {
				packageInfo = pm.getPackageInfo(packageName, 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			// install datetime
			String appInstallDate = DateUtils.getDate(
					"yyyy/MM/dd hh:mm:ss.SSS", packageInfo.lastUpdateTime);

			// build datetime
			String appBuildDate = DateUtils.getDate("yyyy/MM/dd hh:mm:ss.SSS",
					DateUtils.getBuildDate(context));

			Log.e(TAG, "appBuildDate = " + appBuildDate);
			Log.e(TAG, "appInstallDate = " + appInstallDate);

		} catch (Exception e) {
		}

	}

	static class DateUtils {

		public static String getDate(String dateFormat) {
			Calendar calendar = Calendar.getInstance();
			return new SimpleDateFormat(dateFormat, Locale.getDefault())
					.format(calendar.getTime());
		}

		public static String getDate(String dateFormat, long currenttimemillis) {
			return new SimpleDateFormat(dateFormat, Locale.getDefault())
					.format(currenttimemillis);
		}

		public static long getBuildDate(Context context) {

			try {
				ApplicationInfo ai = context.getPackageManager()
						.getApplicationInfo(context.getPackageName(), 0);
				ZipFile zf = new ZipFile(ai.sourceDir);
				ZipEntry ze = zf.getEntry("classes.dex");
				long time = ze.getTime();

				return time;

			} catch (Exception e) {
			}

			return 0l;
		}

	}
}
