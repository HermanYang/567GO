package com.app567go.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CyclePagerAdapter extends PagerAdapter
{
	
	private ArrayList<View>	mViews	= null;
	
	public CyclePagerAdapter()
	{
		super();
		mViews = new ArrayList<View>();
	}
	
	public void addView(View view)
	{
		mViews.add(view);
	}
	
	public View getView(int index)
	{
		return mViews.get(index);
	}
	
	@Override
	public int getItemPosition(Object object)
	{
		return mViews.indexOf(object);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
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
	public Object instantiateItem(ViewGroup container, int position)
	{
		View view = mViews.get(position);
		ViewGroup viewParent = (ViewGroup) view.getParent();
		if (viewParent != null)
		{
			viewParent.removeView(view);
		}
		container.addView(view);
		return view;
	}
	
}