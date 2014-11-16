package com.app567go.base;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.app567go.R;
import com.app567go.controller.Controller;

public class AnimationPlayer
{
	private static AnimationPlayer	mInstance		= null;
	private static Object			INSTANCE_LOCK	= new Object();

	private AnimationPlayer()
	{
	}

	public static AnimationPlayer getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new AnimationPlayer();
				}
			}
		}
		return mInstance;
	}
	
	public static void destoryInstance()
	{
		mInstance = null;
	}
	
	public void fadeInView(View view)
	{
		Controller controller = Controller.getInstance();
		Context context = controller.getAppContext();

		view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
	}

}
