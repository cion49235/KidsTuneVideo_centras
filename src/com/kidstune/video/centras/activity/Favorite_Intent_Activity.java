package com.kidstune.video.centras.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.kidstune.video.centras.R;
import com.kidstune.video.centras.data.Favorite_Data;
import com.kidstune.video.centras.db.Favorite_DBopenHelper;
import com.kidstune.video.centras.util.ImageLoader;

public class Favorite_Intent_Activity extends SherlockActivity implements OnItemClickListener, OnClickListener, AdViewListener{
	public Favorite_DBopenHelper favorite_mydb;
	public SQLiteDatabase mdb;
	public Cursor cursor;
	public ConnectivityManager connectivityManger;
	public NetworkInfo mobile;
	public NetworkInfo wifi;
	public static ListView listview_favorite;
	public FavoriteAdapter<Favorite_Data> adapter;
	public static LinearLayout layout_listview_favorite, layout_nodata;
	public int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public static RelativeLayout ad_layout;
	public Context context;
	private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_favorite);
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD, "AX0004B36");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD_FULL, "AX0004B37");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_SHALLWE, "4wSC9xA6l_fwOJDgXNn_JA");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "zkl206g4");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/8604112566");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1080845761");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_CAULY, "4qW0j8xS");
		addBannerView();
		
		context = this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mActionBar = getSupportActionBar();
		mActionBar.setTitle(context.getString(R.string.txt_favorite_intent_activity1));
		mActionBar.setDisplayShowHomeEnabled(false);
//		mActionBar.setDisplayShowTitleEnabled(false);
		
		layout_listview_favorite = (LinearLayout)findViewById(R.id.layout_listview_favorite);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		listview_favorite = (ListView)findViewById(R.id.listview_favorite);
		favorite_mydb = new Favorite_DBopenHelper(this);

		displayList();
	}	
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("zkl206g4");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		displayList();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(favorite_mydb != null){
			favorite_mydb.close();
		}
	}
	
	
	public void displayList(){
		List<Favorite_Data>contactsList = getContactsList();
		adapter = new FavoriteAdapter<Favorite_Data>(
				this, R.layout.activity_intent_favorite_listrow, contactsList);
		
		listview_favorite.setAdapter(adapter);
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ //허니콤 버전에서만 실행 가능한 API 사용}
			listview_favorite.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    	}
		listview_favorite.setOnItemClickListener(this);
		listview_favorite.setFastScrollEnabled(false);
		listview_favorite.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		if(listview_favorite.getCount() == 0){
			layout_nodata.setVisibility(View.VISIBLE);
			layout_listview_favorite.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.GONE);
			layout_listview_favorite.setVisibility(View.VISIBLE);
		}
	}
	
	public List<Favorite_Data> getContactsList() {
		List<Favorite_Data>contactsList = new ArrayList<Favorite_Data>();
		try{
			favorite_mydb = new Favorite_DBopenHelper(this);
			mdb = favorite_mydb.getWritableDatabase();
	        cursor = mdb.rawQuery("select * from favorite_list order by _id desc", null);
	        while (cursor.moveToNext()){
				addContact(contactsList,cursor.getInt(0), cursor.getString(1), cursor.getString(2), 
						cursor.getString(3),cursor.getString(4), cursor.getString(5));
	        }
		}catch (Exception e) {
		}finally{
			cursor.close();
			favorite_mydb.close();
			mdb.close();
		}
		return contactsList;
	}
	
	public void addContact(List<Favorite_Data> contactsList, int _id, String id,String num,String subject,String thumb,String portal){
		contactsList.add(new Favorite_Data(_id, id, num, subject, thumb, portal));
	}
	
	@Override
	public void onClick(View view) {
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Favorite_Data favorite_data = (Favorite_Data)adapter.getItem(position);
		String id = favorite_data.getId();
		String num = favorite_data.getNum();
		String subject = favorite_data.getSubject();
		String thumb = favorite_data.getThumb();
		String portal = favorite_data.getPortal();
		
		Intent intent = new Intent(this, SubActivity.class);
		intent.putExtra("num", num);
		intent.putExtra("subject", subject);
		intent.putExtra("thumb", thumb);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		
	}
	
	public class FavoriteAdapter<T extends Favorite_Data>extends ArrayAdapter<T>{
		public List<T> contactsList;
		ImageLoader imgLoader = new ImageLoader(context);
		public ImageButton bt_favorite_delete;
		public String num = "empty";
		public FavoriteAdapter(Context context, int textViewResourceId, List<T> items) {
			super(context, textViewResourceId, items);
			contactsList = items;
		}
		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			try{
				if(view == null){
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = vi.inflate(R.layout.activity_favorite_listrow, null);
				}
				final T contacts = contactsList.get(position);
				if (contacts != null) {
					TextView txt_favorite_subject = (TextView)view.findViewById(R.id.txt_favorite_subject);
					txt_favorite_subject.setText(contacts.getSubject());
					txt_favorite_subject.setTextColor(Color.BLACK);
					
					ImageView img_favorite_thumb = (ImageView)view.findViewById(R.id.img_favorite_thumb);
					img_favorite_thumb.setFocusable(false);
					String image_url = contacts.getThumb();
					imgLoader.DisplayImage(image_url, R.drawable.no_image, img_favorite_thumb);
				}
				
				bt_favorite_delete = (ImageButton)view.findViewById(R.id.bt_favorite_delete);
				bt_favorite_delete.setFocusable(false);
				bt_favorite_delete.setSelected(false);
				bt_favorite_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try{
							favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" +contacts.get_id(), null);
							favorite_mydb.close();
							displayList();
							Toast.makeText(context, context.getString(R.string.txt_favorite_activity1), Toast.LENGTH_SHORT).show();							
						}catch(Exception e){
						}
					}
				});
			}catch (Exception e) {
			}
			return view;
		}
	}
	
	//** BannerAd 이벤트들 *************
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
	}

	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}