
package com.depot.cs4900;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.depot.cs4900.data.*;

public class CatalogByPopularity extends ListActivity {
    private static final String CLASSTAG = CatalogByPopularity.class.getSimpleName();
    private static final int MENU_CREATE = Menu.FIRST;
    
    private TextView empty;    
    private ProgressDialog progressDialog;
    private CatalogAdapter catalogAdapter;
    private List<CatalogEntry> catalog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Constants.LOGTAG, " " + CatalogByPopularity.CLASSTAG + " onCreate");

        this.setContentView(R.layout.catalog_by_price);

        this.empty = (TextView) findViewById(R.id.empty);

        // set list properties
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setEmptyView(this.empty);
    }   

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(Constants.LOGTAG, " " + CatalogByPopularity.CLASSTAG + " onResume");

        CatalogFetcher cf = new CatalogFetcher();
        // Parse the data from catalog.json file
        catalog = cf.getCatalogFromFile("/data/data/com.depot.cs4900/files/products.json");
		catalogAdapter = new CatalogAdapter(CatalogByPopularity.this, catalog, 2); //2: Sort by popularity
		setListAdapter(catalogAdapter);
    }    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, CatalogByPopularity.MENU_CREATE, 0, R.string.menu_product_create)
				.setIcon(android.R.drawable.ic_menu_add);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case MENU_CREATE:
			try {
				// Perform action on click
				Intent intent = new Intent(
						Constants.INTENT_ACTION_NEW_PRODUCT);
				startActivity(intent);
			} catch (Exception e) {
				Log.i(Constants.LOGTAG + ": " + CatalogByPopularity.CLASSTAG,
						"Failed to create a new product [" + e.getMessage() + "]");
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(Constants.INTENT_ACTION_PRODUCT_DETAIL);
        intent.putExtras(catalog.get((int)id).toBundle());
        startActivity(intent);
    }    

}
