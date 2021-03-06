package com.activity.principali;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.activity.principali.SimpleGestureFilter.SimpleGestureListener;
import com.classi.server.MioDbHelper;
import com.classi.server.UserFunctions;
import com.classi.server.UserFunctionsFacade;
import com.bdsirunisa.buonotouristunisa.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TariffeActivity extends Activity implements SimpleGestureListener {
	 /** AB BANNER CHE INSERISCO */
	  private AdView adView;

	  /* ID UNITA PUBBLICITARIO */
	  private static final String AD_UNIT_ID = "ca-app-pub-9936535009091025/4159664194";
	  
    private SimpleGestureFilter detector; 
	private MioDbHelper mMioDbHelper = null;
	private static final int TariffaDa = 1;
	private static final int TariffaA = 2;
	private static final int Alert = 3;
	private String sceltaTariffaDa="";
	private String sceltaTariffaA="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tariffe);
		
		createAdModBanner();
		
		mMioDbHelper = new MioDbHelper(getApplicationContext());
		 delete();
		 riempiDB();
		 
		View widgetTariffaDa=findViewById(R.id.idBottoni_TariffaDa);
		View widgetTariffaA=findViewById(R.id.idBottoni_TariffaA);
		
		widgetTariffaDa.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(TariffaDa);
			}
		});
		
		
		widgetTariffaA.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(TariffaA);
			}
		});
		
		widgetTariffaA.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(TariffaA);
			}
		});
		
	
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm(savedInstanceState);	
		
		detector = new SimpleGestureFilter(this,this); // GESTORE SWIPE
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
				onCreate(savedInstanceState);
			}
		});
	}

	protected void createCercaActivity() {
		try{
			startActivity(new Intent(this,CercaActivity.class));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class));
			this.overridePendingTransition(R.anim.anim_late_in_left, R.anim.anim_zero);		
		}finally{
			finish();
		}
	}
	
	private void settaListenerBottoniForm(final Bundle savedInstanceState) {
		Button buttonCercaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Cerca);
		Button buttonAnnullaForm =(Button)findViewById(R.id.idBottoniFormTariffe_Annulla);
		
		buttonAnnullaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			 onCreate(savedInstanceState);
			}
		});
		buttonCercaForm.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				Button inputPartenza = (Button) findViewById(R.id.idBottoni_TariffaDa);
	            Button inputDestinazione = (Button) findViewById(R.id.idBottoni_TariffaA);
	            String partenza = inputPartenza.getText().toString();
	            String destinazione = inputDestinazione.getText().toString();
	            if( partenza.compareTo(getString(R.string.partenza)) !=0 && destinazione.compareTo(getString(R.string.destinazione))!= 0 ){
	            	 NetAsync();
	            }else{
	            	showDialog(Alert);
	        	}
               
			}
		});
	}

	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
			switch (id) {
				case TariffaDa:
					dialog = TariffaDa();
					break;
				case TariffaA:
					dialog = TariffaA();
					break;
				case Alert:
					dialog = Alert();
					break; 
				default:
					dialog = null;
					break;
				}
		return dialog;
		}
	
public Dialog TariffaDa(){
		
	
	    ArrayList<String> options1=tuttoTranneFiscianoLancusi();
		
		final String options[]=new String[options1.size()];
		int lunghezza=options1.size();
		
		for(int i=0;i<lunghezza;i++){
			options[i]=options1.get(i);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.TariffaDa));
		builder.setSingleChoiceItems(options, 139, new DialogInterface.OnClickListener() {
		@Override
		
			public void onClick(DialogInterface dialog, int which) {
			sceltaTariffaDa = options[which];
			}
		});
		
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
			public void onClick(DialogInterface dialog, int which) {
			Button widgetPartenza=(Button)findViewById(R.id.idBottoni_TariffaDa);
			if(sceltaTariffaDa=="")
			{
				widgetPartenza.setText(getString(R.string.partenza));
				dialog.dismiss();
			}
			else
			{
			widgetPartenza.setText(sceltaTariffaDa);
			dialog.dismiss();
			}
		}
		});
		
		builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
	
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			dialog.dismiss();
		}
		});
		AlertDialog alert = builder.create();
		return alert;
	}
	
	
