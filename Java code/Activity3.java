package com.tegav2.cmur;
import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Activity3 extends Activity implements OnRatingBarChangeListener 
{
	static View rootView;
	TextView textView;
	RatingBar ratingbar;
	Button button;
	public static String LOG_TAG = "Tega error";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        
        // initialize both the TextView and the Rating Bar
        textView = (TextView) findViewById(R.id.textView1);
        ((RatingBar) findViewById(R.id.ratingBar1)).setOnRatingBarChangeListener(this);
        button = (Button) findViewById(R.id.button1);
        
       
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
        		rootView = inflater.inflate(R.layout.activity_3,
                        container, false);
        	}            
            else 
            {
            	    ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        	return rootView;
        }
    }
    
    public void rateDriver(View view)
    {	
    	Intent intent = new Intent(this, Activity1.class);
    	Toast.makeText(getBaseContext(), "Thanks! Have a nice ride!", Toast.LENGTH_LONG).show();
		startActivity(intent);
		return;
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) 
	{
		final int numberOfStars = ratingBar.getNumStars();
		textView.setText(rating +"/"+numberOfStars);
		
	}
	
	public void writeToFile(String filename, String line)
	{
		
		FileOutputStream outputStream;
		try
		{
			outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
			outputStream.write(line.getBytes());
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/* delete the file */
	public void deleteFile(String filename, Context context)
	{
		context.deleteFile(filename);
	}
	
	/* Check if external storage is  writable */
	public boolean isExternalStorageWritable()
	{
		String STATE = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(STATE))
		{
			return true;
		}
		return false;
	}
	
	/* check if external storage is at least readable */
	public static boolean isExternalStorageReadable()
	{
		String STATE = Environment.getExternalStorageState();
		
		if(Environment.MEDIA_MOUNTED.equals(STATE) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(STATE))
		{
			return true;
		}
		return false;
	}
	
	/* get Downloads directory */
	public File getStorageDir(String storageName)
	{
		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), storageName);
		
		if(!file.mkdirs())
		{
			Log.e(LOG_TAG, "Directory not Created");
		}
		return file;
	}
}
