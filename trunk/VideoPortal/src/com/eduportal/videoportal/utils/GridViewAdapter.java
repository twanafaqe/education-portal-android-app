package com.eduportal.videoportal.utils;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.eduportal.videoportal.R;
import com.eduportal.videoportal.classes.Packet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private final ArrayList<Packet> mNewPacketList;

	public GridViewAdapter(Context context, ArrayList<Packet> packetArray) {
		this.context = context;
		this.mNewPacketList = packetArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			gridView = inflater.inflate(R.layout.grid_view_cell_frame, null);

			((TextView) gridView.findViewById(R.id.textViewBaslik)).setText(mNewPacketList.get(position).getTitle());
			((TextView) gridView.findViewById(R.id.TextViewAdet)).setText(mNewPacketList.get(position).getAdet());
			((TextView) gridView.findViewById(R.id.TextViewIzleme)).setText(mNewPacketList.get(position).getIzleme());
			
			 AQuery  aq= new AQuery(gridView);
	            aq.id(R.id.imageViewGrig).image(mNewPacketList.get(position).getImage(),true,false);

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return mNewPacketList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}