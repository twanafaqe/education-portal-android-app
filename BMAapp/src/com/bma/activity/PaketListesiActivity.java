package com.bma.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bma.activity.R;
import com.bma.classes.Packet;
import com.bma.webservis.HttpRead;
import com.bma.utils.CustomListAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PaketListesiActivity extends Activity {
	Intent intentPaketAct;
	LinearLayout paket;
	ListView lv;

	private static final String READ_PACKET_URL = "http://json.marifane.com/packets.php";

	// testing from a real server:
	// private static final String READ_COMMENTS_URL =
	// "http://www.mybringback.com/webservice/comments.php";

	// JSON IDS:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_PACKET = "packet";
	private static final String TAG_PACKET_ID = "packet_id";
	private static final String TAG_TITLE = "paket_adi";
	private static final String TAG_CATEGORY = "kategori";
	private static final String TAG_USERNAME = "egitmen";
	private static final String TAG_IMAGE_URL = "resim";
	private static final String TAG_POST_PAGE = "page";

	// it's important to note that the message is both in the parent branch of
	// our JSON tree that displays a "Post Available" or a "No Post Available"
	// message,
	// and there is also a message for each individual post, listed under the
	// "posts"
	// category, that displays what the user typed as their message.

	// An array of all of our comments
	private JSONArray mPacketJsonArray = null;
	// manages all of our comments in a list.
	private ArrayList<Packet> mPacketArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_packet);
		
		lv = (ListView) findViewById(R.id.listView1);
		//new LoadPackets().execute("1");
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Toast.makeText(PaketListesiActivity.this, arg2+"",
						Toast.LENGTH_LONG).show();
				
			}
		});
		/*
		 * paket = (LinearLayout) findViewById(R.id.lpaket1);
		 * paket.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub intentPaketAct = new Intent(getApplicationContext(),
		 * PaketIcerikListesiActivity.class); startActivity(intentPaketAct); }
		 * });
		 */
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask
		new LoadPackets().execute("1");
	}

	private ProgressDialog pDialog;

	public class LoadPackets extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PaketListesiActivity.this);
			pDialog.setMessage("Loading Comments...");
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
			if (result != null) {
				Toast.makeText(PaketListesiActivity.this, result,
						Toast.LENGTH_LONG).show();
				updatePacketList();
			}

		}

	}

	private String updatePacketListData(String pageNum) {
		// TODO Auto-generated method stub

		int success = 0;

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mPacketArrayList = new ArrayList<Packet>();

		// Bro, it's time to power up the J parser
		HttpRead httpread = new HttpRead();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(TAG_POST_PAGE, pageNum));

		Log.d("request!", "starting");
		// getting product details by making HTTP request
		JSONObject json = httpread.makeHttpRequest(READ_PACKET_URL, "POST",
				params);
		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail."
			// (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				mPacketJsonArray = json.getJSONArray(TAG_PACKET);

				// looping through all posts according to the json object
				// returned
				for (int i = 0; i < mPacketJsonArray.length(); i++) {
					JSONObject c = mPacketJsonArray.getJSONObject(i);

					// gets the content of each tag
					Packet mPaket = new Packet();
					String id = c.getString(TAG_PACKET_ID);
					String title = c.getString(TAG_TITLE);
					String category = c.getString(TAG_CATEGORY);
					String username = c.getString(TAG_USERNAME);
					String image = c.getString(TAG_IMAGE_URL);

					mPaket.setId(id);
					mPaket.setTitle(title);
					mPaket.setCategory(category);
					mPaket.setUsername(username);
					mPaket.setImage(image);
					// adding HashList to ArrayList
					mPacketArrayList.add(mPaket);

					// annndddd, our JSON data is up to date same with our
					// array
					// list
				}
			} else {

				Packet mPaket = new Packet("");

				// adding HashList to ArrayList
				mPacketArrayList.add(mPaket);

			}
			return json.getString(TAG_MESSAGE);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "Did not go well..";

	}

	private void updatePacketList() {
		// TODO Auto-generated method stub
		
		CustomListAdapter mListAdapter = new CustomListAdapter(PaketListesiActivity.this, mPacketArrayList);
		lv.setAdapter(mListAdapter);
		

	}

}
