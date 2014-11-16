package com.app567go.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.app567go.R;
import com.app567go.manager.ResourceManager;
import com.app567go.model.PhotoItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoGridViewAdapter extends BaseAdapter
{
	private ArrayList<PhotoItem>	mPhotoItems	= null;
	
	public PhotoGridViewAdapter()
	{
		mPhotoItems = new ArrayList<PhotoItem>();
	}
	
	public void addItem(PhotoItem photoItem)
	{
		mPhotoItems.add(photoItem);
	}
	
	@Override
	public int getCount()
	{
		return mPhotoItems.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mPhotoItems.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView = (ImageView) convertView;
		if (imageView == null)
		{
			Context context = parent.getContext();
			imageView = new ImageView(context);
		}
		
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		int paddingLeft = resourceManager.getDimensionPixelSize(R.dimen.photo_item_padding_left);
		int paddingTop = resourceManager.getDimensionPixelSize(R.dimen.photo_item_padding_top);
		
		int width = resourceManager.getDimensionPixelSize(R.dimen.photo_item_width);
		int height = resourceManager.getDimensionPixelSize(R.dimen.photo_item_height);
		
		String url = mPhotoItems.get(position).getThumbnailUrl();
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		imageView.setScaleType(ScaleType.CENTER);
		imageView.setLayoutParams(new GridView.LayoutParams(width, height));
		imageView.setBackgroundResource(R.drawable.photo_item_bg);
		imageView.setPadding(paddingLeft, paddingTop, 0, 0);
		
		imageLoader.displayImage(url, imageView );
		
		return imageView;
		
	}
}
