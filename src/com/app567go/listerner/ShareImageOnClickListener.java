package com.app567go.listerner;

import android.view.View;
import android.view.View.OnClickListener;

import com.app567go.constant.Command;
import com.app567go.controller.Controller;

public class ShareImageOnClickListener implements OnClickListener
{
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
			Controller.getInstance().dealWith(Command.ONE_KEY_SHARE);
	}
	
}
