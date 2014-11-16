package com.app567go.activity;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;

import com.app567go.constant.Command;

public class HomePageActivity extends BaseActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mController.dealWith(Command.INITIALIZE);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		
		ArrayList<Object> args = new ArrayList<Object>();
		
		args.add(newConfig.orientation);
		mController.dealWith(Command.ORIENTATION_CHANGED, args);
		
	}
}
