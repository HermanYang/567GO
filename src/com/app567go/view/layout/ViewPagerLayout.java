package com.app567go.view.layout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.app567go.R;
import com.app567go.adapter.BasicPagerAdapter;
import com.app567go.manager.ResourceManager;
import com.app567go.view.IndicatorView;

public class ViewPagerLayout extends FrameLayout
{

	private ViewPager		mViewPager		= null;
	private IndicatorView	mIndicatorView	= null;
	private boolean			mShowIndicator	= true;

	public ViewPagerLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}

	public ViewPagerLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}

	public ViewPagerLayout(Context context)
	{
		super(context);
		initialize();
	}

	public void initialize()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();

		mViewPager = new ViewPager(getContext());
		mIndicatorView = new IndicatorView(getContext());
		BasicPagerAdapter adapter = new BasicPagerAdapter();

		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		layoutParams.gravity = Gravity.LEFT;

		mViewPager.setAdapter(adapter);

		mIndicatorView.setIndicatorSize(resourceManager.getDimensionPixelSize(R.dimen.indicatorView_item_size));
		mIndicatorView.setIndicatorMargin(resourceManager.getDimensionPixelSize(R.dimen.indicatorView_item_margin));

		mViewPager.setOnPageChangeListener(mOnPageChangeListener);

		addView(mViewPager, layoutParams);
		addView(mIndicatorView);

	}

	public void addPageView(View view, String title)
	{
		BasicPagerAdapter adapter = (BasicPagerAdapter) mViewPager.getAdapter();
		adapter.addView(view, title);
		mIndicatorView.setIndicatorAmount(adapter.getCount());
		adapter.notifyDataSetChanged();
	}

	public void setShowIndicator(boolean show)
	{
		mShowIndicator = show;
		if (mShowIndicator)
		{
			mIndicatorView.setVisibility(View.VISIBLE);
		}
		else
		{
			mIndicatorView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		if (mShowIndicator)
		{
			// layout indicator view to botton and center
			int width = mIndicatorView.getWidth();
			int height = mIndicatorView.getHeight();
			int indecatorLeft = (right - left - width) / 2;
			int indecatorTop = (bottom - top - height);
			int indecatorRight = indecatorLeft + width;
			int indecatorBottom = bottom - top;

			mIndicatorView.layout(indecatorLeft, indecatorTop, indecatorRight, indecatorBottom);
		}
	}

	private OnPageChangeListener	mOnPageChangeListener	= new ViewPager.SimpleOnPageChangeListener()
															{
																@Override
																public void onPageScrollStateChanged(int state)
																{
																	super.onPageScrollStateChanged(state);
																	if (state == ViewPager.SCROLL_STATE_IDLE)
																	{
																		mIndicatorView.setActiveIndex(mViewPager.getCurrentItem());
																	}
																}

															};

}