package com.depot.cs4900;

import java.util.List;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ViewFlipper;
import android.widget.EditText;

import com.depot.cs4900.data.*;
import com.depot.cs4900.network.*;
import org.apache.http.client.ResponseHandler;

public class Product extends Activity {
	private static final String CLASSTAG = Product.class.getSimpleName();
	private static final int MENU_DELETE = Menu.FIRST;

	Prefs myprefs = null;

	private EditText title_text;
	private EditText desciption_text;
	private EditText price_text;

	private Button update_button;
	private Button cancel_button;

	private ProgressDialog progressDialog;

	private CatalogEntry product;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOGTAG, " " + Product.CLASSTAG
					+ " update/delete worker thread done.");
			progressDialog.dismiss();

			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);

		myprefs = new Prefs(getApplicationContext());

		product = CatalogEntry.fromBundle(getIntent().getExtras());

		title_text = (EditText) findViewById(R.id.product_title);
		title_text.setText(product.get_title());

		desciption_text = (EditText) findViewById(R.id.product_description);
		desciption_text.setText(product.get_description());

		price_text = (EditText) findViewById(R.id.product_price);
		price_text.setText(product.get_price());

		// update
		update_button = (Button) findViewById(R.id.product_update_button);
		update_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				updateProdut();
			}
		});
		// cancel
		cancel_button = (Button) findViewById(R.id.product_cancel_button);
		cancel_button.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(Constants.LOGTAG + ": " + Product.CLASSTAG, " onResume");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Product.MENU_DELETE, 0, R.string.menu_product_delete)
				.setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			try {
				// Perform action on click
				deleteProdut();
			} catch (Exception e) {
				Log.i(Constants.LOGTAG + ": " + Product.CLASSTAG,
						"Failed to delete the product [" + e.getMessage() + "]");
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void updateProdut() {

		Log.v(Constants.LOGTAG, " " + Product.CLASSTAG + " updateProduct");

		// Get ready to send the HTTP PUT request to update the Product data on
		// the server

		// Get ready to send the HTTP PUT request to update the Product data on
		// the server
		final ResponseHandler<String> responseHandler = HTTPRequestHelper
				.getResponseHandlerInstance(this.handler);
		final HashMap<String, String> params = new HashMap<String, String>();
		if (!title_text.getText().toString().equals("")) {
			params.put("title", title_text.getText().toString());
		}
		if (!desciption_text.getText().toString().equals("")) {
			params.put("description", desciption_text.getText().toString());
		}
		if (!price_text.getText().toString().equals("")) {
			params.put("price", price_text.getText().toString());
		}

		this.progressDialog = ProgressDialog.show(this, " Working...",
				" Updating Product", true, false);

		product.set_title(title_text.getText().toString());
		product.set_description(desciption_text.getText().toString());
		product.set_price(price_text.getText().toString());

		// update product on the server in a separate thread for
		// ProgressDialog/Handler
		// when complete send "empty" message to handler
		new Thread() {
			@Override
			public void run() {
				// Update the product in the JSON file
				CatalogFetcher cf = new CatalogFetcher();
				cf.replace(Constants.CATALOG_JSON_FILE_NAME, product);
				
				// Update the product on the server
				if (myprefs.getMode() == Constants.ONLINE
						&& myprefs.isValid()) {
					HTTPRequestHelper helper = new HTTPRequestHelper(
							responseHandler);
					helper.performPut(
							HTTPRequestHelper.MIME_TEXT_PLAIN,
							myprefs.getServer() + "/products/"
									+ product.get_product_id() + ".json", null,
							null, null, params);
				}
				
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void deleteProdut() {

		Log.v(Constants.LOGTAG, " " + Product.CLASSTAG + " deleteProduct");

		this.progressDialog = ProgressDialog.show(this, " Working...",
				" Deleting Product", true, false);

		// delete product on the server in a separate thread for
		// ProgressDialog/Handler
		// when complete send "empty" message to handler
		new Thread() {
			@Override
			public void run() {
				// Delete the product from JSON file
				CatalogFetcher cf = new CatalogFetcher();
				cf.delete(Constants.CATALOG_JSON_FILE_NAME, product);
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

}
