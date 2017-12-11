package com.kidstune.video.centras.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidstune.video.centras.R;
import com.kidstune.video.centras.activity.Tab5_Activity;
import com.kidstune.video.centras.data.Main_Data;
import com.kidstune.video.centras.util.ImageLoader;


public class Tab5Adapter extends BaseAdapter{
	public Context context;
	public ImageLoader imgLoader;
	public String num = "empty";
	public Cursor cursor;
	public ImageButton bt_favorite;
	public ArrayList<Main_Data> list;
	public ListView listview_main;
	public Tab5Adapter(Context context, ArrayList<Main_Data> list, ListView listview_main) {
		this.imgLoader = new ImageLoader(context.getApplicationContext());
		this.context = context;
		this.list = list;
		this.listview_main = listview_main;
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

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		try{
			if(view == null){	
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.activity_tab_listrow, parent, false); 
			}
			ImageView img_thumb = (ImageView)view.findViewById(R.id.img_thumb);
			img_thumb.setFocusable(false);
			imgLoader.DisplayImage(list.get(position).thumb, R.drawable.no_image, img_thumb);

			TextView txt_subject = (TextView)view.findViewById(R.id.txt_subject);
			txt_subject.setText(list.get(position).subject);
			txt_subject.setTextColor(Color.BLACK);
			
			bt_favorite = (ImageButton)view.findViewById(R.id.bt_favorite);
			bt_favorite.setFocusable(false);
			bt_favorite.setSelected(false);
			cursor = Tab5_Activity.favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where num = '"+list.get(position).num+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				num = cursor.getString(cursor.getColumnIndex("num"));
			}else{
				num = "empty";
				
			}
			if(num.equals("empty")){
				bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
			}else{
				bt_favorite.setImageResource(R.drawable.bt_favorite_pressed);	
			}
			
			bt_favorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					cursor = Tab5_Activity.favorite_mydb.getReadableDatabase().rawQuery(
							"select * from favorite_list where num = '"+list.get(position).num+"'", null);
					if(null != cursor && cursor.moveToFirst()){
						num = cursor.getString(cursor.getColumnIndex("num"));
					}else{
						num = "empty";
					}
					if(num.equals("empty")){
						bt_favorite.setImageResource(R.drawable.bt_favorite_pressed);
						ContentValues cv = new ContentValues();
						cv.put("id", list.get(position).id);
	    				cv.put("num", list.get(position).num);
	    				cv.put("subject", list.get(position).subject);
	    				cv.put("thumb", list.get(position).thumb);
	    				cv.put("portal", list.get(position).portal);
						Tab5_Activity.favorite_mydb.getWritableDatabase().insert("favorite_list", null, cv);
						if(Tab5_Activity.adapter != null){
							Tab5_Activity.adapter.notifyDataSetChanged();
						}
						Toast.makeText(context, context.getString(R.string.txt_main_activity1), Toast.LENGTH_SHORT).show();
					}else{
						bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
						Tab5_Activity.favorite_mydb.getWritableDatabase().delete("favorite_list", "num" + "=" +num, null);
						if(Tab5_Activity.adapter != null){
							Tab5_Activity.adapter.notifyDataSetChanged();
						}
						Toast.makeText(context, context.getString(R.string.txt_main_activity2), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}catch (Exception e) {
		}finally{
			Tab5_Activity.favorite_mydb.close();
			if(cursor != null){
				cursor.close();	
			}
		}
		return view;
	}
}