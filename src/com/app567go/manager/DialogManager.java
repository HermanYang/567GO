package com.app567go.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

import com.app567go.R;
import com.app567go.constant.Command;
import com.app567go.constant.DialogId;
import com.app567go.controller.Controller;

public class DialogManager
{
	private static DialogManager	mInstance		= null;
	private static Object			INSTANCE_LOCK	= new Object();
	private ProgressDialog			mProgressDialog	= null;
	
	public static DialogManager getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new DialogManager();
				}
			}
		}
		return mInstance;
	}
	
	public static void destoryInstance()
	{
		mInstance = null;
	}
	
	private DialogManager()
	{
		Controller controller = Controller.getInstance();
		ResourceManager res = ResourceManager.getInstance();
		mProgressDialog = new ProgressDialog(controller.getForegroundActivity());
		mProgressDialog.setTitle(ResourceManager.getInstance().getString(R.string.loading_background_data_title));
		mProgressDialog.setMessage(res.getString(R.string.loading_background_data_message));
	}
	
	public Dialog getDialogById(DialogId id)
	{
		switch (id)
		{
			case PROGRESS:
			{
				return mProgressDialog;
			}
			
			case QUIT:
			{
				Controller controller = Controller.getInstance();
				ResourceManager resourceManager = ResourceManager.getInstance();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(controller.getForegroundActivity());
				builder.setTitle(resourceManager.getString(R.string.quit_dialog_title));
				builder.setMessage(resourceManager.getString(R.string.quit_dialog_message));
				
				builder.setPositiveButton(resourceManager.getString(R.string.quit_dialog_positive_text), new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Controller controller = Controller.getInstance();
						controller.dealWith(Command.QUIT);
						dialog.dismiss();
					}
				});
				
				builder.setNegativeButton(resourceManager.getString(R.string.quit_dialog_negative_text), new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				
				return builder.create();
			}
			
			default:
				break;
		}
		
		return null;
	}
}
