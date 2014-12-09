package com.tegav2.cmur;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class RemoteFileWriter 
{
	public void establishConnection()
	{
		try
		{
		HttpClient browser = new DefaultHttpClient();
		HttpPost psot = new HttpPost();
		HttpResponse response = browser.execute(psot);
		@SuppressWarnings("unused")
		HttpEntity entity = response.getEntity();
		}
		
		catch(Exception e)
		{
			Log.e("Tega","Remote file error");
		}
	}
}
