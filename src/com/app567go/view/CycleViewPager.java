package com.app567go.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.app567go.R;
import com.app567go.adapter.CyclePagerAdapter;
import com.app567go.listerner.OnCyclePagerViewSettledListener;

public class CycleViewPager extends ViewPager
{
	
	private int								mInterval								= 1000;
	private boolean							mStartFlipping							= false;
	private ScheduledExecutorService		mScheduledExecutorService				= null;
	private ScheduledFuture<?>				mScheduledFuture						= null;
	private OnCyclePagerViewSettledListener	mOnCyclePagerViewPageSettledListener	= null;
	
	private Handler							mShowNextHandler						= null;
	private Runnable						mShowNextRunnable						= null;
	
	public CycleViewPager(Context context)
	{
		super(context);
		initialize();
	}
	
	public CycleViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public void showNext()
	{
		CyclePagerAdapter adapter = (CyclePagerAdapter) getAdapter();
		int size = adapter.getCount();
		int currentPosition = getCurrentItem();
		
		int nextPosition = (currentPosition + 1) % size;
		setCurrentItem(nextPosition, true);
		
	}
	
	public void showPrevious()
	{
		CyclePagerAdapter adapter = (CyclePagerAdapter) getAdapter();
		int size = adapter.getCount();
		int currentPosition = getCurrentItem();
		
		int prevousPosition = (currentPosition + size - 1) % size;
		setCurrentItem(prevousPosition, true);
	}
	
	public void startFlipping()
	{
		mStartFlipping = true;
		mScheduledFuture = mScheduledExecutorService.scheduleWithFixedDelay(mShowNextRunnable, mInterval, mInterval, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public int getCurrentItem()
	{
		return super.getCurrentItem();
	}
	
	public boolean isAutoFlipping()
	{
		return mStartFlipping;
	}
	
	public void stopFlipping()
	{
		mStartFlipping = false;
		mScheduledFuture.cancel(true);
	}
	
	public void setFlippingInterval(int interval)
	{
		mInterval = interval;
	}
	
	public void setOnPageSettledListener(OnCyclePagerViewSettledListener onCyclePagerViewPageSettledListener)
	{
		mOnCyclePagerViewPageSettledListener = onCyclePagerViewPageSettledListener;
	}
	
	private void initialize()
	{
		setBackgroundResource(R.color.white_alpha_50);
		mScheduledExecutorService = Executors.newScheduledThreadPool(10);
		setOnPageChangeListener(onPageChangeListener);
		
		mShowNextHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				showNext();
			}
		};
		
		mShowNextRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				if (mStartFlipping)
				{
					mShowNextHandler.obtainMessage().sendToTarget();
				}
			}
		};
	}
	
	private ViewPager.OnPageChangeListener	onPageChangeListener	= new ViewPager.OnPageChangeListener()
																	{
																		
																		private boolean	mTemproraryStoped	= false;
																		
																		@Override
																		public void onPageSelected(int arg0)
																		{
																		}
																		
																		@Override
																		public void onPageScrolled(int arg0, float arg1, int arg2)
																		{
																			
																		}
																		
																		@Override
																		public void onPageScrollStateChanged(int state)
																		{
																			
																			switch (state)
																			{
																				case ViewPager.SCROLL_STATE_DRAGGING:
																				{
																					if (isAutoFlipping())
																					{
																						mTemproraryStoped = true;
																						stopFlipping();
																					}
																					break;
																				}
																				
																				case ViewPager.SCROLL_STATE_SETTLING:
																				{
																					mOnCyclePagerViewPageSettledListener.onCyclePagerViewSettled();
																					break;
																				}
																				
																				case ViewPager.SCROLL_STATE_IDLE:
																				{
																					if (mTemproraryStoped)
																					{
																						mTemproraryStoped = false;
																						startFlipping();
																					}
																					break;
																				}
																				
																				default:
																					break;
																			}
																		}
																		
																	};
}