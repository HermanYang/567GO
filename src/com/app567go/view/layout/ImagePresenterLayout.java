package com.app567go.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app567go.R;
import com.app567go.adapter.CyclePagerAdapter;
import com.app567go.base.ConnectionChangeBroadcaster;
import com.app567go.listerner.OnCyclePagerViewSettledListener;
import com.app567go.listerner.OnNetworkStateChangedListener;
import com.app567go.manager.ResourceManager;
import com.app567go.view.CycleViewPager;
import com.app567go.view.IndicatorView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImagePresenterLayout extends FrameLayout implements OnNetworkStateChangedListener
{
	
	private final int		INDICATORVIEW_VERTICAL_OFFSET	= 10;
	private final int		FLIPPING_INTERVAL				= 3000;
	
	private CycleViewPager	mImageCycleViewPager			= null;
	private IndicatorView	mIndicatorView					= null;
	
	private String[]		mImageUrl						= null;
	
	public ImagePresenterLayout(Context context)
	{
		super(context);
		initialize();
	}
	
	public ImagePresenterLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initialize();
	}
	
	public ImagePresenterLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}
	
	private void initialize()
	{
		ConnectionChangeBroadcaster.register(this);
	}
	
	void setImageUrls(String[] urls)
	{
		
		Context context = getContext();
		ResourceManager resourceManager = ResourceManager.getInstance();
		mImageUrl = urls.clone();
		
		mImageCycleViewPager = new CycleViewPager(context);
		mIndicatorView = new IndicatorView(context);
		
		CyclePagerAdapter cyclePagerAdapter = new CyclePagerAdapter();
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		ImageView view = null;
		
		Builder builder = new DisplayImageOptions.Builder();
		builder.showImageForEmptyUri(R.drawable.default_presenter_image);
		builder.showImageOnFail(R.drawable.default_presenter_image);
		builder.showImageOnLoading(R.drawable.default_presenter_image);
		
		for (String url : urls)
		{
			view = new ImageView(context);
			view.setLayoutParams(layoutParams);
			ImageLoader.getInstance().displayImage(url, view, builder.build());
			cyclePagerAdapter.addView(view);
		}
		
		mImageCycleViewPager.setAdapter(cyclePagerAdapter);
		
		mImageCycleViewPager.setFlippingInterval(FLIPPING_INTERVAL);
		mImageCycleViewPager.setPageMargin(resourceManager.getDimensionPixelSize(R.dimen.cycleViewPager_page_margin));
		
		mIndicatorView.setIndicatorAmount(cyclePagerAdapter.getCount());
		mIndicatorView.setIndicatorSize(resourceManager.getDimensionPixelSize(R.dimen.indicatorView_item_size));
		mIndicatorView.setIndicatorMargin(resourceManager.getDimensionPixelSize(R.dimen.indicatorView_item_margin));
		mIndicatorView.setActiveIndex(mImageCycleViewPager.getCurrentItem());
		mImageCycleViewPager.startFlipping();
		
		mImageCycleViewPager.setOnPageSettledListener(new OnCyclePagerViewSettledListener()
		{
			@Override
			public void onCyclePagerViewSettled()
			{
				int index = mImageCycleViewPager.getCurrentItem();
				mIndicatorView.setActiveIndex(index);
			}
		});
		
		addView(mImageCycleViewPager, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		addView(mIndicatorView);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		
		// layout indicator view to botton and center
		int width = mIndicatorView.getWidth();
		int height = mIndicatorView.getHeight();
		int indecatorLeft = (right - left - width) / 2;
		int indecatorTop = (bottom - top - height);
		int indecatorRight = indecatorLeft + width;
		int indecatorBottom = bottom - top;
		
		indecatorTop -= INDICATORVIEW_VERTICAL_OFFSET;
		indecatorBottom -= INDICATORVIEW_VERTICAL_OFFSET;
		
		mIndicatorView.layout(indecatorLeft, indecatorTop, indecatorRight, indecatorBottom);
	}
	
	@Override
	public void onNetworkConnected()
	{
		CyclePagerAdapter cyclePagerAdapter = (CyclePagerAdapter) mImageCycleViewPager.getAdapter();
		Builder builder = new DisplayImageOptions.Builder();
		builder.showImageForEmptyUri(R.drawable.default_presenter_image);
		builder.showImageOnFail(R.drawable.default_presenter_image);
		builder.showImageOnLoading(R.drawable.default_presenter_image);
		
		for (int i = 0; i < cyclePagerAdapter.getCount(); ++i)
		{
			ImageView view = (ImageView) cyclePagerAdapter.getView(i);
			ImageLoader.getInstance().displayImage(mImageUrl[i], view, builder.build());
		}
	}
	
	@Override
	public void onNetworkDisconnected()
	{
		// TODO Auto-generated method stub
		
	}
	
}
