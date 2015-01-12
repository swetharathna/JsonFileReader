package com.cmpe277.jsonreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private String[] name;
	private String[] brand;
	private String[] upc;
	private String[] seller;
	private String[] price;
	private Bitmap[] btmp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new MyAsyncTask().execute();
	}

	private class MyAsyncTask extends AsyncTask<String, String, String>{
		
		protected String doInBackground(String... arg0){
			String jsonContent = null;
			InputStream inputStream = null;
			BufferedReader reader = null;
			
			try {
				    inputStream = getAssets().open("semantic3.json");
		        	int size = inputStream.available();
		        	reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), size);
		        	
		        	StringBuilder theStringBuilder = new StringBuilder(size);
		        	String line = null;
		        	
		        	while((line = reader.readLine()) != null)
		        	{
		        		theStringBuilder.append(line+"\n");
		        	}
		        	jsonContent = theStringBuilder.toString();
					/*Log.i("MyAsyncTask- jsonCOntent size", Integer.toString(jsonContent.length()));
					  Log.i("MyAsyncTask- jsonCOntent", jsonContent);
					*/

				}catch(IOException e)
					{
						Log.i("MyAsyncTask", "raised exception");
			        	e.printStackTrace();
					}finally {
						Log.i("MyAsyncTask", " at finally");
						try{if(inputStream != null)inputStream.close();
						if(reader != null)reader.close();}
						catch(Exception e){}
					}
	         
	        JSONObject jsonObject;
	        try{
					Log.i("MyAsyncTask- ", "Retrieve jsonContent");
					
		        	jsonObject = new JSONObject(jsonContent);
	
		        	JSONArray queryArray = jsonObject.getJSONArray("results");
		        	int resultSize = queryArray.length();
	
					name = new String[resultSize];
					brand = new String[resultSize];
					upc = new String[resultSize];
					seller = new String[resultSize];
					price = new String[resultSize];
					
		        	for(int i=0; i< queryArray.length(); i++)
		        	{
		        		JSONObject jsonAttributes = queryArray.getJSONObject(i);
		        		
		        		name[i] = jsonAttributes.getString("name");
		        		brand[i]= jsonAttributes.getString("brand");
		        		upc[i]= jsonAttributes.getString("upc");
		        		
		        		JSONObject siteObj = jsonAttributes.getJSONObject("sitedetails");
		        		
		        		// read latestoffers value set from json
		        		JSONArray offersArrayObj = siteObj.getJSONArray("latestoffers");
		        		JSONObject sellerObj = offersArrayObj.getJSONObject(0);
		        		price[i] = sellerObj.getString("price");
		        		seller[i] = sellerObj.getString("seller");
		        		
		        /*		Log.i("Name", jsonAttributes.getString("name"));	
		        		Log.i("Brand", jsonAttributes.getString("brand"));	
		        		Log.i("Upc", jsonAttributes.getString("upc"));	
		        		Log.i("Price", sellerObj.getString("price"));	
		        		Log.i("Seller", sellerObj.getString("seller"));
		         */	
		        	}
	 
	            }
		        catch (JSONException ex) {
					Log.i("MyAsyncTask", " caught excpt at JSON");
		            ex.printStackTrace();
		            return null;
		        }

	        // Read from image URL and creates a BitMap for each image.
	        createBitMapFromURL();
	        
	        return jsonContent;
		}

		// Function to create image bitmap from the product image urls.
		void createBitMapFromURL()
		{
        	Log.i("MyAsyncTask", "createBitMapFromURL called");

		  try{
			
			btmp = new Bitmap[4];
			
			// URLs of the product images
			URL url1 = new URL("http://s3.amazonaws.com/shopperplus/uploads/new_category/image/2538/medium_Samsung-Galaxy-Tab-10-1.jpg");
			URL url2 = new URL("https://gigaom2.files.wordpress.com/2012/01/galaxy-tab-7-7.jpeg");
			URL url3 = new URL("https://gigaom2.files.wordpress.com/2013/06/galaxy-tab-3-10-1-inch.jpg?w=300&h=200&crop=1");
			URL url4 = new URL("http://ecx.images-amazon.com/images/I/71Z8hdqSxrL._SX522_.jpg");
	
	        btmp[0] = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
	        btmp[1] = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
	        btmp[2] = BitmapFactory.decodeStream(url3.openConnection().getInputStream());
	        btmp[3] = BitmapFactory.decodeStream(url4.openConnection().getInputStream());
	        
	        }catch(IOException e)
	        {
	        	Log.i("MyAsyncTask", "createBitMapFromURL-raised exception");
	            e.printStackTrace();	
	        }
	    }
	
		// Function to load the images into textView from the Bitmap
		void loadImages()
		{	
        	Log.i("MyAsyncTask", "loadImages called");

			TextView textView1 = (TextView)findViewById(R.id.textView1);
			TextView textView2 = (TextView)findViewById(R.id.textView2);
			TextView textView3 = (TextView)findViewById(R.id.textView3);
			TextView textView4 = (TextView)findViewById(R.id.textView4);
			 
			Drawable BD1 = new BitmapDrawable(getResources(), btmp[0]);
			Drawable BD2 = new BitmapDrawable(getResources(), btmp[1]);
			Drawable BD3= new BitmapDrawable(getResources(), btmp[2]);
			Drawable BD4 = new BitmapDrawable(getResources(), btmp[3]);

 			BD1.setBounds(0, 0, (int)(BD1.getIntrinsicWidth()*0.5), 
                    (int)(BD1.getIntrinsicHeight()*0.5));

	    	BD2.setBounds(0, 0, (int)(BD2.getIntrinsicWidth()*0.5), 
                    (int)(BD2.getIntrinsicHeight()*0.5));

			BD3.setBounds(0, 0, (int)(BD3.getIntrinsicWidth()*0.43), 
                    (int)(BD3.getIntrinsicHeight()*0.4));

			BD4.setBounds(0, 0, (int)(BD4.getIntrinsicWidth()*0.25), 
                    (int)(BD4.getIntrinsicHeight()*0.25));
			
			// This would draw the images left of the textview
			textView1.setCompoundDrawables(BD1, null, null, null);
			textView2.setCompoundDrawables(BD2, null, null, null);
			textView3.setCompoundDrawables(BD3, null, null, null);
			textView4.setCompoundDrawables(BD4, null, null, null);
			
		}
		
		// Function to add the product details read from JSON to the UI
		void addJSONDataToView()
		{
        	Log.i("MyAsyncTask", "addJSONDataToView called");

			TextView textView1 = (TextView)findViewById(R.id.textView1);
			TextView textView2 = (TextView)findViewById(R.id.textView2);
			TextView textView3 = (TextView)findViewById(R.id.textView3);
			TextView textView4 = (TextView)findViewById(R.id.textView4);
			
			String detail1 = new String("BRAND :"+ brand[0] + "\n" + "NAME :" + name[0] + "\n" + "UPC :" + upc[0] + "\n"+ "SELLER :" + seller[0] + "\n" + "PRICE :" + price[0] + "\n");
			String detail2 = new String("BRAND :"+ brand[1] + "\n" + "NAME :" + name[1] + "\n" + "UPC :" + upc[1] + "\n"+ "SELLER :" + seller[1] + "\n" + "PRICE :" + price[1] + "\n");
			String detail3 = new String("BRAND :"+ brand[2] + "\n" + "NAME :" + name[2] + "\n" + "UPC :" + upc[2] + "\n"+ "SELLER :" + seller[2] + "\n" + "PRICE :" + price[2] + "\n");
			String detail4 = new String("BRAND :"+ brand[3] + "\n" + "NAME :" + name[3] + "\n" + "UPC :" + upc[3] + "\n"+ "SELLER :" + seller[3] + "\n" + "PRICE :" + price[3] + "\n");

			textView1.setText(detail1);
			textView2.setText(detail2);
			textView3.setText(detail3);
			textView4.setText(detail4);
			
		}
		
		protected void onPostExecute(String result)
		{
			Log.i("MyAsyncTask", "onPostExecute");
			loadImages();
			addJSONDataToView();
		}
 }
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}
}
