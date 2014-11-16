package com.app567go.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app567go.R;
import com.app567go.controller.Controller;
import com.app567go.manager.ViewStore;

public class VideoWebView extends Html5WebView
{
	
	public VideoWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public VideoWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public VideoWebView(Context context)
	{
		super(context);
		initialize();
	}
	
	@SuppressLint("NewApi")
	private void initialize()
	{
		setWebChromeClient(new VideoWebViewChromeClient());
		setWebViewClient(new VideoWebViewClient());
	}
	
}

class VideoWebViewChromeClient extends WebChromeClient
{
	Handler				mHandler	= new Handler();
	CustomViewCallback	mCallback	= null;
	
	@Override
	public void onHideCustomView()
	{
		Log.v("onHideCustomView", "Hide");
		
		if (mCallback != null)
		{
			mCallback.onCustomViewHidden();
			mCallback = null;
		}
		
		Controller controller = Controller.getInstance();
		Activity currenetActivity = controller.getForegroundActivity();
		ViewStore viewStore = ViewStore.getInstance();
		View homePage = viewStore.getViewById(R.id.layout_home_page);
		currenetActivity.setContentView(homePage);
		
		currenetActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	}
	
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback)
	{
		
		Log.v("onShowCustomView", "Show");
		super.onShowCustomView(view, callback);
		Controller controller = Controller.getInstance();
		Activity currenetActivity = controller.getForegroundActivity();
		currenetActivity.setContentView(view);
		
		currenetActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		
		mCallback = callback;
		
	}
	
	@Override
	public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback)
	{
		onShowCustomView(view, callback);
	}
	
}

class VideoWebViewClient extends WebViewClient
{
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		view.loadUrl(url);
		return true;
	}
	
}