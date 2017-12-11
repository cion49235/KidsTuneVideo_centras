package com.kidstune.video.centras.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kidstune.video.centras.R;
import com.kidstune.video.centras.data.Ani_Sub_Data;
import com.kidstune.video.centras.util.FadingActionBarHelperBase;
import com.kidstune.video.centras.util.ImageLoader;


public class SubAdapter extends BaseAdapter{
	public Context context;
//	public ListView listview_sub;
	public ImageLoader imgLoader;
	public SharedPreferences settings,pref;
	public Editor edit;
	ArrayList<Ani_Sub_Data> list;
	public SubAdapter(Context context,ArrayList<Ani_Sub_Data> list, ListView listview_sub) {
		this.imgLoader = new ImageLoader(context.getApplicationContext());
		this.context = context;
		this.list = list;
//		this.listview_sub = listview_sub;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		try{
			pref = context.getSharedPreferences(context.getString(R.string.txt_pref), Activity.MODE_PRIVATE);
			String array_subject= pref.getString("array_subject", "");
			String array_videoid = pref.getString("array_videoid", "");
			
			if(view == null){	
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.activity_sub_listrow, parent, false); 
			}
			
			ImageView sub_img_thumb = (ImageView)view.findViewById(R.id.sub_img_thumb);
			sub_img_thumb.setFocusable(false);
			imgLoader.DisplayImage(list.get(position).thumb, R.drawable.no_image, sub_img_thumb);
			
			TextView sub_txt_subject = (TextView)view.findViewById(R.id.sub_txt_subject);
			if(sub_txt_subject != null){
				sub_txt_subject.setText(list.get(position).subject);
				if(list.get(position).subject.equals(array_subject) && list.get(position).videoid.equals(array_videoid)){
					sub_txt_subject.setTextColor(Color.RED);
				}else{
					sub_txt_subject.setTextColor(Color.BLACK);
				}
			}
			LinearLayout layout_sublistrow = (LinearLayout)view.findViewById(R.id.layout_sublistrow);
			if(FadingActionBarHelperBase.listview_sub.isItemChecked(position+1)){
//				view.setBackgroundColor(Color.parseColor("#ddffaa"));
//				layout_sublistrow.setBackground(context.getResources().getDrawable(R.drawable.listrow_color_green));
				layout_sublistrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.listrow_color_green));
			}else{
//				view.setBackgroundColor(Color.parseColor("#00000000"));
//				layout_sublistrow.setBackground(context.getResources().getDrawable(R.drawable.listrow_color_black));
				layout_sublistrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.listrow_color_black));
			}
		
		}catch (Exception e) {
		}
		return view;
	}
	
}