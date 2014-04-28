package com.eduportal.videoportal.DrawerMenu.Fragment;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduportal.videoportal.R;
import com.eduportal.videoportal.DrawerMenu.Fragment.DrawerMyPacketFragment.LoadPackets;
import com.eduportal.videoportal.classes.Packet;
import com.eduportal.videoportal.scrollpages.ScrollTabActivity;
import com.eduportal.videoportal.servis.HttpRead;
import com.eduportal.videoportal.utils.GridViewAdapter;
import com.eduportal.videoportal.utils.SessionManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DrawerHomeFragment extends Fragment {
	Intent intentPaketAct;
	GridView paketTablo;
	private EditText user, pass;
	private Button mSubmit;
	private boolean loginIN;
	private static final String READ_NEWS_URL = "http://json.marifane.com/news.php";
	private static final String LOGIN_URL = "http://json.marifane.com/login.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_PACKET = "packet";
	private static final String TAG_PACKET_ID = "packet_id";
	private static final String TAG_TITLE = "paket_adi";
	private static final String TAG_CATEGORY = "kategori";
	private static final String TAG_USERNAME = "egitmen";
	private static final String TAG_IMAGE_URL = "resim";
	private static final String TAG_DERS_ADET = "adet";
	private static final String TAG_IZLEME = "izleme";
	private static final String TAG_POST_PAGE = "page";

	private JSONArray mPacketJsonArray = null;
		
	private ArrayList<Packet> mPacketArrayList;
	private SessionManager session;
	
	public DrawerHomeFragment(){}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
     View rootView;
     session = new SessionManager(getActivity());
     //session.checkLogin();
     
	if(session.isLoggedIn()){ 
		rootView = inflater.inflate(R.layout.drawer_home_fragment, container, false);
     }else{	 
    	rootView = inflater.inflate(R.layout.drawer_login_home_fragment, container, false);

     }
       
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(session.isLoggedIn()){		
			new LoadPackets().execute("1");
			paketTablo = (GridView) getActivity().findViewById(R.id.yeniPaketler);
			paketTablo.setOnItemClickListener(new OnItemClickListener() {

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
					
				}
			});
	    	 
	     }else{
	    	user = (EditText) getActivity().findViewById(R.id.loginEditTextUserName);
	 		pass = (EditText) getActivity().findViewById(R.id.loginEditTextUserPassword);

	 		mSubmit = (Button) getActivity().findViewById(R.id.loginButtonEnter);

	 		mSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					new AttemptLogin().execute();
					
				}
			});
	     }

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
		JSONObject json = httpread.makeHttpRequest(READ_NEWS_URL, "POST",
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
					int adet = c.getInt(TAG_DERS_ADET);
					int izleme = c.getInt(TAG_IZLEME);

					mPaket.setId(id);
					mPaket.setTitle(title);
					mPaket.setCategory(category);
					mPaket.setUsername(username);
					mPaket.setImage(image);
					mPaket.setAdet(Integer.toString(adet));
					mPaket.setIzleme(Integer.toString(izleme));
					
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
		
		GridViewAdapter mListAdapter = new GridViewAdapter(getActivity(), mPacketArrayList);
		paketTablo.setAdapter(mListAdapter);

	}
	
	class AttemptLogin extends AsyncTask<String, String, String> {

		int success = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			String username = user.getText().toString();
			String password = pass.getText().toString();
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				HttpRead httpread = new HttpRead();
				// getting product details by making HTTP request
				JSONObject json = httpread.makeHttpRequest(READ_NEWS_URL, "POST",
						params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());

					session.createLoginSession(username, password);
					session.reStart();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String result) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (result != null) {
			
			}

		}

	}

	
}
