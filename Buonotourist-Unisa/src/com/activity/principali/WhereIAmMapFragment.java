package com.activity.principali;

import com.example.buonotouristunisa.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONObject;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolylineOptions;


public class WhereIAmMapFragment extends  FragmentActivity{
	LocationListener myLocationListener= new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onProviderDisabled(String provider) {
		}	
		@Override
		public void onLocationChanged(Location location) {
			latitudinePosition=location.getLatitude();
			longitudinePosition=location.getLongitude();
			settaPercorsoMappa();
		}
	};
	double latitudinePosition;
	double longitudinePosition;
	LocationManager locationManager;
	LocationProvider networkProvider;
	private LatLng PROVENIENCE_POINT ;
	private LatLng DESTINATION_POINT = new LatLng(40.7721084,14.7993696);	 
	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	ComputeDistanceBetween calcolatoreDistanze = new ComputeDistanceBetween();
	boolean firstTime=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_whereiamapfragment);
		googleMap= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		// SETTO I LISTNER PER CAMBIARE LE ATTIVITA !
		settaListenerBottoniNavbar(savedInstanceState);

		// CONNESSIONE CON IL SERVER CON PHP
		
		
		// METODI DELLA MAPPA (( SI CONNETTE SOLO LE E' DISPONIBILE LA RETE ))
		NetAsyncMap();  
	}
	@Override
	protected void onPause(){
		super.onPause();
		locationManager.removeUpdates(myLocationListener);
	}
	@Override
	protected void onResume(){
		super.onResume();
		if(firstTime){
			settaPosizioneLocalizzazioneRete();
		}else{
			firstTime=!firstTime;
		}
	}
	
	private void settaListenerBottoniNavbar(final Bundle savedInstanceState) {
		Button buttonCercaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Cerca);
		Button buttonCorseNavbar =(Button)findViewById(R.id.idBottoniNavbar_Corse);
		Button buttonTariffeNavbar =(Button)findViewById(R.id.idBottoniNavbar_Tariffe);
		buttonCercaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 createCercaActivity();
			}
		});
		buttonCorseNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createCorseActivity();
			}
		});
		buttonTariffeNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createTariffeeActivity();
			}
		});
	}
	
	
	protected void createCercaActivity() {
		try{
			startActivity(new Intent(this,CercaActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
			}finally{
			finish();
		}
	}


	protected void createTariffeeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	
	
	// METODI DI MAPPA CHE SETTANO IL PERCORSO
	

	public void NetAsyncMap(){
	    new NetCheck().execute();
	}
	
/**
 * Async Task che controlla se la rete � disponibile
 **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(WhereIAmMapFragment.this);
            nDialog.setTitle(getString(R.string.stoControllandoRete));
            nDialog.setMessage(getString(R.string.caricamento));
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
            	settaPosizioneLocalizzazioneRete();
                nDialog.dismiss();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.connessioneAssente), Toast.LENGTH_SHORT).show();            }
        	}
    }
    
	private void settaPercorsoMappa(){
		//map.moveCamera(CameraUpdateFactory.newLatLngZoom(start,5));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		//map.addMarker(new MarkerOptions().position(start));
		//map.setOnMarkerClickListener(this);
		googleMap.setMyLocationEnabled(true);  // centra sulla mia pos.
		//googleMap.setTrafficEnabled(true);     // mostra stato traffico
		//aggiungo un marker sulla posizione start
		//map.addMarker(new MarkerOptions().position(start).title("Il mio pin").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		MarkerOptions options = new MarkerOptions();
		PROVENIENCE_POINT= new LatLng(latitudinePosition,longitudinePosition);
	    options.position(PROVENIENCE_POINT);
	    options.position(DESTINATION_POINT);
	    googleMap.clear(); // LO USO PER RIMUOVERE TUTTI I MARKER
	    googleMap.addMarker(options);
	    String url = getMapsApiDirectionsUrl();
	    ReadTask downloadTask = new ReadTask();
	    downloadTask.execute(url);
	    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PROVENIENCE_POINT,10));
	    addMarkers();
	    String distanza=""+Math.floor(calcolatoreDistanze.distance(PROVENIENCE_POINT.latitude,DESTINATION_POINT.latitude,PROVENIENCE_POINT.longitude,DESTINATION_POINT.longitude));
        Toast.makeText(getApplicationContext(),distanza+" Km(as the crow flies)", Toast.LENGTH_SHORT).show();            
	}
	public void settaPosizioneLocalizzazioneRete(){
		locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
		networkProvider= locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
		if(networkProvider == null){
	        Toast.makeText(getApplicationContext(),"Provider Rete Assente Sul dispositivo", Toast.LENGTH_LONG).show();            
		}else{
			if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30, 500,myLocationListener);
			}
		}
		
	}
	private String getMapsApiDirectionsUrl() {
	    String waypoints = "waypoints=optimize:true|"
	        + PROVENIENCE_POINT.latitude + "," + PROVENIENCE_POINT.longitude
	        + "|" + "|" + DESTINATION_POINT.latitude + ","
	        + DESTINATION_POINT.longitude;
	 
	    String sensor = "sensor=false";
	    String params = waypoints + "&" + sensor;
	    String output = "json";
	    String url = "https://maps.googleapis.com/maps/api/directions/"
	        + output + "?" + params;
	    return url;
	  }
	 
	  private void addMarkers() {
	    if (googleMap != null) {
	      googleMap.addMarker(new MarkerOptions().position(DESTINATION_POINT)
	          .title("Punto di destinazione"));
	      googleMap.addMarker(new MarkerOptions().position(PROVENIENCE_POINT)
	          .title("Punto di provenienza"));
	    }
	  }
	 
	  private class ReadTask extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... url) {
	      String data = "";
	      try {
	        HttpConnection http = new HttpConnection();
	        data = http.readUrl(url[0]);
	      } catch (Exception e) {
	        Log.d("Background Task", e.toString());
	      }
	      return data;
	    }
	 
	    @Override
	    protected void onPostExecute(String result) {
	      super.onPostExecute(result);
	      new ParserTask().execute(result);
	    }
	  }
	 
	  private class ParserTask extends
	      AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
	 
	    @Override
	    protected List<List<HashMap<String, String>>> doInBackground(
	        String... jsonData) {
	 
	      JSONObject jObject;
	      List<List<HashMap<String, String>>> routes = null;
	 
	      try {
	        jObject = new JSONObject(jsonData[0]);
	        PathJSONParser parser = new PathJSONParser();
	        routes = parser.parse(jObject);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	      return routes;
	    }
	 
	    @Override
	    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
	      ArrayList<LatLng> points = null;
	      PolylineOptions polyLineOptions = null;
	 
	      // traversing through routes
	      for (int i = 0; i < routes.size(); i++) {
	        points = new ArrayList<LatLng>();
	        polyLineOptions = new PolylineOptions();
	        List<HashMap<String, String>> path = routes.get(i);
	 
	        for (int j = 0; j < path.size(); j++) {
	          HashMap<String, String> point = path.get(j);
	 
	          double lat = Double.parseDouble(point.get("lat"));
	          double lng = Double.parseDouble(point.get("lng"));
	          LatLng position = new LatLng(lat, lng);
	 
	          points.add(position);
	        }
	 
	        polyLineOptions.addAll(points);
	        polyLineOptions.width(5);
	        polyLineOptions.color(Color.BLUE);
	      }
	 
	      googleMap.addPolyline(polyLineOptions);
	    }
	  }

}
