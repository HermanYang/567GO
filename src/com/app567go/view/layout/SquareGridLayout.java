package com.app567go.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SquareGridLayout extends ViewGroup
{

	private int	mHorizontalSpacing	= 0;
	private int	mVerticalSpacing	= 0;
	private int	mNumColums			= 0;

	public SquareGridLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}

	public SquareGridLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}

	public SquareGridLayout(Context context)
	{
		super(context);
		initialize();
	}

	private void initialize()
	{
	}

	public int getNumColumns()
	{
		return mNumColums;
	}

	public int getVerticalSpacing()
	{
		return mVerticalSpacing;
	}

	public int getHorizontalSpacing()
	{
		return mHorizontalSpacing;
	}

	public void setNumColumns(int numColumn)
	{
		mNumColums = numColumn;
	}

	public void setVerticalSpacing(int verticalSpacing)
	{
		mVerticalSpacing = verticalSpacing;
	}

	public void setHorizontalSpacing(int horizontalSpacing)
	{
		mHorizontalSpacing = horizontalSpacing;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int count = getChildCount();

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int numRows = count / 3;

		if (count % mNumColums != 0)
		{
			numRows += 1;
		}

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int childWidth = (width - paddingLeft - paddingRight - ((mNumColums - 1) * mHorizontalSpacing)) / mNumColums;
		int childHeight = (height - paddingTop - paddingBottom - ((numRows - 1) * mVerticalSpacing)) / numRows;

		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);

		for (int i = 0; i < count; i++)
		{
			View view = getChildAt(i);
			view.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{

		int count = getChildCount();

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();

		int left = paddingLeft;
		int top = paddingTop;

		for (int i = 0; i < count; i++)
		{
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();

			view.layout(left, top, left + width, top + height);

			// move on for next child
			if ((i + 1) % mNumColums != 0)
			{
				left += width + mHorizontalSpacing;
			}
			else
			{
				left = paddingLeft;
				top += height + mVerticalSpacing;
			}

		}
	}

}
