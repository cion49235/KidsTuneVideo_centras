package com.kidstune.video.centras.activity;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.kidstune.video.centras.R;
import com.kidstune.video.centras.adapter.TabContentAdapter;
import com.kidstune.video.centras.util.PreferenceUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;
import kr.co.inno.autocash.service.AutoServiceActivity;

public class TabContent extends SherlockFragmentActivity implements InterstitialAdListener, CustomPopupListener {
	private ActionBar mActionBar;
	private ViewPager mPager;
	private Tab tab;
	public static Context context;
	public boolean flag;
	public Handler handler;
	public static InterstitialAd interstialAd;
	public static SherlockFragmentActivity activity;
	private int SDK_INT = android.os.Build.VERSION.SDK_INT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabcontent);
		activity = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD, "AX0004B36");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_TAD_FULL, "AX0004B37");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_SHALLWE, "4wSC9xA6l_fwOJDgXNn_JA");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "zkl206g4");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/5794143361");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/1080845761");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_CAULY, "4qW0j8xS");
		
		context = this;
	//  Custom Popup 시작
	    CustomPopup.setCustomPopupListener(this);
	    CustomPopup.startCustomPopup(this, "zkl206g4");
	    
		if (SDK_INT >= Build.VERSION_CODES.M){ 
			checkPermission();
		}else {
			mActionBar = getSupportActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//			mActionBar.setTitle(context.getString(R.string.app_name));
//			mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setDisplayShowTitleEnabled(false);
			mPager = (ViewPager) findViewById(R.id.pager);
			
			FragmentManager fm = getSupportFragmentManager();
			ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					super.onPageSelected(position);
					mActionBar.setSelectedNavigationItem(position);
				}
			};

			mPager.setOnPageChangeListener(ViewPagerListener);
			TabContentAdapter adapter = new TabContentAdapter(fm);
			mPager.setAdapter(adapter);
			exit_handler();
			auto_service();
			
			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					mPager.setCurrentItem(tab.getPosition());
				}

				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				}

				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
				}
			};
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent0)).setTabListener(tabListener);
			mActionBar.addTab(tab);

			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent1)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent2)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent3)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent4)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent5)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent6)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent7)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent8)).setTabListener(tabListener);
			mActionBar.addTab(tab);
		}
	}
	
	private void auto_service() {
        Intent intent = new Intent(context, AutoServiceActivity.class);
        context.stopService(intent);
        context.startService(intent);
    }
	
	private void checkPermission() {
		if (checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                // Explain to the user why we need to write the permission.
            	Return_AlertShow(context.getString(R.string.permission_cancel));
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        } else {
        	mActionBar = getSupportActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//			mActionBar.setTitle(context.getString(R.string.app_name));
//			mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setDisplayShowTitleEnabled(false);
			mPager = (ViewPager) findViewById(R.id.pager);
			
			FragmentManager fm = getSupportFragmentManager();
			ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					super.onPageSelected(position);
					mActionBar.setSelectedNavigationItem(position);
				}
			};

			mPager.setOnPageChangeListener(ViewPagerListener);
			TabContentAdapter adapter = new TabContentAdapter(fm);
			mPager.setAdapter(adapter);
			exit_handler();
			auto_service();
			
			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					mPager.setCurrentItem(tab.getPosition());
				}

				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				}

				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
				}
			};
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent0)).setTabListener(tabListener);
			mActionBar.addTab(tab);

			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent1)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent2)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent3)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent4)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent5)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent6)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent7)).setTabListener(tabListener);
			mActionBar.addTab(tab);
			
			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent8)).setTabListener(tabListener);
			mActionBar.addTab(tab);
        }
	}
	
	@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                try{
                    if ( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    	mActionBar = getSupportActionBar();
            			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//            			mActionBar.setTitle(context.getString(R.string.app_name));
//            			mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
            			mActionBar.setDisplayShowHomeEnabled(false);
            			mActionBar.setDisplayShowTitleEnabled(false);
            			mPager = (ViewPager) findViewById(R.id.pager);
            			
            			FragmentManager fm = getSupportFragmentManager();
            			ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            				@Override
            				public void onPageSelected(int position) {
            					super.onPageSelected(position);
            					mActionBar.setSelectedNavigationItem(position);
            				}
            			};

            			mPager.setOnPageChangeListener(ViewPagerListener);
            			TabContentAdapter adapter = new TabContentAdapter(fm);
            			mPager.setAdapter(adapter);
            			exit_handler();
            			auto_service();
            			
            			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            				@Override
            				public void onTabSelected(Tab tab, FragmentTransaction ft) {
            					mPager.setCurrentItem(tab.getPosition());
            				}

            				@Override
            				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            				}

            				@Override
            				public void onTabReselected(Tab tab, FragmentTransaction ft) {
            				}
            			};
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent0)).setTabListener(tabListener);
            			mActionBar.addTab(tab);

            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent1)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent2)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent3)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent4)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent5)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent6)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent7)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
            			
            			tab = mActionBar.newTab().setText(context.getString(R.string.txt_tabcontent8)).setTabListener(tabListener);
            			mActionBar.addTab(tab);
                    } else {
                    	Return_AlertShow(context.getString(R.string.permission_cancel));
                    }
                    break;
                }catch (ArrayIndexOutOfBoundsException e){
                }catch (Exception e){
                }
        	}
    	}
	
	public void Return_AlertShow(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(msg);
        builder.setNeutralButton(context.getString(R.string.txt_finish_yes), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
            	PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
                finish();
            	dialog.dismiss();
            }
        });
        AlertDialog myAlertDialog = builder.create();
        myAlertDialog.show();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Custom Popup 종료
		CustomPopup.stopCustomPopup();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(Search_Activity.adapter != null){
			Search_Activity.adapter.notifyDataSetChanged();
		}
		if(Tab1_Activity.adapter != null){
			Tab1_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab2_Activity.adapter != null){
			Tab2_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab3_Activity.adapter != null){
			Tab3_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab4_Activity.adapter != null){
			Tab4_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab5_Activity.adapter != null){
			Tab5_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab6_Activity.adapter != null){
			Tab6_Activity.adapter.notifyDataSetChanged();
		}
		
		if(Tab7_Activity.adapter != null){
			Tab7_Activity.adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 0, 0, getString(R.string.txt_actionbar_top0))
