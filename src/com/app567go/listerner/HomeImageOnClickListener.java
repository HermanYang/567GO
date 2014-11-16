package com.app567go.listerner;

import android.view.View;
import android.view.View.OnClickListener;

import com.app567go.constant.Command;
import com.app567go.controller.Controller;

public class HomeImageOnClickListener implements OnClickListener
{
	
	@Override
	public void onClick(View v)
	{
		
		Controller.getInstance().dealWith(Command.BACK_TO_HOME_SECTION);
	}
	
}
