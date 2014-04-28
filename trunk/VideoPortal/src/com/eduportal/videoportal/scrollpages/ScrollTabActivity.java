package com.eduportal.videoportal.scrollpages;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.eduportal.videoportal.R;
import com.eduportal.videoportal.classes.Lecture;
import com.eduportal.videoportal.classes.Packet;
import com.eduportal.videoportal.servis.HttpRead;
import com.eduportal.videoportal.utils.SessionManager;

public class ScrollTabActivity extends FragmentActivity {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    Intent intent;
	Bundle bundle; 
    private static final String READ_LECTURE_URL = "http://json.marifane.com/lectures.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_LECTURE = "lecture";
	private static final String TAG_ISIM = "isim";
	private static final String TAG_DETAY = "detay";
	private static final String TAG_VIDEO_URL = "video";	
	private static final String TAG_IMAGE_URL = "resim";
	private static final String TAG_SURE = "sure";

	private JSONArray mLectureJsonArray = null;
		
	private static ArrayList<Lecture> mLecturesArrayList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolltab_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		intent = getIntent();
		bundle = intent.getExtras(); 
		String pid = bundle.getString("pid");
        
		SessionManager session = new SessionManager(getApplicationContext());
	    session.checkLogin();
        new LoadPackets().execute(pid);
    }
    
	private ProgressDialog pDialog;

	public class LoadPackets extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ScrollTabActivity.this);
			pDialog.setMessage("Loading items...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg) {

			String mesage = updatePacketListData(arg[0]);

			return mesage;

		}

		@Override
		protected void onPostExecute(String result) {

			pDialog.dismiss();
			
			mViewPager = (ViewPager) findViewById(R.id.pager);
	        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
			mViewPager.setAdapter(mDemoCollectionPagerAdapter);

		}

	}
	
	private String updatePacketListData(String pageNum) {
		// TODO Auto-generated method stub

		int success = 0;

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mLecturesArrayList = new ArrayList<Lecture>();

		// Bro, it's time to power up the J parser
		HttpRead httpread = new HttpRead();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pid", pageNum));

		Log.d("request!", "starting");
		// getting product details by making HTTP request
		JSONObject json = httpread.makeHttpRequest(READ_LECTURE_URL, "POST",
				params);
		
		try {
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				mLectureJsonArray = json.getJSONArray(TAG_LECTURE);

				for (int i = 0; i < mLectureJsonArray.length(); i++) {
					JSONObject c = mLectureJsonArray.getJSONObject(i);

					// gets the content of each tag
					Lecture mLecture = new Lecture();
					String isim = c.getString(TAG_ISIM);
					String detay = c.getString(TAG_DETAY);
					String resim = c.getString(TAG_IMAGE_URL);
					String video = c.getString(TAG_VIDEO_URL);
					String sure = c.getString(TAG_SURE);

					mLecture.setIsim(isim);
					mLecture.setDetay(detay);
					mLecture.setResim(resim);
					mLecture.setVideo(video);
					mLecture.setSure(sure);
					
					mLecturesArrayList.add(mLecture);

					
				}
			} else {

				Lecture mLecture = new Lecture("");

				
				mLecturesArrayList.add(mLecture);
				mLecturesArrayList.add(mLecture);

			}
			return json.getString(TAG_MESSAGE);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Did not go well..";

	}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            
            args.putString(TAG_IMAGE_URL, mLecturesArrayList.get(i).getResim());
            args.putString(TAG_VIDEO_URL, mLecturesArrayList.get(i).getVideo());
            args.putString(TAG_ISIM, mLecturesArrayList.get(i).getIsim());
            args.putString(TAG_DETAY, mLecturesArrayList.get(i).getDetay());
            args.putString(TAG_SURE, mLecturesArrayList.get(i).getSure());
            
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mLecturesArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Ders " + (position + 1);
        }
    }

    public static class DemoObjectFragment extends Fragment {
    	Bundle args;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.scrolltab_frame_layout, container, false);
            args = getArguments();
            AQuery  aq= new AQuery(rootView);
            aq.id(R.id.imageButtonVideo).image(args.getString(TAG_IMAGE_URL),true,false);

            ((TextView) rootView.findViewById(R.id.textViewDersinAdi)).setText(args.getString(TAG_ISIM));
            ((TextView) rootView.findViewById(R.id.textViewDersinKonusu)).setText(args.getString(TAG_DETAY));
            ((TextView) rootView.findViewById(R.id.textViewDersinSuresi)).setText(args.getString(TAG_SURE));
            
            ((ImageButton) rootView.findViewById(R.id.imageButtonVideo)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent inPlayer = new Intent(getActivity(),Player.class);
					Bundle bundle = new Bundle(); 
					bundle.putString("url", args.getString(TAG_VIDEO_URL));
					inPlayer.putExtras(bundle);
					startActivity(inPlayer);
				}
			});
            
            
            return rootView;
        }
    }
    
    
    
}