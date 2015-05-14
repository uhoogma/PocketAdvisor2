package com.example.pocketadvisor2;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
//import android.widget.Toast;

/**
 * MainActivity.java
 * Tegu on aplikatsiooni k2ivitamisel esimesena k2ivituva
 * klassiga mis sisaldab meelespeade (ka nime all ToDo) nimekirja, v6imalust
 * seda nimekirja filtreerida ja luua uusi ToDo-sid. Klass nagu paljud teised
 * pa''rineb algselt Jonathan Simon 86ikust "Head First Android Development",
 * kuid seda on ulatuslikult modifitseeritud. Klass kasutab kuulareid ToDode
 * listil klikkamise registreerimiseks ja rippmenyyga (Spinner) tehtava valiku
 * kuvamiseks.
 * @version 0.1
 * @author 	Jonathan Simon
 * @author  Arun Krishna 
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class MainActivity extends Activity implements OnItemClickListener, 
	OnItemSelectedListener {
	
	/** Konstant m22ramaks kindlaks, et nupu New meetodi saadetud s6numile
	 * vastas 6ige klassi 6ige meetod  */
	public static final int TODO_ENTRY_REQUEST_CODE = 1;
	
	/** Konstant m22ramaks kindlaks, et listi itemil klikkimisel saadetud
	 * s6numile vastas 6ige klassi 6ige meetod  */
	public static final int TODO_EDIT_REQUEST_CODE = 2;

	/** Meetodite s6numi identifikaator */
	public static final String EXTRA_MESSAGE = "foo";

	/** Listi itemi id (mis tuleb andmebaasist) algv22rtustamine */
	private int idtochange = -1;
	
	/** ToDo-de  nimekiri */	
	ListView lvCustomList;
	
	/** ToDo-de nimekirja adapter */	
	ToDoListAdapter contactListAdapter ;
	
	/** ToDo-de nimekirja sorteerimiswidget */	
	Spinner spinner ;
	
	/** Andmebaasiga suhtlemiseks vajalikke meetodeid sisaldava klassi 
	 * instance */
	private SqlHandler sqlHandler;

	/**
	 * Klassi MainActivity vaate loov meetod
    * @param savedInstanceState v6imaldab taastada vaate salvestatud
    * seisu na''iteks ekraani keeramise ja''rel
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
   	super.onCreate(savedInstanceState);
   		// vaate ja dropdowni seadmine
      	setContentView(R.layout.activity_main);
         spinner = (Spinner) findViewById(R.id.spinner1);
         
         /** Spinneri valikute kysimine xml failist */
         String all = this.getString(R.string.all_spinner_choice);
         String only_overdue = this.getString(R.string.only_overdue_spinner_choice);
         String only_upcoming = this.getString(R.string.only_upcoming_spinner_choice);
         String completed = this.getString(R.string.completed_spinner_choice);
         String deleted = this.getString(R.string.deleted_spinner_choice);
         
         final String[] paths = { 
      			all, only_overdue, only_upcoming, completed, deleted};
         
      	/** Spinneri valikud */
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity
         		.this, android.R.layout.simple_spinner_item, paths);
         adapter.setDropDownViewResource(android.R.layout.
         	simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         spinner.setOnItemSelectedListener(this);
         lvCustomList = (ListView) findViewById(R.id.todos_list);
         // Listi kuvamine k6igi aktiivsete ToDo-dega
         showList(0, 1);
   }

   /** Androidi sissehitatud valikumenyy kasutamine, selles aplikatsioonis ei
    *  kasutata
    *  @param menu Menyy
    */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   /**
	 * ToDo-de listi loov ja n2itav meetod
    * @param selection Kuupa''eva valik
    * 0 = m6lemad allpool olevad
    * 1 = ainult ylel2inud
    * 2 = ainult kustutatud
    * @param selected_flag filtri parameeter ToDo staatuse j2rgi
    * 1 = aktiivsed
    * 2 = L6puni viidud
    * 3 = Mingil p6hjusel enne l6puleviimist kustutatud
    */
   private void showList(int selection, int selected_flag) {
   	/** Andmed loetakse SqLite andmebaasist Arraylisti */
      ArrayList<ToDo> todos = new ArrayList<ToDo>();
      
      // puhastatakse nimistu, kui see on juba olemas
      todos.clear();
      
      /** Praegune kuupa''ev stringiks */
      Calendar d = Calendar.getInstance();
		int x = d.get(Calendar.YEAR);
      int y = d.get(Calendar.MONTH)+1;
		int z = d.get(Calendar.DATE);
		String dfh = String.valueOf(x)+"-"+String.valueOf(y)+"-"+
			String.valueOf(z);
	   
	   /** Pa''ringu ja''rgi filtreerimisse string */
      String query = "SELECT * FROM "+
         SqlDbHelper.DATABASE_TABLE +
         " WHERE " + 
         SqlDbHelper.POCKETADVISOR_COLUMN_FLAG + " = " +
         selected_flag +
         " ORDER BY " +
         SqlDbHelper.POCKETADVISOR_COLUMN_DATE + 
         " ASC ";
      
      Cursor c1 = null;
      try {
      /** Pa''ringu teostamine ja tulemuste lugemine vahetabelisse -
       * kursorisse */
      sqlHandler = new SqlHandler(this);
      /** Kursori ta''itmine */
      c1 = sqlHandler.selectQuery(query);
      
      if (c1 != null && c1.getCount() != 0) {
         if (c1.moveToFirst()) {
            do {
            	// Andmete kysimine kursori yhelt realt
               int id = (c1.getInt(c1.getColumnIndex
               		(SqlDbHelper.POCKETADVISOR_COLUMN_ID)));
               String date = (c1.getString(c1.getColumnIndex
               		(SqlDbHelper.POCKETADVISOR_COLUMN_DATE)));
               String title = (c1.getString(c1.getColumnIndex
               		(SqlDbHelper.POCKETADVISOR_COLUMN_TITLE)));
               String description = (c1.getString(c1.getColumnIndex
               		(SqlDbHelper.POCKETADVISOR_COLUMN_DESC)));
              
               // Case 0: Overdue active ToDo-s
               if ((dfh.compareTo(date) > 0) && 
               		selection == 1 && 
               		selected_flag == 1) {
               	ToDo todoinstance = new ToDo(id, date, title, description);
               	todos.add(todoinstance);
               }
               // Case 1: Upcoming active ToDo-s
               else if((dfh.compareTo(date) <= 0) && 
               		selection == 2 && 
               		selected_flag == 1) {
               	ToDo todoinstance = new ToDo(id, date, title, description);
               	todos.add(todoinstance);
               }
               // Case 2: All active ToDo-s
               else if (selection == 0 && selected_flag == 1) {
               	ToDo todoinstance = new ToDo(id, date, title, description);
               	todos.add(todoinstance);
               }
               // Case 3: Completed ToDo-s
               else if(selection == 0 && selected_flag == 2) {
               	ToDo todoinstance = new ToDo(id, date, title, description);
               	todos.add(todoinstance);
               }
               // Case 4: Deleted ToDo-s
               else if(selection == 0 && selected_flag == 3) {
               	ToDo todoinstance = new ToDo(id, date, title, description);
               	todos.add(todoinstance);
               }
         	} while (c1.moveToNext());
      	}
      }
      } catch (Exception e) {
      	e.printStackTrace();
      } finally {
      	c1.close();
      }
      /** Uue adapteri loomine, sellele kuulari lisamine ja selle lisamine */ 
      ToDoListAdapter contactListAdapter = new ToDoListAdapter(
          MainActivity.this, todos);
      lvCustomList.setOnItemClickListener(this);
      lvCustomList.setAdapter(contactListAdapter);
   }
   
   /** Klikitud itemi andmebaasi _id hankimine, selle asetamine klassi-
    * muutujasse ja ToDo vaatamise/muutmise vaatele minemise meetodi 
    * v2ljakutsumine
    * @param contactListAdapter listile lisatud adapter
    * @param view Kehtiv vaade
    * @param position Listi itemi positsioon
    * @param id Listi itemi idemtifikaator
    */
   @Override
   public void onItemClick(AdapterView<?> contactListAdapter, View view, 
   		int position, long id) {
      ToDo a = (ToDo) contactListAdapter.getAdapter().getItem(position);
      int idtd = a.getId();
      idtochange = idtd;
      viewSpecificToDo(view);
   }
   
   /** Debug. Eelnevalt valitud itemi v22rtuse kuvamine
    * @param l Listivaade
    * @param v Ka''esolev vaade
    * @param position Listi itemi positsioon
    * @param id Kisti itemi idemtifikaator
    */
   protected void onListItemClick(ListView l, View v, int position, long id) {
      //String selectedValue = (String) contactListAdapter.getItem(position);
      //Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
   }
   
   /** Uue ToDo loomine ja selle kavatsuse saatmine AddToDoActivity klassile
    * @param view Ka''esolev vaade
    */
   public void newToDo(View view){
		Intent intent = new Intent(view.getContext(), AddToDoActivity.class);
		startActivityForResult(intent, TODO_ENTRY_REQUEST_CODE);
	}
   
   /** ToDo vaatamine ja vo muutmine. Klassile ViewOrChangeToDo pannakse
    * kaasa ToDo andmebaasi id 6ige pa''ringu tegemiseks
    * @param view Vaade
    */
	public void viewSpecificToDo (View view){
		String message = Integer.toString(idtochange);
		Intent intent = new Intent(this, ViewOrChangeToDo.class);
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivityForResult(intent, TODO_EDIT_REQUEST_CODE);
	}
	
	/** Tagastatud andmete alusel uue, v6i muudetud ToDo loomine
    * @param requestCode Pa''ringukood konstandist
    * @param resultCode Tagastatud kood
    * @param data Tagastatud andmed
    */
	@Override
 	protected void onActivityResult(int requestCode, int resultCode,
 			Intent data) {
		// Uue ToDo loomine
 		if (requestCode == TODO_ENTRY_REQUEST_CODE) {
	 		if (resultCode == RESULT_OK) {
	 			String date = data.getStringExtra("date");
	 			String title = data.getStringExtra("title");
	 			String desc = data.getStringExtra("description");
	 			
	 			// ylakomade sisestamise v6imaldamine
	 			title = title.replace("'","''");
	 			desc = desc.replace("'","''");
	 			
	 			/** flag = 1 - vastloodud ToDo on alati aktiivne*/
	 			int flag = 1;
	 			
	 			//Insert ka''sk
	 		   String query = "INSERT INTO " + 
   	 		   SqlDbHelper.DATABASE_TABLE + " ( " + 
   	 		   SqlDbHelper.POCKETADVISOR_COLUMN_DATE+ " , " + 
   	 		   SqlDbHelper.POCKETADVISOR_COLUMN_TITLE + " , " +  
   	 		   SqlDbHelper.POCKETADVISOR_COLUMN_DESC + " , " + 
   	 		   SqlDbHelper.POCKETADVISOR_COLUMN_FLAG + " ) " + 
   	 		   "  VALUES " +
   	 		   "('"+ date + "','" + title + "','" + desc + "','" + flag + "')";
	 		   
	 		   // Pa''ringu ka''aivitamine ja Listi va''rskendamine
	 			sqlHandler.executeQuery(query);
	 			showList(0, 1);
	 		}
	 	}
 		/** Olemasoleva Todo muutmine*/
 		else if(requestCode == TODO_EDIT_REQUEST_CODE) {
	 		if (resultCode == RESULT_OK) {
	 			int id = data.getIntExtra("itemid", -1);
	 			String date = data.getStringExtra("date");
	 			String title = data.getStringExtra("title");
	 			String desc = data.getStringExtra("description");
	 			
	 			// ylakomade sisestamise v6imaldamine
	 			title = title.replace("'","''");
	 			desc = desc.replace("'","''");
	 			
	 			int flag = data.getIntExtra("flag", -1);
	 			
	 			// P2ringu k2ivitamine ja Listi va''rskendamine
	 			String query = "UPDATE " +
   	 			SqlDbHelper.DATABASE_TABLE + 
   	 			" SET "+ 
   	 			SqlDbHelper.POCKETADVISOR_COLUMN_DATE + 
   	 			" = '" + date + "'," +
   	 			SqlDbHelper.POCKETADVISOR_COLUMN_TITLE +
   	 			" = '" + title + "'," + 
   	 			SqlDbHelper.POCKETADVISOR_COLUMN_DESC +
   	 			" = '" + desc + "'," + 
   	 			SqlDbHelper.POCKETADVISOR_COLUMN_FLAG + 
   	 			" = '" + flag + "'" + 
   	 			" WHERE " +
   	 			SqlDbHelper.POCKETADVISOR_COLUMN_ID + 
   	 			" = " + id;
	 			
	 			// Pa''ringu ka''aivitamine ja listi va''rskendamine
	 			sqlHandler.executeQuery(query);
	 			showList(0, 1);
	 		}
	 	}
	}
	
	/** Listi va''rskendamine Spinneris tehtud valiku alusel 
    * @param parent Spinneri adapter 
    * @param v Vaade
    * @param position ToDo positsioon Listis
    * @param id ToDo id
    */
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
   	int filter_choice =  spinner.getSelectedItemPosition();
   	//String asdf = String.valueOf(filter_choice);//(filter_choice);
   	//Toast.makeText(getApplicationContext(), asdf, Toast.LENGTH_LONG).show();
   	
   	switch (filter_choice) {
      	case 0: /** Case 2: All active ToDo-s */
      		showList(0, 1);
            break;
        	case 1: /** Case 1: Overdue active ToDo-s */
        		showList(1, 1);
            break;
        	case 2: /** Case 1: Upcoming active ToDo-s */
        		showList(2, 1);
        		break;
        	case 3: /** Case 3: Completed ToDo-s */
        		showList(0, 2);
            break;
        	case 4: /** Case 3: Deleted ToDo-s */
        		showList(0, 3);
        		break;
       	} 
	 } 
	
	/** Igaks juhuks
	* @param arg0 Spinneri adapter 
	 * */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//showList(0, 1);
	}

}
