package com.example.pocketadvisor2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * SqlHandler.java
 * Tegu on klassiga mida kasutatakse andmebaasi loomiseks,
 * sellele ligipa''a''su saavutamiseks ja selles pa''ringute sooritamiseks
 * @version 0.1
 * @author  Arun Krishna
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class SqlHandler {
   /** Andmebaasi nimi */
   public static final String DATABASE_NAME = "pocketadvisor.db";
   
   /** Andmebaasi versioon, versiooni uuendamisel andmebaasi sisu kustub */
   public static final int DATABASE_VERSION = 14;
   
   /** Kehtiv kontekst */
   Context context;
   
   /** Andmebaasi instance */
   private SQLiteDatabase sqlDatabase;
   
   /** Helperklassi instance */
   private SqlDbHelper dbHelper;
   
   /** Klassi SqlHandler konstruktor
    * @param context Kehtiv kontekst
    */
   public SqlHandler(Context context) {
      /** Klassi SqlDbHelper instance'i loomine */
   	dbHelper = new SqlDbHelper(context, DATABASE_NAME, null,
   			DATABASE_VERSION);
   	sqlDatabase = dbHelper.getWritableDatabase();
   }
   
   /** Meetod DDL pa''ringu ka''ivitamiseks. Andmebaas avatakse ja 
    * sooritatkse tegevused, vea korral prinditakse teade.
    * @param query Ka''ivitatav SQL pa''ringustring
    */
   public void executeQuery(String query) {
   	try {
         if (sqlDatabase.isOpen()) {
         	sqlDatabase.close();
         }
         sqlDatabase = dbHelper.getWritableDatabase();
         sqlDatabase.execSQL(query);
      } catch (Exception e) {
      	System.out.println("DATABASE ERROR " + e);
      }
   }
   
   /** Meetod DML pa''ringu ka''ivitamiseks. Andmebaas avatakse ja 
    * sooritatakse tegevused, vea korral prinditakse teade.
    * @param query Ka''ivitatav SQL pa''ringustring
    * @return Tagastakse kursor pa''ringutulemustega
    */
   public Cursor selectQuery(String query) {
      Cursor c1 = null;
      try {
         if (sqlDatabase.isOpen()) {
         	sqlDatabase.close();
         }
         sqlDatabase = dbHelper.getWritableDatabase();
         c1 = sqlDatabase.rawQuery(query, null);
      } catch (Exception e) {
      	System.out.println("DATABASE ERROR " + e);
      }
      return c1;
   }
   
}
