package com.example.proveandroid;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	private static final int MENUITEM_COMANDO_1=1;
	private static final int MENUITEM_COMANDO_2=2;
	private static final int MENUITEM_COMANDO_3=3;
	private static final int MENUITEM_COMANDO_4=4;
	private static final int MENUITEM_COMANDO_5=5;
	private static final int MENUITEM_COMANDO_6=6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.cerca);
		View widgetPartenza=findViewById(R.id.partenza);
		View widgetArrivo=findViewById(R.id.arrivo);
		widgetPartenza.setId(1);
		widgetArrivo.setId(2);
		registerForContextMenu(widgetPartenza);
		registerForContextMenu(widgetArrivo);
		
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo){
		
		if(v.getId()==1){
		menu.setHeaderTitle("Partenza da:");
		menu.add(Menu.NONE,MENUITEM_COMANDO_1,1,"Nola");
		menu.add(Menu.NONE,MENUITEM_COMANDO_2,2,"Sarno");
		menu.add(Menu.NONE,MENUITEM_COMANDO_3,3,"Palma Campania");
		menu.add(Menu.NONE,MENUITEM_COMANDO_4,4,"Caserta");}
		else
		{
			menu.setHeaderTitle("Arrivo a:");
			menu.add(Menu.NONE,MENUITEM_COMANDO_5,5,"Fisciano");
			menu.add(Menu.NONE,MENUITEM_COMANDO_6,6,"Lancusi");
			
		}
 	}
	
	public boolean onContextItemSelected(MenuItem item){
		int id=item.getItemId();
		switch(id){
		case MENUITEM_COMANDO_1:
			Toast.makeText(this, "Nola", Toast.LENGTH_SHORT).show();
			return true;
		case MENUITEM_COMANDO_2:
			Toast.makeText(this, "Sarno", Toast.LENGTH_SHORT).show();
			return true;
		case MENUITEM_COMANDO_3:
			Toast.makeText(this, "Palma campania", Toast.LENGTH_SHORT).show();
			return true;
		case MENUITEM_COMANDO_4:
			Toast.makeText(this, "Caserta", Toast.LENGTH_SHORT).show();
			return true;
		case MENUITEM_COMANDO_5:
			Toast.makeText(this, "fisciano", Toast.LENGTH_SHORT).show();
			return true;
		case MENUITEM_COMANDO_6:
			Toast.makeText(this, "lancusi", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
		}
	public void onContextMenuClosed(Menu menu){
		Toast.makeText(this, "chiuso", Toast.LENGTH_SHORT).show();
		
	}
	
	
}
