package com.app567go.model;

public class PhotoItem
{
	private String	mThumbnailUrl	= null;
	private String	mContentUrl		= null;
	
	public PhotoItem(String thumbnailUrl, String contentUrl)
	{
		mThumbnailUrl = thumbnailUrl;
		mContentUrl = contentUrl;
	}

	public String getThumbnailUrl()
	{
		return mThumbnailUrl;
	}
	
	public void setThumbnailUrl(String thunbnailUrl)
	{
		mThumbnailUrl = thunbnailUrl;
	}
	
	public String getContentUrl()
	{
		return mContentUrl;
	}
	
	public void setContentUrl(String contentUrl)
	{
		mContentUrl = contentUrl;
	}
	
}
