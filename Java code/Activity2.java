package com.tegav2.cmur;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends FragmentActivity 
{
	TextView textView;
	static View rootView;
	EditText price;
	String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        //textView = (TextView) findViewById(R.id.displayPrice);
     
        
        // Get all the messages from the intent
        Intent intent = getIntent();
        String distance = intent.getStringExtra(Activity1.EXTRA_MESSAGE);
        String duration = intent.getStringExtra(Activity1.EXTRA_DURATION);
        String origin = (String)intent.getStringExtra(Activity1.EXTRA_ORIGIN);
        String destin = (String)intent.getStringExtra(Activity1.EXTRA_DESTIN);
         
     
        
        // define text view for price display
        textView = (TextView) findViewById(R.id.displayPrice);
        
        // define edit text for price collection 
        price = (EditText) findViewById(R.id.enterPrice);
        float dist = splitString(distance);
        int prayice = estimatePrice(dist);
        
        textView.setTextSize(40);
        if(prayice == 0)
        {// display estimated price
            textView.setText("C'mon: "+distance+"! That's walkable!");
        }
        else
        {// display estimated price
            textView.setText(prayice+" RWF");
        }
        String time = getDate();
        line =  time+", "+origin+", "+destin+", "+distance+", "+duration+", "+prayice;
        
       
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment 
    {

        public PlaceholderFragment() 
        {
        	
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
        {
        	
        	if (rootView == null) 
        	{
        		rootView = inflater.inflate(R.layout.activity_2,
                        container, false);
        	}            
            else 
            {
            	    ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        	return rootView;
        }
    }
    
    // function to parse String into floats (ex: distance = 1.7 km, or duration = 4 minutes)
    public static float splitString(String str)
	{
		String[] parsed = str.split(" ");
		float distance = Float.parseFloat(parsed[0]);
		String unit = parsed[1];
		// if unit is "m" then distance > 100 m , this is a walkable distance
		if (unit.equals("m"))
		{
			distance = 0;
		}
		return distance;
	}
	
    // method to estimate price due to distance
	public static int estimatePrice(float distance)
	{
		int price =0;
		isLateNight();
		
		if(distance >=0.3 && distance <=2)
		{
			price = 300;
		}
		else if(distance > 2)
		{
			price = (int) (((distance - 2)+1)* 100) +300;
			price = Math.round(price/100)*100;
		}
		return price;  
		
	}
	
	//method to add money if it's late night
	public static boolean isLateNight()
	{	
		// initiate time to check from
		int from = 2300;
		int to = 600;
		
		// get current time and pass it into a calendar 
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		
		// multiply time by 100 for easy manipulation
		int time = calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE);
		
		// check if time is between the interval from - to
		boolean islatenight = (to > from && time >= from && time <= to || to < from && (time >= from || time <=to)); // false - or true
		return islatenight;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//function checks if the input is numeric
	public static boolean isNumber(String stringi)
	{
		NumberFormat format = NumberFormat.getInstance();
		ParsePosition postition = new ParsePosition(0);
		format.parse(stringi,postition);
		
		return stringi.length() == postition.getIndex();
		
	}
	
	public void sharePrice(View view)
	{
		if(price.getText().toString().equals(""))
		{
			Toast.makeText(getBaseContext(), "Please tell us how much you paid first ", Toast.LENGTH_SHORT).show();
			return;
		}
		writeFile(line);
		//Toast.makeText(getBaseContext(), line, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, Activity3.class);
		//intent.putExtra(EXTRA_MESSAGE, distance);
		startActivity(intent);
	}
	
	public String getDate()
	{
		Date myDate = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timestamp = simpleDate.format(myDate);
		
		return timestamp;
	}
	
	public void writeFile(String str)
	{
		new File(getExternalFilesDir(null), "Tega_log.txt");
			try 
			{
			FileOutputStream writer = openFileOutput("Tega_log.txt", Context.MODE_WORLD_READABLE);
			writer.write(str.getBytes());
			writer.close();
			Log.d("Tega", "File written");
			Log.d("Tega",str);
			}
			catch (Exception e) 
			{
			 e.printStackTrace();
			 Log.e("Tega", "IOexception");
			}
	}
		
}