package com.app567go.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import cn.sharesdk.framework.ShareSDK;

import com.app567go.R;
import com.app567go.controller.Controller;

public class BaseActivity extends Activity
{
	
	protected Controller	mController	= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mController = Controller.getInstance();
		mController.setAppContext(getApplicationContext());
		mController.registerActivity(this);
		mController.markForegroundActivity(this);
		
		ShareSDK.initSDK(this);
	}
	
	@Override
	public void setContentView(View view)
	{
		ViewGroup viewParent = (ViewGroup) view.getParent();
		if (viewParent != null)
		{
			viewParent.removeView(view);
		}
		super.setContentView(view);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		mController.markForegroundActivity(this);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mController.unregisterActivity(this);
	}
	
	@Override
	protected void onResume()
	{
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		
		mController.markForegroundActivity(this);
		super.onResume();
	}
	
	@Override
	public void onBackPressed()
	{
		if (!mController.onBackPressed())
		{
			super.onBackPressed();
		}
	}
}
