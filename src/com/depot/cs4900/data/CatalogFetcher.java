package com.depot.cs4900.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.*;

import com.depot.cs4900.Constants;



public class CatalogFetcher {

	private static final String CLASSTAG = CatalogFetcher.class.getSimpleName();

	private ArrayList<CatalogEntry> catalog;

	public CatalogFetcher() {
		catalog = new ArrayList<CatalogEntry>();
	}

	/**
	 * Parse JSON.
	 * 
	 * @param  resp
	 */
	private ArrayList<CatalogEntry> parseCatalog(String resp) {
		try {
			JSONArray products = new JSONArray(resp);

			for (int i = 0; i < products.length(); i++) {
				JSONObject product = products.getJSONObject(i);
				int id = product.getInt("id");
				String title = product.getString("title");
				String description = product.getString("description");
				String price = product.getString("price");
				//JSONObject popularity = product.getJSONObject("popularity");
				String popularity = product.getString("popularity");
				
				CatalogEntry ce = new CatalogEntry();
				ce.set_product_id(new Integer(id).toString());
				ce.set_title(title);
				ce.set_description(description);
				ce.set_price(price);
				ce.set_popularity(popularity);
				
				catalog.add(ce);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return catalog;
	}

	public ArrayList<CatalogEntry> getCatalogFromFile(String fileName) {
		long startTime = System.currentTimeMillis();
		// ArrayList<CatalogEntry> results = null;
		BufferedReader reader  = null;

		try {
			// Read contents of the JSON file into a JSON string
			reader = new BufferedReader( new FileReader (fileName));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }

			// Parse the JSON string
			catalog = parseCatalog(stringBuilder.toString());
		} catch (Exception e) {
			Log.e(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG, e);
		}
		finally
		{
			try
			{
				if ( reader != null)
					reader.close( );
			}
			catch ( IOException e)
			{
			}
	     }
		long duration = System.currentTimeMillis() - startTime;
		Log.v(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
				+ " send request and parse reviews duration - " + duration);
		return catalog;
	}
	
	private void persist(String productsInJson) {
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter( new FileWriter(Constants.CATALOG_JSON_FILE_NAME));
			writer.write( productsInJson );

		}
		catch ( IOException e)
		{
			Log.d(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
					+ "Failed to write out file " + e.getMessage());
		}
		finally
		{
			try
			{
				if ( writer != null)
					writer.flush();
					writer.close( );
			}
			catch ( IOException e)
			{
			}
	     }
	}
	
	public void replace(String fileName, CatalogEntry ce){
		long startTime = System.currentTimeMillis();
		// ArrayList<CatalogEntry> results = null;
		BufferedReader reader  = null;

		try {
			// Read contents of the JSON file into a JSON string
			reader = new BufferedReader( new FileReader (fileName));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }

		    JSONArray products = new JSONArray(stringBuilder.toString());
		    for (int i = 0; i < products.length(); i++) {
				JSONObject product = products.getJSONObject(i);
				if (product.getInt("id") == Integer.parseInt(ce.get_product_id())){
						product.put("title", ce.get_title());
						product.put("description", ce.get_description());
						product.put("price", ce.get_price());
						product.put("popularity", ce.get_popularity());
				}
		    }
		    
		    persist(products.toString());
		    
		} catch (Exception e) {
			Log.e(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG, e);
		}
		finally
		{
			try
			{
				if ( reader != null)
					reader.close( );
			}
			catch ( IOException e)
			{
			}
	     }
		long duration = System.currentTimeMillis() - startTime;
		Log.v(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
				+ " send request and parse reviews duration - " + duration);
	}
	
	public void delete(String fileName, CatalogEntry ce){
		long startTime = System.currentTimeMillis();
		// ArrayList<CatalogEntry> results = null;
		BufferedReader reader  = null;

		try {
			// Read contents of the JSON file into a JSON string
			reader = new BufferedReader( new FileReader (fileName));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }

		    JSONArray products = new JSONArray(stringBuilder.toString());
		    JSONArray new_products = new JSONArray();
		    for (int i = 0; i < products.length(); i++) {
				JSONObject product = products.getJSONObject(i);
				if (product.getInt("id") != Integer.parseInt(ce.get_product_id())){
						new_products.put(product);
				}
		    }
		    
		    persist(new_products.toString());
		    
		} catch (Exception e) {
			Log.e(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG, e);
		}
		finally
		{
			try
			{
				if ( reader != null)
					reader.close( );
			}
			catch ( IOException e)
			{
			}
	     }
		long duration = System.currentTimeMillis() - startTime;
		Log.v(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
				+ " send request and parse reviews duration - " + duration);
	}
	
	public void create(String fileName, CatalogEntry ce){
		long startTime = System.currentTimeMillis();
		// ArrayList<CatalogEntry> results = null;
		BufferedReader reader  = null;

		try {
			// Read contents of the JSON file into a JSON string
			reader = new BufferedReader( new FileReader (fileName));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String         ls = System.getProperty("line.separator");

		    while( ( line = reader.readLine() ) != null ) {
		        stringBuilder.append( line );
		        stringBuilder.append( ls );
		    }

		    JSONArray products = new JSONArray(stringBuilder.toString());
			int max_id = 0;
			for (int i = 0; i < products.length(); i++) {
				JSONObject product = products.getJSONObject(i);
				if (product.getInt("id") > max_id)
					max_id = product.getInt("id");
			}
			//ce.set_product_id(new Integer(max_id + 1).toString());
			JSONObject obj = new JSONObject();
			obj.put("id", max_id+1);
			obj.put("title", ce.get_title());
			obj.put("description", ce.get_description());
			obj.put("price", ce.get_price());
			obj.put("popularity", ce.get_popularity());
			products.put(obj);
		    
		    persist(products.toString());
		    
		} catch (Exception e) {
			Log.e(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG, e);
		}
		finally
		{
			try
			{
				if ( reader != null)
					reader.close( );
			}
			catch ( IOException e)
			{
			}
	     }
		long duration = System.currentTimeMillis() - startTime;
		Log.v(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
				+ " send request and parse reviews duration - " + duration);
	}
}
