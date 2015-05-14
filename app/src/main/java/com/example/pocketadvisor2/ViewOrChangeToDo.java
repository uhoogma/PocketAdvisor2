package com.example.pocketadvisor2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ViewOrChangeToDo.java
 * Tegu on klassiga mida kasutatakse olemasoleva 
 * meelespea vaatamiseks ja/v6i muutmiseks. Klass kasutab kuup2eva valikuks
 * kuupa''eva valiku widgetit * mis on APIs juba alates 1. versioonist ning
 * seet6ttu veidi vananenud (deprecated) aga toimib k6igi versioonidega
 * Rohkem:
 * http://stackoverflow.com/questions/3299392/date-picker-in-android
 * @version 0.1
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class ViewOrChangeToDo extends Activity {
	/** DatePickeri identifikaator */
	static final int DATE_DIALOG_ID = 0;
	
	/** DatePickeri ajamuutuja aasta */
	private int mYear;
	
	/** DatePickeri ajamuutuja kuu */
	private int mMonth;
	
	/** DatePickeri ajamuutuja pa''ev */
	private int mDay;
	
	/** ToDo andmebaasi id */
	private int itemid;
	
	/** ToDo staatus 
	 * 1 - aktiivne meelespea
	 * 2 - l6puleviidud meelespea
	 * 3 - l6puleviimata ja kustutatud meelespea */
	private int flag;
	
	/** Raadionuppude (staatused tulevad flagist) grupp */
	private RadioGroup flagstatus;
	
	/** Kuupa''a''eva na''itav silt */
	private TextView mDateDisplay;
	
	/** Datepickerit ka''ivitav nupp */
	private Button mPickDate;
	
	/** Uue meelespea kuupa''ev */
	private String date;
	
	/** Uue meelespea tiitel */
	private String title;
	
	/** Uue meelespea kirjeldus */
	private String description;
	
	/** raadionupu (staatus - aktiivne) identifikaator */
	private static final int RB0_ID = 1000;
	
	/** raadionupu (staatus - completed) identifikaator */
	private static final int RB1_ID = 1001;
	
	/** raadionupu (staatus - deleted) identifikaator */
	private static final int RB2_ID = 1002;
	
	/** Andmebaasiga suhtlemiseks vajalikke meetodeid sisaldav klass */
	private SqlHandler sqlHandler;
		
	/**
	 * Klassi vaate ViewOrChangeToDo loov meetod
    * @param savedInstanceState v6imaldab taastada vaate salvestatud
    * seisu na''iteks ekraani keeramise ja''rel
    */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// paneme vaate paika
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_view_or_change_to_do);
	   
	   // kuvame kuupa''eva ja selle muutmise nuppu
	   mDateDisplay = (TextView) findViewById(R.id.showMyDate);        
	   mPickDate = (Button) findViewById(R.id.myDatePickerButton);
	   
	   // loome uue intendi
	   Intent intent = getIntent();
	   
	   // Saame MainActivity-ilt k2tte s6numi ToDo id-ga Stringi kujul
	   String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	   itemid = Integer.parseInt(message);
	   
	   // Kutsume v2lja meetodi antud ToDo andmetega vaate loomiseks
	   try {
			prepValues(message);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
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
	}
	
	/**
	 * Radiobuttongrupi kontroll ma''a''ramaks kas valikut on muudetud
    * @return Tagastatakse ToDo uus staatus   
    */
	private int flagChanged() {
		// Uurime va''lja milline radiobutton on valitud
		flagstatus = (RadioGroup) findViewById(R.id.radioGroup1);
		int selectedId = flagstatus.getCheckedRadioButtonId();
		// Muudame ToDo staatuse v22rtust
		switch (selectedId) {
   	   case RB0_ID:
   	   	flag = 1;
   		   break;
   	   case RB1_ID:
   	   	flag = 2;
   	   	break;
   	   case RB2_ID:
   	   	flag = 3;
   	   	break;
		}			
		return flag;
	}
	
	/**
	 * Meetod antud ToDo andmetega vaate loomiseks
    * @param message ToDo andmebaasi id Stringi kujul  
    */
	private void prepValues(String message) throws java.text.ParseException{
		/** Valitud ToDo andmebaasi id */
		int messagei = Integer.parseInt(message);
		
		sqlHandler = new SqlHandler(this);
		
		/** Andmebaasi pa''ringustring */
		String query = "SELECT * FROM " +
         SqlDbHelper.DATABASE_TABLE +
         " WHERE " +
         SqlDbHelper.POCKETADVISOR_COLUMN_ID +
         " = " +
         messagei;
		
		/** Kursori ta''itmine */
	   Cursor c1 = sqlHandler.selectQuery(query);
	   
		   if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
               do {
               	itemid = (c1.getInt(c1.getColumnIndex(SqlDbHelper.
               			POCKETADVISOR_COLUMN_ID)));
                  date = (c1.getString(c1.getColumnIndex(SqlDbHelper.
                  		POCKETADVISOR_COLUMN_DATE)));
                  title = (c1.getString(c1.getColumnIndex(SqlDbHelper.
                  		POCKETADVISOR_COLUMN_TITLE)));
                  description = (c1.getString(c1.getColumnIndex(SqlDbHelper.
                  		POCKETADVISOR_COLUMN_DESC)));
                  flag = (c1.getInt(c1.getColumnIndex(SqlDbHelper.
                  		POCKETADVISOR_COLUMN_FLAG)));
               } while (c1.moveToNext());
               c1.close();
   		   }
		  }
		  /** Kuupa''evavorming andmebaasis */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        /** ToDo kuupa''ev Date objektiks */
		  Date convertedToDoDate = sdf.parse(date);
		  
		  /** Kalendri isend */
		  Calendar calendar = Calendar.getInstance();
		  
		  /** ToDo kuupa''ev Kalendri kujul */
        calendar.setTime(convertedToDoDate); 
        
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
        
        //ToDo kuupa''eva va''a''rtustamine
        date = (mYear+"-"+(mMonth)+"-"+mDay);
        
        updateDisplay();
        
	     // Radiobuttonite leidmine
        RadioButton rb0 = (RadioButton)findViewById(R.id.radio0);
        RadioButton rb1 = (RadioButton)findViewById(R.id.radio1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radio2);
        
        // Id-de lisamine radiobuttonitele
        rb0.setId(RB0_ID);
        rb1.setId(RB1_ID);
        rb2.setId(RB2_ID);
        
        // Vajaliku radiobuttoni m2rgistamine vastavalt id-le
	     if (flag==1) {
	   	  rb0.setChecked(true);
		  } else if (flag==2){
			  rb1.setChecked(true);
		  } else if(flag==3){
   	     rb2.setChecked(true);
		  }
	     
	     /** ToDo tiitli ka''tesaamine vastavast EditText elemendist */
	     EditText titleView = (EditText)findViewById(R.id.enterTitle);
	     titleView.setText(title);
	     
	     /** ToDo kirjelduse ka''tesaamine vastavast EditText elemendist */
	     EditText descView = (EditText)findViewById(R.id.enterDescription);
	     descView.setText(description);
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
	    * @param year Ka''esolev aasta
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
          	mDateSetListener,mYear, mMonth, mDay);
       }
       return null;
    }   
	
    /** Androidi sissehitatud valikumenyy kasutamine, selles aplikatsioonis ei
     *  kasutata
     *  @param menu Menyy
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_or_change_to_do, menu);
		return true;
	}
	
	/**
	 * Meetod antud ToDo salvestamiseks- Ka''vitatakse ka juhul kui andmeid 
	 * pole muudetud
    * @param view Kehtiv vaade  
    */
	public void saveExistingToDo(View view){
	   // klass millele info saadetakse
		Intent intent = new Intent(this, MainActivity.class);
		
		// kuupa''evastringi (kuunumbrid algavad 1-st) koostamine
		date = (mYear+"-"+(mMonth+1)+"-"+mDay);
		
		// ToDo tiitli ka''tesaamine vastavast EditText elemendist
		EditText titleView = (EditText)findViewById(R.id.enterTitle);
		title = titleView.getText().toString();
		
		// ToDo kirjelduse ka''tesaamine vastavast EditText elemendist
		EditText descView = (EditText)findViewById(R.id.enterDescription);
		description= descView.getText().toString();
		
		// ToDo uue staatuse kontroll
		int getflag = flagChanged();
		
		// hoiatus puuduva sisendi puhul
		String warning = "";
		
		// hoiatus puuduva tiitli puhul
		if((title.charAt(0)==' ' && title.length()==1) || 
				title.length() < 1){
			warning = warning + "Title can not be empty. ";
		}
		// hoiatus puuduva kirjelduse puhul
		if((description.charAt(0)==' ' && description.length()==1) || 
				description.length() <1){
			warning = warning + "Description can not be empty. ";
		}
		// hoiatuse kuvamine
		if (title.length() < 1 || description.length() <1) {
			Toast.makeText(getApplicationContext(), warning, Toast.LENGTH_SHORT).
				show();
      } else {
      // info saatmine klassile MainActivity
      	intent.putExtra("itemid", itemid);
      	intent.putExtra("date", date);
   		intent.putExtra("title", title);
   		intent.putExtra("description", description);
   		intent.putExtra("flag", getflag);
   		this.setResult(RESULT_OK, intent);
   		finish();
      }
	}
	
}
