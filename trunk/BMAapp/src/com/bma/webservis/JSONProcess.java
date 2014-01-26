package com.bma.webservis;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bma.classes.Kullanici;

public class JSONProcess{
	private String JSONString;

	public JSONProcess(String jsonResult) {
		this.JSONString = jsonResult;
	}

	public Kullanici getKullanici() {
		Kullanici mKullanici = new Kullanici();
		try {
			JSONObject object = new JSONObject(JSONString);
			mKullanici.setKullaniciadi(object.getString("uname"));
			mKullanici.setSifre(object.getString("upass"));
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i(JSONString, JSONString);
		}

		return mKullanici;
	}

}
