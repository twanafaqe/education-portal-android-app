package com.eduportal.videoportal.utils;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.eduportal.videoportal.R;
import com.eduportal.videoportal.classes.Packet;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<Packet> listData;
	private AQuery aq;
	private LayoutInflater layoutInflater;

	public CustomListAdapter(Context context, ArrayList<Packet> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.oltitle = (TextView) convertView.findViewById(R.id.ltitle);
			holder.olusername = (TextView) convertView
					.findViewById(R.id.lusername);
			holder.olcategory = (TextView) convertView
					.findViewById(R.id.lcategory);
			holder.olimage = (ImageView) convertView.findViewById(R.id.limage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Packet newsPacket = (Packet) listData.get(position);

		holder.oltitle.setText(newsPacket.getTitle());
		holder.olusername.setText("By : " + newsPacket.getUsername());
		holder.olcategory.setText("Cat : " + newsPacket.getCategory());
		aq = new AQuery(convertView);
		if (holder.olimage != null) {

			holder.olimage.setImageDrawable(holder.olimage.getContext()
					.getResources().getDrawable(R.drawable.list_placeholder));
			
			aq.id(holder.olimage.getId()).image(newsPacket.getImage(),true,true);
			 //new ImageDownloaderTask(holder.olimage).execute(newsPacket.getImage());
		}

		return convertView;
	}

	static class ViewHolder {
		TextView oltitle;
		TextView olusername;
		TextView olcategory;
		ImageView olimage;
	}
}
