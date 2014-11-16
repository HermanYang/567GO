package com.app567go.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

import com.app567go.R;
import com.app567go.controller.Controller;

public class Utils
{
	public static int getScreenWidth()
	{
		Controller controller = Controller.getInstance();
		Activity activity = controller.getForegroundActivity();
		return activity.getWindowManager().getDefaultDisplay().getWidth();
	}
	
	public static int getScreenHeight()
	{
		Controller controller = Controller.getInstance();
		Activity activity = controller.getForegroundActivity();
		return activity.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public static String convertStreamToString(InputStream is, int bufferSize, String encoding) throws IOException
	{
		
		Reader reader = new BufferedReader(new InputStreamReader(is, encoding));
		StringBuffer content = new StringBuffer();
		char[] buffer = new char[bufferSize];
		int n;
		
		while ((n = reader.read(buffer)) != -1)
		{
			content.append(buffer, 0, n);
		}
		
		return content.toString();
	}
	
	public static void writeFileString(Context context, String fileName, String content) throws IOException
	{
		FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		byte[] bytes = content.getBytes();
		fileOutputStream.write(bytes);
		fileOutputStream.close();
	}
	
	public static void writeJpegImage(Context context, String fileName, Bitmap bitmap) throws IOException
	{
		FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
	}
	
	public static String readFileString(Context context, String fileName) throws IOException
	{
		String content;
		FileInputStream fileInputStream = context.openFileInput(fileName);
		int length = fileInputStream.available();
		byte[] buffer = new byte[length];
		fileInputStream.read(buffer);
		content = EncodingUtils.getString(buffer, "uft-8");
		fileInputStream.close();
		return content;
	}
	
}
