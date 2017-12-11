package com.kidstune.video.centras.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.kidstune.video.centras.R;
import com.kidstune.video.centras.adapter.SubAdapter;
import com.kidstune.video.centras.data.Ani_Sub_Data;
import com.kidstune.video.centras.util.Crypto;
import com.kidstune.video.centras.util.FadingActionBarHelper;
import com.kidstune.video.centras.util.FadingActionBarHelperBase;
import com.kidstune.video.centras.util.ImageLoader;
import com.kidstune.video.centras.util.Utils;
import com.kidstune.video.centras.videoplayer.CustomVideoPlayer;
import com.skplanet.tad.AdFloating;
import com.skplanet.tad.AdFloatingListener;
import com.skplanet.tad.AdRequest.ErrorCode;
import com.skplanet.tad.AdSlot;
public class SubActivity extends SherlockActivity implements OnItemClickListener, OnClickListener,OnCheckedChangeListener, AdViewListener, AdFloatingListener{
	public static Context context;
	public String num, subject, thumb;
	public static SubAdapter sub_adapter;
//	public static ListView listview_sub;
	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public ArrayList<Ani_Sub_Data> list = new ArrayList<Ani_Sub_Data>();
	public Sub_ParseAsync sub_parseAsync = null;
	public boolean retry_alert = false;
	public static String i;
	public ImageView image_header;
	public FadingActionBarHelper helper;
	public ImageLoader imgLoader;
	private ActionBar mActionBar;
	public ToggleButton bt_toggle_check, bt_toggle_expand;
	public boolean list_expand = false;
	public static RelativeLayout ad_layout;
	public static AdFloating mAdFloating;
	public Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	context = this;
	num = getIntent().getStringExtra("num");
	subject = getIntent().getStringExtra("subject");
	thumb = getIntent().getStringExtra("thumb");
	retry_alert = true;
	mActionBar = getSupportActionBar();
	mActionBar.setTitle(subject);
	mActionBar.setDisplayShowHomeEnabled(false);
//	mActionBar.setDisplayShowTitleEnabled(false);
	load();
	}
	
	public void load() {
		setFadingActionBar();
		image_header = (ImageView) findViewById(R.id.image_header);
		try {
			imgLoader = new ImageLoader(context.getApplicationContext());
			imgLoader.DisplayImage(thumb, R.drawable.no_image, image_header);
		} catch (Exception e) {
		}
		displaylist();
	}
	
	
	
	public void setFadingActionBar() {
		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.profile_image)
				.contentLayout(R.layout.activity_sub);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		helper.initContext(this);
		
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD, "AX0004B36");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD_FULL, "AX0004B37");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_SHALLWE, "4wSC9xA6l_fwOJDgXNn_JA");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "zkl206g4");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/8604112566");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1080845761");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_CAULY, "4qW0j8xS");
		addBannerView();
		create_mAdFloating();
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
	
	public void create_mAdFloating(){
		// AdFloating 객체를 생성합니다. 
		mAdFloating = new AdFloating(this); 
		// AdFloating 상태를 모니터링 할 listner 를 등록합니다. listener 에 대한 내용은 아래 참조 mAdFloating.setListener(mListener);   
		// 할당받은 ClientID 를 설정합니다. 
		mAdFloating.setClientId("AX0004B38");   
		// 할당받은 Slot 번호를 설정합니다.
		mAdFloating.setSlotNo(AdSlot.FLOATING);  
		// TestMode 여부를 설정합니다. 
		mAdFloating.setTestMode(false);  
		// 광고를 삽입할 parentView 를 설정합니다.
		mAdFloating.setParentWindow(getWindow()); 
		// 광고를 요청 합니다. 로드시 설정한 값들이 유효한지 판단한 후 광고를 수신합니다.
		// 광고 요청에 대한 결과는 설정한 listener 를 통해 알 수 있습니다.
		mAdFloating.setListener(this);
		// 일일 광고 노출 제한을 설정합니다.
//		mAdFloating.setDailyFrequency(5);
		if (mAdFloating != null) {
			try{
				mAdFloating.loadAd(null);
			}catch(Exception e){ 
				e.printStackTrace(); 
			} 
		}
		handler.postDelayed(new Runnable() {
			 @Override
			 public void run() {
				 if (mAdFloating.isReady()) {
						try {
							mAdFloating.showAd(180, 200);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			 }
		 },3000);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
//		if(sub_adapter != null){
//			sub_adapter.notifyDataSetChanged();	
//		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
		if(sub_parseAsync != null){
			sub_parseAsync.cancel(true);
		}
		if (mAdFloating != null) {
			mAdFloating.destroyAd();
		}
	}
	
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	if (mAdFloating != null) {
				try {
					mAdFloating.moveAd(320, 50);
				} catch (Exception e) {
				}
			}
	    }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	if (mAdFloating != null) {
				try {
					mAdFloating.moveAd(180, 200);
				} catch (Exception e) {
				}
			}
	    }
	    super.onConfigurationChanged(newConfig);
	};
	
	public void displaylist(){
		sub_parseAsync = new Sub_ParseAsync();
		sub_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ //허니콤 버전에서만 실행 가능한 API 사용}
			FadingActionBarHelperBase.listview_sub.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		FadingActionBarHelperBase.listview_sub.setOnItemClickListener(this);
		FadingActionBarHelperBase.listview_sub.setItemsCanFocus(false);
		FadingActionBarHelperBase.listview_sub.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		FadingActionBarHelperBase.listview_sub.setFastScrollEnabled(true);
	}
	
	public void MoreLoad(String number) {
		try{
			if (FadingActionBarHelperBase.listview_sub.getLastVisiblePosition() >= FadingActionBarHelperBase.listview_sub.getCount() - 1
					&& FadingActionBarHelperBase.listview_sub.getChildAt(0).getTop() != 0) {
			}
		}catch(Exception e){
		}
	}
	
	public class Sub_ParseAsync extends AsyncTask<String, Integer, String>{
		String Response;
		Ani_Sub_Data sub_data;
		String total_row;
		String parse_num;
		String parse_subject;
		String parse_thumb;
		String parse_portal;
		ArrayList<Ani_Sub_Data> menuItems = new ArrayList<Ani_Sub_Data>();
		public Sub_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				String sTag;
				if(Integer.parseInt(num) > 0 && Integer.parseInt(num) < 301){
					i = "1";
				}else if(Integer.parseInt(num) > 300 && Integer.parseInt(num) < 601){
					i = "2";
				}else if(Integer.parseInt(num) > 600 && Integer.parseInt(num) < 901){
					i = "3";
				}else if(Integer.parseInt(num) > 900 && Integer.parseInt(num) < 1201){
					i = "4";
				}else if(Integer.parseInt(num) > 1200 && Integer.parseInt(num) < 1501){
					i = "5";
				}else if(Integer.parseInt(num) > 1500 && Integer.parseInt(num) < 1801){
					i = "6";
				}else if(Integer.parseInt(num) > 1800 && Integer.parseInt(num) < 2101){
					i = "7";
				}else if(Integer.parseInt(num) > 2100 && Integer.parseInt(num) < 2401){
					i = "8";
				}else if(Integer.parseInt(num) > 2400 && Integer.parseInt(num) < 2701){
					i = "9";
				}else if(Integer.parseInt(num) > 2700 && Integer.parseInt(num) < 3001){
					i = "10";
				}else if(Integer.parseInt(num) > 3000){
					i = "11";
				}
				try{
				   String data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str8));
		           String str = data+i+".php?view="+num; 
		           HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
		           HttpURLConnection.setFollowRedirects(false);
		           localHttpURLConnection.setConnectTimeout(15000);
		           localHttpURLConnection.setReadTimeout(15000); 
		           localHttpURLConnection.setRequestMethod("GET");
		           localHttpURLConnection.connect();
		           InputStream inputStream = new URL(str).openStream(); //open Stream을 사용하여 InputStream을 생성합니다.
		           XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
		           XmlPullParser xpp = factory.newPullParser();
		           xpp.setInput(inputStream, "EUC-KR"); //euc-kr로 언어를 설정합니다. utf-8로 하니깐 깨지더군요
		           int eventType = xpp.getEventType();
		           while (eventType != XmlPullParser.END_DOCUMENT) {
			        	if (eventType == XmlPullParser.START_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.END_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.START_TAG){
			        		sTag = xpp.getName();
			        		if(sTag.equals("videoid")){
			        			sub_data = new Ani_Sub_Data();
			        			Response = xpp.nextText()+"";
			            	}else if(sTag.equals("subject")){
			            		parse_subject = xpp.nextText()+"";
			            	}else if(sTag.equals("portal")){
			            		parse_portal = xpp.nextText()+"";
			            	}else if(sTag.equals("thumb")){
			            		parse_thumb = xpp.nextText()+"";
			            	}
			        	} else if (eventType == XmlPullParser.END_TAG){
			            	sTag = xpp.getName();
			            	if(sTag.equals("Content")){
			            		sub_data.videoid = Response;
			            		sub_data.subject = parse_subject;
			            		sub_data.portal = parse_portal;
			            		sub_data.thumb = parse_thumb;
			            		list.add(sub_data);
			            	}
			            } else if (eventType == XmlPullParser.TEXT) {
			            }
			            eventType = xpp.next();
			        }
		         }
		         catch (SocketTimeoutException localSocketTimeoutException)
		         {
		         }
		         catch (ClientProtocolException localClientProtocolException)
		         {
		         }
		         catch (IOException localIOException)
		         {
		         }
		         catch (Resources.NotFoundException localNotFoundException)
		         {
		         }
		         catch (XmlPullParserException localXmlPullParserException)
		         {
		         }
		         catch (NullPointerException NullPointerException)
		         {
		         }
				 catch (Exception e)
		         {
		         }
		         return Response;
			}
			
			@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            setSupportProgressBarIndeterminateVisibility(true);
	        }
			@Override
			protected void onPostExecute(String Response) {
				super.onPostExecute(Response);
				setSupportProgressBarIndeterminateVisibility(false);
				try{
					if(Response != null){
						if(list_expand == true){
							for (int i = list.size()-1; i>=0; i--) {
								sub_adapter = new SubAdapter(context, menuItems, FadingActionBarHelperBase.listview_sub);
								FadingActionBarHelperBase.listview_sub.setAdapter(sub_adapter);
								FadingActionBarHelperBase.listview_sub.setFocusable(true);
								FadingActionBarHelperBase.listview_sub.setSelected(true);
								menuItems.add(list.get(i));
							}
						}else{
							for (int i = 0; i < list.size(); i++) {
								sub_adapter = new SubAdapter(context, menuItems, FadingActionBarHelperBase.listview_sub);
								FadingActionBarHelperBase.listview_sub.setAdapter(sub_adapter);
								FadingActionBarHelperBase.listview_sub.setFocusable(true);
								FadingActionBarHelperBase.listview_sub.setSelected(true);
								menuItems.add(list.get(i));
						}
								bt_toggle_check = (ToggleButton)findViewById(R.id.bt_toggle_check);
								bt_toggle_expand = (ToggleButton)findViewById(R.id.bt_toggle_expand);
								bt_toggle_check.setOnCheckedChangeListener(SubActivity.this);
								bt_toggle_expand.setOnCheckedChangeListener(SubActivity.this);
						}
					}else{
						Retry_AlertShow(context.getString(R.string.txt_sub_activity3));
					}
				}catch(NullPointerException e){
				}
			}
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}
		}
	
	@Override
	public void onClick(View view) {
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int selectd_count = 0;
    	SparseBooleanArray sba =  FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
		if(sba.size() != 0){
			for(int i =  FadingActionBarHelperBase.listview_sub.getCount() -1; i>=0; i--){
				if(sba.get(i)){
					sba =  FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
					selectd_count++;
				}
			}
		}
		if(selectd_count == 0){
		}else{
//			if(mAdFloating.isShown()){
//				mAdFloating.closeAd();
//			}
		}
		if(sub_adapter != null){
			sub_adapter.notifyDataSetChanged();
		}
	}

	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
		builder.setTitle(context.getString(R.string.txt_network_error));
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_sub_activity4), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
             	 list = new ArrayList<Ani_Sub_Data>();
             	 list.clear();
             	 sub_parseAsync = new Sub_ParseAsync();
             	 sub_parseAsync.execute();
             	 dialog.dismiss();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_sub_activity5), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
             	 dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
