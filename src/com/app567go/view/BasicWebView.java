package com.app567go.view;

import java.util.ArrayList;

import com.app567go.R;
import com.app567go.constant.Command;
import com.app567go.constant.DialogId;
import com.app567go.controller.Controller;
import com.app567go.manager.DialogManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BasicWebView extends WebView
{
	
	public BasicWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public BasicWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public BasicWebView(Context context)
	{
		super(context);
		initialize();
	}
	
	public void setLoadInView(boolean loadInView)
	{
	}
	
	private void initialize()
	{
		WebSettings webSettings = getSettings();
		
		// disable javascript
		webSettings.setJavaScriptEnabled(false);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
		
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		
		webSettings.setLightTouchEnabled(false);
		
		webSettings.setLoadsImagesAutomatically(true);
		
		// show zoom button
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(false);
		
		// auto adjust the webview to fix the screen
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
		// hide scroll bars
		setVerticalScrollBarEnabled(true);
		setHorizontalScrollbarOverlay(true);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		setWebChromeClient(new BasicWebChromeClient());
		setWebViewClient(new BasicWebClient());
		
	}
	
}

class BasicWebChromeClient extends WebChromeClient
{
	
}

class BasicWebClient extends WebViewClient
{
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		view.requestFocus(WebView.FOCUS_DOWN);
		if (url.startsWith("tel:"))
		{
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
			ArrayList<Object> args = new ArrayList<Object>();
			args.add(intent);
			Controller.getInstance().dealWith(Command.START_ACTIVITY, args);
		}
		else if (url.startsWith("http:") || url.startsWith("https:"))
		{
			view.loadUrl(url);
		}
		else
		{
			int duration = 1000;
			Toast.makeText(view.getContext(), R.string.url_error, duration).show();
			;
		}
		
		return true;
	}
	
	@Override
	public void onPageFinished(WebView view, String url)
	{
		super.onPageFinished(view, url);
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(view);
		Controller.getInstance().dealWith(Command.FINISH_LOADING_WEB_PAGE, args);
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon)
	{
		super.onPageStarted(view, url, favicon);
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(view);
		Controller.getInstance().dealWith(Command.START_LOADING_WEB_PAGE, args);
	}
}