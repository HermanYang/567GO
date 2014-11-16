package com.app567go.manager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app567go.R;
import com.app567go.controller.Controller;

public class ViewStore
{
	private static ViewStore	mInstance		= null;
	private static Object		INSTANCE_LOCK	= new Object();
	
	private ArrayList<View>		mViews			= null;
	
	private ViewStore()
	{
		mViews = new ArrayList<View>();
	}
	
	public static ViewStore getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new ViewStore();
				}
			}
		}
		
		return mInstance;
	}
	
	public static void destoryInstance()
	{
		mInstance = null;
	}
	
	public View getViewById(int id)
	{
		View view = null;
		
		for (View element : mViews)
		{
			if (element.getId() == id)
			{
				view = element;
				return view;
			}
		}
		
		return null;
	}
	
	public void initializeAllViews()
	{
		inflate(R.layout.layout_login_page);
		inflate(R.layout.layout_home_page);
		inflate(R.layout.layout_common_webview_container);
		inflate(R.layout.layout_home_section);
		inflate(R.layout.layout_institute_introduction_section);
		inflate(R.layout.layout_institute_news_section);
		inflate(R.layout.layout_institute_moments_section);
		inflate(R.layout.layout_frequent_questions_section);
		inflate(R.layout.layout_course_section);
		inflate(R.layout.layout_recruitment_news_section);
		inflate(R.layout.layout_online_sign_up_section);
		inflate(R.layout.layout_contact_us_section);
		inflate(R.layout.layout_photo_section);
		
	}
	
	private View inflate(int id)
	{
		Activity foregroundActivity = Controller.getInstance().getForegroundActivity();
		LayoutInflater layoutInflater = (LayoutInflater) foregroundActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = layoutInflater.inflate(id, null);
		
		if (view instanceof ViewGroup)
		{
			registerLayoutChildrenViews((ViewGroup) view);
		}
		
		return view;
	}
	
	private void registerLayoutChildrenViews(ViewGroup layout)
	{
		View view = layout;
		
		// ignore no name view
		if (view.getId() != View.NO_ID)
		{
			mViews.add(view);
		}
		
		for (int i = 0; i < layout.getChildCount(); i++)
		{
			view = layout.getChildAt(i);
			
			if (!mViews.contains(view))
			{
				mViews.add(view);
			}
			
			if (view instanceof ViewGroup)
			{
				registerLayoutChildrenViews((ViewGroup) view);
			}
		}
	}
	
}
