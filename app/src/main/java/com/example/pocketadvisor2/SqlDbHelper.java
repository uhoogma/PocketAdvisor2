package com.example.pocketadvisor2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SqlDbHelper.java
 * Tegu on klassiga mida kasutatakse andmebaasi tabeli
 * loomiseks ja andmebaasi versiooni uuendamisel tabeli kustutamiseks
 * @version 0.1
 * @author  Arun Krishna
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class SqlDbHelper extends SQLiteOpenHelper {
	/** Andmebaasi tabeli nimi */
   public static final String DATABASE_TABLE = "todorecords";

   /** Andmebaasi tabeli tulp - autoincrementiga identifikaator */
	public static final String POCKETADVISOR_COLUMN_ID = "_id";

	/** Andmebaasi tabeli tulp kuupa''ev */
	public static final String POCKETADVISOR_COLUMN_DATE = "date";

	/** Andmebaasi tabeli tulp tiitel */
	public static final String POCKETADVISOR_COLUMN_TITLE = "title";

	/** Andmebaasi tabeli tulp kirjeldus */
	public static final String POCKETADVISOR_COLUMN_DESC = "description";

	/** Andmebaasi tabeli tulp staatus -
	 * 1 - aktiivne meelespea
	 * 2 - l6puleviidud meelespea
	 * 3 - l6puleviimata ja kustutatud meelespea
	 *  */
	public static final String POCKETADVISOR_COLUMN_FLAG = "flag";

	/** Pa''ringu string tabeli loomiseks */
	private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE " +
		DATABASE_TABLE + " (" +
		POCKETADVISOR_COLUMN_ID +
      " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      POCKETADVISOR_COLUMN_DATE  + " DATE NOT NULL, " +
      POCKETADVISOR_COLUMN_TITLE + " TEXT NOT NULL, " +
      POCKETADVISOR_COLUMN_DESC + " TEXT NOT NULL, " +
      POCKETADVISOR_COLUMN_FLAG + " INTEGER NOT NULL" +
      	")";

	/**
	 * Klassi SqlDbHelper konstruktor
    * @param context Kehtiv kontekst
    * @param name Andmebaasi nimi
    * @param factory Tehas kursorite loomiseks
    * @param version Andmebaasi versioon
    */
   public SqlDbHelper(Context context, String name, CursorFactory factory,
      int version) {
         super(context, name, factory, version);
   }

   /**
	 * Andmebaasi tabelit loova skripti ka''ivitamine
    * @param db andmebaas milles skript ka''ivitatakse
    */
	@Override
	public void onCreate(SQLiteDatabase db) {
   	 db.execSQL(SCRIPT_CREATE_DATABASE);
	}

	/**
	 * Andmebaasi tabeli kustutamine andmebaasi versiooniuuendusel
    * @param db andmebaas millest tabel kustutatakse
    * @param oldVersion andmebaasi vana versiooni number
    * @param newVersion andmebaasi uue versiooni number
    */
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   	db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
   	onCreate(db);
   }

}