//		menu.add(context.getString(R.string.txt_menu_bottom1))
//		.setIcon(R.drawable.btn_01_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		menu.add(context.getString(R.string.txt_menu_bottom2))
//		.setIcon(R.drawable.btn_02_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		menu.add(context.getString(R.string.txt_menu_bottom3))
//		.setIcon(R.drawable.btn_03_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		menu.add(0, 0, 0, "")
		.setIcon(R.drawable.actionbar_bt_00_off)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, 1, 0, "")
		.setIcon(R.drawable.actionbar_bt_01_off)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			
		menu.add(0, 2, 0, "")
		.setIcon(R.drawable.actionbar_bt_02_off)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, 3, 0, "")
		.setIcon(R.drawable.actionbar_bt_06_off)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, 4, 0, "")
		.setIcon(R.drawable.actionbar_bt_03_off)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.menu_toggle_check, menu);
	    
	    MenuInflater inflater2 = getSupportMenuInflater();
	    inflater2.inflate(R.menu.menu_toggle_expand, menu);
	    
	    menu.findItem(0).setVisible(false);
	    menu.findItem(1).setVisible(false);
	    menu.findItem(2).setVisible(false);
	    menu.findItem(3).setVisible(false);
	    
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 0){
			Intent intent = new Intent(this, ContactUs_Activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(item.getItemId() == 1){
			String title = TabContent.context.getString(R.string.txt_share_title);
			String url = TabContent.context.getString(R.string.txt_share_url);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");    
			intent.addCategory(Intent.CATEGORY_DEFAULT);
//			intent.putExtra(Intent.EXTRA_SUBJECT, title);
			intent.putExtra(Intent.EXTRA_TEXT, title + "\n" +url);
			startActivity(Intent.createChooser(intent, TabContent.context.getString(R.string.txt_share_sub_title)));
		}else if(item.getItemId() == 2){
			Intent intent = new Intent(this, Favorite_Intent_Activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(item.getItemId() == 3){
			Intent intent = new Intent(this, Search_Intent_Activity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(item.getItemId() == 4){
			SparseBooleanArray sba = FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
			ArrayList<String> array_videoid = new ArrayList<String>();
			ArrayList<String> array_subject = new ArrayList<String>();
			ArrayList<String> array_portal = new ArrayList<String>();
			if(sba.size() != 0){
//				Ani_Sub_Data tmp_sub_data = (Ani_Sub_Data)sub_adapter.getItem(0);
//				String tmp_portal = tmp_sub_data.portal;
//				if(tmp_portal.equals("pandora")){
//					for(int i = FadingActionBarHelperBase.listview_sub.getCount(); i>=0; i--){
//						if(sba.get(i+1)){
//							Ani_Sub_Data sub_data = (Ani_Sub_Data)sub_adapter.getItem(i);
//							String videoid = sub_data.videoid;
//							String subject = sub_data.subject;
//							String portal = sub_data.portal;
//							array_videoid.add(videoid);
//							array_subject.add(subject);
//							array_portal.add(portal);
//							sba = FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
//						}
//					}
//					if(array_videoid.size() != 0){
//						Intent intent = new Intent(context, CustomVideoPlayer.class);
//						intent.putExtra("array_videoid", array_videoid);
//						intent.putExtra("array_subject", array_subject);
//						intent.putExtra("array_portal", array_portal);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//					}else{
//						Toast.makeText(TabContent.context, TabContent.context.getString(R.string.txt_sub_activity6), Toast.LENGTH_SHORT).show();
//					}
//				}else if(tmp_portal.equals("daum")){
//					for(int i = FadingActionBarHelperBase.listview_sub.getCount(); i>=0; i--){
//						if(sba.get(i+1)){
//							Ani_Sub_Data sub_data = (Ani_Sub_Data)sub_adapter.getItem(i);
//							String videoid = sub_data.videoid;
//							String subject = sub_data.subject;
//							String portal = sub_data.portal;
//							array_videoid.add(videoid);
//							array_subject.add(subject);
//							array_portal.add(portal);
//							sba = FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
//						}
//					}
//					if(array_videoid.size() != 0){
//						Intent intent = new Intent(context, CustomVideoPlayer.class);
//						intent.putExtra("array_videoid", array_videoid);
//						intent.putExtra("array_subject", array_subject);
//						intent.putExtra("array_portal", array_portal);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(intent);
//					}else{
//						Toast.makeText(TabContent.context, TabContent.context.getString(R.string.txt_sub_activity6), Toast.LENGTH_SHORT).show();
//					}
//				}
//				else{
//					for(int i=0; i < FadingActionBarHelperBase.listview_sub.getCount(); i++){
//						if(sba.get(i+1)){
//							Ani_Sub_Data sub_data = (Ani_Sub_Data)sub_adapter.getItem(i);
//							String videoid = sub_data.videoid;
//							String subject = sub_data.subject;
//							String portal = sub_data.portal;
//							array_videoid.add(videoid);
//							array_subject.add(subject);
//							array_portal.add(portal);
//							sba = FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
//						}
//					}
//					if(array_videoid.size() != 0){
//						Intent intent = new Intent(context, CustomYoutubePlayer.class);
//						intent.putExtra("array_videoid", array_videoid);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//						startActivity(intent);
//					}else{
//						Toast.makeText(TabContent.context, TabContent.context.getString(R.string.txt_sub_activity6), Toast.LENGTH_SHORT).show();
//					}
//				}
				for(int i = FadingActionBarHelperBase.listview_sub.getCount(); i>=0; i--){
					if(sba.get(i+1)){
						Ani_Sub_Data sub_data = (Ani_Sub_Data)sub_adapter.getItem(i);
						String videoid = sub_data.videoid;
						String subject = sub_data.subject;
						String portal = sub_data.portal;
						array_videoid.add(videoid);
						array_subject.add(subject);
						array_portal.add(portal);
						sba = FadingActionBarHelperBase.listview_sub.getCheckedItemPositions();
					}
				}
				if(array_videoid.size() != 0){
					Intent intent = new Intent(context, CustomVideoPlayer.class);
					intent.putExtra("array_videoid", array_videoid);
					intent.putExtra("array_subject", array_subject);
					intent.putExtra("array_portal", array_portal);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}else{
					Toast.makeText(TabContent.context, TabContent.context.getString(R.string.txt_sub_activity6), Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(TabContent.context, TabContent.context.getString(R.string.txt_sub_activity6), Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView == bt_toggle_check){
			if(isChecked == true){
				bt_toggle_check.setBackgroundResource(R.drawable.actionbar_bt_04_on);
				for(int i=0; i < FadingActionBarHelperBase.listview_sub.getCount(); i++){
					FadingActionBarHelperBase.listview_sub.setItemChecked(i, true);
				}
			}else{
				bt_toggle_check.setBackgroundResource(R.drawable.actionbar_bt_04_off);
				for(int i=0; i < FadingActionBarHelperBase.listview_sub.getCount(); i++){
					FadingActionBarHelperBase.listview_sub.setItemChecked(i, false);
				}
			}
		}else if(buttonView == bt_toggle_expand){
			list = new ArrayList<Ani_Sub_Data>();
			list.clear();
			if(isChecked == true){
				bt_toggle_expand.setBackgroundResource(R.drawable.actionbar_bt_05_off);
				list_expand = isChecked;
			}else{
				bt_toggle_expand.setBackgroundResource(R.drawable.actionbar_bt_05_on);
				list_expand = isChecked;
			}
			load();
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
	
	//** AdFloatingListener 이벤트들 *************
	@Override
	public void onAdWillLoad() {

	}

	@Override
	public void onAdResized() {
	
	}
	
	@Override
	public void onAdResizeClosed() {
	
	}
	
	@Override
	public void onAdPresentScreen() {
		Log.i("dsu", "플로팅배너onAdPresentScreen");
	}
	
	@Override
	public void onAdLoaded() {
		Log.i("dsu", "플로팅배너onAdLoaded");
	}
	
	@Override
	public void onAdLeaveApplication() {
		Log.i("dsu", "플로팅배너onAdLeaveApplication");
	}
	
	@Override
	public void onAdExpanded() {
		
	}
	
	@Override
	public void onAdExpandClosed() {
		
	}
	
	@Override
	public void onAdDismissScreen() {
		Log.i("dsu", "플로팅배너onAdDismissScreen");
	}

	@Override
	public void onAdFailed(ErrorCode arg0) {
		Log.i("dsu", "플로팅배너onAdFailed : " + arg0);
	}

	@Override
	public void onAdClicked() {
	
	}		
	
	@Override
	public void onAdClosed(boolean arg0) {
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
