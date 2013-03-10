package com.depot.cs4900.data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
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
				String popularity = "0";
				
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

	/**
	 * Call Yelp.
	 * 
	 * @return
	 */
	public ArrayList<CatalogEntry> getCatalogFromFile(String fileName) {
		long startTime = System.currentTimeMillis();
		// ArrayList<CatalogEntry> results = null;

		try {
			// Read contents of the JSON file into a JSON string
			BufferedReader reader = new BufferedReader( new FileReader (fileName));
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
		long duration = System.currentTimeMillis() - startTime;
		Log.v(Constants.LOGTAG, " " + CatalogFetcher.CLASSTAG
				+ " send request and parse reviews duration - " + duration);
		return catalog;
	}
}