public Dialog TariffaA(){
		
		final String[] options;
		options=soloUniversita();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.TariffaA));
		builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
		@Override
		
			public void onClick(DialogInterface dialog, int which) {
			sceltaTariffaA = options[which];
			}
		});
		
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@Override
			public void onClick(DialogInterface dialog, int which) {
			Button widgetPartenza=(Button)findViewById(R.id.idBottoni_TariffaA);
			widgetPartenza.setText(sceltaTariffaA);
			if(sceltaTariffaA=="")
			{
				widgetPartenza.setText(getString(R.string.destinazione));
				dialog.dismiss();
			}
			else
			{
			    widgetPartenza.setText(sceltaTariffaA);
			    dialog.dismiss();
			}
			}
		});
		
		builder.setNegativeButton(getString(R.string.annulla), new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			dialog.dismiss();
		}
		});
		AlertDialog alert = builder.create();
		return alert;
	}

public Dialog Alert(){
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle(getString(R.string.Attenzione));
	builder.setMessage(getString(R.string.Messaggio));
	builder.setCancelable(false);
	
	builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which) {
	// Annullato!
		dialog.dismiss();
	}
	});
	AlertDialog alert = builder.create();
	return alert;
}
	
	public void NetAsync(){
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
            nDialog = new ProgressDialog(TariffeActivity.this);
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
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                Toast.makeText(getApplicationContext(),getString(R.string.connessioneAssente), Toast.LENGTH_SHORT).show();            }
        }
    }

    /**
     * Async Task che invia dati al My Sql database
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        

		private ProgressDialog pDialog;

        String partenza,destinazione;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Button inputPartenza = (Button) findViewById(R.id.idBottoni_TariffaDa);
            Button inputDestinazione = (Button) findViewById(R.id.idBottoni_TariffaA);
            partenza = inputPartenza.getText().toString();
            destinazione = inputDestinazione.getText().toString();
            pDialog = new ProgressDialog(TariffeActivity.this);
            pDialog.setTitle(getString(R.string.contattoServer));
            pDialog.setMessage(getString(R.string.invioDati));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	UserFunctionsFacade userFunction = new UserFunctions();
            JSONObject json = userFunction.caricaTariffe(partenza,destinazione);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
				try {
					   if (json.getString("success") != null) {
							String number = json.getString("success");
					        if(Integer.parseInt(number) == 1){
					            pDialog.setMessage(getString(R.string.caricamentoDatiRicevuti));
					            pDialog.setTitle(getString(R.string.RicevoDati));
					            JSONObject json_costi = json.getJSONObject("costi");
					            TextView abbonamento = (TextView)findViewById(R.id.idTextViewFormTariffe_Abbonamento);
					            abbonamento.setText(getString(R.string.costoA)+" "+json_costi.getString("costoAbbonamento") + "�");
					            abbonamento.setTextColor(getResources().getColor(R.color.costiAbbonamentoBiglietto));
					            TextView biglietto = (TextView)findViewById(R.id.idTextViewFormTariffe_Biglietto);
					            biglietto.setText(getString(R.string.costoB)+" "+json_costi.getString("costoBiglietto") + "�");
					            biglietto.setTextColor(getResources().getColor(R.color.costiAbbonamentoBiglietto));
					            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notification_sound);
					            mp.start();
					            pDialog.dismiss();
					        }else{
					            pDialog.dismiss();
					            Toast.makeText(getApplicationContext(),getString(R.string.datiInviatiNonValidi), Toast.LENGTH_SHORT).show();            
					         }
					   }else{
				            Toast.makeText(getApplicationContext(),"JSON SUCCESS NOT 1 (IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
					   }
				} catch (NumberFormatException e) {
		            Toast.makeText(getApplicationContext(),"ERROR NUMBER FORMAT", Toast.LENGTH_SHORT).show();            
				} catch (JSONException e) {
		            Toast.makeText(getApplicationContext(),"ERROR JSON SUCCESS O INTERNI(IMPOSSIBILE)", Toast.LENGTH_SHORT).show();            
				}
        }
    }
    
    public void riempiDB(){
    	SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
    	ContentValues values= new ContentValues();
    	
    	values.put("nome", "Fisciano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Lancusi");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Cimitile");
    	db.insert("rubrica", null, values);
    	values.put("nome", "San Paolo Bel Sito");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Sicignano Scalo");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Contursi");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Eboli");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Caserta");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Castello di Cisterna");
    	db.insert("rubrica", null, values);
    	values.put("nome", "San Gennaro Vesuviano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Sarno");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Pomigliano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Nola");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Lauro");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Boscoreale");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Pompei");
        db.insert("rubrica", null, values);
    	values.put("nome", "Nocera");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Roccarainola");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Cicciano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Palma Campania");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Marzano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Marigliano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Saviano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "San Vitaliano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Striano");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Mariglianella");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Tufino");
    	db.insert("rubrica", null, values);
    	values.put("nome", "Napoli");
    	db.insert("rubrica", null, values);
    }
    
    
    private String[] soloUniversita() {
    	//Chiediamo l'accesso al db
    	SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
        String[] universita=new String[2];
    	
    	final String sql = "SELECT * FROM rubrica where _id=1 or _id=2 order by nome";
    	int i=0;
    	Cursor c = db.rawQuery(sql, null);
    	
    	while(c.moveToNext()) { 
    		universita[i]=c.getString(1);
    		i++;
    	}
    	return universita;
    }
    
    
    private ArrayList<String> tuttoTranneFiscianoLancusi() {
    	//Chiediamo l'accesso al db
    	SQLiteDatabase db = mMioDbHelper.getReadableDatabase();
    	ArrayList<String> paesi =new ArrayList<String>();
    	
    	final String sql = "SELECT * FROM rubrica where _id!=1 and _id!=2 order by nome";
    	
    	Cursor c = db.rawQuery(sql, null);
    	
    	while(c.moveToNext()) { 
    		paesi.add(c.getString(1));
    		//Toast.makeText(getApplicationContext(),""+c.getString(1), Toast.LENGTH_SHORT).show();
    	}
    	return paesi;
    }
    
    
    public void delete(){
	   	SQLiteDatabase db = mMioDbHelper.getWritableDatabase();
		db.delete("rubrica", null, null);
	}
    
   // -------------------->  ************** SWIPE
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    @Override
     public void onSwipe(int direction) {
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT : createCorseActivity();
                                             break;
      case SimpleGestureFilter.SWIPE_LEFT :  createCercaActivity();
                                             break;   
      }
     }
     @Override
     public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
     }
     
     // *************************** ADMOB METHOD
     
 	private void createAdModBanner() {
 		 // Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);
	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.
	    LinearLayout layout = (LinearLayout) findViewById(R.id.idLayout_BannerAdmob);
	    layout.removeAllViews();
	    layout.addView(adView);
	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    //final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
	    //String deviceid = tm.getDeviceId();
	    AdRequest adRequest = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	    .build();
	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	}
    //gestione rotazione metodo che si attiva ogni volta che tuoto il dispositivo
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig); 
    }	
     @Override
     public void onResume() {
       super.onResume();
       if (adView != null) {
         adView.resume();
       }
     }
     @Override
     public void onPause() {
       if (adView != null) {
         adView.pause();
       }
       super.onPause();
     }
     /** Called before the activity is destroyed. */
     @Override
     public void onDestroy() {
       // Destroy the AdView.
       if (adView != null) {
         adView.destroy();
       }
       super.onDestroy();
     }
}
