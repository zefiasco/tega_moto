 package com.tegav2.cmur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Activity1 extends FragmentActivity 
{
	 /** The following project class was built using tutorials provided by George Mathew
	  * 
	 * George Mathew is credited for all code similar to his own implementation
	 * The tutorials he designed can be found at 
	 * http://wptrafficanalyzer.in/blog/drawing-driving-route-directions-between-two-locations-using-google-directions-in-google-map-android-api-v2/
	 * The title of the tutorial is "Drawing driving route directions between two locations using Google Directions in Google Map Android API V2"
	 * My sincere gratitude to George Mathew and appreciation for his guidance 
	 * 
	 * Modifications were made by me:
	 * 
	 * Fiacre MUSHIMIRE
 
	 */
	
	
	GoogleMap map;
	GoogleMap googlemap;
	ArrayList<LatLng> markerPoints;
	TextView display_text;
	String distance;
	Context context;
	String duration;
	String origy;
	String desty;
	LatLng origin;
	LatLng dest;
	ProgressBar spinner;
	
	
	MarkerOptions markerOptions;
	private final LatLng KIGALI = new LatLng(-1.955140, 30.0866008   );
	public final static String EXTRA_MESSAGE ="com.tegav2.cmur.DISTANCE";
	public final static String EXTRA_TIME ="com.tegav2.cmur.TIME";
	public final static String EXTRA_DURATION ="com.tegav2.cmur.DURATION";
	public final static String EXTRA_ORIGIN ="com.tegav2.cmur.ORIGIN";
	public final static String EXTRA_DESTIN ="com.tegav2.cmur.DESTIN";
	/** This method is called when the user click the button "Go!" */
	public void guessPrice(View view)
	{
		if(distance == null)
		{
			Toast.makeText(getBaseContext(), "Please select two location first", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, Activity2.class);
		intent.putExtra(EXTRA_MESSAGE, distance);
		intent.putExtra(EXTRA_DURATION, duration);
		intent.putExtra(EXTRA_ORIGIN, origy);
		intent.putExtra(EXTRA_DESTIN, desty);
		intent.putExtra(EXTRA_TIME, distance);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//map.setMapType(GoogleMap.MAP_TYPE_HYBRID);		
		
		//googlemap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		display_text = (TextView) findViewById(R.id.tv_distance_time);
		
		// Initializing 
		markerPoints = new ArrayList<LatLng>();
		
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		
		
		// Getting Map for the SupportMapFragment
		map = fm.getMap();
		
		// Enable MyLocation Button in the Map
		map.setMyLocationEnabled(true);		
		
		//CheckConnectivity connected = new CheckConnectivity();		
		if(!CheckConnectivity.isConnected(this))
		{
			Toast.makeText(getBaseContext(), "You are not connected on internet", Toast.LENGTH_SHORT).show();
			alert();
			Log.d("TEGA","No access to Internet");
			return;
		}
		
		// Setting onclick event listener for the map
		map.setOnMapClickListener(new OnMapClickListener() 
		{
			
			@Override
			public void onMapClick(LatLng point) 
			{
				
				
				// Already two locations				
				if(markerPoints.size()>1){
					markerPoints.clear();
					map.clear();					
				}
				
				// Adding new item to the ArrayList
				markerPoints.add(point); 
				
				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();
				
				// Setting the position of the marker
				options.position(point);
				
				/** 
				 * For the start location, the color of marker is BLUE and
				 * for the end location, the color of marker is RED.
				 */
				if(markerPoints.size()==1)
				{
					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				}
				else if(markerPoints.size()==2)
				{
					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				}
							
				
				// Add new marker to the Google Map Android API V2
				map.addMarker(options);
				
				// Checks, whether start and end locations are captured
				if(markerPoints.size() >= 2)
				{					
					origin = markerPoints.get(0);
					dest = markerPoints.get(1);
					
					// get latitude and longitude from origin
					double laty = origin.latitude;
					double longy = origin.longitude;
					
					origy = laty+", "+longy;
					
					// get latitude and longitude from destination
					laty = origin.latitude;
					longy = origin.longitude;
					
					desty = laty+", "+longy;
					
					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(origin, dest);
					
					//spinner.setVisibility(View.VISIBLE);
					//spinner.setVisibility(View.GONE);
					
					DownloadTask downloadTask = new DownloadTask();					
					
					Toast.makeText(getBaseContext(), "Please wait...", Toast.LENGTH_LONG).show();
					
					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
					
					
				}
				
			}
		});
		//set map to Hybrid map-view and zoom to Kigali
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		//CameraUpdate cameraupdate = CameraUpdateFactory.newLatLng(KIGALI);
		CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(KIGALI,17);
		map.animateCamera(cameraupdate);
		
	}
	
	private String getDirectionsUrl(LatLng origin,LatLng dest)
	{
					
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null)
                {
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }
        catch(Exception e)
        {
                Log.d("Exception while downloading url", e.toString());
        }
        finally
        {
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>
	{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) 
		{
			ArrayList<LatLng> points = null;
			//ArrayList<Float> altitudes = null;
			PolylineOptions lineOptions = null;
			new MarkerOptions();
			distance = "";
			duration = "";
			
			
			
			if(result.size()<1)
			{
				Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
				
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++)
			{
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();				
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	
					
					if(j==0){	// Get distance from the list
						distance = (String)point.get("distance");						
						continue;
					}else if(j==1){ // Get duration from the list
						duration = (String)point.get("duration");
						continue;
					}
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					//Float altitude = getAltitude(position);
					points.add(position);
					//altitudes.add(altitude);
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(4);
				lineOptions.color(Color.RED);	
				
			}
			
			  
			
			display_text.setText("Distance:"+distance + ",\t Duration:"+duration);
			
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);							
		}			
    }   
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void alert()
	{
		AlertDialog.Builder alertDiag = new AlertDialog.Builder(Activity1.this);
		alertDiag.setTitle("Tega - error");
		alertDiag.setMessage("\t You are not connected to internet!");
		
		// set neutral button: Exit the app message
		
		         alertDiag.setNeutralButton("Exit",new DialogInterface.OnClickListener() 
		         {		
		                public void onClick(DialogInterface dialog,int id) 
		                {
		                    // exit the app and go to the HOME		
		                    Activity1.this.finish();		
		                }
		
		            });		
		          
		         AlertDialog alertDialog = alertDiag.create();
		
		         // show alert		
		         alertDialog.show();		
	}
}

// Class that find name of the place
