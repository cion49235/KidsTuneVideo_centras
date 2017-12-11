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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;
import com.kidstune.video.centras.R;
import com.kidstune.video.centras.adapter.SearchIntentAdapter;
import com.kidstune.video.centras.data.Main_Data;
import com.kidstune.video.centras.db.Favorite_DBopenHelper;
import com.kidstune.video.centras.util.Crypto;
import com.kidstune.video.centras.util.HangulUtil;
import com.kidstune.video.centras.util.Utils;

public class Search_Intent_Activity extends SherlockActivity implements OnItemClickListener, OnClickListener, OnScrollListener, AdViewListener {
	public static LinearLayout layout_nodata, layout_listview_main;
	public boolean flag;
	public static Favorite_DBopenHelper favorite_mydb;
	public static ListView listview_main;
	public static SearchIntentAdapter adapter;
	public static ArrayList<Main_Data> list;
	public static LinearLayout layout_progress;
	public Cursor cursor;
	public static AlertDialog alertDialog;
	public SharedPreferences settings,pref;
	public Editor edit;
	public Search_ParseAsync search_parseAsync = null;
	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public static EditText edit_searcher;
	public static ImageButton bt_search_result; 
	public String searchKeyword;
	private ActionBar mActionBar;
	public Context context;
	public static RelativeLayout ad_layout;
	public Search_Intent_Activity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_search);
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
		mActionBar.setTitle(context.getString(R.string.txt_search_intent_activity1));
		mActionBar.setDisplayShowHomeEnabled(false);
//		mActionBar.setDisplayShowTitleEnabled(false);
		edit_searcher = (EditText)findViewById(R.id.edit_searcher);
		bt_search_result = (ImageButton)findViewById(R.id.bt_search_result);
		bt_search_result.setOnClickListener(this);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
		listview_main = (ListView)findViewById(R.id.listview_main);
		favorite_mydb = new Favorite_DBopenHelper(this);
		list = new ArrayList<Main_Data>();
		list.clear();
		seacher_start();
//		displaylist();
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
	public void onDestroy() {
		super.onDestroy();
		if(search_parseAsync != null){
			search_parseAsync.cancel(true);
		}
		
		if(favorite_mydb != null){
			favorite_mydb.close();
		}
		
	}
	
	public void seacher_start(){
		edit_searcher.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					searchKeyword = s.toString().toLowerCase();
				} catch (Exception e) {
				}
			}
		});
	}
	
	public void displaylist(){
		search_parseAsync = new Search_ParseAsync();
		search_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ //허니콤 버전에서만 실행 가능한 API 사용}
			listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		listview_main.setOnScrollListener(this);
		listview_main.setOnItemClickListener(this);
		listview_main.setItemsCanFocus(false);
		listview_main.setFastScrollEnabled(false);
	}
	
	public class Search_ParseAsync extends AsyncTask<String, Integer, String>{
		String Response;
		Main_Data main_data;
		String num;
		String subject;
		String thumb;
		String portal;
		ArrayList<Main_Data> menuItems = new ArrayList<Main_Data>();
		public Search_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				 try{
					 String data = Crypto.decrypt(Utils.data, TabContent.context.getString(R.string.txt_str0));
					 String sTag;
					 String str = data;
			         HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
			         HttpURLConnection.setFollowRedirects(true);
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
				        		if(sTag.equals("Album")){
				        			main_data = new Main_Data();
				        			Response = xpp.getAttributeValue(null, "id") + "";
				            	}else if(sTag.equals("num")){
				            		num = xpp.nextText()+"";
				            	}else if(sTag.equals("subject")){
				            		subject = xpp.nextText()+"";
				            	}else if(sTag.equals("thumb")){
				            		thumb = xpp.nextText()+"";
				            	}else if(sTag.equals("portal")){
				            		portal = xpp.nextText()+"";
				            	}
				        	} else if (eventType == XmlPullParser.END_TAG){
				            	sTag = xpp.getName();
				            	if(sTag.equals("Album")){
				            		main_data.id = Response;
				            		main_data.num = num;
				            		main_data.subject = subject;
				            		main_data.thumb = thumb;
				            		main_data.portal = portal;
				            		
				            		boolean isAdd = false;
				            		if(searchKeyword != null && "".equals(searchKeyword.trim()) == false){
				            			String iniName = HangulUtil.getHangulInitialSound(main_data.subject,searchKeyword);
				            			if(iniName.indexOf(searchKeyword) >= 0){
				            				isAdd = true;
				            			}
				            		}else{
				            			isAdd = true;
				            		}
				            		if(isAdd){
				            			list.add(main_data);
				            		}
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
//				 catch (InvalidKeyException e) 
//		         {
//		         }
//				 catch (NoSuchAlgorithmException e) 
//		         {
//		         }
//				 catch (InvalidKeySpecException e) 
//		         {
//		         }
//				 catch (NoSuchPaddingException e) 
//		         {
//		         } 
//				 catch (InvalidAlgorithmParameterException e)
//		         {
//		         }
//				 catch (IllegalBlockSizeException e)
//		         {
//		         }
//				 catch (BadPaddingException e) 
//		         {
//		         }
				return Response;
			}
			
			@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            layout_progress.setVisibility(View.VISIBLE);
	            
	        }
			@Override
			protected void onPostExecute(String Response) {
				super.onPostExecute(Response);
				 layout_progress.setVisibility(View.GONE);
				try{
					if(Response != null){
						for(int i=0;; i++){
							if(i >= list.size()){
//							while (i > list.size()-1){
								adapter = new SearchIntentAdapter(context, menuItems, listview_main);
								listview_main.setAdapter(adapter);
								listview_main.setFocusable(true);
								listview_main.setSelected(true);
								layout_listview_main.setVisibility(View.VISIBLE);
								if(list.size() == 0){
									layout_nodata.setVisibility(View.VISIBLE);
								}else{
									layout_nodata.setVisibility(View.GONE);
								}
//								layout_nodata.setVisibility(View.GONE);
								return;
							}
							menuItems.add(list.get(i));
						}
						
					}else{
//						layout_nodata.setVisibility(View.VISIBLE);
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
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
	}
	@Override
	public void onClick(View view) {
		if(view == bt_search_result){
			String search_text = edit_searcher.getText().toString();
			if ((search_text != null) && (search_text.length() > 0)){
				list = new ArrayList<Main_Data>();
				list.clear();
				displaylist();
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
	    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
			}else{
				Toast.makeText(this, context.getString(R.string.txt_search_activity5), Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Main_Data main_data = (Main_Data)adapter.getItem(position);
		String id = main_data.id;
		String num = main_data.num;
		String subject = main_data.subject;
		String thumb = main_data.thumb;
		String portal = main_data.portal;
		
		Intent intent = new Intent(this, SubActivity.class);
		intent.putExtra("num", num);
		intent.putExtra("subject", subject);
		intent.putExtra("thumb", thumb);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
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
