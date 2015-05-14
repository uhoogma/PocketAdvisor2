package com.example.pocketadvisor2;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * AddToDoActivity.java
 * Tegu on klassiga mida kasutatakse uue meelespea
 * lisamiseks. Klass kasutab kuup2eva valikuks kuupa''eva valiku widgetit
 * mis on APIs juba alates 1. versioonist ning seet6ttu veidi vananenud
 * (deprecated) aga toimib k6igi versioonidega
 * Rohkem:
 * http://stackoverflow.com/questions/3299392/date-picker-in-android
 * @version 0.1
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class AddToDoActivity extends Activity {
	/** DatePickeri ajamuutuja aasta */
	private int mYear;
	
	/** DatePickeri ajamuutuja kuu */
	private int mMonth;
	
	/** DatePickeri ajamuutuja pa''ev */
	private int mDay;
	
	/** Kuupa''eva na''itav silt */
	private TextView mDateDisplay;
	
	/** Datepickerit ka''ivitav nupp */
	private Button mPickDate;
	
	/** DatePickeri identifikaator */
	static final int DATE_DIALOG_ID = 0;
	
	/**
	 * Klassi vaate AddToDoActivity loov meetod
    * @param savedInstanceState v6imaldab taastada vaate salvestatud
    * seisu na''iteks ekraani keeramise ja''rel
    */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// vaate seadmine
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_add_to_do);
	   
	   // Kuupa''a''eva ja Datepickeri na''itamine
	   mDateDisplay = (TextView) findViewById(R.id.showMyDate);        
	   mPickDate = (Button) findViewById(R.id.myDatePickerButton);
	   
	   /**
		 * DatePickeri ka''ivitamine
	    * @deprecated
	    */
	   mPickDate.setOnClickListener(new View.OnClickListener() {
	   @SuppressWarnings("deprecation")
			public void onClick(View v) {
	           showDialog(DATE_DIALOG_ID);
	      }
	   });

	   /** Kehtiva kuupa''eva na''itamine */
	   final Calendar c = Calendar.getInstance();
	   mYear = c.get(Calendar.YEAR);
	   mMonth = c.get(Calendar.MONTH);
	   mDay = c.get(Calendar.DAY_OF_MONTH);

	   // Uuendatud kuupa''eva na''itamine
	   updateDisplay();
	}
	
	/**
	 * Kuupa''aeva uuendamine
    */
	private void updateDisplay() {
	   this.mDateDisplay.setText(
	      new StringBuilder()
            // Kuu algab nullist seega lisame yhe
	      	.append(mDay).append("-")
            .append(mMonth + 1).append("-")
            .append(mYear).append(" "));
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
	    new DatePickerDialog.OnDateSetListener() {
		/**
		 * Kuvatava kuupa''eva uuendamine
	    * @param view Ka''esolev vaade
	    * @param year ka''esolev aasta
	    * @param monthOfYear ka''esolev kuu	    
	    * @param dayOfMonth  ka''esoleva kuu pa''ev
	    */
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
					mYear = year;
	            mMonth = monthOfYear;
	            mDay = dayOfMonth;
	            updateDisplay();				
			}
	    };
	    
	 /** DatePickeri identifikaatori kontrolimine 
	  * @param id DatePickeri identifikaator
	  */   
    @Override
    protected Dialog onCreateDialog(int id) {
       switch (id) {
       case DATE_DIALOG_ID:
          return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
       }
       return null;
    }   
    
   /** Androidi sissehitatud valikumenyy kasutamine, selles aplikatsioonis ei
    *  kasutata
    *  @param menu Menyy
    */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_to_do, menu);
		return true;
	}
	
	/**
	 * Uue meelespea salvestamine
    * @param view Vaade
    */
	public void saveNewToDo(View view){
		// klass millele info saadetakse
		Intent intent = new Intent(this, MainActivity.class);
		
		// hoiatus puuduva sisendi puhul
		String warning = "";
		
		// kuupa''evastringi (kuunumbrid algavad 1-st) koostamine
		String date = (mYear+"-"+(mMonth+1)+"-"+mDay);
		
		// Meelespea tiitli ka''tesaamine vastavast EditText elemendist
		EditText titleView = (EditText)findViewById(R.id.enterTitle);
		String title= titleView.getText().toString();
		
		// Meelespea kirjelduse ka''tesaamine vastavast EditText elemendist
		EditText descView = (EditText)findViewById(R.id.enterDescription);
		String desc= descView.getText().toString();
		
		// hoiatus puuduva tiitli puhul
		if((title.charAt(0)==' ' && title.length()==1) || title.length() < 1){
			warning = warning + "Title can not be empty. ";
		}
		// hoiatus puuduva kirjelduse puhul
		if((desc.charAt(0)==' ' && desc.length()==1) || desc.length() <1){
			warning = warning + "Description can not be empty. ";
		}
		// hoiatuse kuvamine
		if (title.length() < 1 || desc.length() <1) {
			Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT)
				.show();
      } else {
      	// info saatmine klassile MainActivity
      	intent.putExtra("date", date);
   		intent.putExtra("title", title);
   		intent.putExtra("description", desc);
   		this.setResult(RESULT_OK, intent);
   		finish();
      }
	}
	/**
	 * @param view Kehtiv vaade
	 * */
	public void cancelSaving(View view){
		// klass millele info saadetakse
		Intent intent = new Intent(this, MainActivity.class);
		this.setResult(RESULT_CANCELED, intent);
		finish();
	}
	
}
