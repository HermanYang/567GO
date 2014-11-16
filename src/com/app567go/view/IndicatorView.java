package com.app567go.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.app567go.R;

public class IndicatorView extends View
{
	
	private Drawable	mIdldDrawable			= null;
	private Drawable	mActiveDrawable			= null;
	
	private int			mIndicatorAmount		= 0;
	private int			mIndicatorMarginPixel	= 0;
	private int			mActiveIndex			= 0;
	private int			mIndicatorSize			= 0;
	
	public IndicatorView(Context context)
	{
		super(context);
		initialize();
	}
	
	public IndicatorView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public IndicatorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	public void setIndicatorAmount(int amount)
	{
		mIndicatorAmount = amount;
		
		adjustViewSize();
	}
	
	public void setActiveIndex(int index)
	{
		if (index < mIndicatorAmount)
		{
			mActiveIndex = index;
			invalidate();
		}
	}
	
	public void setIndicatorMargin(int pixel)
	{
		mIndicatorMarginPixel = pixel;
		
		adjustViewSize();
	}
	
	public void setIndicatorSize(int size)
	{
		mIndicatorSize = size;
		mActiveDrawable.setBounds(0, 0, mIndicatorSize, mIndicatorSize);
		mIdldDrawable.setBounds(0, 0, mIndicatorSize, mIndicatorSize);
		
		adjustViewSize();
	}
	
	private void initialize()
	{
		Resources resources = getResources();
		
		mIdldDrawable = resources.getDrawable(R.drawable.dot_gray);
		mActiveDrawable = resources.getDrawable(R.drawable.dot_bright);
		
	}
	
	private void adjustViewSize()
	{
		int heightOffset = 5;
		int width = mIndicatorAmount * mIndicatorSize + (mIndicatorAmount - 1) * mIndicatorMarginPixel;
		int height = mIndicatorSize + heightOffset;
		LayoutParams layoutParams = getLayoutParams();
		
		if (layoutParams != null)
		{
			layoutParams.width = width;
			layoutParams.height = height;
		}
		else
		{
			layoutParams = new ViewGroup.LayoutParams(width, height);
			setLayoutParams(layoutParams);
		}
		
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.save();
		
		for (int i = 0; i < mIndicatorAmount; ++i)
		{
			if (i == mActiveIndex)
			{
				mActiveDrawable.draw(canvas);
			}
			else
			{
				mIdldDrawable.draw(canvas);
			}
			
			canvas.translate(mIndicatorSize + mIndicatorMarginPixel, 0);
		}
		canvas.restore();
	}
	
}
