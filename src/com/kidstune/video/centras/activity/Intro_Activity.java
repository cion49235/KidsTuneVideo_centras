package com.kidstune.video.centras.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.kidstune.video.centras.R;

public class Intro_Activity extends Activity {
	public static LinearLayout intro_layout;
	public Handler handler;
	public Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		context = this;
		handler = new Handler();
		handler.postDelayed(runnable, 2000);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(handler != null) handler.removeCallbacks(runnable);
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Intent intent = new Intent(context, TabContent.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			//fade_animation
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			if(handler != null) handler.removeCallbacks(runnable);
			finish();
		}
	}