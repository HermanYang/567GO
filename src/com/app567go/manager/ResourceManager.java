package com.app567go.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;

import com.app567go.R;
import com.app567go.controller.Controller;
import com.app567go.listerner.OnHttpResponseListener;
import com.app567go.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

public class ResourceManager
{
	private static ResourceManager	mInstance				= null;
	private static Object			INSTANCE_LOCK			= new Object();
	
	private final static String		SHARE_FILE_NAME			= "share_pic.jpg";
	private final static String		SERVER_URL				= "http://121.199.47.174/api/section_data";
	private final static String		SECTION_DATA_FILE_NAME	= "SectionData.json";
	private Resources				mLocalResources			= null;
	private JSONObject				mSectionData			= null;
	private AsyncHttpClient			mClient					= null;
	private AssetManager			mAssertManager			= null;
	private RequestHandle			mRequestHandle			= null;
	
	private boolean					mInitialized			= false;
	
	public static ResourceManager getInstance()
	{
		if (mInstance == null)
		{
			synchronized (INSTANCE_LOCK)
			{
				if (mInstance == null)
				{
					mInstance = new ResourceManager();
				}
			}
		}
		return mInstance;
	}
	
	public static void destroyInstance()
	{
		mInstance = null;
	}
	
	private ResourceManager()
	{
		Controller controller = Controller.getInstance();
		Context appContext = controller.getAppContext();
		mLocalResources = appContext.getResources();
		mAssertManager = appContext.getAssets();
		mClient = new AsyncHttpClient();
		mClient.setTimeout(5);
		
		mRequestHandle = mClient.get(SERVER_URL, new JsonHttpResponseHandler("utf-8")
		{
			
			@Override
			public void onSuccess(JSONObject response)
			{
				if (response == null)
				{
					return;
				}
				
				mSectionData = response;
				Controller controller = Controller.getInstance();
				Context appContext = controller.getAppContext();
				try
				{
					Utils.writeFileString(appContext, SECTION_DATA_FILE_NAME, mSectionData.toString());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				mInitialized = true;
			}
			
		});
		
	}
	
	public void setCallBack(final Runnable callBack)
	{
		final int delay = 1000;
		final int timeout = 5000;
		final Handler handler = new Handler();
		final Runnable runnable = new Runnable()
		{
			
			@Override
			public void run()
			{
				
				if (mInitialized)
				{
					handler.post(callBack);
				}
				else
				{
					handler.postDelayed(this, delay);
				}
			}
		};
		
		Runnable timeoutRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				Controller controller = Controller.getInstance();
				Context appContext = controller.getAppContext();
				InputStream inputStream = null;
				String jsonString;
				
				mRequestHandle.cancel(true);
				
				try
				{
					jsonString = Utils.readFileString(appContext, SECTION_DATA_FILE_NAME);
					mSectionData = new JSONObject(jsonString);
					mInitialized = true;
				}
				catch (IOException e)
				{
					try
					{
						inputStream = mAssertManager.open(SECTION_DATA_FILE_NAME);
						jsonString = Utils.convertStreamToString(inputStream, 1024, "utf-8");
						mSectionData = new JSONObject(jsonString);
						mInitialized = true;
					}
					catch (IOException ioException)
					{
						ioException.printStackTrace();
					}
					catch (JSONException jsonException)
					{
						jsonException.printStackTrace();
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		};
		
		handler.postDelayed(runnable, delay);
		handler.postDelayed(timeoutRunnable, timeout);
	}
	
	public String getString(int id)
	{
		return mLocalResources.getString(id);
	}
	
	public int getInt(int id)
	{
		return mLocalResources.getInteger(id);
	}
	
	public int getDimensionPixelSize(int id)
	{
		return mLocalResources.getDimensionPixelSize(id);
	}
	
	public void getHttpRequest(String url, final OnHttpResponseListener callback)
	{
		mClient.get(url, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(int statusCode, String content)
			{
				super.onSuccess(statusCode, content);
				callback.onHttpResponse(content);
			}
			
		});
	}
	
	public String[] getStringArray(int id)
	{
		return mLocalResources.getStringArray(id);
	}
	
	public Drawable getDrawable(int id)
	{
		return mLocalResources.getDrawable(id);
	}
	
	public String[] getJSONStringArray(String id)
	{
		JSONArray jsonArray;
		try
		{
			jsonArray = mSectionData.getJSONArray(id);
			int length = jsonArray.length();
			String[] strings = new String[length];
			for (int i = 0; i < length; ++i)
			{
				strings[i] = jsonArray.getString(i);
			}
			
			return strings;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getJSONString(String id)
	{
		String result = null;
		try
		{
			result = mSectionData.getString(id);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject getJsonObject(String id)
	{
		JSONObject result = null;
		try
		{
			result = mSectionData.getJSONObject(id);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String getShareFilePath()
	{
		String path = "";
		try
		{
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().exists())
			{
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SHARE_FILE_NAME;
			}
			else
			{
				path = Controller.getInstance().getAppContext().getFilesDir().getAbsolutePath() + "/" + SHARE_FILE_NAME;
			}
			File file = new File(path);
			
			if (file.exists())
			{
				file.delete();
			}
			
			file.createNewFile();
			Bitmap pic = BitmapFactory.decodeResource(mLocalResources, R.drawable.ic_launcher);
			FileOutputStream fos = new FileOutputStream(file);
			pic.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return path;
	}
}
