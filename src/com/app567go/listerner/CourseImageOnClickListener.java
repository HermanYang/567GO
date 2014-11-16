package com.app567go.listerner;

import android.view.View;
import android.view.View.OnClickListener;

import com.app567go.constant.Command;
import com.app567go.controller.Controller;

public class CourseImageOnClickListener implements OnClickListener
{
	
	@Override
	public void onClick(View v)
	{
		
		Controller.getInstance().dealWith(Command.HOME_SECTION_TO_COURSE_SECTION);
	}
	
}
