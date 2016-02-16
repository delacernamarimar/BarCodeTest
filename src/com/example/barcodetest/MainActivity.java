package com.example.barcodetest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public int SCANNER_REQUEST_CODE = 123;
	ListView lv;
	private ArrayAdapter<String> adapter;
	Button btnScan,btnCheck;
	ArrayList<String> al= new ArrayList<String>();
	EditText id, ticket;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
	}

	private void initViews() {
		//tvScanResults = (TextView) findViewById(R.id.tvResults);
		btnScan = (Button) findViewById(R.id.btnScan);
		btnScan.setOnClickListener(this);
		lv= (ListView) findViewById(R.id.listView1);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		id = (EditText) findViewById(R.id.editText1);
		ticket = (EditText) findViewById(R.id.editText2);
		btnCheck=(Button) findViewById(R.id.button1);
		lv.setAdapter(adapter);
		btnCheck.setOnClickListener(this);
		ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (requestCode == SCANNER_REQUEST_CODE) {
			// Handle scan intent
			if (resultCode == Activity.RESULT_OK) {
				// Handle successful scan
				String contents = intent.getStringExtra("SCAN_RESULT");
				String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
				byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
				int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
				Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null : intentOrientation;
				String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
				//tvScanResults.setText(contents + "\n\n" + formatName);
				adapter.add(contents);
				adapter.notifyDataSetChanged();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
			}
		} else {
			// Handle other intents
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnScan) {
			// go to fullscreen scan
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("QR_CODE", "QR_CODE");
			startActivityForResult(intent, SCANNER_REQUEST_CODE);
		}
		if(v.getId()==R.id.button1){
//			try{
//				//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.3/abella/checker.php?student="+id+",%20"+ticket)));
//
//
//		         // note : you may also need
//		         //HttpURLConnection.setInstanceFollowRedirects(false)
//				
////				URL u = new URL ("http://192.168.1.3/abella/checker.php?student="+id+",%20"+ticket);
////				HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
////				huc.setRequestMethod ("HEAD");  //OR  huc.setRequestMethod ("HEAD"); 
////				//d xa ka connect
////				huc.connect () ; 
//
//				
//	           
//				
//			}catch(Exception e){Toast.makeText(this, e.getMessage()+ "we", Toast.LENGTH_LONG).show();}
//		
			String ids=id.getText().toString();
			String tickets=ticket.getText().toString();
			
			   try {
				   //URL url = new URL("http://192.168.1.3/abella/checker.php?student="+id+",%20"+ticket);
				   //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				   //urlConnection.setRequestMethod ("HEAD");
				   DefaultHttpClient  httpclient = new DefaultHttpClient();
				   HttpGet httppost = new HttpGet("http://192.168.1.3/abella/checker.php?student="+ids+",%20"+tickets);
				   HttpResponse response = httpclient.execute(httppost);
				           HttpEntity ht = response.getEntity();

				           BufferedHttpEntity buf = new BufferedHttpEntity(ht);

				           InputStream is = buf.getContent();


				           BufferedReader r = new BufferedReader(new InputStreamReader(is));

				           StringBuilder total = new StringBuilder();
				           String line;
				           while ((line = r.readLine()) != null) {
				               total.append(line + "\n");
				           }

				   adapter.add(total.toString());
				   Toast.makeText(this, "naa o wla???123 "+total.toString(), Toast.LENGTH_LONG).show();
			   }
			
			   catch(Exception e){
				   Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show();}
			 

		}
	}
}
