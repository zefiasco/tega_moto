package com.tegav2.cmur;
import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileWritter 
{
	public static String LOG_TAG = "Tega error";
	Context context;
	String filename = "tegaReports.txt";
	
	File file = new File(context.getFilesDir(), filename);
	
	public static void writeToFile(String filename, String line)
	{
		FileOutputStream outputStream;
		try
		{
			outputStream = new FileOutputStream(filename);
			outputStream.write(line.getBytes());
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/* delete the file */
	public static void deleteFile(String filename, Context context)
	{
		context.deleteFile(filename);
	}
	
	/* Check if external storage is  writable */
	public static boolean isExternalStorageWritable()
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
