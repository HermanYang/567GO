package com.app567go.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;

public class Html5WebView extends BasicWebView
{
	
	public Html5WebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public Html5WebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public Html5WebView(Context context)
	{
		super(context);
		initialize();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initialize()
	{
		WebSettings webSettings = getSettings();
		
		// enable javascript
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		
		webSettings.setLightTouchEnabled(true);
		
		webSettings.setLoadsImagesAutomatically(true);
		
		// show zoom button
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(false);
		
		// auto adjust the webview to fix the screen
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
	}
	
}
