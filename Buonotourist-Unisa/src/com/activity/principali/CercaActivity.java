package com.activity.principali;


import com.example.proveandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class CercaActivity extends Activity  {

    
	private static final int PartenzaDa = 1;
	private static final int PartenzaA = 2;
	private static final int TimePiker = 3;
	private int hour;
    private int minute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cerca);
		
		
		
		// SETTO I LISTENER AGLI ELEMENTI CREATI CON XML
		settaListenerBottoniNavbar(savedInstanceState);
		settaListenerBottoniForm(savedInstanceState);
		
		View widgetPartenza=findViewById(R.id.idBottoni_Provenienza);
		View widgetArrivo=findViewById(R.id.idBottoni_Destinazione);
		View widgetOrario=findViewById(R.id.idBottoni_Orario);
		
		
		widgetPartenza.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(PartenzaDa);
			}
		});
		
		
		widgetArrivo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(PartenzaA);
			}
		});
		
		
		widgetOrario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(TimePiker);
			}
		});
		
		
}


	private void settaListenerBottoniNavbar(final Bundle savedInstanceState) {
		Button buttonCercaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Cerca);
		Button buttonCorseNavbar =(Button)findViewById(R.id.idBottoniNavbar_Corse);
		Button buttonTariffeNavbar =(Button)findViewById(R.id.idBottoniNavbar_Tariffe);
		Button buttonLinguaNavbar =(Button)findViewById(R.id.idBottoniNavbar_Lingua);
		buttonCercaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 onCreate(savedInstanceState);
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
		buttonLinguaNavbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createLinguaActivity();
			}
		});
	}

	protected void createCorseActivity() {
		try{
			startActivity(new Intent(this,CorseActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
			}finally{
			finish();
		}
	}

	protected void createLinguaActivity() {
		try{
			startActivity(new Intent(this,LinguaActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}
	}

	protected void createTariffeeActivity() {
		try{
			startActivity(new Intent(this,TariffeActivity.class));
			this.overridePendingTransition(R.anim.late_in_left, R.anim.zero);		
		}finally{
			finish();
		}
	}

	private void settaListenerBottoniForm(final Bundle savedInstanceState) {
		Button buttonCercaForm =(Button)findViewById(R.id.idBottoniFormCerca_Cerca);
		Button buttonAnnullaForm =(Button)findViewById(R.id.idBottoniFormCerca_Annulla);
		buttonAnnullaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					 onCreate(savedInstanceState);
			}
		});
		buttonCercaForm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//INTERROGHERA' IL DATABASE
			}
		});
	}
	
	//sovrascrivo il metodo di activity ,e viene richiamato quando viene creata la prima volta la finestra di dialogo per associargli un id
		protected Dialog onCreateDialog(int id) {
			Dialog dialog;
				switch (id) {
					case PartenzaDa:
						dialog = createDa();
						break;
					case PartenzaA:
						dialog = createA();
						break;
					case TimePiker:
				            return new TimePickerDialog(this, timePickerListener, hour, minute,false);
				    default:
						dialog = null;
						break;
					}
			return dialog;
			}
		 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
	         
			 
		        @Override
		        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
		            // TODO Auto-generated method stub
		            hour   = hourOfDay;
		            minute = minutes;
		 
		            updateTime(hour,minute);
		             
		         }
		 
		    };
		 // finestra di dialogo del primo bottone
			public Dialog createDa(){
			
				final String[] options = { "Nola", "Sarno", "Caserta", "Palma Campania", "San Paolo Bel Sito" };
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Partenza Da:");
				builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
				@Override
				
					public void onClick(DialogInterface dialog, int which) {
					String option = options[which];
					Button widgetPartenza=(Button)findViewById(R.id.idBottoni_Provenienza);
					widgetPartenza.setText(option);
					}
				});
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
					public void onClick(DialogInterface dialog, int which) {
						dismissDialog(PartenzaDa);
					}
				});
				
				builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				       dismissDialog(PartenzaDa);
				}
				});
				AlertDialog alert = builder.create();
				return alert;
			}
			
			
			//finestra dialogo del secondo bottone
			public Dialog createA(){
				
				final String[] options = {"Fisciano","Lancusi"};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Arrivo A:");
				
				builder.setSingleChoiceItems(options, 2, new DialogInterface.OnClickListener() {
				@Override
				
					public void onClick(DialogInterface dialog, int which) {
					String option = options[which];
					Button widgetDestinazione=(Button)findViewById(R.id.idBottoni_Destinazione);
					widgetDestinazione.setText(option);
					}
				});
				
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
					public void onClick(DialogInterface dialog, int which) {
						dismissDialog(PartenzaA);
					}
				});
				
				builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				       dismissDialog(PartenzaA);
				}
				});
				AlertDialog alert = builder.create();
				return alert;
			}
			
			//gestisce il tempo
			private void updateTime(int hours, int mins) {
		        
		        String timeSet = "";
		        if (hours > 12) {
		            hours -= 12;
		            timeSet = "PM";
		        } else if (hours == 0) {
		            hours += 12;
		            timeSet = "AM";
		        } else if (hours == 12)
		            timeSet = "PM";
		        else
		            timeSet = "AM";
		 
		         
		        String minutes = "";
		        if (mins < 10)
		            minutes = "0" + mins;
		        else
		            minutes = String.valueOf(mins);
		 
		        // Append in a StringBuilder
		         String aTime = new StringBuilder().append(hours).append(':')
		                .append(minutes).append(" ").append(timeSet).toString();
		 
		         
		       Button widgetOrario=(Button)findViewById(R.id.idBottoni_Orario);
		       widgetOrario.setText(aTime);
		    }


			
	
	
	
	
}