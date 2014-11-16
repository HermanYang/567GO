package com.app567go.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BasicPagerAdapter extends PagerAdapter
{

	private ArrayList<View>		mViews		= null;
	private ArrayList<String>	mViewTitles	= null;

	public BasicPagerAdapter()
	{
		mViews = new ArrayList<View>();
		mViewTitles = new ArrayList<String>();
	}

	public void addView(View view, String title)
	{
		mViews.add(view);
		mViewTitles.add(title);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		View view = mViews.get(position);
		container.removeView(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		View view = mViews.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount()
	{
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj)
	{
		return view == obj;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return mViewTitles.get(position);
	}

}
