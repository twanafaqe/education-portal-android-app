package com.eduportal.videoportal.DrawerMenu.Fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.eduportal.videoportal.classes.Packet;
import com.eduportal.videoportal.servis.HttpRead;
import com.eduportal.videoportal.utils.CustomListAdapter;
import com.eduportal.videoportal.R;
import com.eduportal.videoportal.scrollpages.ScrollTabActivity;

public class DrawerMyPacketFragment extends Fragment {

	Intent intentPaketAct;
	LinearLayout paket;
	ListView lv;

	private static final String READ_PACKET_URL = "http://json.marifane.com/packets.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_PACKET = "packet";
	private static final String TAG_PACKET_ID = "packet_id";
	private static final String TAG_TITLE = "paket_adi";
	private static final String TAG_CATEGORY = "kategori";
	private static final String TAG_USERNAME = "egitmen";
	private static final String TAG_IMAGE_URL = "resim";
	private static final String TAG_POST_PAGE = "page";

	private JSONArray mPacketJsonArray = null;
		
	private ArrayList<Packet> mPacketArrayList;

	public DrawerMyPacketFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.drawer_mypacket_fragment,container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		lv = (ListView) getActivity().findViewById(R.id.listviewLecture);
		
		new LoadPackets().execute("1");
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent inScrollTab = new Intent(getActivity(),ScrollTabActivity.class);
				Bundle bundle = new Bundle(); 
				Packet pk = mPacketArrayList.get(arg2);
				bundle.putString("pid", pk.getId());
				inScrollTab.putExtras(bundle);
				startActivity(inScrollTab);
				//Toast.makeText(getActivity(), pk.getId(),Toast.LENGTH_LONG).show();
				
			}
		});

	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//new LoadPackets().execute("1");
		
	}

	private ProgressDialog pDialog;

	public class LoadPackets extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Your Packet...");
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
				Toast.makeText(getActivity(), result,
						Toast.LENGTH_LONG).show();
				updatePacketList();
			}

		}

	}

	private String updatePacketListData(String pageNum) {
		int success = 0;
		mPacketArrayList = new ArrayList<Packet>();

		HttpRead httpread = new HttpRead();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(TAG_POST_PAGE, pageNum));

		Log.d("request!", "starting");
		JSONObject json = httpread.makeHttpRequest(READ_PACKET_URL, "POST",
				params);
		
		try {

			success = json.getInt(TAG_SUCCESS);
			if (success == 1) {

				mPacketJsonArray = json.getJSONArray(TAG_PACKET);

				for (int i = 0; i < mPacketJsonArray.length(); i++) {
					JSONObject c = mPacketJsonArray.getJSONObject(i);

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
					
					mPacketArrayList.add(mPaket);

				}
			} else {

				Packet mPaket = new Packet("");

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
		
		CustomListAdapter mListAdapter = new CustomListAdapter(getActivity(), mPacketArrayList);
		lv.setAdapter(mListAdapter);

	}

}

