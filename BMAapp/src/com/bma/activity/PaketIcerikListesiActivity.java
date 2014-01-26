package com.bma.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bma.activity.R;

public class PaketIcerikListesiActivity extends Activity {
	Intent intentPaketIcerikAct;
	LinearLayout paketicerik;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paket_icerik_listesi);
		paketicerik = (LinearLayout) findViewById(R.id.lpaketicerik1);
		paketicerik.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentPaketIcerikAct = new Intent(getApplicationContext(),
						PaketIcerikActivity.class);
				startActivity(intentPaketIcerikAct);
			}
		});
	}

}
