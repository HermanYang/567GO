package com.app567go.base;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.app567go.listerner.OnNetworkStateChangedListener;

public class ConnectionChangeBroadcaster extends BroadcastReceiver
{
	private static ArrayList<OnNetworkStateChangedListener>	mObserverList	= new ArrayList<OnNetworkStateChangedListener>();
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		boolean connected = false;
		
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		
		if (State.CONNECTED == state)
		{
			connected = true;
		}
		
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		
		if (State.CONNECTED == state)
		{
			connected = true;
		}
		
		if (connected)
		{
			for (OnNetworkStateChangedListener onNetworkStateChanged : mObserverList)
			{
				onNetworkStateChanged.onNetworkConnected();
			}
		}
		else
		{
			for (OnNetworkStateChangedListener onNetworkStateChanged : mObserverList)
			{
				onNetworkStateChanged.onNetworkDisconnected();
			}
		}
	}
	
	public static void register(OnNetworkStateChangedListener listener)
	{
		mObserverList.add(listener);
	}
	
	public static void unregister(OnNetworkStateChangedListener listener)
	{
		mObserverList.remove(listener);
	}
}
