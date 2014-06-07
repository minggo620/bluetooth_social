package edu.minggo.chat.adapters;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * ±Ì«ÈPagerAdapter
 * @author minggo
 * @created 2013-2-16œ¬ŒÁ11:40:42
 */
public class SmileyPagerAdapter extends PagerAdapter {
	public ArrayList<View> views = new ArrayList<View>();
	public SmileyPagerAdapter(ArrayList<View> views){
		this.views = views;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public int getCount() {
		return views.size();
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		
		return views.get(position);
	}

}
