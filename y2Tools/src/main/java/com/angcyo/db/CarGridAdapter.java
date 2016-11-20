package com.angcyo.db;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.angcyo.y2tools.R;

public class CarGridAdapter extends BaseAdapter {
	private Context context;
	private List<CarGridListItem> list;

	public CarGridAdapter(Context context, List<CarGridListItem> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public CarGridAdapter() {
		// TODO Auto-generated constructor stub
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

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.grid_item_simple, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.img_car_logo);
			holder.text = (TextView) convertView
					.findViewById(R.id.text_car_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CarGridListItem item = list.get(position);
		holder.img.setImageBitmap(item.getLogo());
		// holder.img.setBackgroundResource(R.drawable.window_bg);
		holder.text.setText(item.getName());

		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.grid_item_anim);
		animation.setStartOffset(20 * position);
		convertView.startAnimation(animation);

		// Log.i("GridAdapter", item.name);

		// (Integer.valueOf(123)).byteValue();
		return convertView;
	}

	private static class ViewHolder {
		ImageView img;
		TextView text;
	}

}
