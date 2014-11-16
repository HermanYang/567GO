package com.app567go.base;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseApplication extends Application
{
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		DisplayImageOptions.Builder displayOptionBuilder = new DisplayImageOptions.Builder();
		displayOptionBuilder.cacheInMemory(true);
		displayOptionBuilder.cacheOnDisc(true);
		
		DisplayImageOptions options = displayOptionBuilder.build();
		
		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext());
		configBuilder.threadPoolSize(3);
		configBuilder.threadPriority(Thread.NORM_PRIORITY - 1);
		configBuilder.tasksProcessingOrder(QueueProcessingType.FIFO);
		configBuilder.defaultDisplayImageOptions(options);
		
		ImageLoaderConfiguration configuration = configBuilder.build();
		
		ImageLoader.getInstance().init(configuration);
		
	}
}
