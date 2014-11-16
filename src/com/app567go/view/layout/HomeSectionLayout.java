package com.app567go.view.layout;

import com.app567go.constant.ResourceId;
import com.app567go.manager.ResourceManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class HomeSectionLayout extends FrameLayout
{

	private ImagePresenterLayout	mImagePresenterLayout	= null;
	private GuidanceLayout			mGuidanceLayout			= null;
	private double					mImageAspectRatio		= 0.375;

	public HomeSectionLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}

	public HomeSectionLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}

	public HomeSectionLayout(Context context)
	{
		super(context);
		initialize();
	}

	private void initialize()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		mImagePresenterLayout = new ImagePresenterLayout(getContext());
		mGuidanceLayout = new GuidanceLayout(getContext());
		
		mImagePresenterLayout.setImageUrls(resourceManager.getJSONStringArray(ResourceId.HOME_SECTION_IMAGE_URLS));
		
		addView(mImagePresenterLayout);
		addView(mGuidanceLayout);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// dynamic confirm children views' size
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		int imagePresenterLayoutHeight = (int) (width * mImageAspectRatio);

		int imagePresenterLayoutHeightMeasureSpc = MeasureSpec.makeMeasureSpec(imagePresenterLayoutHeight, MeasureSpec.AT_MOST);

		int guidanceLayoutHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height - imagePresenterLayoutHeight, MeasureSpec.AT_MOST);

		mImagePresenterLayout.measure(widthMeasureSpec, imagePresenterLayoutHeightMeasureSpc);
		mGuidanceLayout.measure(widthMeasureSpec, guidanceLayoutHeightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);

		// dynamic place children views.
		int imagePresenterLayoutBottom = mImagePresenterLayout.getMeasuredHeight();
		
		mImagePresenterLayout.layout(left, top, right, imagePresenterLayoutBottom);

		mGuidanceLayout.layout(left, imagePresenterLayoutBottom, right, bottom);

	}

}
