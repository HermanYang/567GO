package com.app567go.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.app567go.controller.Controller;

public class AppSetting
{
	public final static String			PREFERENCECES_FILE_NAME	= "preferences";
	public final static String			BOOLEAN_FIRST_INSTALLED	= "first_installed";

	private static AppSetting			mInstance				= null;
	private static Object				INSTANCE_LOCK			= new Object();

	private SharedPreferences			mSharedPreferences		= null;
	private SharedPreferences.Editor	mEditor					= null;

	private AppSetting()
	{
		Controller controller = Controller.getInstance();
		Context context = controller.getAppContext();
		mSharedPreferences = context.getSharedPreferences(PREFERENCECES_FILE_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();

		checkPreferenceItems();
	}

	public static AppSetting getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new AppSetting();
				}
			}
		}
		return mInstance;
	}

	public static void destoryInstance()
	{
		mInstance = null;
	}
	
	public void save()
	{
		mEditor.commit();
	}

	public boolean getBoolean(String valeName, boolean defalutValue)
	{
		return mSharedPreferences.getBoolean(valeName, defalutValue);
	}

	public void setBoolean(String valueName, boolean value)
	{
		mEditor.putBoolean(valueName, value);
	}

	private void checkPreferenceItems()
	{
		if (!mSharedPreferences.contains(BOOLEAN_FIRST_INSTALLED))
		{
			setBoolean(BOOLEAN_FIRST_INSTALLED, true);
		}
	}

}
