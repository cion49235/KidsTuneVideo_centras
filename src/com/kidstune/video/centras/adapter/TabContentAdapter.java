package com.kidstune.video.centras.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kidstune.video.centras.activity.FavoriteActivity;
import com.kidstune.video.centras.activity.Search_Activity;
import com.kidstune.video.centras.activity.Tab1_Activity;
import com.kidstune.video.centras.activity.Tab2_Activity;
import com.kidstune.video.centras.activity.Tab3_Activity;
import com.kidstune.video.centras.activity.Tab4_Activity;
import com.kidstune.video.centras.activity.Tab5_Activity;
import com.kidstune.video.centras.activity.Tab6_Activity;
import com.kidstune.video.centras.activity.Tab7_Activity;

public class TabContentAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 9;

	public TabContentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			Search_Activity search_activity = new Search_Activity();
			return search_activity;
		case 1:
			Tab1_Activity tab1_activiy = new Tab1_Activity();
			return tab1_activiy;
		case 2:
			Tab2_Activity tab2_activiy = new Tab2_Activity();
			return tab2_activiy;
		case 3:
			Tab3_Activity tab3_activiy = new Tab3_Activity();
			return tab3_activiy;
		case 4:
			Tab4_Activity tab4_activiy = new Tab4_Activity();
			return tab4_activiy;
		case 5:
			Tab5_Activity tab5_activiy = new Tab5_Activity();
			return tab5_activiy;
		case 6:
			Tab6_Activity tab6_activiy = new Tab6_Activity();
			return tab6_activiy;
		case 7:
			Tab7_Activity tab7_activiy = new Tab7_Activity();
			return tab7_activiy;
		case 8:
			FavoriteActivity favorite_activity = new FavoriteActivity();
			return favorite_activity;
			
		}
		return null;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}
