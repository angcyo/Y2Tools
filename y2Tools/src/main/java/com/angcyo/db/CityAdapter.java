package com.angcyo.db;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.angcyo.y2tools.R;

public class CityAdapter extends BaseAdapter {

	private Context context;
	private List<CityListItem> list;

	public CityAdapter(Context context, List<CityListItem> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.list_item_simple, null);

		TextView item1 = (TextView) rootView.findViewById(R.id.item1);
		TextView item2 = (TextView) rootView.findViewById(R.id.item2);
		TextView item3 = (TextView) rootView.findViewById(R.id.item3);

		CityListItem pItem = list.get(position);
		item1.setText(pItem.getName());
		item2.setText("");
		item3.setText(pItem.getZone());

		return rootView;
	}

}
