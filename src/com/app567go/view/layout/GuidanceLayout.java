package com.app567go.view.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app567go.R;
import com.app567go.constant.Command;
import com.app567go.controller.Controller;

public class GuidanceLayout extends RelativeLayout
{
	private SquareGridLayout	mSquareGridLayout	= null;
	private final int			COLUMN				= 3;
	
	public GuidanceLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public GuidanceLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public GuidanceLayout(Context context)
	{
		super(context);
		initialize();
	}
	
	@SuppressLint("NewApi")
	private void initialize()
	{
		mSquareGridLayout = new SquareGridLayout(getContext());
		
		mSquareGridLayout.setNumColumns(COLUMN);
		
		mSquareGridLayout.setHorizontalSpacing(20);
		mSquareGridLayout.setVerticalSpacing(20);
		
		mSquareGridLayout.setPadding(20, 20, 20, 20);
		
		View instituteIntroductionout = new ImageView(getContext());
		instituteIntroductionout.setBackgroundResource(R.drawable.institute_introduction_btn_selector);
		mSquareGridLayout.addView(instituteIntroductionout);
		instituteIntroductionout.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_INSTITUTE_INTRODUCTION_SECTION);
			}
		});
		
		ImageView instituteNews = new ImageView(getContext());
		mSquareGridLayout.addView(instituteNews);
		instituteNews.setBackgroundResource(R.drawable.institute_news_btn_selector);
		instituteNews.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_INSTITUTE_NEWS_SECTION);
			}
		});
		
		ImageView instituteMoments = new ImageView(getContext());
		mSquareGridLayout.addView(instituteMoments);
		instituteMoments.setBackgroundResource(R.drawable.institute_moments_btn_selector);
		instituteMoments.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_INSTITUE_MOMENT_SECTION);
			}
		});
		
		ImageView frequentQuestions = new ImageView(getContext());
		mSquareGridLayout.addView(frequentQuestions);
		frequentQuestions.setBackgroundResource(R.drawable.frequent_questions_btn_selector);
		frequentQuestions.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_FREQUENT_QUESTION_SECTION);
			}
		});
		
		ImageView course = new ImageView(getContext());
		mSquareGridLayout.addView(course);
		course.setBackgroundResource(R.drawable.course_btn_selector);
		course.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_COURSE_SECTION);
			}
		});
		
		ImageView mentor = new ImageView(getContext());
		mSquareGridLayout.addView(mentor);
		mentor.setBackgroundResource(R.drawable.mentor_btn_selector);
		mentor.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_MENTOR_SECTION);
			}
		});
		
		ImageView recruitmentNews = new ImageView(getContext());
		mSquareGridLayout.addView(recruitmentNews);
		recruitmentNews.setBackgroundResource(R.drawable.recruitment_news_btn);
		recruitmentNews.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_RECRUITMENT_NEWS_SECTION);
			}
		});
		
		ImageView onlineSignUp = new ImageView(getContext());
		mSquareGridLayout.addView(onlineSignUp);
		onlineSignUp.setBackgroundResource(R.drawable.online_sign_up_btn_selector);
		onlineSignUp.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_ONLINE_SIGN_UP_SECTION);
			}
		});
		
		ImageView contactUs = new ImageView(getContext());
		mSquareGridLayout.addView(contactUs);
		contactUs.setBackgroundResource(R.drawable.contact_us_btn_selector);
		contactUs.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Controller controller = Controller.getInstance();
				controller.dealWith(Command.HOME_SECTION_TO_CONTACT_US_SECTION);
			}
		});
		
		addView(mSquareGridLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
	}
	
}