//		.setIcon(R.drawable.actionbar_bt_00_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		menu.add(0, 1, 0, getString(R.string.txt_actionbar_top1))
//		.setIcon(R.drawable.actionbar_bt_01_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//			
//		menu.add(0, 2, 0, getString(R.string.txt_actionbar_top2))
//		.setIcon(R.drawable.actionbar_bt_02_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		menu.add(0, 3, 0, getString(R.string.txt_actionbar_top3))
//		.setIcon(R.drawable.actionbar_bt_06_off)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
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
		}
		return true;
	}
	
	public void exit_handler(){
    	handler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what == 0){
    				flag = false;
    			}
    		}
    	};
    }
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("zkl206g4");
//        	adInfo.setTestMode(false);
        	interstialAd = new InterstitialAd(this);
        	interstialAd.setAdInfo(adInfo, this);
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
	
	//** InterstitialAd 이벤트들 *************
	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
		finish();
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}
	
	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
	}
	
	//** CustomPopup 이벤트들 *************
	@Override
	public void onCloseCustomPopup(String arg0) {
	}

	@Override
	public void onHasNoCustomPopup() {
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	}

	@Override
	public void onStartedCustomPopup() {
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	}
	
	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 if(!flag){
//				 addInterstitialView();
				 Toast.makeText(context, context.getString(R.string.txt_tabcontent9) , Toast.LENGTH_SHORT).show();
				 flag = true;
				 handler.sendEmptyMessageDelayed(0, 2000);
				 return false;
			 }else{
				 handler.postDelayed(new Runnable() {
					 @Override
					 public void run() {
						 PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
						 finish();
					 }
				 },0);
			 }
			 return false;	 
		 }
		return super.onKeyDown(keyCode, event);
	}
}
