package com.app567go.view.layout;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.app567go.R;
import com.app567go.adapter.PhotoGridViewAdapter;
import com.app567go.constant.Command;
import com.app567go.constant.ResourceId;
import com.app567go.controller.Controller;
import com.app567go.manager.ResourceManager;
import com.app567go.model.PhotoItem;

public class PhotoSectionLayout extends FrameLayout
{
	private int			COLUMN_NUM		= 3;
	private GridView	mPhotoGridView	= null;
	
	public PhotoSectionLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initalize();
	}
	
	public PhotoSectionLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initalize();
	}
	
	public PhotoSectionLayout(Context context)
	{
		super(context);
		initalize();
	}
	
	private void initalize()
	{
		ResourceManager resourceManager = ResourceManager.getInstance();
		
		int padding = resourceManager.getDimensionPixelSize(R.dimen.container_padding);
		
		mPhotoGridView = new GridView(getContext());
		mPhotoGridView.setNumColumns(COLUMN_NUM);
		mPhotoGridView.setBackgroundResource(R.color.white);
		mPhotoGridView.setPadding(padding, padding, padding, padding);
		mPhotoGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mPhotoGridView.setGravity(Gravity.CENTER);
		
		mPhotoGridView.setVerticalSpacing(resourceManager.getDimensionPixelSize(R.dimen.container_padding));
		
		mPhotoGridView.setOnItemClickListener(new PhotoGridOnItemClickListener());
		
		JSONObject jsonObject = null;
		String[] photoData = resourceManager.getJSONStringArray(ResourceId.PHOTO_SECTION_DATA);
		PhotoGridViewAdapter gridViewAdapter = new PhotoGridViewAdapter();
		
		for (String jsonString : photoData)
		{
			try
			{
				jsonObject = new JSONObject(jsonString);
				String thumbnailUrl = jsonObject.getString("thumbnail_url");
				String contentUrl = jsonObject.getString("content_url");
				PhotoItem photoItem = new PhotoItem(thumbnailUrl, contentUrl);
				
				gridViewAdapter.addItem(photoItem);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		
		mPhotoGridView.setAdapter(gridViewAdapter);
		
		addView(mPhotoGridView);
	}
	
	private class PhotoGridOnItemClickListener implements OnItemClickListener
	{
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			PhotoGridViewAdapter adapter = (PhotoGridViewAdapter) parent.getAdapter();
			PhotoItem item = (PhotoItem) adapter.getItem(position);
			Controller controller = Controller.getInstance();
			ArrayList<Object> args = new ArrayList<Object>();
			args.add(item.getContentUrl());
			controller.dealWith(Command.SHOW_WEB_CONTENT, args);
		}
		
	}
	
}
