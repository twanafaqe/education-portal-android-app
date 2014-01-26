package com.bma.activity;

import com.bma.activity.R;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity {
	Intent intentPaketlerAct;
	LinearLayout paketlerim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        paketlerim = (LinearLayout) findViewById(R.id.lpaketlerim);
        paketlerim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentPaketlerAct = new Intent(getApplicationContext(),
						PaketListesiActivity.class);
				startActivity(intentPaketlerAct);
			}
		});
     
    }

}
