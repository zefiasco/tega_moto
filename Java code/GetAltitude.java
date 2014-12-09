package com.tegav2.cmur;

import java.sql.Date;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;

public class GetAltitude 
{

	public GetAltitude() 
	{
		// TODO Auto-generated constructor stub
	}
	// Function that retrieve altitude at each point 
	public double getAlt(Location point)
	{
		double altitude;
		altitude = point.getAltitude();	
		
		return altitude;
	}
	
	/**
	 * 
	 * Function to find Location Object
	 * @return location
	 */
	public Location getLocation(LatLng point)
	{
		//Convert LatLng to Location
		  Location location = new Location("here");
		  location.setLatitude(point.latitude);
		  location.setLongitude(point.longitude);
		  location.setTime(new Date(0).getTime()); //Set time as current Date
		  
		  return location;
	}
	
	/**
	 * 
	 * Function to add new altitude to the altitude arraylist
	 * @return altitudes arrayList
	 */
	public ArrayList<Double> addLat(double lat , ArrayList<Double> altitudes)
	{
		altitudes.add(lat);
		return altitudes;
		
	}
	
	/**
	 * 
	 * Function to calculate average altitude
	 * @return average altitude
	 */
	public double calculateAverageAlt(ArrayList <Double> altitudes) 
	{
		  Double sum = (double) 0;
		  
		  if(!altitudes.isEmpty()) 
		  {
		    for (double mark : altitudes) 
		    {
		        sum += mark;
		    }
		    return sum.doubleValue() / altitudes.size();
		  }
		  return sum;
	}
	
	// function to determine if the journey is uphill or not 
	public boolean isUphill(double start, double average) 
	{	
		// if grade (slope) is superior to 100% then it was uphill
		double grade = computeSlope(start, average);
		  if (grade > 100)
		  {
			  return true;
		  }
		  else
			  return false;
	}
	
	// function to determine if the journey is down hill or not 
		public boolean isDownHill(double start, double average) 
		{	
			// if grade (slope) is superior to 100% then it was uphill
			double grade = computeSlope(start, average);
			  if (grade < 100)
			  {
				  return true;
			  }
			  else
				  return false;
		}
	public double computeSlope(double start, double average)
	{
		double slope = 100 * (average / start);
		return slope;
	}
}
