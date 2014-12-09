package com.tegav2.cmur;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnectivity 
{

	public CheckConnectivity() 
	{
		// TODO Auto-generated constructor stub
	}
	
	// Function that check if the device is connected on Internet 
	public static boolean isConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	// Function that check if the device's GPS is ON
	public static boolean isGPSEnabled(Context context)
	{
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
